package de.glomex.player.api.lifecycle;

import de.glomex.player.api.ListenerTag;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 * TODO: split to 3 i/f
 */
@SuppressWarnings("UnusedDeclaration")
public interface LifecycleListener extends ListenerTag {

    void onContentResolved(@NotNull MediaID mediaID);

    void onContentError(@NotNull MediaID mediaID, @NotNull String message);

    void onAdsResolved(@NotNull MediaID mediaID);

    void onAdsError(@NotNull MediaID mediaID, @NotNull String message);

    void onContentStarted(@NotNull MediaID mediaID);

    void onContentCompleted(@NotNull MediaID mediaID);

    void onAdStarted(@NotNull MediaID adID);

    void onAdCompleted(@NotNull MediaID adID);

    void onLifecycleStarted(@NotNull MediaID mediaID);

    void onLifecycleCompleted(@NotNull MediaID mediaID);

    void onLifecycleError(@NotNull MediaID mediaID, @NotNull String message);
}
