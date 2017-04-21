package de.glomex.player;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.AdPosition;
import de.glomex.player.api.lifecycle.ContentResolver;
import de.glomex.player.api.media.MediaID;
import de.glomex.player.javafx.JavaFXApplication;
import de.glomex.player.model.lifecycle.AdvertiseData;
import de.glomex.player.model.media.ContentInfo;
import de.glomex.player.model.media.MediaUUID;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Example of 3rd party APP.
 *
 *
 * Created by <b>me@olexxa.com</b>
 */
public class TestFXApplication extends JavaFXApplication {

    final MediaID clipID = new MediaUUID();
    final URL url = new URL("http://static.ipoker.com/aogtwister/video/Age%20Of%20The%20Gods%2009.mp4");
    final ContentInfo media = new ContentInfo(clipID, url, 300l);

    List<Advertise> ads = new ArrayList<>();
    final URL preRollAD = new URL("https://r4---sn-4g5e6nls.gvt1.com/videoplayback/id/a33fc5b2685eb16e/itag/15/source/gfp_video_ads/requiressl/yes/acao/yes/mime/video%2Fmp4/ip/213.187.248.13/ipbits/0/expire/1492789747/sparams/acao,expire,id,ip,ipbits,itag,mime,mm,mn,ms,mv,pl,requiressl,source/signature/1E2AF645B47C0B86DD984EA192FF4F78E611DCEF.4BF7067411C1F551CF17E5DAD0B86CB598E7366B/key/cms1/pl/24/redirect_counter/1/cm2rm/sn-3c2s7d/req_id/ff483976c1b0a3ee/cms_redirect/yes/mm/34/mn/sn-4g5e6nls/ms/ltu/mt/1492768281/mv/m?file=file.mp4");
    final URL secondAD = new URL("http://playertest.longtailvideo.com/action-60fps.mp4");

    ContentResolver contentResolver = mediaID -> {
        if (mediaID == clipID)
            return  media;
        throw new IllegalStateException();
    };

    public TestFXApplication() throws MalformedURLException {
        ads.add(new AdvertiseData(preRollAD, AdPosition.preRoll));
        ads.add(new AdvertiseData(secondAD, "00:05"));
    }

    @Override
    public void initializeAPI(@NotNull PlayerAPI api, @NotNull Node playerComponent) {
//        api.etcController().requestFullScreen();
        api.etcController().setAutoPlay(true);
        api.etcController().setAutoShutdown(true);

        api.etcController().contentResolver(contentResolver);
        api.etcController().adResolver(mediaID -> ads);

        api.playlistManager().addContent(new MediaUUID());
        api.playlistManager().addContent(clipID);
        api.playlistManager().addContent(new MediaUUID());

        api.playlistManager().skipTo(1);
        api.playbackController().seek(TimeUnit.SECONDS.toMillis(120));

        api.playbackController().play();
    }

}
