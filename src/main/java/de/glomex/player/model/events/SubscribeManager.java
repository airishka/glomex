package de.glomex.player.model.events;

import de.glomex.player.api.events.Listener;
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

    private final Map<Class<? extends Listener>, Set<? extends Listener>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <L extends Listener> void registerListener(@NotNull L listener) {
        Class<L> type = (Class<L>) listener.getClass();
        synchronized (lock) {
            for (Class<L> probe : EventHandler.types) {
                if (probe.isAssignableFrom(type)) {
                    Set<L> set;
                    if (!listeners.containsKey(probe)) {
                        set = new HashSet<>();
                        listeners.put(probe, set);
                    } else {
                        set = (Set<L>) listeners.get(probe);
                    }
                    set.add(listener);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <L extends Listener> void unregisterListener(@NotNull L listener) {
        Class<L> type = (Class<L>) listener.getClass();
        synchronized (lock) {
            for (Class<L> probe : EventHandler.types) {
                if (probe.isAssignableFrom(type)) {
                    if (listeners.containsKey(probe)) {
                        Set<L> set = (Set<L>) listeners.get(probe);
                        set.remove(listener);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <L extends Listener> Set<L> get(@NotNull Class<L> type) {
        synchronized (lock) {
            return listeners.containsKey(type) ? (Set<L>) listeners.get(type) : Collections.<L>emptySet();
        }
    }
}
