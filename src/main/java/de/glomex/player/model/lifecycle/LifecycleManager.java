package de.glomex.player.model.lifecycle;

import com.google.inject.Inject;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.lifecycle.MediaResolver;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.events.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private static final Logger log = Logger.getLogger(LifecycleManager.class.getName());

    @Inject private MediaResolver mediaResolver;
    @Inject private AdResolver adResolver;
    @Inject private EventHandler eventHandler;

    private Future<MediaData> mediaFuture;
    private Future<List<AdData>> adsFuture;

    private Lifecycle lifecycle;

    public void open(@NotNull MediaID mediaID) {
        lifecycle = new Lifecycle(mediaID);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        mediaFuture = executor.submit(
            () -> mediaResolver.resolve(mediaID)
        );
        adsFuture = executor.submit(
            () -> adResolver.resolve(mediaID)
        );
    }

    public void destroy() {
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
        // check for media
        try {
            lifecycle.media = mediaFuture.get();
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
        } catch (InterruptedException interrupted) {
            log.fine("Getting ads interrupted " + lifecycle.mediaID);
        } catch (ExecutionException error) {
            log.warning("Error getting ads " + lifecycle.mediaID + ": " + error.getCause().getMessage());
            log.warning("Skipping ads");
        } finally {
            adsFuture = null;
        }

        lifecycle.resolve();
        System.out.println(lifecycle.stops());

        return lifecycle;
    }

}
