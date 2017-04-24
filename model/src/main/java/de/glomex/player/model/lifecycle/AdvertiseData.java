package de.glomex.player.model.lifecycle;

import de.glomex.player.api.media.AdPosition;
import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.media.MediaUUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO: add priority
 *
 * Created by <b>me@olexxa.com</b>
 */
public class AdvertiseData implements Advertise {

    MediaID id;
    protected URL metadataURL;
    protected AdPosition position;
    protected Long time;

    // mock: for tests
    public AdvertiseData(URL url, String time) throws MalformedURLException {
        this(url, new AdPosition(time));
    }

    // mock: for tests
    public AdvertiseData(URL url, AdPosition position) throws MalformedURLException {
        metadataURL = url;
        id = new MediaUUID();
        this.position = position;
    }

    AdvertiseData(@NotNull Advertise origin) {
        this.id = origin.id();
        this.metadataURL = origin.url();
        this.position = origin.position();
    }

    @Override
    public @NotNull MediaID id() {
        return id;
    }


    @Override
    public @NotNull URL url() {
        return metadataURL;
    }

    @Override
    public @NotNull AdPosition position() {
        return position;
    }

    /**
     * Cap value to duration passed.
     */
    @NotNull AdvertiseData resolve(@Nullable Long duration) {
        if (position.isRelative()) {
            //noinspection ConstantConditions
            if (duration == null)
                time = position.relative() == 0? 0l : null;
            else
                time = Math.round(duration * position.relative());
        } else {
            time = position.time();
            if (duration != null)
                time = Math.min(duration, time);
        }
        return this;
    }

    boolean scheduled() {
        return time != null;
    }

    @Nullable Long time() {
        return time;
    }

    long order() {
        return time != null ? time * 10 + (position.isRelative()? 0 : 1) : Long.MAX_VALUE;
    }

}
