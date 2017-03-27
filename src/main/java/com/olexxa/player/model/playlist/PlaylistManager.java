package com.olexxa.player.model.playlist;

import com.olexxa.player.api.playlist.Content;
import com.olexxa.player.api.playlist.PlaylistControl;
import com.olexxa.player.model.api.ContentImpl;
import com.olexxa.player.model.lifecycle.LifecycleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This object allows to
 * - manage playlist
 * - navigate on it
 * <p>
 * It doesn't control playback still.
 * Commands like next(), prev(), skipTo() doesn't trigger elements to be player,
 * it just send command to lifecycle manager to open content.
 * <p>
 * To start playing it, either play() command must be issued, or autoplay feature should be turned on
 * <p>
 * IMPROVE: playlist can be extracted to separate object; manager will only contain operations, whilst playlist will keep statuses, current selection and options
 * <p>
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistManager implements PlaylistControl {

    private static final int MAX_HISTORY_SIZE = 20;

    private enum Status {nonPlayed, isBeingPlayed, played}

    private final LifecycleManager lifecycleManager;

    private final Object lock = new Object();
    private final List<Content> playlist = new ArrayList<>();
    // improve: history + currentContent duplicates statuses
    private final Map<UUID, Status> statuses = new HashMap<>();
    private final Deque<Content> history = new ArrayDeque<>(MAX_HISTORY_SIZE);
    @Nullable
    private Content currentContent;

    private boolean repeatable;
    private boolean random;

    public PlaylistManager(@NotNull LifecycleManager lifecycleManager) {
        this.lifecycleManager = lifecycleManager;
    }

    @Override
    public void addContent(URL... urls) {
        Stream.of(urls)
            .map(url -> (Content) new ContentImpl(UUID.randomUUID(), url))
            .forEach(playlist::add);
    }

    @Override
    public void addContent(Content... contents) {
        playlist.addAll(Arrays.asList(contents));
    }

    @Override
    public void removeContent(@Nullable Content contentToRemove) {
        if (contentToRemove == null)
            return;
        synchronized (lock) {
            if (contentToRemove.equals(currentContent))
                next();
            playlist.remove(contentToRemove);
            statuses.remove(contentToRemove.uuid());
            history.removeIf(candidate -> candidate.equals(contentToRemove));
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            playlist.clear();
            statuses.clear();
            history.clear();
            if (currentContent != null)
                lifecycleManager.close();
            currentContent = null;
        }
    }

    @Override
    public void shuffle() {
        statuses.clear();
        if (currentContent != null)
            statuses.put(currentContent.uuid(), Status.isBeingPlayed);
        Collections.shuffle(playlist);
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
    public boolean skipTo(@NotNull Content nextContent) {
        if (nextContent == null)
            return false;

        if (currentContent != null) {
            statuses.put(currentContent.uuid(), Status.played);
            if (history.size() > MAX_HISTORY_SIZE)
                history.pollFirst();
            history.offerLast(currentContent);
            lifecycleManager.close();
        }

        if (!playlist.contains(nextContent))
            addContent(nextContent);

        currentContent = nextContent;
        statuses.put(nextContent.uuid(), Status.isBeingPlayed);
        lifecycleManager.open(nextContent);

        // TODO: fire NEXT ITEM

        return true;
    }

    @Override
    public boolean next() {
        // todo: handle full list, events
        synchronized (lock) {
            if (playlist.isEmpty())
                return false;

            Integer nextIndex = null;
            if (!repeatable) {
                Stream<Content> stream = playlist.stream()
                    .filter(content -> {
                        Status s = statuses.get(content.uuid());
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
                    int currentIndex = currentContent == null ? 0 : playlist.indexOf(currentContent);
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
                    if (currentContent == null)
                        nextIndex = 0;
                    else {
                        nextIndex = playlist.indexOf(currentContent) + 1;
                        if (nextIndex > playlist.size())
                            nextIndex = 0;
                    }
                }
            }
            if (nextIndex == null) {
                // todo: fire END OF LIST
                return false;
            } else {
                Content nextContent = playlist.get(nextIndex);
                skipTo(nextContent);
                return true;
            }
        }
    }

    @Override
    public boolean prev() {
        // improve: think if prev should work directly without history in non-random
        Content nextContent = history.pollLast();
        if (nextContent == null) {
            int index = currentContent == null ? 0 : playlist.indexOf(currentContent);
            index--;
            if (index < 0)
                index = playlist.size();
            nextContent = playlist.get(index);
        }
        if (nextContent == null)
            return false;
        else {
            skipTo(nextContent);
            return true;
        }
    }

}
