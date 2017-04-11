package de.glomex.player.api.etc;

import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface EtcControl extends ControlTag {

    /* Set media resolver */
    void mediaResolver(@NotNull MediaResolver mediaResolver);

    void adResolver(@NotNull AdResolver adResolver);

    <UI_IN> void embedInto(@NotNull UI_IN container);

    void requestFullScreen();

    void setAutoPlay(boolean state);

    void destroy(@Nullable Runnable callback);

}
