package de.glomex.player;

import de.glomex.player.api.PlayerFactory;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.AdPosition;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.javafx.JavaFXApplication;
import de.glomex.player.javafx.JavaFXUtils;
import de.glomex.player.model.lifecycle.AdMetaData;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of 3rd party APP.
 *
 * It prepares UI Container element to integrate player into,
 * creates UI controls for playback and initialize a player API.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class TestFXApplication extends JavaFXApplication {

    private void createAPI(@NotNull Stage stage) throws MalformedURLException {
        URL preRollAD = new URL("http://static.ipoker.com/aogtwister/video/Age%20Of%20The%20Gods%2009.mp4");
        URL secondAD = new URL("http://static.ipoker.com/aogtwister/video/Age%20Of%20The%20Gods%2009.mp4");
        URL url = new URL("http://static.ipoker.com/aogtwister/video/Age%20Of%20The%20Gods%2009.mp4");
        MediaID clipID = new MediaUUID();

        List<AdData> ads = new ArrayList<>();
        ads.add(new AdMetaData(){{ metadataURL = preRollAD; position = AdPosition.preRoll; }});
        ads.add(new AdMetaData(){{ metadataURL = secondAD; time = 5 * 1000l; }});

        Platform.setImplicitExit(true);

        PlayerFactory.<Stage, Node>createPlayerAPI(
            stage, true,
            (api, playerComponent) -> {
                Parent pane = JavaFXUtils.createBars(
                    "3rd party APP controls", playerComponent,
                    api.playbackController(), api.subscribeManager()
                );
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();

                //api.requestFullScreen();
                api.etcController().mediaResolver(mediaID -> new MediaMetadata(mediaID, url));
                api.etcController().adResolver(mediaID -> ads);
                api.playlistManager().addContent(clipID);
                api.playlistManager().skipTo(clipID);
                api.playbackController().play();

                stage.setOnCloseRequest((event) -> {
                    api.etcController().shutdown(() -> {
                    });
                    Platform.exit();
                });
            }
        );
    }

}
