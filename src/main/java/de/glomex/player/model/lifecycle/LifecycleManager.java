package de.glomex.player.model.lifecycle;

import com.google.inject.Inject;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.ExecutionManager;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.events.EventHandler;
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
    private final @NotNull EventHandler eventHandler;

    private Future<MediaData> mediaFuture;
    private Future<List<AdData>> adsFuture;

    private Lifecycle lifecycle;

    @Inject
    public LifecycleManager(
        @NotNull ExecutionManager executor,
        @NotNull EtcController etcController,
        @NotNull EventHandler eventHandler
    ) {
        this.executor = executor;
        this.etcController = etcController;
        this.eventHandler = eventHandler;
    }

    public void open(@NotNull MediaID mediaID) {
        lifecycle = new Lifecycle(mediaID);

        mediaFuture = executor.submit(
            () -> etcController.mediaResolver().resolve(mediaID)
        );
        adsFuture = executor.submit(
            () -> etcController.adResolver().resolve(mediaID)
        );
    }

    public void shutdown() {
        cancelMedia();
        cancelAd();
    }

    private void cancelMedia() {
        if (mediaFuture != null) {
            mediaFuture.cancel(true);
            mediaFuture = null;
        }
    }

    private void cancelAd() {
        if (adsFuture != null) {
            adsFuture.cancel(true);
            adsFuture = null;
        }
    }

    public Lifecycle lifecycle() {
        return mediaFuture != null || adsFuture != null? obtainLifecycle() : lifecycle;
    }

    private Lifecycle obtainLifecycle() {
        LifecycleListener listener;
        // check for media
        try {
            lifecycle.media = mediaFuture.get();
            listener = eventHandler.listener(LifecycleListener.class);
            listener.onMediaResolved(lifecycle.mediaID);
        } catch (InterruptedException interrupted) {
            log.fine("Getting media interrupted " + lifecycle.mediaID);
            cancelAd();
            return lifecycle;
        } catch (ExecutionException error) {
            log.severe("Error getting media " + lifecycle.mediaID + ": " + error.getCause().getMessage());
            cancelAd();
            return lifecycle;
        } finally {
            mediaFuture = null;
        }

        // check for ads
        try {
            lifecycle.ads(adsFuture.get());
            listener.onAdResolved(lifecycle.mediaID);
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
