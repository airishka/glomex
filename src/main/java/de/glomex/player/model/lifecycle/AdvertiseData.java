package de.glomex.player.model.lifecycle;

import de.glomex.player.api.media.AdPosition;
import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.media.MediaUUID;
import org.jetbrains.annotations.NotNull;

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
    public AdvertiseData(String time) {
        this(new AdPosition(time));
    }

    // mock: for tests
    public AdvertiseData(AdPosition position) {
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
    public AdPosition position() {
        return position;
    }

    /**
     * Cap value to duration passed.
     */
    public Long resolve(Long duration) {
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
        return time;
    }

    public boolean scheduled() {
        return time != null;
    }

    public Long time() {
        return time;
    }

    public long order() {
        return time != null ? time * 10 + (position.isRelative()? 0 : 1) : Long.MAX_VALUE;
    }

}
