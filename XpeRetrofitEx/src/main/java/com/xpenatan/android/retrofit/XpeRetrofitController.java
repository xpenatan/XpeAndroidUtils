package com.xpenatan.android.retrofit;

import java.util.ArrayList;

import retrofit2.Call;

public class XpeRetrofitController {

    private final ArrayList<XpeRequestCallback> callers = new ArrayList<>();

    private boolean isCall;
    private boolean holdCall;

    public void push(XpeRequestCallback callback) {
        callback.retrofitController = this;
        callers.add(callback);
        if(!holdCall) {
            next();
        }
    }

    public void pushToFirst(XpeRequestCallback callback) {
        callback.retrofitController = this;
        callers.add(0, callback);
    }

    void next() {
        if(callers.size() != 0) {
            isCall = true;
            // prepare to call
            XpeRequestCallback first = callers.get(0);
            first.onPrepareCall();
            /**
             *   Now remove it.  This feature allows to add more calls on onPrepareCall if this current caller
             *   is not ready yet to call
            */
            XpeRequestCallback next = callers.remove(0);

            Call<?> caller = next.onCreateCall();
            String exception = null;
            if(caller == null ) {
                exception = "Caller is null";
            }
            if(exception != null)
                throw new RuntimeException(exception);
            caller.enqueue(next);
        }
    }

    void onRespose(XpeRequestCallback callback) {
        // may be on main thread
        try {
            callback.onResponse(this);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        if(callers.size() == 0)
            isCall = false;
        else {
            next();
        }
    }

    public void holdCall() {
        holdCall = true;
    }

    public void call() {
        if(!isCall) {
            holdCall = false;
            next();
        }
    }

    public void clearCallers() {
        callers.clear();
        isCall = false;
    }

    public void postMainThread(Runnable runnable) {
        runnable.run();
    }

}
