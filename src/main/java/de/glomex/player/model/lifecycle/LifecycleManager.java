package de.glomex.player.model.lifecycle;

import de.glomex.player.api.playlist.Content;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class LifecycleManager {

    private final Content content;

    public LifecycleManager(Content content) {
        this.content = content;
    }

    public void destroy() {
        // fixme: implement it
    }

}
