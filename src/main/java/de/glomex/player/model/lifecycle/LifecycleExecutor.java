package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.media.Media;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.api.ActionDispatcher;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.playback.EmptyPlaybackListener;
import de.glomex.player.model.playback.WaitingPlaybackController;
import de.glomex.player.model.player.MediaPlayer;
import de.glomex.player.model.player.MediaPlayerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleExecutor {

    private static enum Playing { content, ad }

    private final @NotNull ActionDispatcher actionDispatcher;
    private final @NotNull SubscribeManager subscribeManager;
    private final @NotNull LifecycleListener lifecycleListener;

    private final @NotNull PlaybackListener playbackListener;
    private final @NotNull MediaPlayer mediaPlayer;

    private final @NotNull ResolvedLifecycle lifecycle;
    private final @NotNull Consumer<MediaID> callback;

    private Playing playing;
    private Media current;

    boolean played; // mock: remove

    @SuppressWarnings("ConstantConditions")
    LifecycleExecutor(@NotNull ResolvedLifecycle lifecycle, @NotNull Consumer<MediaID> callback) {
        this.actionDispatcher = GlomexPlayerFactory.instance(ActionDispatcher.class);
        this.subscribeManager = GlomexPlayerFactory.instance(SubscribeManager.class);
        this.lifecycleListener = GlomexPlayerFactory.instance(LifecycleListener.class);

        this.lifecycle = lifecycle;
        this.callback = callback;

        playbackListener = createPlaybackListener(callback);

        MediaPlayerFactory mediaPlayerFactory = GlomexPlayerFactory.instance(MediaPlayerFactory.class);
        mediaPlayer = mediaPlayerFactory.createPlayer(lifecycle.content());
    }

    private PlaybackListener createPlaybackListener(@NotNull Consumer<MediaID> callback) {
        PlaybackListener playbackListener = new EmptyPlaybackListener() {
            @Override
            public void onReady() {
                //Consumer<MediaID> function = playing == Playing.content? lifecycleListener::onContentStarted : lifecycleListener::onAdStarted;
                //function.accept(current.id());
                if (playing == Playing.content)
                    lifecycleListener.onContentStarted(current.id());
                else
                    lifecycleListener.onAdStarted(current.id());
            }

            @Override
            public void onError(@NotNull String message) {
                if (playing == Playing.content)
                    lifecycleListener.onContentError(current.id(), message);
                else
                    next();
            }

            @Override
            public void onFinished() {
                if (playing == Playing.content) {
                    lifecycleListener.onContentCompleted(current.id());
                    played = true; // mock: remove
                } else {
                    lifecycleListener.onAdCompleted(current.id());
                }
                next();
            }
        };
        subscribeManager.registerListener(playbackListener);
        return playbackListener;
    }

    public void start() {
        next();
        PlaybackControl previous = actionDispatcher.switchController(mediaPlayer);
        mediaPlayer.seek(previous.getPosition()); // api calls may seek(), so position to be restored
        if (shouldStart(previous))
            mediaPlayer.play();
        // todo: onContentStarted? or later after callback?
    }

    private boolean shouldStart(@Nullable PlaybackControl previous) {
        Boolean shouldStart = null;
        if (previous != null)
            if (previous instanceof WaitingPlaybackController)
                shouldStart = ((WaitingPlaybackController) previous).shouldPlay();
            else
                shouldStart = previous.isPlaying();

        if (shouldStart == null) {
            EtcController etcController = GlomexPlayerFactory.instance(EtcController.class);
            shouldStart = etcController.autoplay();
        }
        return shouldStart;
    }

    public void shutdown() {
        subscribeManager.unregisterListener(playbackListener);

        WaitingPlaybackController coming = new WaitingPlaybackController();
        PlaybackControl previous = actionDispatcher.switchController(coming);
        coming.shouldPlay(previous.isPlaying());

        mediaPlayer.shutdown();
    }

    private void next() { // mock: remove
        // fixme: implement this
        playing = Playing.content;
        current = lifecycle.content;

        if (played) { // mock: remove
            callback.accept(current.id());
            shutdown();
        }
    }

}
