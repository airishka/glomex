package de.glomex.player.model;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.LogManager;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerTestCase extends TestCase {

    protected GlomexPlayer glomexPlayer;
    protected EtcController etcController;

    protected final MediaID mediaID = new MediaUUID();
    protected final URL mediaURL = new URL("http://olexa.com/1.avi");
    protected final MediaData media = new MediaMetadata(mediaID, mediaURL);

    public PlayerTestCase() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        LogManager.getLogManager().readConfiguration(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/logging.properties")
        );

        glomexPlayer = GlomexPlayerFactory.create();
        etcController = GlomexPlayerFactory.instance(EtcController.class);
    }

}
