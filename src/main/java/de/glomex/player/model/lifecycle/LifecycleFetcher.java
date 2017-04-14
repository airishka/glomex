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
import de.glomex.player.model.playback.PlaybackControllerAdapter;
import de.glomex.player.model.playback.WaitinglaybackController;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcher {

    private static final Logger log = Logging.getLogger(LifecycleManager.class);

    private final @NotNull ExecutionManager executor;
    private final @NotNull EtcController etcController;
    private final @NotNull LifecycleListener lifecycleListener;

    private Future<MediaData> mediaFuture;
    private Future<List<AdData>> adsFuture;

    private Lifecycle lifecycle;

    private final CountDownLatch latch = new CountDownLatch(2);

    @Inject
    public LifecycleFetcher(
        @NotNull ExecutionManager executor,
        @NotNull EtcController etcController,
        @NotNull LifecycleListener lifecycleListener,
        @NotNull PlaybackListener playbackListener,
        @NotNull ActionDispatcher actionDispatcher
    ) {
        this.executor = executor;
        this.etcController = etcController;
        this.lifecycleListener = lifecycleListener;
    }

    public void fetch(@NotNull MediaID mediaID) {
        lifecycle = new Lifecycle(mediaID);

        mediaFuture = executor.submit(() -> {
            MediaData mediaData = null;
            try {
                mediaData = etcController.mediaResolver().resolve(mediaID);
                lifecycle.media = mediaData;
                lifecycleListener.onMediaResolved(mediaID);
            } catch (Throwable error) {
                log.severe("Error getting media " + mediaID + ": " + error.getMessage());
                lifecycleListener.onMediaError(mediaID);
                // N.B. cancel add loading if still running
                cancelAdsFetch();
            } finally {
                latch.countDown();
            }
            return mediaData;
        });

        adsFuture = executor.submit(() -> {
            List<AdData> ads = null;
            try {
                ads = etcController.adResolver().resolve(mediaID);
                lifecycle.ads(ads);
                lifecycleListener.onAdsResolved(mediaID);
            } catch (Throwable error) {
                log.warning("Error getting ads " + mediaID + ": " + error.getMessage());
                log.warning("Skipping ads");
                lifecycleListener.onAdError(mediaID);
            } finally {
                latch.countDown();
            }
            return ads;
        });

        // Lock this thread and wait until
        if (etcController.autoplay())
            playbackController.play();

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.severe("Interrupted: " + e.getMessage());
        }



    {
        // fixme
        WaitinglaybackController coming = new WaitinglaybackController();
        PlaybackControllerAdapter previous = actionDispatcher.playbackController(coming);
        coming.shouldPlay(previous.shouldPlay());
    }

}

    // FIXME!!!!!!!!!!!!!!!!!!!!11 this is called from executor thread, FX is angry
    // FIXME: instead: make main thread call it when ready
    private void checkIfReady() {
        if (!lifecycle.ready())
            return;

        // PlaybackController coming = new PlaybackController(); // fixme
        PlaybackControllerAdapter coming = mockPlayer(); // mock: remove

        PlaybackControllerAdapter previous = actionDispatcher.playbackController(coming);
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

        PlaybackControllerAdapter coming = new PlaybackControllerAdapter();
        PlaybackControllerAdapter previous = actionDispatcher.playbackController(coming);
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



//    public Lifecycle lifecycle() {
//        return lifecycle.ready()? lifecycle : obtainLifecycle();
//    }

//    private Lifecycle obtainLifecycle() {
//        try {
//            lifecycle.media = mediaFuture.get();
//            lifecycleListener.onMediaResolved(lifecycle.mediaID);
//        } catch (InterruptedException interrupted) {
//            log.fine("Getting media interrupted " + lifecycle.mediaID);
//            cancelAdsFetch();
//            return lifecycle;
//        } catch (ExecutionException error) {
//            log.severe("Error getting media " + lifecycle.mediaID + ": " + error.getCause().getMessage());
//            cancelAdsFetch();
//            return lifecycle;
//        } finally {
//            mediaFuture = null;
//        }
//
//        check for ads
//        try {
//            lifecycle.ads(adsFuture.get());
//            lifecycleListener.onAdsResolved(lifecycle.mediaID);
//        } catch (InterruptedException interrupted) {
//            log.fine("Getting ads interrupted " + lifecycle.mediaID);
//        } catch (ExecutionException error) {
//            log.warning("Error getting ads " + lifecycle.mediaID + ": " + error.getCause().getMessage());
//            log.warning("Skipping ads");
//        } finally {
//            adsFuture = null;
//        }
//
//        lifecycle.resolve();
//        log.finest(Arrays.toString(lifecycle.stops().toArray()));
//
//        return lifecycle;
//    }

}
