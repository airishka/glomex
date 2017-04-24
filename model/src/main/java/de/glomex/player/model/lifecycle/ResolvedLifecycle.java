package de.glomex.player.model.lifecycle;

import de.glomex.player.api.media.Content;
import de.glomex.player.api.media.Media;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class ResolvedLifecycle {

    final @NotNull MediaID mediaID;
    final @NotNull Content content;
    final @NotNull List<AdvertiseData> ads;
    final @NotNull List<Long> stops;

    @SuppressWarnings("ConstantConditions")
    ResolvedLifecycle(@NotNull Lifecycle lifecycle) {
        mediaID = lifecycle.mediaID;
        content = lifecycle.content();

        if (lifecycle.ads() != null) {
            if (!content.isStream() && content.duration() < 1)
                throw new IllegalArgumentException("Content duration must be greater than zero");

            ads = lifecycle.ads().stream()
                .<AdvertiseData>map(AdvertiseData::new)
                .map(ad -> ad.resolve(content.duration()))
                .filter(AdvertiseData::scheduled)
                .sorted(Comparator.comparingLong(AdvertiseData::order))
                .collect(Collectors.toList());

            stops = ads.stream()
                .map(AdvertiseData::time)
                .collect(Collectors.toList());
        } else {
            ads = Collections.emptyList();
            stops = Collections.emptyList();
        }
    }

    public @NotNull Content content() {
        return content;
    }

    public List<Long> stops() {
        return stops;
    }

}
