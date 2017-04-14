package de.glomex.player.model.lifecycle;

import com.google.inject.Inject;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.javafx.JavaFXPlayer;
import de.glomex.player.model.api.ActionDispatcher;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.ExecutionManager;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.playback.PlaybackController;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private static final Logger log = Logging.getLogger(LifecycleManager.class);

    private final @NotNull ExecutionManager executor;
    private final @NotNull EtcController etcController;
    private final @NotNull LifecycleListener lifecycleListener;
    private final @NotNull PlaybackListener playbackListener;
    private final @NotNull ActionDispatcher actionDispatcher;

    private Future<MediaData> mediaFuture;
    private Future<List<AdData>> adsFuture;

    private Lifecycle lifecycle;

    @Inject
    public LifecycleManager(
        @NotNull ExecutionManager executor,
        @NotNull EtcController etcController,
        @NotNull LifecycleListener lifecycleListener,
        @NotNull PlaybackListener playbackListener,
        @NotNull ActionDispatcher actionDispatcher
    ) {
        this.executor = executor;
        this.etcController = etcController;
        this.lifecycleListener = lifecycleListener;
        this.playbackListener = playbackListener;
        this.actionDispatcher = actionDispatcher;
    }

    public void open(@NotNull MediaID mediaID) {
        lifecycle = new Lifecycle(mediaID);

        mediaFuture = executor.submit(() -> {
            MediaData mediaData = null;
            try {
                mediaData = etcController.mediaResolver().resolve(mediaID);
                lifecycle.media = mediaData;
                lifecycleListener.onMediaResolved(mediaID);
                checkIfReady();
            } catch (Throwable error) {
                log.severe("Error getting media " + mediaID + ": " + error.getMessage());
                lifecycleListener.onMediaError(mediaID);
                // N.B. cancel add loading if still running
                cancelAdsFetch();
            }
            return mediaData;
        });

        adsFuture = executor.submit(() -> {
            List<AdData> ads = null;
            try {
                ads = etcController.adResolver().resolve(mediaID);
                lifecycle.ads(ads);
                lifecycleListener.onAdsResolved(mediaID);
                checkIfReady();
            } catch (Throwable error) {
                log.warning("Error getting ads " + mediaID + ": " + error.getMessage());
                log.warning("Skipping ads");
                lifecycleListener.onAdError(mediaID);
            }
            return ads;
        });
    }

    private void checkIfReady() {
        if (!lifecycle.ready())
            return;

        // PlaybackController coming = new PlaybackController(); // fixme
        PlaybackController coming = mockPlayer(); // mock: remove

        PlaybackController previous = actionDispatcher.playbackController(coming);
        Boolean shouldPlay = null;
        if (previous != null)
            shouldPlay = previous.shouldPlay();
        if (shouldPlay == null)
            shouldPlay = etcController.autoplay();

        if (shouldPlay)
            coming.play();
    }

    public void shutdown() {
        cancelMediaFetch();
        cancelAdsFetch();

        PlaybackController coming = new PlaybackController();
        PlaybackController previous = actionDispatcher.playbackController(coming);
        coming.shouldPlay(previous.shouldPlay());

        previous.shutdown();
        // mock: remove
        if (player != null)
            player.shutdown();
    }

    private void cancelMediaFetch() {
        if (mediaFuture != null) {
            mediaFuture.cancel(true);
            mediaFuture = null;
        }
    }

    private void cancelAdsFetch() {
        if (adsFuture != null) {
            adsFuture.cancel(true);
            adsFuture = null;
        }
    }

    // mock: remove it
    private JavaFXPlayer player;

    // mock: remove it
    PlaybackController mockPlayer() {
        return new PlaybackController() {
            public void play() {
                super.play();
                if (player == null) {
                    player = new JavaFXPlayer();
                    player.eventListener = playbackListener;
                    player.openMedia(lifecycle.media);
                } else
                    player.play();
            }

            public void pause() {
                super.pause();
                player.pause();
            }

            public void seek(long position) {
                player.seek(position);
            }

            public long getPosition() {
                return player.getPosition();
            }
        };
    }


    public Lifecycle lifecycle() {
        return lifecycle.ready()? lifecycle : obtainLifecycle();
    }

    private Lifecycle obtainLifecycle() {
        try {
            lifecycle.media = mediaFuture.get();
            lifecycleListener.onMediaResolved(lifecycle.mediaID);
        } catch (InterruptedException interrupted) {
            log.fine("Getting media interrupted " + lifecycle.mediaID);
            cancelAdsFetch();
            return lifecycle;
        } catch (ExecutionException error) {
            log.severe("Error getting media " + lifecycle.mediaID + ": " + error.getCause().getMessage());
            cancelAdsFetch();
            return lifecycle;
        } finally {
            mediaFuture = null;
        }

        // check for ads
        try {
            lifecycle.ads(adsFuture.get());
            lifecycleListener.onAdsResolved(lifecycle.mediaID);
        } catch (InterruptedException interrupted) {
            log.fine("Getting ads interrupted " + lifecycle.mediaID);
        } catch (ExecutionException error) {
            log.warning("Error getting ads " + lifecycle.mediaID + ": " + error.getCause().getMessage());
            log.warning("Skipping ads");
        } finally {
            adsFuture = null;
        }

        lifecycle.resolve();
        log.finest(Arrays.toString(lifecycle.stops().toArray()));

        return lifecycle;
    }

}
