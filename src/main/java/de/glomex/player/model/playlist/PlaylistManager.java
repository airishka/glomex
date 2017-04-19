package de.glomex.player.model.playlist;

import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.api.playlist.PlaylistListener;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.lifecycle.EmptyLifecycleListener;
import de.glomex.player.model.lifecycle.LifecycleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This object allows to
 * - manage playlist
 * - navigate on it
 * It doesn't control playback still.
 *
 * Commands like next(), prev(), skipTo() don't start playback,
 * just control populate manager to open content.
 *
 * To start playing it, either play() command must be issued to playback control, or auto play feature must be turned on
 *
 * IMPROVE: separate playlist management (adding/modifying/etc) and operations with status, options, etc
 *
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistManager extends EmptyLifecycleListener implements PlaylistControl {

    private static final Logger log = Logging.getLogger(PlaylistManager.class);

    private static final int MAX_HISTORY_SIZE = 20;

    private enum Status {nonPlayed, isBeingPlayed, played}

    private final PlaylistListener playlistListener;

    private final Object lock = new Object();

    private boolean repeatable;
    private boolean random;

    private final List<MediaID> playlist = new ArrayList<>();
    private final Map<MediaID, Status> statuses = new HashMap<>(); // statuses are needed because history size is limited
    private final Deque<MediaID> history = new ArrayDeque<>();

    private @Nullable MediaID current;
    private @Nullable LifecycleManager lifecycleManager;

    public PlaylistManager(@NotNull PlaylistListener playlistListener) {
        this.playlistListener = playlistListener;
    }

    @Override
    public void setRandom(boolean state) {
        random = state;
    }

    @Override
    public void setRepeatable(boolean state) {
        repeatable = state;
    }

    @Override
    public void addContent(@NotNull MediaID... medias) {
        // public API, so caller may ignore @NotNull
        // noinspection ConstantConditions
        if (medias == null)
            return;
        synchronized (lock) {
            playlist.addAll(Arrays.asList(medias));
        }
        playlistListener.onChanged();
    }

    @Override
    public void removeContent(@NotNull MediaID toRemove) {
        // public API, so caller may ignore @NotNull
        // noinspection ConstantConditions
        if (toRemove == null)
            return;
        synchronized (lock) {
            if (toRemove.equals(current))
                next();
            history.removeIf(candidate -> candidate.equals(toRemove));
            playlist.remove(toRemove);
            statuses.remove(toRemove);
        }
        playlistListener.onChanged();
    }

    @Override
    public void clear() {
        synchronized (lock) {
            cleanup();
        }
        playlistListener.onChanged();
    }

    public void shutdown() {
        cleanup();
    }

    private void cleanup() {
        playlist.clear();
        statuses.clear();
        history.clear();
        if (current != null) {
            // noinspection ConstantConditions
            lifecycleManager.shutdown();
            lifecycleManager = null;
        }
        current = null;
    }


    @Override
    public void shuffle() {
        statuses.clear();
        if (current != null)
            statuses.put(current, Status.isBeingPlayed);
        Collections.shuffle(playlist);
        playlistListener.onChanged();
    }

    @Override
    public boolean skipTo(int index) {
        synchronized (lock) {
            if (index > playlist.size())
                return false;

            MediaID coming = playlist.get(index);
            //noinspection SimplifiableIfStatement
            if (coming == null)
                return false;

            return skipTo(coming, true);
        }
    }

    @Override
    public boolean skipTo(@NotNull MediaID coming) {
        // Annotated as not null, but in case 3rd party caller ignores it, it's checked for null
        // noinspection ConstantConditions
        if (coming == null)
            return false;

        synchronized (lock) {
            if (!playlist.contains(coming))
                addContent(coming);

            return skipTo(coming, true);
        }
    }

    // outer synchronization required
    private boolean skipTo(@NotNull MediaID coming, boolean add2History) {
        log.entering("PlaylistManager", "skipTo", coming);
        if (current != null) {
            statuses.put(current, Status.played);

            if (add2History) {
                // control history size by ourselves
                if (history.size() > MAX_HISTORY_SIZE)
                    history.pollFirst();
                history.offerLast(current);
            }

            // noinspection ConstantConditions
            lifecycleManager.shutdown();
            lifecycleManager = null;
        }

        current = coming;
        statuses.put(coming, Status.isBeingPlayed);

        playlistListener.onNext(coming);

        lifecycleManager = GlomexPlayerFactory.instance(LifecycleManager.class);
        lifecycleManager.open(coming);

        return true;
    }

    @Override
    public void onLifecycleCompleted(@NotNull MediaID mediaID) {
        synchronized (lock) {
            if (mediaID.equals(current))
                next();
        }
    }

    @Override
    public boolean next() {
        return traverse(this::findNext, true);
    }

    @Override
    public boolean prev() {
        return traverse(this::findPrev, false);
    }

    private boolean traverse(Supplier<MediaID> mediaFinder, boolean add2History) {
        synchronized (lock) {
            if (playlist.isEmpty())
                return false;

            MediaID coming = mediaFinder.get();
            if (coming == null) {
                playlistListener.onFinished();
                return false;
            } else {
                skipTo(coming, add2History);
                return true;
            }
        }
    }

    // outer synchronization required
    // todo: could be enforced with ReentranceLock, but this will pollute code with dev-time checks
    private MediaID findNext() {
        Integer nextIndex = null;
        if (!repeatable) {
            Stream<MediaID> stream = playlist.stream()
                .filter(mediaID -> {
                    Status s = statuses.get(mediaID);
                    return s == null || Status.nonPlayed == s;
                });
            if (random) {
                List<Integer> nonPlayed = stream
                    .map(playlist::indexOf)
                    .collect(Collectors.toList());
                if (!nonPlayed.isEmpty()) {
                    int random = new Random().nextInt(nonPlayed.size());
                    nextIndex = nonPlayed.get(random);
                }
            } else {
                int currentIndex = current == null ? 0 : playlist.indexOf(current);
                nextIndex = stream
                    .map(content -> {
                        int i = playlist.indexOf(content) - currentIndex;
                        if (i < 0)
                            i += currentIndex;
                        return i;
                    })
                    .min(Comparator.<Integer>naturalOrder())
                    .map(index -> index + currentIndex)
                    .orElse(null);
            }
        } else {
            if (random)
                nextIndex = new Random().nextInt(playlist.size());
            else {
                if (current == null)
                    nextIndex = 0;
                else {
                    nextIndex = playlist.indexOf(current) + 1;
                    if (nextIndex > playlist.size())
                        nextIndex = 0;
                }
            }
        }
        return nextIndex != null? playlist.get(nextIndex) : null;
    }

    // outer synchronization required
    private MediaID findPrev() {
        // improve: think if prev should work directly without history in non-random
        MediaID previous = history.pollLast();
        // history is empty, or element in history was removed from playlist in race (just in case)
        // the last can't happens because history is synchronized with playlist,
        // don't want to pollute code with anti-dev mistakes code
        if (previous == null) { // || !playlist.contains(previous)) {
            int index;
            if (random) {
                index = new Random().nextInt(playlist.size());
            } else {
                index = current == null ? 0 : playlist.indexOf(current);
                index--;
                if (index < 0)
                    index = playlist.size();
            }
            previous = playlist.get(index);
        }
        return previous;
    }

}
