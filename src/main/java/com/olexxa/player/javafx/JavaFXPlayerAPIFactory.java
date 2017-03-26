package com.olexxa.player.javafx;

import com.olexxa.player.api.PlayerAPI;
import com.olexxa.player.api.PlayerAPIFactory;
import com.olexxa.player.model.api.PlayerAPIImpl;
import javafx.scene.Node;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.util.function.BiConsumer;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayerAPIFactory extends PlayerAPIFactory<Stage, Node> {

    static Stage stage;
    static MediaView mediaView;

    @Override
    protected void create(Stage embedInto, boolean useDefaultControls, BiConsumer<PlayerAPI, Node> callback) {
        stage = embedInto;
        mediaView = new MediaView();

        PlayerAPIImpl api = new PlayerAPIImpl();

        Node playerUI = useDefaultControls?
            JavaFXUtils.createBars("API Default controls", mediaView, api.playbackController()) :
            mediaView;

        callback.accept(api, playerUI);
    }

}
