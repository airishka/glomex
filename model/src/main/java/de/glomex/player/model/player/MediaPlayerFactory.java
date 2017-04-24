package de.glomex.player.model.player;

import de.glomex.player.api.media.Media;
import de.glomex.player.api.playback.PlaybackListener;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class MediaPlayerFactory<P extends MediaPlayer> {

    public static <Player extends MediaPlayer> MediaPlayerFactory get() {
        ServiceLoader<MediaPlayerFactory> loader = ServiceLoader.load(MediaPlayerFactory.class);
        Iterator<MediaPlayerFactory> implementations = loader.iterator();
        if (!implementations.hasNext())
            throw new IllegalArgumentException("No implementation is registered using META-INF services for " + MediaPlayerFactory.class);

        @SuppressWarnings("unchecked")
        MediaPlayerFactory<Player> factory = implementations.next();
        return factory;
    }

    public abstract boolean supportMarkers();

    public abstract boolean reusable();

    public abstract @NotNull P createPlayer(@NotNull Media media);

    public abstract void activate(@NotNull P player);
}
