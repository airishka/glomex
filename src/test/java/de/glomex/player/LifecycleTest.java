package de.glomex.player;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.AdPosition;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaResolver;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.lifecycle.AdMetaData;
import de.glomex.player.model.lifecycle.Lifecycle;
import de.glomex.player.model.lifecycle.LifecycleManager;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("ConstantConditions")
public class LifecycleTest extends TestCase {

    public void testClip() throws MalformedURLException {
        MediaID clipId = new MediaUUID();
        MediaMetadata clip = new MediaMetadata(clipId, "http://olexa.com/1.avi") {{
            duration = 143 * 1000l;
            isStream = false;
        }};

        List<AdData> ads = new ArrayList<>();
        ads.add(new AdMetaData() {{ position = AdPosition.preRoll; }});             // 0
        ads.add(new AdMetaData() {{ position = AdPosition.postRoll; }});            // 143 000
        ads.add(new AdMetaData() {{ position = new AdPosition("00:03:20.010"); }}); // 143 000
        ads.add(new AdMetaData() {{ position = new AdPosition("00:01:40.000"); }}); // 100 000
        ads.add(new AdMetaData() {{ position = new AdPosition("00:00:10.000"); }}); //  10 000
        ads.add(new AdMetaData() {{ position = new AdPosition(0); }});              //       0
        ads.add(new AdMetaData() {{ position = AdPosition.thirdQuarterRoll; }});    // 107 250

        List<Long> stops = Arrays.asList(0l, 0l, 10 * 1000l, 100 * 1000l, 107250l, 143 * 1000l, 143 * 1000l);

        doTest(clipId, clip, ads, stops);
    }


    public void testStream() throws MalformedURLException {
        MediaID streamId = new MediaUUID();
        MediaMetadata stream = new MediaMetadata(streamId, "http://olexa.com/1.avi") {{ isStream = true; }};

        List<AdData> ads = new ArrayList<>();
        ads.add(new AdMetaData() {{ position = AdPosition.preRoll; }});          // 0
        ads.add(new AdMetaData() {{ position = new AdPosition(200 * 1000l); }}); // 200 000
        ads.add(new AdMetaData() {{ position = AdPosition.midRoll; }});          // n/a
        ads.add(new AdMetaData() {{ position = new AdPosition(100 * 1000l); }}); // 100 000
        ads.add(new AdMetaData() {{ position = new AdPosition(0); }});           //  0

        List<Long> stops = Arrays.asList(0l, 0l, 100 * 1000l, 200 * 1000l);

        doTest(streamId, stream, ads, stops);
    }

    private void doTest(MediaID mediaId, MediaMetadata media, List<AdData> ads, List<Long> stops) {
        GlomexPlayer player = GlomexPlayerFactory.create();
        player.etcController().mediaResolver(
            mediaID -> media
        );
        player.etcController().adResolver(mediaID -> ads);

        LifecycleManager lifecycleManager = GlomexPlayerFactory.instance(LifecycleManager.class);
        lifecycleManager.open(mediaId);

        Lifecycle lifecycle = lifecycleManager.lifecycle;
        assertEquals(lifecycle.stops(), stops);
    }

}
