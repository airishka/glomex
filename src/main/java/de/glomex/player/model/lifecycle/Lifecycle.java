package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Nulls - mean not fetched
 * No ad - empty list.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class Lifecycle {

    //private static final Logger log = Logging.getLogger(Lifecycle.class);

    public final MediaID mediaID;

    private @Nullable MediaData media;
    private @Nullable List<AdMetaData> ads;

    public Lifecycle(@NotNull MediaID mediaID) {
        this.mediaID = mediaID;
    }

    void media(MediaData media) {
        this.media = media;
    }

    public @Nullable MediaData media() {
        return media;
    }

    void ads(@NotNull List<AdData> ads) {
        this.ads = ads.stream()
            .<AdMetaData>map(AdMetaData::new)
            .collect(Collectors.toList());
    }

    public @Nullable List<AdMetaData> ads() {
        return ads;
    }

    // must be only called when media is obtained
    public void resolve() {
        if (ads == null)
            return;

        //noinspection ConstantConditions
        if (!media.isStream() && media.duration() < 1)
            throw new IllegalArgumentException("Media duration must be greater than ero");

        ads.stream()
            .forEach(ad -> ad.resolve(media.duration()));

        ads = ads.stream()
            .filter(AdMetaData::scheduled)
            .sorted(Comparator.comparingLong(AdMetaData::time))
            .collect(Collectors.toList());
    }

    public List<Long> stops() {
        return ads == null?
            Collections.emptyList() :
            ads.stream().map(AdMetaData::time).collect(Collectors.toList());
    }

    public boolean ready() {
        return media != null && ads != null;
    }
}
