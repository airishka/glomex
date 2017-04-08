package de.glomex.player.model.events;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class EventTracker {

    private static final Logger log = Logger.getLogger(EventTracker.class.getName());

    public void trackEvent(@NotNull String message) {
        log.fine(message);
    }

}
