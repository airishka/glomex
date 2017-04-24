package de.glomex.player.model.events;

import de.glomex.player.api.ListenerTag;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.model.InternalTag;
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

    class Storage extends HashMap<Class<? extends ListenerTag>, Set<ListenerTag>> {}

    private final Storage
        internals = new Storage(),
        externals = new Storage();

    @Override
    public void registerListener(@NotNull ListenerTag listener) {
        Class type = listener.getClass();
        Storage target =  listener instanceof InternalTag? internals : externals;
        synchronized (lock) {
            EventHandler.listenerTypes.stream()
                .filter(probe -> probe.isAssignableFrom(type))
                .forEach(probe -> {
                    Set<ListenerTag> set = target.computeIfAbsent(probe, key -> new HashSet<>());
                    set.add(listener);
                });
            //for (Class<? extends ListenerTag> probe : EventHandler.listenerTypes) {
            //    if (probe.isAssignableFrom(type)) {
            //        Set<ListenerTag> set;
            //        if (!target.containsKey(probe)) {
            //            set = new HashSet<>();
            //            target.put(probe, set);
            //        } else {
            //            set = target.get(probe);
            //        }
            //        Set<ListenerTag> set = target.computeIfAbsent(probe, key -> new HashSet<>());
            //        set.add(listener);
            //    }
            //}
        }
    }

    @Override
    public void unregisterListener(@NotNull ListenerTag listener) {
        Class type = listener.getClass();
        Storage target =  listener instanceof InternalTag? internals : externals;
        synchronized (lock) {
            EventHandler.listenerTypes.stream()
                .filter(probe -> probe.isAssignableFrom(type))
                .filter(target::containsKey)
                .forEach(probe -> {
                    Set<ListenerTag> set = target.get(probe);
                    set.remove(listener);
                });
            //for (Class<? extends ListenerTag> probe : EventHandler.listenerTypes) {
            //    if (probe.isAssignableFrom(type)) {
            //        if (target.containsKey(probe)) {
            //            Set<ListenerTag> set = target.get(probe);
            //            set.remove(listener);
            //        }
            //    }
            //}
        }
    }

    public <L extends ListenerTag> Set<L> internals(@NotNull Class<L> type) {
        return get(type, true);
    }

    public <L extends ListenerTag> Set<L> externals(@NotNull Class<L> type) {
        return get(type, false);
    }

    @SuppressWarnings("unchecked")
    private <L extends ListenerTag> Set<L> get(@NotNull Class<L> type, boolean internal) {
        Storage target = internal? internals : externals;
        synchronized (lock) {
            return target.containsKey(type) ? (Set<L>) target.get(type) : Collections.<L>emptySet();
        }
    }

    public void shutdown() {
        externals.clear();
        internals.clear();
    }

}
