package de.glomex.player.model.events;

import de.glomex.player.api.events.Listener;
import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.PlaylistListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * This class dispatches event to underlying systems:
 *  - tracking
 *  - api callbacks
 *  - loggers
 *
 * This class contract is to:
 *   1) populate events with data from content registry (which absent in event itself but needed for reporting)
 *   2) transform events into proper format
 *   3) call underlying components
 *
 * There are could be several loggers, each will be notified.
 * Loggers will have event already transformed to string representation.
 *
 * Listeners will be notified asynchronously, with non-blocking approach.
 * Code won't wait listeners' answer to notify the next one.
 * Errors in callbacks are ignored. // improve: such listener could be marked as bad and turned off
 * Results are not returned.
 *
 * FIXME: this mock implementation mixes all sub-handlers all together...
 *
 * Created by <b>me@olexxa.com</b>
 */
public class EventHandler {

    public static final Logger log = Logger.getLogger(EventHandler.class.getName());

    static final Class[] types = new Class[] {PlaylistListener.class, LifecycleListener.class, PlaybackListener.class};

    @SuppressWarnings("unchecked")
    private static final List<Class<? extends Listener>> listenerTypes = Arrays.asList(types);

    private final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);

    private final Listener proxyListener;
    private final SubscribeManager subscribeManager;

    private final List<EventLogger> loggers = new ArrayList<>();
    private final EventTracker eventTracker;

    public EventHandler(SubscribeManager subscribeManager) {
        this.eventTracker = new EventTracker(); // fixme: implement tracker
        this.subscribeManager = subscribeManager;
        proxyListener = (Listener) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            types,
            this::invocationHandler
        );
    }

    public void addLogger(@NotNull EventLogger logger) {
        loggers.add(logger);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <L extends Listener> L listener(@NotNull Class<L> type) {
        // should be assert - this is internal development error
        if (listenerTypes.contains(type))
            return (L) proxyListener;
        throw new IllegalStateException("Listener type " + type.getName() + " isn't supported");
    }

    @Nullable
    private Object invocationHandler(@NotNull Object proxy, @NotNull Method method, @Nullable Object[] args) {
        // improve: do we need to block this until everything is completed?...

        String message = createLogMessage(method, args);
        // Loggers, synchronous
        for (EventLogger logger: loggers)
            logger.logEvent(message);

        // Callbacks
        @SuppressWarnings("unchecked")
        Class<? extends Listener> type = (Class<? extends Listener>) method.getDeclaringClass();
        for (Listener listener: subscribeManager.get(type))
            proceed(listener, method, args);

        // Tracker
        executor.execute(() -> eventTracker.trackEvent(message));

        return null;
    }

    // todo: it's assumed that all listeners' methods return no value
    private void proceed(@NotNull Listener listener, @NotNull Method method, @Nullable  Object[] args) {
        executor.execute(() -> {
            try {
                method.invoke(listener, args);
            // fixme: this is for debug
            } catch (InvocationTargetException | IllegalAccessException e) {
            //} catch (Throwable e) {
                log.warning("Exception calling listener " + e.getMessage());
            }
        });
    }

    private String createLogMessage(@NotNull Method method, @Nullable Object[] args) {
        StringBuilder message = new StringBuilder();
        message.append(method.getName());
        if (args != null) {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (Object arg: args)
                joiner.add(arg.toString());
            message.append(joiner.toString());
        }
        return message.toString();
    }

}
