package de.glomex.player.api.lifecycle;

import de.glomex.player.api.events.ListenerTag;
import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface LifecycleListener extends ListenerTag {

    void onMediaResolved(@NotNull MediaID contentID);

    void onMediaError(@NotNull MediaID mediaID);

    void onAdsResolved(@NotNull MediaID contentID);

    void onAdError(@NotNull MediaID adID);

    void onContentStarted(@NotNull MediaID contentID);

    void onContentCompleted(@NotNull MediaID contentID);

    void onAdStarted(@NotNull MediaID adID);

    void onAdCompleted(@NotNull MediaID adID);

}
