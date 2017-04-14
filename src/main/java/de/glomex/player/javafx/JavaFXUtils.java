package de.glomex.player.javafx;

import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXUtils {

    static class JavaFXPlaybackListener implements PlaybackListener {

        private final ImprovedButton playBtn;
        private final Text positionWidget;

        public JavaFXPlaybackListener(ImprovedButton playBtn, Text positionWidget) {
            this.playBtn = playBtn;
            this.positionWidget = positionWidget;
        }

        @Override
        public void onPlay() {
            Platform.runLater( () -> {
                playBtn.suppressCommands = true;
                playBtn.setSelected(true);
                playBtn.suppressCommands = false;
            });
        }

        @Override
        public void onPause() {
            Platform.runLater( () -> {
                playBtn.suppressCommands = true;
                playBtn.setSelected(false);
                playBtn.suppressCommands = false;
            });
        }

        @Override
        public void onSeek(double position) {
            Platform.runLater( () -> positionWidget.setText("At " + position) );
        }

    }

    static class ImprovedButton extends ToggleButton {

        private boolean suppressCommands;

        public ImprovedButton(Image onImage, Image offImage, Runnable onFunction, Runnable offFunction) {
            setSelected(true);
            ImageView toggleImage = new ImageView();
            toggleImage.setPreserveRatio(true);
            toggleImage.setFitWidth(32);
            toggleImage.imageProperty().bind(Bindings
                    .when(selectedProperty())
                    .then(onImage)
                    .otherwise(offImage)
            );
            setGraphic(toggleImage);

            setBorder(Border.EMPTY);
            setBackground(Background.EMPTY);

            selectedProperty().addListener(
                (observable, oldValue, value) -> {
                    if (!suppressCommands)
                        (value ? onFunction : offFunction).run();
                }
            );
        }
    }

    public static VBox createBars(String text, Node component, PlaybackControl playbackControl, SubscribeControl subscribeManager) {
        VBox layout = new VBox();
        {
            HBox buttonBar = new HBox();
            buttonBar.setStyle("-fx-border-color: black");
            {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                buttonBar.setBackground(new Background(new BackgroundImage(
                    new Image(cl.getResourceAsStream("background.gif")),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT
                )));
                buttonBar.setAlignment(Pos.CENTER);

                Label label = new Label(text);
                label.setAlignment(Pos.CENTER_LEFT);

                Text positionWidget = new Text();
                ImprovedButton playBtn = new ImprovedButton(
                    new Image(cl.getResourceAsStream("play.png")),
                    new Image(cl.getResourceAsStream("stop.png")),
                    playbackControl::play,
                    playbackControl::pause
                );

                subscribeManager.registerListener(new JavaFXPlaybackListener(playBtn, positionWidget));

                buttonBar.getChildren().addAll(label, playBtn, positionWidget);
            }
            layout.getChildren().addAll(buttonBar, component);
        }
        return layout;
    }

}
