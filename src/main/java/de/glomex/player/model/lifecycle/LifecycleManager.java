package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private static final Logger log = Logging.getLogger(LifecycleManager.class);

    private final @NotNull LifecycleListener lifecycleListener;

    private final @NotNull LifecycleFetcher lifecycleFetcher;
    private @Nullable LifecycleExecutor lifecycleExecutor;

    public LifecycleManager(@NotNull MediaID mediaID) {
        this.lifecycleListener = GlomexPlayerFactory.instance(LifecycleListener.class);

        lifecycleListener.onLifecycleStarted(mediaID);
        lifecycleFetcher = new LifecycleFetcher(mediaID);
        lifecycleFetcher.fetch(this::lifecycle);
    }

    private void lifecycle(@NotNull Lifecycle lifecycle) {
        if (lifecycle.content() == null) {
            lifecycleListener.onLifecycleError(lifecycle.mediaID, "Error fetching content");
            shutdown();
        } else {
            ResolvedLifecycle resolved = new ResolvedLifecycle(lifecycle);
            lifecycleExecutor = new LifecycleExecutor(resolved, this::complete);
            lifecycleExecutor.start();
        }
    }

    void complete(@NotNull MediaID mediaID) {
        lifecycleListener.onLifecycleCompleted(mediaID);
        shutdown();
    }

    public void shutdown() {
        lifecycleFetcher.shutdown();
        if (lifecycleExecutor != null)
            lifecycleExecutor.shutdown();
    }

}
