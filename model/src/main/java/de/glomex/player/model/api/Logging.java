package de.glomex.player.model.api;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class Logging {

    public static Logger getLogger(@NotNull Class probe) {
        return Logger.getLogger(probe.getName());
    }

}
