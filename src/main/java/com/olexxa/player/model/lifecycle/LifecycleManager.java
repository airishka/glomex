package com.olexxa.player.model.lifecycle;

import com.olexxa.player.api.playlist.Content;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    Content content;

    public void open(Content content) {
        if (this.content != null)
            close();
        this.content = content;
        // fixme: implement it
    }

    public void close() {
        content = null;
        // fixme: implement it
    }

    public Content current() {
        return content;
    }
}
