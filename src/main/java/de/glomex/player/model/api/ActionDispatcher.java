package de.glomex.player.model.api;

import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.model.playback.WaitingPlaybackController;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

// improve: if this will listen more controls: extract common base multiplexer class for this and event handler
public class ActionDispatcher {

    private static final Logger log = Logging.getLogger(ActionDispatcher.class);

    private final Object lock = new Object();

    private final PlaybackControl playbackProxy;

    private PlaybackControl playbackDelegate;

    public ActionDispatcher() {
        playbackProxy = (PlaybackControl) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{PlaybackControl.class},
            this::delegate
        );
        playbackDelegate = new WaitingPlaybackController();
    }

    public @NotNull PlaybackControl playbackProxy() {
        return playbackProxy;
    }

    public @NotNull PlaybackControl playbackDelegate() {
        return playbackDelegate;
    }

    public PlaybackControl switchController(@NotNull PlaybackControl delegate) {
        synchronized (lock) {
            log.fine("Set delegate: " + delegate);
            PlaybackControl previous = this.playbackDelegate;
            this.playbackDelegate = delegate;
            return previous;
        }
    }

    private Object delegate(Object proxy, Method method, Object[] args) {
        synchronized (lock) {
            log.logp(Level.FINEST, method.getDeclaringClass().getSimpleName(), method.getName(), "", args);
            if (playbackDelegate != null)
                try {
                    return method.invoke(playbackDelegate, args);
                } catch (IllegalAccessException error) {
                    log.severe("Implementation error: " + error);
                    throw new RuntimeException(error);
                } catch (InvocationTargetException error) {
                    log.severe("Error in delegated method: " + error.getTargetException().getMessage());
                    throw new RuntimeException(error.getTargetException());
                }
            else
                return null;
        }
    }

}
