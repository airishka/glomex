package de.glomex.player.api.media;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface Media {

    @NotNull MediaID id();

    @NotNull URL url();

}
