package de.glomex.player.model.api;

import de.glomex.player.api.playback.PlaybackControl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class ActionDispatcher {

    private static final Logger log = Logger.getLogger(ActionDispatcher.class.getName());

    private final Object lock = new Object();

    private final PlaybackControl playbackProxy;

    private PlaybackControl playbackDelegate;

    public ActionDispatcher() {
        playbackProxy = (PlaybackControl) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{PlaybackControl.class},
            this::delegate
        );
    }

    void playbackController(PlaybackControl delegate) {
        synchronized (lock) {
            this.playbackDelegate = delegate;
        }
    }

    public PlaybackControl playbackController() {
        synchronized (lock) {
            return playbackProxy;
        }
    }

    private Object delegate(Object proxy, Method method, Object[] args) {
        synchronized (lock) {
            log.finest(method.getName() + "(" + (args == null? "" : Arrays.toString(args)) + ")");
            if (playbackDelegate != null)
                try {
                    return method.invoke(playbackDelegate, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.severe("Implementation error: " + e);
                    throw new RuntimeException(e);
                }
            else
                return null;
        }
    }

}
