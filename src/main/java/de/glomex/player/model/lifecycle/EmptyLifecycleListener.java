package de.glomex.player.model.lifecycle;

import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class EmptyLifecycleListener implements LifecycleListener {

    @Override
    public void onContentResolved(@NotNull MediaID mediaID) {}

    @Override
    public void onContentError(@NotNull MediaID mediaID) {}

    @Override
    public void onAdsResolved(@NotNull MediaID mediaID) {}

    @Override
    public void onAdsError(@NotNull MediaID mediaID) {}

    @Override
    public void onContentStarted(@NotNull MediaID mediaID) {}

    @Override
    public void onContentCompleted(@NotNull MediaID mediaID) {}

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
