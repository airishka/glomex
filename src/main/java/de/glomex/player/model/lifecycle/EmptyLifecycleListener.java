package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class EmptyLifecycleListener implements LifecycleListener {

    @Override
    public void onMediaResolved(@NotNull MediaID contentID) {}

    @Override
    public void onMediaError(@NotNull MediaID mediaID) {}

    @Override
    public void onAdsResolved(@NotNull MediaID contentID) {}

    @Override
    public void onAdError(@NotNull MediaID adID) {}

    @Override
    public void onMediaStarted(@NotNull MediaID contentID) {}

    @Override
    public void onMediaCompleted(@NotNull MediaID contentID) {}

    @Override
    public void onAdStarted(@NotNull MediaID adID) {}

    @Override
    public void onAdCompleted(@NotNull MediaID adID) {}

    @Override
    public void onLifecycleStarted(@NotNull MediaID mediaID) {}

    @Override
    public void onLifecycleCompleted(@NotNull MediaID mediaID) {}

    @Override
    public void onLifecycleError(@NotNull MediaID mediaID) {}

}
