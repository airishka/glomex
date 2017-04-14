package de.glomex.player.model.playlist;

import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.api.playlist.PlaylistListener;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.media.MediaUUID;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistTest extends TestCase {

    final MediaID
        a = new MediaUUID(),
        b = new MediaUUID(),
        c = new MediaUUID(),
        d = new MediaUUID();

    PlaylistControl playlist;
    MediaID current;

    public PlaylistTest() throws MalformedURLException {}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        GlomexPlayer player = GlomexPlayerFactory.create();
        player.subscribeManager().registerListener(new PlaylistListener() {
            public void onChanged() {}
            public void onNext(@NotNull MediaID mediaID) {
                current = mediaID;
            }
            public void onFinished() {}
        });

        playlist = player.playlistManager();
    }

    private MediaID waitAssertSame(MediaID specimen) {
        return waitAndAssert(true, specimen);
    }
    private MediaID waitAssertNotSame(MediaID... specimens) {
        return waitAndAssert(false, specimens);
    }
    private MediaID waitAndAssert(boolean same, MediaID... specimens) {
        while (current == null)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        if (same)
            Arrays.stream(specimens)
                .forEach(specimen -> assertSame(specimen, current));
        else
            Arrays.stream(specimens)
                .forEach(specimen -> assertNotSame(specimen, current));
        MediaID last = current;
        current = null;
        return last;
    }

    public void testNotRepeatableNotRandom() {
        playlist.addContent(a, b, c, d);

        playlist.setRandom(false);
        playlist.setRepeatable(false);

        playlist.skipTo(a);
        waitAssertSame(a);
        playlist.skipTo(b);
        waitAssertSame(b);

        playlist.next();
        waitAssertSame(c);
        playlist.next();
        waitAssertSame(d);

        playlist.prev();
        waitAssertSame(c);
        playlist.prev();
        waitAssertSame(b);
        playlist.prev();
        waitAssertSame(a);
    }

    public void testNotRepeatableRandom() {
        playlist.addContent(a, b, c, d);

        playlist.setRandom(true);
        playlist.setRepeatable(false);

        playlist.skipTo(b);
        waitAssertSame(b);
        playlist.next();
        MediaID second = waitAssertNotSame(b);
        playlist.next();
        MediaID third = waitAssertNotSame(b, second);
        playlist.next();
        waitAssertNotSame(b, second, third);

        playlist.prev();
        waitAssertSame(third);
        playlist.prev();
        waitAssertSame(second);
        playlist.prev();
        waitAssertSame(b);
    }

}