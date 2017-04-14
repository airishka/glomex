package de.glomex.player.model.api;

import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.model.playback.PlaybackControllerAdapter;
import de.glomex.player.model.playback.WaitinglaybackController;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class ActionDispatcher {

    private static final Logger log = Logging.getLogger(ActionDispatcher.class);

    private final Object lock = new Object();

    private final PlaybackControl playbackProxy;

    private PlaybackControllerAdapter playbackDelegate;

    public ActionDispatcher() {
        playbackProxy = (PlaybackControl) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{PlaybackControl.class},
            this::delegate
        );
        playbackDelegate = new WaitinglaybackController();
    }

    public @NotNull PlaybackControl playbackProxy() {
        return playbackProxy;
    }

    public @NotNull
    PlaybackControllerAdapter playbackDelegate() {
        return playbackDelegate;
    }

    public PlaybackControllerAdapter playbackController(@NotNull PlaybackControllerAdapter delegate) {
        synchronized (lock) {
            PlaybackControllerAdapter previous = this.playbackDelegate;
            this.playbackDelegate = delegate;
            return previous;
        }
    }

    private Object delegate(Object proxy, Method method, Object[] args) {
        synchronized (lock) {
            log.finest(method.getName() + "(" + (args == null? "" : Arrays.toString(args)) + ")");
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
