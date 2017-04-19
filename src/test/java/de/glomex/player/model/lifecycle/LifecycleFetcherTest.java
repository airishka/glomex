package de.glomex.player.model.lifecycle;

import de.glomex.player.model.PlayerTestCase;
import de.glomex.player.model.api.GlomexPlayerFactory;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcherTest extends PlayerTestCase {

    LifecycleFetcher fetcher;

    public LifecycleFetcherTest() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();
        fetcher = GlomexPlayerFactory.instance(LifecycleFetcher.class);
    }

    public void testNormal() throws InterruptedException {
        etcController.mediaResolver( (mediaID) -> media );
        etcController.adResolver( (mediaID) -> Collections.emptyList() );

        CountDownLatch latch = new CountDownLatch(1);
        fetcher.startFetching(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertSame(media, lifecycle.media());
            assertSame(Collections.emptyList(), lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }

    public void testBadAD() throws InterruptedException {
        etcController.mediaResolver( (mediaID) -> media);
        etcController.adResolver( (mediaID) -> { throw new IllegalArgumentException("Emulate exception for test"); });

        CountDownLatch latch = new CountDownLatch(1);
        fetcher.startFetching(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertSame(media, lifecycle.media());
            assertNull(lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }

    public void testBadMedia() throws InterruptedException {
        etcController.mediaResolver( mediaID -> { throw new IllegalArgumentException("Emulate exception for test"); });
        etcController.adResolver( mediaID -> { sleep(); return Collections.emptyList(); } );

        CountDownLatch latch = new CountDownLatch(1);
        fetcher.startFetching(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertNull(lifecycle.media());
            assertNull(lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }


}
