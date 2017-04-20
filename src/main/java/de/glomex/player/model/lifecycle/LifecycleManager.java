package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.javafx.JavaFXPlayer;
import de.glomex.player.model.api.ActionDispatcher;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.playback.EmptyPlaybackListener;
import de.glomex.player.model.playback.PlaybackControllerAdapter;
import de.glomex.player.model.playback.WaitingPlaybackController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private static final Logger log = Logging.getLogger(LifecycleManager.class);

    private final @NotNull LifecycleListener lifecycleListener;
    private final @NotNull ActionDispatcher actionDispatcher;

    private final @NotNull LifecycleFetcher lifecycleFetcher;

    private @Nullable Lifecycle lifecycle;

    public LifecycleManager(@NotNull MediaID mediaID) {
        this.lifecycleListener = GlomexPlayerFactory.instance(LifecycleListener.class);
        this.actionDispatcher = GlomexPlayerFactory.instance(ActionDispatcher.class);

        lifecycleListener.onLifecycleStarted(mediaID);
        lifecycleFetcher = new LifecycleFetcher(mediaID);
        lifecycleFetcher.fetch(this::lifecycle);
    }

    private void lifecycle(@NotNull Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        if (lifecycle.content() == null) {
            lifecycleListener.onLifecycleError(lifecycle.mediaID);
            shutdown();
            return;
        }

        lifecycle.resolve();

        // todo: add /register/ itself as playback listener
        // todo: iterate via content and add
        playItem(); // mock
    }

    private void playItem() {
        // mock: replace with proper playback adapter when implemented
        PlaybackListener playbackListener = GlomexPlayerFactory.instance(PlaybackListener.class);
        EtcController etcController = GlomexPlayerFactory.instance(EtcController.class);
        SubscribeManager subscribeManager = GlomexPlayerFactory.instance(SubscribeManager.class);
        subscribeManager.registerListener(new EmptyPlaybackListener() {
            @Override
            public void onFinished() {
                assert lifecycle != null;
                complete(lifecycle.mediaID);
            }
        });
        JavaFXPlayer coming = new JavaFXPlayer(playbackListener, etcController.autoplay(), etcController.fullscreen());
        // mock: end

        // normal code
        WaitingPlaybackController previous = (WaitingPlaybackController) actionDispatcher.playbackController(coming);
        //noinspection ConstantConditions
        coming.openMedia(lifecycle.content());
        Boolean shouldPlay = null;
        if (previous != null)
            shouldPlay = previous.shouldPlay();
        if (shouldPlay == null)
            shouldPlay = etcController.autoplay();

        if (shouldPlay)
            coming.play();
    }

    private void complete(@NotNull MediaID mediaID) {
        lifecycleListener.onLifecycleCompleted(mediaID);
        shutdown();
    }

    public void shutdown() {
        lifecycleFetcher.shutdown();

        // todo: remove itself as playback listener

        WaitingPlaybackController coming = new WaitingPlaybackController();
        PlaybackControllerAdapter previous = (PlaybackControllerAdapter) actionDispatcher.playbackController(coming);
        coming.shouldPlay(previous.isPlaying());

        previous.shutdown();
    }

}
