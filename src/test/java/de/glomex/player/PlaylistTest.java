package de.glomex.player;

import de.glomex.player.api.playlist.Content;
import de.glomex.player.model.api.ContentImpl;
import de.glomex.player.model.lifecycle.LifecycleManager;
import de.glomex.player.model.playlist.PlaylistManager;
import junit.framework.TestCase;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistTest extends TestCase {

    public void testNext() throws MalformedURLException {
        PlaylistManager playlist = new PlaylistManager();

        Content
            a = new ContentImpl("http://olexxa.com/1.avi"),
            b = new ContentImpl("http://olexxa.com/2.avi"),
            c = new ContentImpl("http://olexxa.com/3.avi"),
            d = new ContentImpl("http://olexxa.com/4.avi");

        playlist.addContent(a, b, c, d);

//        playlist.setRandom(true);
        playlist.setRepeatable(false);

        playlist.skipTo(a);
        assertSame(a, playlist.currentContent);
        playlist.skipTo(b);
        assertSame(b, playlist.currentContent);
        playlist.next();
        assertNotSame(a, playlist.currentContent);
        assertNotSame(b, playlist.currentContent);
        Content third = playlist.currentContent;
        playlist.next();
        assertNotSame(third, playlist.currentContent);
//        System.out.println(third.url());
    }


}
