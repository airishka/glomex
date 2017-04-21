package de.glomex.player.javafx;

import de.glomex.player.api.media.Media;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.player.MediaPlayerFactory;
import de.glomex.player.model.player.PlayerListener;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXMediaPlayerFactory extends MediaPlayerFactory<JavaFXMediaPlayer> {

    @Override
    public boolean supportMarkers() {
        return true;
    }

    @Override
    public boolean reusable() {
        return false;
    }

    @Override
    public @NotNull JavaFXMediaPlayer createPlayer(@NotNull Media media) {
        JavaFXMediaPlayer fxPlayer = new JavaFXMediaPlayer(media);

        // This can't be move into constructor - it's not created yet
        @SuppressWarnings("unchecked")
        PlayerListener<JavaFXMediaPlayer> playerListener = GlomexPlayerFactory.instance(PlayerListener.class);
        playerListener.onCreated(fxPlayer);

        return fxPlayer;
    }

}
