package de.glomex.player.javafx;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.PlayerFactory;
import de.glomex.player.model.api.Logging;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.logging.Logger;

/**
 * Example of 3rd party APP.
 *
 * It prepares UI Container element to integrate player into,
 * creates UI controls for playback and initialize a player API.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXApplication extends Application {

    private static final Logger log = Logging.getLogger(JavaFXApplication.class);

    @Override
    public void start(@NotNull Stage stage) throws Exception {
        Platform.setImplicitExit(true);

        stage.setTitle("Java FX Player");
        stage.setMinWidth(400);
        stage.setMinHeight(300);

        createAPI(stage);
    }

    protected void createAPI(@NotNull Stage stage) throws MalformedURLException {
        PlayerFactory.<Stage, Node>createPlayer(
            stage, true,
            (api, playerComponent) -> {
                Parent pane = JavaFXUtils.createBars(
                    "3rd party APP controls", playerComponent,
                    api.playbackController(), api.subscribeManager()
                );
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.setOnCloseRequest((event) -> api.etcController().shutdown(Platform::exit));
                stage.show();

                initializeAPI(api, playerComponent);
            }
        );
    }

    @SuppressWarnings("unused")
    public void initializeAPI(@NotNull PlayerAPI playerAPI, @NotNull Node playerComponent) {}

}
