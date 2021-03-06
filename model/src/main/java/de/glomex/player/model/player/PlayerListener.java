package de.glomex.player.model.player;

import de.glomex.player.api.ListenerTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface PlayerListener<P extends MediaPlayer> extends ListenerTag {

    void onCreated(@NotNull P player);

    void onActivated(@NotNull P player);

}
