package com.olexxa.player.api;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;

/**
 * TODO: should it be async? In term of model, there is nothing to wait during api creation. Or could be hidden from api user
 *
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerAPIFactory<UI_IN, UI_OUT> {

    public static <UI_IN, UI_OUT> void createAPI(
        UI_IN embedInto,
        boolean useDefaultControls,
        BiConsumer<PlayerAPI, UI_OUT> callback
    ) {
        ServiceLoader<PlayerAPIFactory> loader = ServiceLoader.load(PlayerAPIFactory.class);
        Iterator<PlayerAPIFactory> implementations = loader.iterator();
        if (!implementations.hasNext())
            throw new IllegalArgumentException("No PlayerAPI implementation is registered using META-INF services");

        @SuppressWarnings("unchecked")
        PlayerAPIFactory<UI_IN, UI_OUT> factory = implementations.next();
        factory.create(embedInto, useDefaultControls, callback);
    }

    protected abstract void create(UI_IN embedInto, boolean useDefaultControls, BiConsumer<PlayerAPI, UI_OUT> callback);

}
