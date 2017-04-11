package de.glomex.player;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.glomex.player.api.lifecycle.AdData;
import de.glomex.player.api.lifecycle.AdPosition;
import de.glomex.player.api.lifecycle.AdResolver;
import de.glomex.player.api.lifecycle.MediaResolver;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.model.lifecycle.AdMetaData;
import de.glomex.player.model.lifecycle.Lifecycle;
import de.glomex.player.model.lifecycle.LifecycleManager;
import de.glomex.player.model.media.MediaMetadata;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;

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
        ads.add(new AdMetaData() {{ position = AdPosition.preRoll; }});  // 0
        ads.add(new AdMetaData() {{ position = AdPosition.postRoll; }}); // 143 000
        ads.add(new AdMetaData() {{ position = new AdPosition(200 * 1000l); }}); // 143 000
        ads.add(new AdMetaData() {{ position = new AdPosition(100 * 1000l); }}); // 100 000
        ads.add(new AdMetaData() {{ position = new AdPosition(10 * 1000l); }});  //  10 000
        ads.add(new AdMetaData() {{ position = new AdPosition(0); }});           //       0
        ads.add(new AdMetaData() {{ position = AdPosition.thirdQuarterRoll; }}); // 107 250

        List<Long> stops = Arrays.asList(0l, 0l, 10 * 1000l, 100 * 1000l, 107250l, 143 * 1000l, 143 * 1000l);

        doTest(clipId, clip, ads, stops);
    }


    public void testStream() throws MalformedURLException {
        MediaID streamId = new MediaUUID();
        MediaMetadata stream = new MediaMetadata(streamId, "http://olexa.com/1.avi") {{ isStream = true; }};

        List<AdData> ads = new ArrayList<>();
        ads.add(new AdMetaData() {{ position = AdPosition.preRoll; }});  // 0
        ads.add(new AdMetaData() {{ position = new AdPosition(200 * 1000l); }}); // 200 000
        ads.add(new AdMetaData() {{ position = AdPosition.midRoll; }});
        ads.add(new AdMetaData() {{ position = new AdPosition(100 * 1000l); }}); // 100 000
        ads.add(new AdMetaData() {{ position = new AdPosition(0); }});  //  0

        List<Long> stops = Arrays.asList(0l, 0l, 100 * 1000l, 200 * 1000l);

        doTest(streamId, stream, ads, stops);
    }

    private void doTest(MediaID mediaId, MediaMetadata media, List<AdData> ads, List<Long> stops) {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MediaResolver.class).toInstance( id -> media );
                bind(AdResolver.class).toInstance( id -> ads );
            }
        });
        LifecycleManager lifecycleManager = injector.getInstance(LifecycleManager.class);
        lifecycleManager.open(mediaId);

        Lifecycle lifecycle = lifecycleManager.lifecycle();
        assertEquals(lifecycle.stops(), stops);
    }

}
