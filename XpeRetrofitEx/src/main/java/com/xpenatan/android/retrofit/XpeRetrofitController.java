package com.xpenatan.android.retrofit;

import java.util.ArrayList;

import retrofit2.Call;

public class XpeRetrofitController {

    private final ArrayList<XpeRequestCallback> callers = new ArrayList<>();

    private boolean isCall;

    public void push(XpeRequestCallback callback) {
        callers.add(callback);
    }

    public void pushToFirst(XpeRequestCallback callback) {
        callers.add(0, callback);
    }

    public void next() {
        if(callers.size() != 0) {
            // prepare to call
            XpeRequestCallback first = callers.get(0);
            first.retrofitController = this;
            first.onPrepareCall(this);
            /**
             *   Now remove it.  This feature allows to add more calls on onPrepareCall if this current caller
             *   is not ready yet to call
            */
            XpeRequestCallback next = callers.remove(0);
            first.retrofitController = this;

            Call<?> caller = next.onCreateCall(this);
            String exception = null;
            if(caller == null ) {
                exception = "Caller is null";
            }
            if(exception != null)
                throw new RuntimeException(exception);
            caller.enqueue(next);
        }
    }

    void onRespose() {
        if(callers.size() == 0)
            isCall = false;
    }

    public void call() {
        if(isCall)
            throw new ExceptionInInitializerError("Call already called. Clear first before Calling");
        isCall = true;
        next();
    }

    public void clearCallers() {
        callers.clear();
        isCall = false;
    }

    public void postMainThread(Runnable runnable) {
        runnable.run();
    }

}
