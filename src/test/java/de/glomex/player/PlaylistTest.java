package de.glomex.player;

import de.glomex.player.api.events.ListenerTag;
import de.glomex.player.api.playlist.MediaID;
import de.glomex.player.api.playlist.PlaylistListener;
import de.glomex.player.model.api.EtcController;
import de.glomex.player.model.api.ExecutionManager;
import de.glomex.player.model.api.GlomexPlayer;
import de.glomex.player.model.events.EventHandler;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.lifecycle.LifecycleManager;
import de.glomex.player.model.media.MediaUUID;
import de.glomex.player.model.playlist.PlaylistManager;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistTest extends TestCase {

    class MockPlaylistListener implements PlaylistListener {
        public void onChanged() {}
        public void onNext(@NotNull MediaID mediaID) { current = mediaID; }
        public void onFinished() {}
    }

    final MediaID
        a = new MediaUUID(),
        b = new MediaUUID(),
        c = new MediaUUID(),
        d = new MediaUUID();

    final PlaylistManager playlist = new PlaylistManager(new MockPlaylistListener()) {
        @SuppressWarnings("ConstantConditions")
        @Override
        protected LifecycleManager createLifecycleManager() {
            GlomexPlayer player = new GlomexPlayer();
            return new LifecycleManager(player.executionManager(), (EtcController) player.etcController(), player.eventHandler());
        }
    };

    MediaID current;

    public PlaylistTest() throws MalformedURLException {}

    public void testNotRepeatableNotRandom() {
        playlist.addContent(a, b, c, d);

        playlist.setRandom(false);
        playlist.setRepeatable(false);

        playlist.skipTo(a);
        assertSame(a, current);
        playlist.skipTo(b);
        assertSame(b, current);

        playlist.next();
        assertSame(c, current);
        playlist.next();
        assertSame(d, current);

        playlist.prev();
        assertSame(c, current);
        playlist.prev();
        assertSame(b, current);
        playlist.prev();
        assertSame(a, current);
    }

    public void testNotRepeatableRandom() {
        playlist.addContent(a, b, c, d);

        playlist.setRandom(true);
        playlist.setRepeatable(false);

        playlist.skipTo(b);
        assertSame(b, current);
        playlist.next();
        assertNotSame(b, current);
        MediaID second = current;
        playlist.next();
        assertNotSame(b, current);
        assertNotSame(second, current);
        MediaID third = current;
        playlist.next();
        assertNotSame(b, current);
        assertNotSame(second, current);
        assertNotSame(third, current);

        playlist.prev();
        assertSame(third, current);
        playlist.prev();
        assertSame(second, current);
        playlist.prev();
        assertSame(b, current);
    }

}
