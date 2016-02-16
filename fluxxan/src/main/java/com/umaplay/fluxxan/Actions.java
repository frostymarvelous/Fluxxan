package com.umaplay.fluxxan;

/**
 * An Action that can be passed to {@link Flux#Flux(Object, Actions)}
 */
public interface Actions {
    /**
     * A convenience method to inject the dispatcher into the action
     * @param dispatcher The dispatcher
     */
    public void setDispatcher(Dispatcher dispatcher);
}
