package de.glomex.player.model.api;

import com.google.inject.Provides;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class EtcController implements EtcControl {

    private MediaResolver mediaResolver;
    private AdResolver adResolver;

    @Override
    public void mediaResolver(@NotNull MediaResolver mediaResolver) {
        this.mediaResolver = mediaResolver;
    }

    @Override
    public void adResolver(@NotNull AdResolver adResolver) {
        this.adResolver = adResolver;
    }

    @Override
    public <UI_IN> void embedInto(@NotNull UI_IN container) {
        throw new IllegalStateException("FIXME: Not implemented"); // fixme
    }

    @Override
    public void requestFullScreen() {
        throw new IllegalStateException("FIXME: Not implemented"); // fixme
    }

    @Override
    public void setAutoPlay(boolean state) {
        throw new IllegalStateException("FIXME: Not implemented"); // fixme
    }

    @Override
    public void destroy(@Nullable Runnable callback) {
        throw new IllegalStateException("FIXME: Not implemented"); // fixme
    }

    @Provides
    public MediaResolver mediaResolver() {
        return mediaResolver;
    }

    @Provides
    public AdResolver adResolver() {
        return adResolver;
    }

}
