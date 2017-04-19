package de.glomex.player;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.AdPosition;
import de.glomex.player.api.lifecycle.MediaResolver;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.javafx.JavaFXApplication;
import de.glomex.player.model.lifecycle.AdMetaData;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of 3rd party APP.
 *
 *
 * Created by <b>me@olexxa.com</b>
 */
public class TestFXApplication extends JavaFXApplication {

    final MediaID clipID = new MediaUUID();
    final URL url = new URL("http://static.ipoker.com/aogtwister/video/Age%20Of%20The%20Gods%2009.mp4");
    final MediaMetadata media = new MediaMetadata(clipID, url, 300l);

    List<AdData> ads = new ArrayList<>();
    final URL preRollAD = new URL("http://olexxa.com/ad1.avi");
    final URL secondAD = new URL("http://olexxa.com/ad2.avi");

    MediaResolver mediaResolver = mediaID -> {
        if (mediaID == clipID)
            return  media;
        throw new IllegalStateException();
    };

    public TestFXApplication() throws MalformedURLException {
        ads.add(new AdMetaData(AdPosition.preRoll){{ metadataURL = preRollAD; }});
        ads.add(new AdMetaData("00:05"){{ metadataURL = secondAD; }});
    }

    @Override
    public void initializeAPI(@NotNull PlayerAPI api, @NotNull Node playerComponent) {
//        api.etcController().requestFullScreen();
        api.etcController().setAutoPlay(true);

        api.etcController().mediaResolver(mediaResolver);
        api.etcController().adResolver(mediaID -> ads);

        api.playlistManager().addContent(new MediaUUID());
        api.playlistManager().addContent(new MediaUUID());
        api.playlistManager().addContent(clipID);
        api.playlistManager().skipTo(0);

        api.playbackController().play();
    }

}
