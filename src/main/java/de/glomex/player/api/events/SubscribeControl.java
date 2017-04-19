package de.glomex.player.api.events;

import de.glomex.player.api.ControlTag;
import de.glomex.player.api.ListenerTag;
import org.jetbrains.annotations.NotNull;

/**
 * Listener can implement several listener interfaces at once.
 * Supports several listeners of the same type:
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface SubscribeControl extends ControlTag {

    void registerListener(@NotNull ListenerTag listener);

    void unregisterListener(@NotNull ListenerTag listener);

}
