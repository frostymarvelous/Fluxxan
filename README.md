# DroidFlux

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-DroidFlux-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1786)

DroidFlux is an Android port of the popular [Fluxxor](http://fluxxor.com) library that seeks to implement the [Flux Architecture](https://facebook.github.io/flux/) as popularised by Facebook.

This library is direct port of Fluxxor and works a lot like it. It is currently highly experimental but I wish to develop it into a full fledged library. It is untested and I advise against using it in production.

I currently need help in the following aspects:
  - Writing Tests
  - Proper Concurrency
  - Performance Tuning
  - Documentation

Until that is done, I won't be releasing it on Maven/JCenter.

### Version
0.0.3


### ChangeLog

####0.0.3
  - Added queue and background thread to handle dispatches off the main thread to increase UI performance.
  - Store listener notification is no longer done on the main thread. UI changes need to be explicitly executed on the main thread.
  - Added `ThreadUtils.runOnMain` helper to help run UI updates on the main thread from `onChanged` method
  - Added `StoreListenerFragment` and `StoreListenerView` base classes
  - Renamed `StoreActivity` to `StoreListenerActivity`
  - Added `Dispatcher.start` and `Dispatcher.stop` methods

####0.0.2
  - Notify store listeners on the UI thread
  - Added `getFlux` method to `Store`
  - Synchronized `Dispatcher.dispatch` method

####0.0.1
  - Initial Code Commit


### Installation

Simply clone the project and open in Android Studio. It contains a sample that illustrates a basic use. You can play around with it.

### Actions

This object holds all the actions you can take in your App. You can structure this however you wish. It must implement `com.whisppa.droidfluxlib.Actions` or better still extend `com.whisppa.droidfluxlib.impl.AbstractActionsImpl` which implements the required interface. 

Only Actions should dispatch `Payload`s. If you need anything done in your app, you call an Action and it does the work on your behalf. Any Asynchronous work must be done in the method before the `Payload` is dispatched.

    public class MyActions extends AbstractActionsImpl {
        public static final String GET_USER = "GET_USER";
    
        public void getUsers() {
            try {
                mDispatcher.dispatch(new Payload(GET_USER, new Bundle()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


### Payload

A Payload holds both the action type (A Constant string which Stores can bind to) and data bundle.

### Store

A given Store holds some required data. It reacts to dispatched payloads to perform actions. A store returns it's State (any custom object) via `getState`. It binds to actions via the annotation `@BindAction("ActionName")`. The given method be accessible (public) and take a single `Payload` parameter. Stores must implement `com.whisppa.droidfluxlib.Store` or extend the abstract implementation `com.whisppa.droidfluxlib.impl.AbstractStoreImpl`.

A store can take listeners of type `StoreListener` which it notifies of changes to its state using `notifyListeners()`.

A payload will usually maybe carry some data which a Store will use. E.g. A user ID which the store will use in retrieving data from it's backing store. A payload can actually contain the user data which the Store will simply set as state. More examples to come soon.

Stores cannot be asynchronous by nature and must return immediately. Any Asynchronous activity must be done in the `Action`. Also, the handler method will be run on the UI thread and therefore should return as quickly as possible.

    public class MyStore extends AbstractStoreImpl<String> {
        String user = "User: Default";
    
		@BindAction(MyActions.GET_USER)
        public void getUser(Payload payload) {
            user = "User: " + new Random().nextInt();
            this.notifyListeners();
        }
    
        @Override
        public String getState() {
            return user;
        }
    }


### Flux
This is the main Class that holds everything together. This should be the central point for accessing 
  - Stores
  - Actions
 
There should be only one instance of this in your App and the best place to instantiate it is in a Custom Application.

    public class MyApp extends Application {
    
        static Flux<MyActions> DroidFlux;
    
        @Override
        public void onCreate() {
            super.onCreate();

            DroidFlux = new Flux<MyActions>(new Store[]{new MyStore(), new MyOtherStore()}, new MyActions());
            DroidFlux.getDispatcher().start();
        }

        public void onTerminate() {
            super.onTerminate();

            DroidFlux.getDispatcher().stop();
        }
    
        public static Flux<MyActions> getFlux() {
            return DroidFlux;
        }
    }

	
### License
The MIT License
