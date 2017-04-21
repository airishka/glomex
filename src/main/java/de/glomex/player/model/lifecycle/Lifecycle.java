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
    private @Nullable List<Advertise> ads;

    public Lifecycle(@NotNull MediaID mediaID) {
        this.mediaID = mediaID;
    }

    void content(@NotNull Content content) {
        this.content = content;
    }

    public @Nullable Content content() {
        return content;
    }

    void ads(@NotNull List<Advertise> ads) {
        this.ads = ads;
    }

    public @Nullable List<Advertise> ads() {
        return ads;
    }

}
