package de.glomex.player.api.media;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoField.MILLI_OF_DAY;

/**
 * Created by <b>me@olexxa.com</b>
*/
@SuppressWarnings("UnusedDeclaration")
public class AdPosition {

    public static final AdPosition
        preRoll =          new AdPosition(0),
        firstQuarterRoll = new AdPosition(0.25),
        midRoll =          new AdPosition(0.50),
        thirdQuarterRoll = new AdPosition(0.75),
        postRoll =         new AdPosition(1.0);

    private final @Nullable Double relative;
    private Long time;

    private AdPosition(double relative) {
        this.relative = relative;
    }

    public AdPosition(@NotNull String timeText) {
        LocalTime localTime = LocalTime.parse(timeText);
        time = localTime.getLong(MILLI_OF_DAY);
        relative = null;
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
