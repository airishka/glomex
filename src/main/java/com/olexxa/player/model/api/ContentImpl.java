package com.olexxa.player.model.api;

import com.olexxa.player.api.Content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class ContentImpl implements Content {

    final UUID uuid;
    final URL url;

    public ContentImpl(UUID uuid, String url) throws MalformedURLException {
        this(uuid, new URL(url));
    }

    public ContentImpl(UUID uuid, URL url) {
        this.uuid = uuid;
        this.url = url;
    }

    public UUID uuid() {
        return uuid;
    }

    public URL url() {
        return url;
    }

}
