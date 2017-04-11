package de.glomex.player.api.events;

import de.glomex.player.api.etc.ControlTag;
import org.jetbrains.annotations.NotNull;

/**
 * Listener can implement several listener interfaces at once.
 * Supports several listeners of the same type:
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface SubscribeControl extends ControlTag {

    void registerListener(@NotNull ListenerTag listener);

    void unregisterListener(@NotNull ListenerTag listener);

}
