package de.glomex.player.model.media;

import de.glomex.player.api.media.Content;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class ContentInfo implements Content {

    public final MediaID id;

    protected boolean isStream;
    protected @NotNull URL url;
    protected @Nullable Long duration;
//    protected Integer width, height;

    public ContentInfo(@NotNull MediaID id, @NotNull String url, @Nullable Long duration) throws MalformedURLException {
        this(id, new URL(url), duration);
    }

    public ContentInfo(@NotNull MediaID id, @NotNull URL url, @Nullable Long duration) {
        this.id = id;
        this.url = url;
        this.duration = duration;
        isStream = duration != null; // fixme
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
    public @NotNull URL url() {
        return url;
    }

    @Override
    public @Nullable Long duration() {
        return duration;
    }

//    public Integer width() {
//        return width;
//    }
//
//    public Integer height() {
//        return height;
//    }

    @Override
    public String toString() {
        return "Content " + id + " " + url;
    }

}
