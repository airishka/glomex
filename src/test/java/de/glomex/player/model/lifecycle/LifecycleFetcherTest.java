package de.glomex.player.model.lifecycle;

import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.lifecycle.LifecycleFetcher;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcherTest extends TestCase {

    GlomexPlayer glomexPlayer;
    EtcController etcController;
    LifecycleFetcher fetcher;

    final MediaID mediaID = new MediaUUID();
    final URL mediaURL = new URL("http://olexa.com/1.avi");

    public LifecycleFetcherTest() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();
        glomexPlayer = GlomexPlayerFactory.create();
        etcController = GlomexPlayerFactory.instance(EtcController.class);
        fetcher = GlomexPlayerFactory.instance(LifecycleFetcher.class);
    }

    public void testFetcher() {
        etcController.mediaResolver( (mediaID) -> new MediaMetadata(mediaID, mediaURL));
        etcController.adResolver( (mediaID) -> { throw new IllegalArgumentException("Test excception"); });

        fetcher.startFetching(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertNull(lifecycle.ads())
        });
    }
}
