package de.glomex.player.javafx;

/**
 * Java8 has no empty callback, whilst using Runnable considered as bad practise because of confusing threads relations.
 *
 * Created by <b>me@olexxa.com</b>
 */
@FunctionalInterface
public interface Callback {

    void callback();

}
