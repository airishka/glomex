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
 * TODO: MediaPlayerFactory & player - move to separate actor.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleExecutor {

    private static enum Playing { content, ad }

    private final @NotNull ActionDispatcher actionDispatcher;
    private final @NotNull SubscribeManager subscribeManager;
    private final @NotNull LifecycleListener lifecycleListener;
    private final @NotNull MediaPlayerFactory mediaPlayerFactory;

    private final @NotNull PlaybackListener playbackListener;
    private final @NotNull MediaPlayer contentPlayer;
    private MediaPlayer player;

    private final @NotNull ResolvedLifecycle lifecycle;
    private final @NotNull Consumer<MediaID> callback;

    private Playing playing;
    private Media current;
    private long position;

    boolean played; // mock: remove

    @SuppressWarnings("ConstantConditions")
    LifecycleExecutor(@NotNull ResolvedLifecycle lifecycle, @NotNull Consumer<MediaID> callback) {
        this.actionDispatcher = GlomexPlayerFactory.instance(ActionDispatcher.class);
        this.subscribeManager = GlomexPlayerFactory.instance(SubscribeManager.class);
        this.lifecycleListener = GlomexPlayerFactory.instance(LifecycleListener.class);

        this.lifecycle = lifecycle;
        this.callback = callback;

        playbackListener = createPlaybackListener();

        mediaPlayerFactory = GlomexPlayerFactory.instance(MediaPlayerFactory.class);
        contentPlayer = mediaPlayerFactory.createPlayer(lifecycle.content());
    }

    private PlaybackListener createPlaybackListener() {
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
                if (playing == Playing.content) {
                    lifecycleListener.onContentError(current.id(), message);
                    shutdown();
                } else
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
        PlaybackControl previous = actionDispatcher.playbackDelegate();
        position = previous.getPosition();
        // if such functionality is needed, it may bypass pre-rolls

        next();

        if (shouldStart(previous))
            contentPlayer.play();
    }

    private void next() { // mock: remove
        if (player != null && player != contentPlayer)
            player.shutdown();

        nextMedia();

        if (current == null) {
            callback.accept(lifecycle.mediaID);
            shutdown();
            return;
        }

        if (playing == Playing.content || mediaPlayerFactory.reusable()) {
            player = contentPlayer;
            // fixme: implement logic for reusable player
        } else {
            player = mediaPlayerFactory.createPlayer(current);
        }
        // todo: deactivate previous :)
        //noinspection unchecked
        mediaPlayerFactory.activate(player);
        actionDispatcher.switchController(player);
        if (playing == Playing.content) {
            player.seek(position); // api calls may seek(), so position to be restored
        }
        player.play();
    }

    private void nextMedia() {
        // fixme: find next lifecycle media by position
        playing = Playing.content;
        current = played? null : lifecycle.content;
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

        contentPlayer.shutdown();
        if (player != contentPlayer)
            player.shutdown();
    }

}
