package de.glomex.player.javafx;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.PlayerFactory;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayerAPIFactory extends PlayerFactory<Stage, Node> {

    private static final Logger log = Logging.getLogger(JavaFXPlayerAPIFactory.class);

    // fixme: its mostly mocks, remove
    static Stage stage;
    static MediaView mediaView;
    static TextArea logPane;
    static PlaylistControl playlistManager;

    public static void log(String string) {
        logPane.appendText(string);
    };

    @Override
    protected void create(Stage embedInto, boolean useDefaultControls, BiConsumer<PlayerAPI, Node> callback) {
        stage = embedInto;
        mediaView = new MediaView();

        HBox playerUI = new HBox();

        logPane = new TextArea();
        logPane.setWrapText(true);
//        logPane.lookupAll(".scroll-pane").stream().filter(node -> node instanceof ScrollPane).forEach(node -> ((ScrollPane) node).setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS));
//        logPane.textProperty().addListener((observable, oldValue, newValue) -> {
//            logPane.selectPositionCaret(logPane.getLength());
//            logPane.deselect();
//            logPane.setScrollTop(Double.MAX_VALUE);
//            logPane.setScrollLeft(Double.MIN_VALUE);
//        });
        logPane.maxHeightProperty().bind(playerUI.heightProperty());
        logPane.minHeightProperty().bind(playerUI.heightProperty());

        GlomexPlayer api = GlomexPlayerFactory.create();

        Node leftPane = useDefaultControls?
            JavaFXUtils.createBars("API Default controls", mediaView, api.playbackController(), api.subscribeManager()) :
            mediaView;
        playerUI.getChildren().addAll(leftPane, logPane);

        playlistManager = api.playlistManager();
        api.addEventLogger(new JavaFXEventLogger(logPane));
        log("Player created \n");

        callback.accept(api, playerUI);
    }

}
