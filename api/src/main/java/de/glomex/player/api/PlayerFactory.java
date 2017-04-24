package de.glomex.player.api;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;

/**
 * TODO: should it be async? In term of model, there is nothing to wait during api creation.
 * Or could be hidden from api user, encapsulating it within synchronous methods with internal locks.
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class PlayerFactory<UI_IN, UI_OUT> {

    public static <UI_IN, UI_OUT> void createPlayer(
        @NotNull UI_IN embedInto,
        boolean useDefaultControls,
        @NotNull BiConsumer<PlayerAPI, UI_OUT> callback
    ) {
        ServiceLoader<PlayerFactory> loader = ServiceLoader.load(PlayerFactory.class);
        Iterator<PlayerFactory> implementations = loader.iterator();
        if (!implementations.hasNext())
            throw new IllegalArgumentException("No implementation is registered using META-INF services" + PlayerFactory.class);

        @SuppressWarnings("unchecked")
        PlayerFactory<UI_IN, UI_OUT> factory = implementations.next();
        factory.create(embedInto, useDefaultControls, callback);
    }

    protected abstract void create(
        @NotNull UI_IN embedInto,
        boolean useDefaultControls,
        @NotNull BiConsumer<PlayerAPI, UI_OUT> callback
    );

}
