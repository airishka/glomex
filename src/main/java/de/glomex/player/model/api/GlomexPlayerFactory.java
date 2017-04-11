package de.glomex.player.model.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayerFactory {

    static class GlomexModule extends AbstractModule {
        @Override
        protected void configure() {
            // TODO:
        }
    }

    public static GlomexPlayer create() {
        Injector injector = Guice.createInjector(new GlomexModule());
        return injector.getInstance(GlomexPlayer.class);
    }

}
