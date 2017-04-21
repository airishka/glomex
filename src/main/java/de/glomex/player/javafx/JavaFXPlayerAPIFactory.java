package de.glomex.player.javafx;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.PlayerFactory;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.playback.EmptyPlaybackListener;
import de.glomex.player.model.player.MediaPlayer;
import de.glomex.player.model.player.PlayerListener;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayerAPIFactory extends PlayerFactory<Stage, Node> {

    private static final Logger log = Logging.getLogger(JavaFXPlayerAPIFactory.class);

    @Override
    protected void create(@NotNull Stage stage, boolean useDefaultControls, @NotNull BiConsumer<PlayerAPI, Node> callback) {
        MediaView mediaView = new MediaView();

        HBox playerUI = new HBox();

        TextArea logPane = new TextArea();
        logPane.setWrapText(true);
        //logPane.lookupAll(".scroll-pane").stream().filter(node -> node instanceof ScrollPane).forEach(node -> ((ScrollPane) node).setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS));
        //logPane.textProperty().addListener((observable, oldValue, newValue) -> {
        //    logPane.selectPositionCaret(logPane.getLength());
        //    logPane.deselect();
        //    logPane.setScrollTop(Double.MAX_VALUE);
        //    logPane.setScrollLeft(Double.MIN_VALUE);
        //});
        logPane.maxHeightProperty().bind(playerUI.heightProperty());
        logPane.minHeightProperty().bind(playerUI.heightProperty());

        GlomexPlayer api = GlomexPlayerFactory.create();

        Node leftPane = useDefaultControls?
            JavaFXUtils.createBars("API Default controls", mediaView, api.playbackController(), api.subscribeManager()) :
            mediaView;
        playerUI.getChildren().addAll(leftPane, logPane);

        api.subscribeManager().registerListener(new EmptyPlaybackListener() {
            @Override
            public void onReady() {
                JavaFXUtils.ensureFxThread(() -> {
                    stage.sizeToScene();
                    stage.centerOnScreen();
                    if (((EtcController) api.etcController()).fullscreen()) // fixme: cast to controller...
                        stage.setFullScreen(true);
                });
            }
        });
        api.subscribeManager().registerListener(new PlayerListener<JavaFXMediaPlayer>() {
            @Override
            public void onCreated(@NotNull JavaFXMediaPlayer player) {
                JavaFXUtils.ensureFxThread(() ->
                    mediaView.setMediaPlayer(player.component())
                );
            }
        });

        api.addEventLogger(new JavaFXEventLogger(logPane));
        logPane.appendText("Player created \n");

        callback.accept(api, playerUI);
    }

}
