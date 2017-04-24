package de.glomex.player.api.etc;

import de.glomex.player.api.ListenerTag;

/**
 * Java8 has no empty callback, whilst using Runnable considered as bad practise because of confusing threads relations.
 *
 * Created by <b>me@olexxa.com</b>
 */
@FunctionalInterface
public interface ShutdownListener extends ListenerTag {

    void onShutdown();

}
