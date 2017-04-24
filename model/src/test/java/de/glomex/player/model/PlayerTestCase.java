package de.glomex.player.model;

import de.glomex.player.api.media.Content;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.media.ContentInfo;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerTestCase extends TestCase {

    protected GlomexPlayer glomexPlayer;
    protected EtcController etcController;

    protected final MediaID mediaID = new MediaUUID();
    protected final URL mediaURL = new URL("http://olexa.com/1.avi");
    protected final Content content = new ContentInfo(mediaID, mediaURL, 300l);

    public PlayerTestCase() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        LogManager.getLogManager().readConfiguration(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/logging.properties")
        );

        glomexPlayer = GlomexPlayerFactory.create();
        glomexPlayer.etcController().setAutoPlay(false);
        glomexPlayer.etcController().setAutoShutdown(false);
        etcController = GlomexPlayerFactory.instance(EtcController.class);
    }

    protected void await(CountDownLatch latch) {
        try {
            if (!latch.await(10, TimeUnit.SECONDS))
                fail("Timeout");
        } catch (InterruptedException ignored) {
            fail("Interrupted");
        }
    }

    protected boolean sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            return true;
        } catch (InterruptedException ignored) {
        }
        return false;
    }

}
