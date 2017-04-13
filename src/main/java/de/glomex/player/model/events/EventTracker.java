package de.glomex.player.model.events;

import de.glomex.player.model.api.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class EventTracker {

    private static final Logger log = Logging.getLogger(EventTracker.class);

    public void trackEvent(@NotNull String message) {
        // fixme: implement tracking
        log.fine(message);
    }

}
