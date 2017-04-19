package de.glomex.player.api.lifecycle;

import de.glomex.player.api.events.ListenerTag;
import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 * TODO: split to 3 i/f
 */
@SuppressWarnings("unused")
public interface LifecycleListener extends ListenerTag {

    void onMediaResolved(@NotNull MediaID mediaID);

    void onMediaError(@NotNull MediaID mediaID);

    void onAdsResolved(@NotNull MediaID adID);

    void onAdError(@NotNull MediaID adID);

    void onMediaStarted(@NotNull MediaID mediaID);

    void onMediaCompleted(@NotNull MediaID mediaID);

    void onAdStarted(@NotNull MediaID adID);

    void onAdCompleted(@NotNull MediaID adID);

    void onLifecycleStarted(@NotNull MediaID adID);

    void onLifecycleCompleted(@NotNull MediaID mediaID);

    void onLifecycleError(@NotNull MediaID mediaID);
}
