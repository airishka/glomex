package de.glomex.player.javafx;

import de.glomex.player.model.api.Logging;
import de.glomex.player.model.events.EventLogger;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXEventLogger implements EventLogger {

    private static final Logger log = Logging.getLogger(JavaFXEventLogger.class);

    private final TextArea logArea;

    public JavaFXEventLogger(@NotNull TextArea logArea) {
        this.logArea = logArea;
    }

    @Override
    public void logEvent(@NotNull String eventMessage) {
        JavaFXUtils.ensureFxThread(() -> {
            logArea.appendText(eventMessage + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }
}
