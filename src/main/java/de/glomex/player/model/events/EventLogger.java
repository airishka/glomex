package de.glomex.player.model.events;

import de.glomex.player.api.events.ListenerTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface EventLogger extends ListenerTag {

    void logEvent(@NotNull String event);

}
