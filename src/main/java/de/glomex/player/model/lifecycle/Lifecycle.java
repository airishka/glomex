package de.glomex.player.model.lifecycle;

import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.Content;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Nulls - mean not fetched
 * No ad - empty list.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class Lifecycle {

    //private static final Logger log = Logging.getLogger(Lifecycle.class);

    public final MediaID mediaID;

    private @Nullable Content content;
    private @Nullable List<AdvertiseData> ads;

    public Lifecycle(@NotNull MediaID mediaID) {
        this.mediaID = mediaID;
    }

    void content(Content content) {
        this.content = content;
    }

    public @Nullable Content content() {
        return content;
    }

    void ads(@NotNull List<Advertise> ads) {
        this.ads = ads.stream()
            .<AdvertiseData>map(AdvertiseData::new)
            .collect(Collectors.toList());
    }

    public @Nullable List<AdvertiseData> ads() {
        return ads;
    }

    // must be only called when content is obtained
    public void resolve() {
        if (ads == null)
            return;

        //noinspection ConstantConditions
        if (!content.isStream() && content.duration() < 1)
            throw new IllegalArgumentException("Content duration must be greater than zero");

        ads.stream()
            .forEach(ad -> ad.resolve(content.duration()));

        ads = ads.stream()
            .filter(AdvertiseData::scheduled)
            .sorted(Comparator.comparingLong(AdvertiseData::order))
            .collect(Collectors.toList());
    }

    public List<Long> stops() {
        return ads == null?
            Collections.emptyList() :
            ads.stream().map(AdvertiseData::time).collect(Collectors.toList());
    }

    public boolean ready() {
        return content != null && ads != null;
    }
}
