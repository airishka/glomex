package de.glomex.player.api.media;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface Advertise extends Media {

    /** URL to get ad metadata */
    @NotNull URL url();

    /** Ad Placement */
    @NotNull AdPosition position();

}
