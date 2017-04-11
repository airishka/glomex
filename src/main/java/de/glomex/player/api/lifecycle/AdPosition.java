package de.glomex.player.api.lifecycle;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
*/
@SuppressWarnings("unused")
public class AdPosition {

    public static final AdPosition
        preRoll = new AdPosition(0),
        firstQuarterRoll = new AdPosition(0.25),
        midRoll = new AdPosition(0.50),
        thirdQuarterRoll = new AdPosition(0.75),
        postRoll = new AdPosition(1.0);

    private final Double relative;
    private Long time;

    private AdPosition(double relative) {
        this.relative = relative;
    }

    public AdPosition(long time) {
        this.time = time;
        relative = null;
    }

    public AdPosition(@NotNull TimeUnit timeUnit, long time) {
        this.time = timeUnit.toMillis(time);
        relative = null;
    }

    public boolean isRelative() {
        return relative != null;
    }

    public Long time() {
        return time;
    }

    public Double relative() {
        return relative;
    }

}
