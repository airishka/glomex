package de.glomex.player;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.AdPosition;
import de.glomex.player.api.lifecycle.ContentResolver;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.javafx.JavaFXApplication;
import de.glomex.player.model.lifecycle.AdvertiseData;
import de.glomex.player.model.media.ContentInfo;
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
    final ContentInfo media = new ContentInfo(clipID, url, 300l);

    List<Advertise> ads = new ArrayList<>();
    final URL preRollAD = new URL("http://olexxa.com/ad1.avi");
    final URL secondAD = new URL("http://olexxa.com/ad2.avi");

    ContentResolver contentResolver = mediaID -> {
        if (mediaID == clipID)
            return  media;
        throw new IllegalStateException();
    };

    public TestFXApplication() throws MalformedURLException {
        ads.add(new AdvertiseData(AdPosition.preRoll){{ metadataURL = preRollAD; }});
        ads.add(new AdvertiseData("00:05"){{ metadataURL = secondAD; }});
    }

    @Override
    public void initializeAPI(@NotNull PlayerAPI api, @NotNull Node playerComponent) {
//        api.etcController().requestFullScreen();
        api.etcController().setAutoPlay(true);

        api.etcController().contentResolver(contentResolver);
        api.etcController().adResolver(mediaID -> ads);

        api.playlistManager().addContent(new MediaUUID());
        api.playlistManager().addContent(new MediaUUID());
        api.playlistManager().addContent(clipID);
        api.playlistManager().skipTo(0);

        api.playbackController().play();
    }

}
