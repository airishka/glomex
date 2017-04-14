package de.glomex.player.model.lifecycle;

import com.google.inject.Inject;
import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.javafx.JavaFXPlayer;
import de.glomex.player.model.api.ActionDispatcher;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.ExecutionManager;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.playback.PlaybackControllerAdapter;
import de.glomex.player.model.playback.WaitingPlaybackController;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private static final Logger log = Logging.getLogger(LifecycleManager.class);

    private final @NotNull ExecutionManager executorManager;
    private final @NotNull EtcController etcController;
    private final @NotNull LifecycleListener lifecycleListener;
    private final @NotNull PlaybackListener playbackListener;
    private final @NotNull ActionDispatcher actionDispatcher;

    private LifecycleFetcher lifecycleFetcher;

    Lifecycle lifecycle;

    @Inject
    public LifecycleManager(
        @NotNull ExecutionManager executorManager,
        @NotNull EtcController etcController,
        @NotNull LifecycleListener lifecycleListener,
        @NotNull PlaybackListener playbackListener,
        @NotNull ActionDispatcher actionDispatcher
    ) {
        this.executorManager = executorManager;
        this.etcController = etcController;
        this.lifecycleListener = lifecycleListener;
        this.playbackListener = playbackListener;
        this.actionDispatcher = actionDispatcher;
    }

    public void open(@NotNull MediaID mediaID) {
        lifecycleFetcher = new LifecycleFetcher(executorManager, etcController, lifecycleListener);
        lifecycleFetcher.startFetching(mediaID, this::lifecycle);
    }

    void lifecycle(@NotNull Lifecycle lifecycle) {
        this.lifecycle = lifecycle;

        // PlaybackController coming = new PlaybackController();
        // mock: replace with proper playback adapter when implemented
        JavaFXPlayer coming = new JavaFXPlayer(playbackListener, etcController.autoplay(), etcController.fullscreen());

        WaitingPlaybackController previous = (WaitingPlaybackController) actionDispatcher.playbackController(coming);
        //noinspection ConstantConditions
        coming.openMedia(lifecycle.media());
        Boolean shouldPlay = null;
        if (previous != null)
            shouldPlay = previous.shouldPlay();
        if (shouldPlay == null)
            shouldPlay = etcController.autoplay();

        if (shouldPlay)
            coming.play();
    }

    public void shutdown() {
        lifecycleFetcher.shutdown();

        WaitingPlaybackController coming = new WaitingPlaybackController();
        PlaybackControllerAdapter previous = (PlaybackControllerAdapter) actionDispatcher.playbackController(coming);
        coming.shouldPlay(previous.isPlaying());

        previous.shutdown();
    }

}
