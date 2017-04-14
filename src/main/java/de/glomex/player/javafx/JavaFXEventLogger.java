package de.glomex.player.javafx;

import de.glomex.player.model.events.EventLogger;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXEventLogger implements EventLogger {

    private final TextArea logArea;

    public JavaFXEventLogger(@NotNull TextArea logArea) {
        this.logArea = logArea;
    }

    @Override
    public void logEvent(@NotNull String eventMessage) {
        Platform.runLater(() -> {
            logArea.appendText(eventMessage + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }
}
