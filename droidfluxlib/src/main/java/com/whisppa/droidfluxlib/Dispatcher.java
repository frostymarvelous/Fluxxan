package com.whisppa.droidfluxlib;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by user on 5/6/2015.
 */
public interface Dispatcher {
    public void addStore(@NonNull String name, @NonNull Store store);
    public void dispatch(@NonNull Payload payload) throws Exception;
    public void dispatch(@NonNull Payload payload, DispatchExceptionListener dispatchExceptionListener) throws Exception;
    public void waitFor(String waitingStoreName, Set<String> storeNames, Callback callback) throws Exception;
    public boolean isDispatching();
}