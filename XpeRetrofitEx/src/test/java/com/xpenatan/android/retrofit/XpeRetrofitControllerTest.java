package com.xpenatan.android.retrofit;

import com.xpenatan.android.retrofit.network.RetrofitTest;
import com.xpenatan.android.retrofit.network.api.ApiPathTests;
import com.xpenatan.android.retrofit.network.api.CEPServiceAPI;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

public class XpeRetrofitControllerTest {

    @Test
    public void shouldPassSimpleTest() {

        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        Retrofit retrofit = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);

        XpeRetrofitController controller = new XpeRetrofitController();

        controller.push(new XpeRequestCallback() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("#### Retrofit response");
                System.out.println("##------: " + status);

                if(status == XpeRequestStatus.SUCCESS || status == XpeRequestStatus.ERROR)
                    retrofitController.next();
            }

            @Override
            public void onPrepareCall(XpeRetrofitController retrofitController) {

            }

            @Override
            public Call onCreateCall(XpeRetrofitController retrofitController) {
                CEPServiceAPI cepServiceAPI = RetrofitTest.provideCEPApi(retrofit);
                return cepServiceAPI.checkCEP("74230-110");
            }
        });

        System.out.println("#### Retrofit Calling");
        controller.call();
        System.out.println("#### Retrofit Finish");
    }

}
