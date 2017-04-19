package de.glomex.player.model.lifecycle;

import de.glomex.player.model.PlayerTestCase;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcherTest extends PlayerTestCase {

    public LifecycleFetcherTest() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testNormal() throws InterruptedException {
        etcController.contentResolver((mediaID) -> content);
        etcController.adResolver( (mediaID) -> Collections.emptyList() );

        CountDownLatch latch = new CountDownLatch(1);

        LifecycleFetcher fetcher = new LifecycleFetcher(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertSame(content, lifecycle.content());
            assertSame(Collections.emptyList(), lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }

    public void testBadAD() throws InterruptedException {
        etcController.contentResolver((mediaID) -> content);
        etcController.adResolver( (mediaID) -> { throw new IllegalArgumentException("Emulate exception for test"); });

        CountDownLatch latch = new CountDownLatch(1);
        LifecycleFetcher fetcher = new LifecycleFetcher(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertSame(content, lifecycle.content());
            assertNull(lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }

    public void testBadMedia() throws InterruptedException {
        etcController.contentResolver(mediaID -> {
            throw new IllegalArgumentException("Emulate exception for test");
        });
        etcController.adResolver( mediaID -> { sleep(); return Collections.emptyList(); } );

        CountDownLatch latch = new CountDownLatch(1);
        LifecycleFetcher fetcher = new LifecycleFetcher(mediaID, (lifecycle) -> {
            assertNotNull(lifecycle);
            assertEquals(mediaID, lifecycle.mediaID);
            assertNull(lifecycle.content());
            assertNull(lifecycle.ads());
            latch.countDown();
        });
        await(latch);
    }

}
