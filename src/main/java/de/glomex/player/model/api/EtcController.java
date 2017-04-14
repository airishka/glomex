package de.glomex.player.model.api;

import de.glomex.player.api.etc.Callback;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * FIXME: Create default resolvers, they are mandatory even if API doesn't set them
 *
 * Created by <b>me@olexxa.com</b>
 */
public class EtcController implements EtcControl {

    private final GlomexPlayer glomexPlayer;

    private MediaResolver mediaResolver;
    private AdResolver adResolver;
    private boolean autoplay;
    private boolean fullscreen;

    public EtcController(@NotNull GlomexPlayer glomexPlayer) {
        this.glomexPlayer = glomexPlayer;
    }

    @Override
    public <UI_IN> void embedInto(@NotNull UI_IN container) {
        throw new IllegalStateException("FIXME: Not implemented"); // fixme
    }

    @Override
    public void requestFullScreen() {
        fullscreen = true;
    }

    public boolean fullscreen() {
        return fullscreen;
    }

    @Override
    public void setAutoPlay(boolean state) {
        autoplay = state;
    }

    public boolean autoplay() {
        return autoplay;
    }

    @Override
    public void shutdown(@Nullable Callback callback) {
        glomexPlayer.shutdown();
        if (callback != null)
            callback.callback();
    }

    @Override
    public void mediaResolver(@NotNull MediaResolver mediaResolver) {
        this.mediaResolver = mediaResolver;
    }

    public @NotNull MediaResolver mediaResolver() {
        return mediaResolver;
    }

    @Override
    public void adResolver(@NotNull AdResolver adResolver) {
        this.adResolver = adResolver;
    }

    public @NotNull AdResolver adResolver() {
        return adResolver;
    }

}
