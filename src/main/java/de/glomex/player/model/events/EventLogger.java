package de.glomex.player.model.events;

import de.glomex.player.api.events.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface EventLogger extends Listener {

    void logEvent(@NotNull String event);

}
