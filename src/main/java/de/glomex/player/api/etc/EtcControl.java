package de.glomex.player.api.etc;

import de.glomex.player.api.ControlTag;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.ContentResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface EtcControl extends ControlTag {

    void contentResolver(@NotNull ContentResolver contentResolver);

    void adResolver(@NotNull AdResolver adResolver);

    <UI_IN> void embedInto(@NotNull UI_IN container);

    void requestFullScreen();

    void setAutoPlay(boolean state);

    void shutdown(@Nullable Callback callback);

}
