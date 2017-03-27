package com.olexxa.player;

import com.olexxa.player.api.playlist.Content;
import com.olexxa.player.model.api.ContentImpl;
import com.olexxa.player.model.lifecycle.LifecycleManager;
import com.olexxa.player.model.playlist.PlaylistManager;
import junit.framework.TestCase;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistTest extends TestCase {

    @Test
    public void testNext() throws MalformedURLException {
        LifecycleManager lifecycleManager = new LifecycleManager();
        PlaylistManager playlist = new PlaylistManager(lifecycleManager);

        Content
            a = new ContentImpl("http://olexxa.com/1.avi"),
            b = new ContentImpl("http://olexxa.com/2.avi"),
            c = new ContentImpl("http://olexxa.com/3.avi"),
            d = new ContentImpl("http://olexxa.com/4.avi");

        playlist.addContent(a, b, c, d);

//        playlist.setRandom(true);
        playlist.setRepeatable(false);

        playlist.skipTo(a);
        assertSame(a, lifecycleManager.current());
        playlist.skipTo(b);
        assertSame(b, lifecycleManager.current());
        playlist.next();
        assertNotSame(a, lifecycleManager.current());
        assertNotSame(b, lifecycleManager.current());
        Content third = lifecycleManager.current();
        playlist.next();
        assertNotSame(third, lifecycleManager);
//        System.out.println(third.url());
    }


}
