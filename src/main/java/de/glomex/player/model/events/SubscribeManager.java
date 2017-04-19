package de.glomex.player.model.events;

import de.glomex.player.api.ListenerTag;
import de.glomex.player.api.events.SubscribeControl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Listener can implements few types at once.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class SubscribeManager implements SubscribeControl {

    // Plain old lock because of small amount of writes: CHM isn't effective
    private final Object lock = new Object();

    private final Map<Class<? extends ListenerTag>, Set<ListenerTag>> listeners = new HashMap<>();

    @Override
    public void registerListener(@NotNull ListenerTag listener) {
        Class type = listener.getClass();
        synchronized (lock) {
            for (Class<? extends ListenerTag> probe : EventHandler.listenerTypes) {
                if (probe.isAssignableFrom(type)) {
                    Set<ListenerTag> set;
                    if (!listeners.containsKey(probe)) {
                        set = new HashSet<>();
                        listeners.put(probe, set);
                    } else {
                        set = listeners.get(probe);
                    }
                    set.add(listener);
                }
            }
        }
    }

    @Override
    public void unregisterListener(@NotNull ListenerTag listener) {
        Class type = listener.getClass();
        synchronized (lock) {
            for (Class<? extends ListenerTag> probe : EventHandler.listenerTypes) {
                if (probe.isAssignableFrom(type)) {
                    if (listeners.containsKey(probe)) {
                        Set<ListenerTag> set = listeners.get(probe);
                        set.remove(listener);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <L extends ListenerTag> Set<L> get(@NotNull Class<L> type) {
        synchronized (lock) {
            return listeners.containsKey(type) ? (Set<L>) listeners.get(type) : Collections.<L>emptySet();
        }
    }
}
