package com.xpenatan.android.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class XpeRequestCallback<T> implements Callback<T>, Runnable {


    XpeRetrofitController retrofitController = null;

    Call<T> resoinseCall = null;
    int code;
    Response<T> rawResponse = null;
    T responseData = null;
    Throwable throwable;
    XpeRequestStatus status;

    private boolean init = false;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        this.rawResponse = response;
        this.code = response.code();
        boolean successful = response.isSuccessful();

        if(successful) {
            status = XpeRequestStatus.SUCCESS;
            String rawJSON = response.message();
            responseData = response.body();

        }
        else {
            ResponseBody responseBody = response.errorBody();
            status = XpeRequestStatus.ERROR;

        }


        retrofitController.postMainThread(this);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        status = XpeRequestStatus.ERROR;
        throwable = t;
        code = 0;
        retrofitController.postMainThread(this);
    }

    protected abstract void onPrepareCall(XpeRetrofitController retrofitController);
    protected abstract Call<T> onCreateCall(XpeRetrofitController retrofitController);
    /**
     * Response status will be LOADING, SUCCESS or ERROR
     */
    public abstract void onResponse(XpeRetrofitController retrofitController);

    void onPrepareCall() {
        if(!init) {
            init = true;
            onPrepareCall(retrofitController);
        }
    }

    Call<T>  onCreateCall() {
        return onCreateCall(retrofitController);
    }

    /**
     * onPrepareCall default is to call one time. This will make it call multiple times as needed
     * Caution: Can cause dead lock
     */
    public void resetOnPrepare() {
        init = false;
    }

    @Override
    public void run() {
        retrofitController.onRespose();
        onResponse(retrofitController);
    }

}
