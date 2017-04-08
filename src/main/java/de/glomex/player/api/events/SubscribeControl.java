package de.glomex.player.api.events;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation can said keep only one listener of each type, forcing app to use facade or multiplexer patterns,
 * or, alternatively, do it by itself.
 *
 * Created by <b>me@olexxa.com</b>
 */
public interface SubscribeControl {

    <L extends Listener> void registerListener(@NotNull L listener);

    <L extends Listener> void unregisterListener(@NotNull L listener);

}
