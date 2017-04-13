package de.glomex.player.model.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.model.events.EventHandler;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayerFactory {

    public static Injector injector;

    private static class GlomexModule extends AbstractModule {

        private final GlomexPlayer glomexPlayer;

        public GlomexModule(GlomexPlayer player) {
            this.glomexPlayer = player;
        }

        @Override
        protected void configure() {
            bind(GlomexPlayer.class).toInstance(glomexPlayer);
            bind(EtcControl.class).toInstance(glomexPlayer.etcController());
            bind(EventHandler.class).toInstance(glomexPlayer.eventHandler());
            bind(ExecutionManager.class).toInstance(glomexPlayer.executionManager());
        }
    }

    public static GlomexPlayer create() {
        GlomexPlayer glomexPlayer = new GlomexPlayer();
        injector = Guice.createInjector(new GlomexModule(glomexPlayer));
        return glomexPlayer;
    }

}
