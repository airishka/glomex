package de.glomex.player.model.lifecycle;

import de.glomex.player.model.PlayerTestCase;
import de.glomex.player.model.lifecycle.AdvertiseData;
import de.glomex.player.model.lifecycle.Lifecycle;
import de.glomex.player.model.lifecycle.LifecycleFetcher;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleFetcherTest extends PlayerTestCase {

    public LifecycleFetcherTest() throws MalformedURLException {}

    class LifecycleHolder {
        Lifecycle lifecycle;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testNormal() throws InterruptedException {
        etcController.contentResolver(mediaID -> content);
        etcController.adResolver(mediaID -> Collections.emptyList());

        Lifecycle lifecycle = get();

        assertNotNull(lifecycle);
        assertEquals(mediaID, lifecycle.mediaID);
        assertSame(content, lifecycle.content());
        assertEquals(Collections.<AdvertiseData>emptyList(), lifecycle.ads());
    }

    public void testBadAD() throws InterruptedException {
        etcController.contentResolver(mediaID -> content);
        etcController.adResolver(mediaID -> { throw new IllegalArgumentException("Emulate exception for test"); });

        Lifecycle lifecycle = get();

        assertNotNull(lifecycle);
        assertEquals(mediaID, lifecycle.mediaID);
        assertSame(content, lifecycle.content());
        assertNull(lifecycle.ads());
    }

    public void testBadMedia() throws InterruptedException {
        etcController.contentResolver(mediaID -> { throw new IllegalArgumentException("Emulate exception for test"); });
        etcController.adResolver(mediaID -> {
            boolean result = sleep(2);
            //noinspection ConstantConditions
            return result? Collections.emptyList() : null; // cause later fail by empty list
        });

        Lifecycle lifecycle = get();

        assertNotNull(lifecycle);
        assertEquals(mediaID, lifecycle.mediaID);
        assertNull(lifecycle.content());
        assertNull("Getting as wasn't cancelled", lifecycle.ads());
    }

    private Lifecycle get() {
        LifecycleHolder holder = new LifecycleHolder();
        CountDownLatch latch = new CountDownLatch(1);
        LifecycleFetcher fetcher = new LifecycleFetcher(mediaID);
        fetcher.fetch((lifecycle) -> {
            holder.lifecycle = lifecycle;
            latch.countDown();
        });
        await(latch);
        return holder.lifecycle;
    }

}
