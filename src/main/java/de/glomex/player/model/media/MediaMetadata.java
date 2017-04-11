package de.glomex.player.model.media;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class MediaMetadata implements MediaData {

    public final MediaID id;

    protected boolean isStream;
    protected URL url;
    protected Long duration;
    protected Integer width, height;

    public MediaMetadata(@NotNull MediaID id, @NotNull String url) throws MalformedURLException {
        this(id, new URL(url));
    }

    public MediaMetadata(@NotNull MediaID id, @NotNull URL url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public @NotNull MediaID id() {
        return id;
    }

    @Override
    public boolean isStream() {
        return isStream;
    }

    @Override
    public URL url() {
        return url;
    }

    @Override
    public Long duration() {
        return duration;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    @Override
    public String toString() {
        return "Media " + id + " " + url;
    }

}
