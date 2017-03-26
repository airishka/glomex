package com.olexxa.player.javafx;

import com.olexxa.player.api.PlaybackControl;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXUtils {

    public static VBox createBars(String text, Node component, PlaybackControl playbackControl) {
        VBox layout = new VBox();
        {
            HBox buttonBar = new HBox();
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

                ToggleButton playBtn = new ToggleButton();
                {
                    playBtn.setSelected(true);
                    Image playImg = new Image(cl.getResourceAsStream("play.png"));
                    Image stopImg = new Image(cl.getResourceAsStream("stop.png"));

                    ImageView toggleImage = new ImageView();
                    toggleImage.setPreserveRatio(true);
                    toggleImage.setFitWidth(32);
                    toggleImage.imageProperty().bind(Bindings
                        .when(playBtn.selectedProperty())
                        .then(playImg)
                        .otherwise(stopImg)
                    );
                    playBtn.setGraphic(toggleImage);

                    playBtn.setBorder(Border.EMPTY);
                    playBtn.setBackground(Background.EMPTY);

                    playBtn.selectedProperty().addListener(
                        (observable, oldValue, value) -> { if (value) playbackControl.play(); else playbackControl.pause(); }
                    );
                }

                buttonBar.getChildren().addAll(label, playBtn);
            }
            layout.getChildren().addAll(buttonBar, component);
        }
        return layout;
    }

}
