package de.glomex.player.model.playlist;

import de.glomex.player.api.media.MediaID;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.api.playlist.PlaylistListener;
import de.glomex.player.model.PlayerTestCase;
import de.glomex.player.model.media.MediaUUID;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistTest extends PlayerTestCase {

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

        glomexPlayer.etcController().contentResolver(mediaID->content);
        glomexPlayer.etcController().adResolver(mediaID->Collections.emptyList());
        playlist = new PlaylistManager(new EmptyPlaylistListener() {
            public void onNext(@NotNull MediaID mediaID) {
                current = mediaID;
            }
            public void onPlaylistFinished() { Thread.currentThread().interrupt(); }
        });
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
