package de.glomex.player.model.lifecycle;

import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.AdPosition;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.model.PlayerTestCase;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.media.ContentInfo;
import de.glomex.player.model.media.MediaUUID;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("ConstantConditions")
public class ResolveTest extends PlayerTestCase {

    public ResolveTest() throws MalformedURLException {}

    public void testClip() throws MalformedURLException {
        MediaID clipId = new MediaUUID();
        ContentInfo clip = new ContentInfo(clipId, "http://olexa.com/1.avi", 300l) {{
            duration = 143 * 1000l;
            isStream = false;
        }};

        List<Advertise> ads = new ArrayList<>();
        ads.add(new AdvertiseData(mediaURL, AdPosition.preRoll));            // 0
        ads.add(new AdvertiseData(mediaURL, AdPosition.postRoll));           // 143 000
        ads.add(new AdvertiseData(mediaURL, "00:03:20.010"));                // 143 000
        ads.add(new AdvertiseData(mediaURL, "00:01:40.000"));                // 100 000
        ads.add(new AdvertiseData(mediaURL, "00:00:10.000"));                //  10 000
        ads.add(new AdvertiseData(mediaURL, new AdPosition(0)));             //       0
        ads.add(new AdvertiseData(mediaURL, AdPosition.thirdQuarterRoll));   // 107 250

        List<Long> stops = Arrays.asList(0l, 0l, 10 * 1000l, 100 * 1000l, 107250l, 143 * 1000l, 143 * 1000l);

        doTest(clipId, clip, ads, stops);
    }

    public void testStream() throws MalformedURLException {
        MediaID streamId = new MediaUUID();
        ContentInfo stream = new ContentInfo(streamId, mediaURL, null);

        List<Advertise> ads = new ArrayList<>();
        ads.add(new AdvertiseData(mediaURL, AdPosition.preRoll));          // 0
        ads.add(new AdvertiseData(mediaURL, new AdPosition(200 * 1000l))); // 200 000
        ads.add(new AdvertiseData(mediaURL, AdPosition.midRoll));          // n/a
        ads.add(new AdvertiseData(mediaURL, new AdPosition(100 * 1000l))); // 100 000
        ads.add(new AdvertiseData(mediaURL, new AdPosition(0)));           //  0

        List<Long> stops = Arrays.asList(0l, 0l, 100 * 1000l, 200 * 1000l);

        doTest(streamId, stream, ads, stops);
    }

    private void doTest(MediaID mediaId, ContentInfo content, List<Advertise> ads, List<Long> stops) {
        etcController.contentResolver(mediaID -> content);
        etcController.adResolver( mediaID -> ads  );

        CountDownLatch latch = new CountDownLatch(1);
        LifecycleFetcher lifecycleFetcher = new LifecycleFetcher(mediaId);
        lifecycleFetcher.fetch(lifecycle -> {
            assertEquals(ads, lifecycle.ads());
            assertEquals(content, lifecycle.content());
            ResolvedLifecycle resolvedLifecycle = new ResolvedLifecycle(lifecycle);
            assertEquals(stops, resolvedLifecycle.stops);
            assertEquals(content, resolvedLifecycle.content());
            latch.countDown();
        });
        await(latch);
    }

}
