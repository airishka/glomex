package de.glomex.player.model.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.lifecycle.LifecycleListener;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.events.EventHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayerFactory {

    private static class GlomexModule extends AbstractModule {

        private final GlomexPlayer glomexPlayer;

        public GlomexModule(GlomexPlayer player) {
            this.glomexPlayer = player;
        }

        @Override
        protected void configure() {
            bind(GlomexPlayer.class).toInstance(glomexPlayer);
            bind(ExecutionManager.class).toInstance(glomexPlayer.executionManager());

            bind(ActionDispatcher.class).toInstance(glomexPlayer.actionDispatcher());
            bind(EventHandler.class).toInstance(glomexPlayer.eventHandler());

            bind(EtcControl.class).toInstance(glomexPlayer.etcController());
            bind(EtcController.class).toInstance((EtcController) glomexPlayer.etcController());
            bind(LifecycleListener.class).toInstance(glomexPlayer.eventHandler().lifecycleListener());
            bind(PlaybackListener.class).toInstance(glomexPlayer.eventHandler().playbackListener());
        }
    }

    private static Injector injector = Guice.createInjector();

    public static GlomexPlayer create() {
        GlomexPlayer glomexPlayer = new GlomexPlayer();
        injector = Guice.createInjector(new GlomexModule(glomexPlayer));
        return glomexPlayer;
    }

    public static @NotNull <L> L instance(@NotNull Class<? extends L> type) {
        return injector.getInstance(type);
    }

}
