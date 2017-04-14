package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.Logging;
import org.jetbrains.annotations.NotNull;

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

    final MediaID mediaID;

    MediaData media;
    private List<AdMetaData> ads;

    public Lifecycle(@NotNull MediaID mediaID) {
        this.mediaID = mediaID;
    }

    public void ads(@NotNull List<AdData> ads) {
        this.ads = ads.stream()
            .<AdMetaData>map(AdMetaData::new)
            .collect(Collectors.toList());
    }

    // must be only called when media is obtained
    public void resolve() {
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
        return ads.stream().map(AdMetaData::time).collect(Collectors.toList());
    }

    public boolean ready() {
        return media != null && ads != null;
    }
}
