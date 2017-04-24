package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Contract for this class is:
 *   - being given a content ID, create lifecycle object
 *   - populate it with data obtained from resolvers, asynchronously and in protected way
 *   - hide synchronization
 *
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcher {

    private static final Logger log = Logging.getLogger(LifecycleFetcher.class);

    private final @NotNull Lifecycle lifecycle;

    private Future<Void> future;

    LifecycleFetcher(@NotNull MediaID mediaID) {
        lifecycle = new Lifecycle(mediaID);
    }

    public void fetch(@NotNull Consumer<Lifecycle> callback) {
        EtcController etcController = GlomexPlayerFactory.instance(EtcController.class);
        LifecycleListener lifecycleListener = GlomexPlayerFactory.instance(LifecycleListener.class);

        CompletableFuture<Void> adsFuture = CompletableFuture
            .supplyAsync(() -> etcController.adResolver().resolve(lifecycle.mediaID))
            .handle((ads, error) -> {
                if (error == null) {
                    lifecycle.ads(ads);
                    lifecycleListener.onAdsResolved(lifecycle.mediaID);
                } else {
                    log.warning("Error resolving ads: " + error.getMessage());
                    log.warning("Skipping ads");
                    lifecycleListener.onAdsError(lifecycle.mediaID, error.getMessage());
                }
                return null;
            });

        CompletableFuture<Void> contentFuture = CompletableFuture
            .supplyAsync(() -> etcController.contentResolver().resolve(lifecycle.mediaID))
            .handle((content, error) -> {
                if (error == null) {
                    lifecycle.content(content);
                    lifecycleListener.onContentResolved(lifecycle.mediaID);
                } else {
                    log.severe("Error resolving content: " + error.getMessage());
                    lifecycleListener.onContentError(lifecycle.mediaID, error.getMessage());
                    adsFuture.cancel(false);
                }
                return null;
            });

        future = CompletableFuture
            .allOf(adsFuture, contentFuture)
            .exceptionally(error -> {
                if (!(error.getCause() instanceof CancellationException))
                    log.severe("Exception in resolvers: " + error.getMessage());
                return null;
            })
            .thenRun(() ->
                callback.accept(lifecycle
                ));
    }

    public void shutdown() {
        if (future != null)
            future.cancel(true);
    }

}
