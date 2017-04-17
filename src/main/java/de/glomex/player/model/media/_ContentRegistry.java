package de.glomex.player.model.media;

import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.lifecycle.AdMetaData;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class _ContentRegistry {

    private final Map<MediaID, WeakReference<MediaMetadata>> medias = new HashMap<>();

    private final Map<MediaID, WeakReference<AdMetaData>> ads = new WeakHashMap<>();

    public void add(@NotNull AdMetaData ad) {
        ads.put(ad.id(), new WeakReference<>(ad));
    }

    public void add(@NotNull MediaMetadata media) {
        medias.put(media.id(), new WeakReference<>(media));
    }

    public void remove(@NotNull MediaID id) {
        if (ads.containsKey(id))
            ads.remove(id);
        if (medias.containsKey(id))
            medias.remove(id);
    }
}
