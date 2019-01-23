package com.xpenatan.android.retrofit;

import com.xpenatan.android.retrofit.network.RetrofitTest;
import com.xpenatan.android.retrofit.network.api.ApiPathTests;
import com.xpenatan.android.retrofit.network.api.CEPServiceAPI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

public class XpeRetrofitControllerTest {

    Retrofit retrofitCep;

    @Before
    public void init() {
        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        retrofitCep = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);
    }

        @Test
    public void oneCallTest() {



        XpeRetrofitController controller = new XpeRetrofitController();

        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### Retrofit response");

                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();

                if(status == XpeRequestStatus.SUCCESS)
                    retrofitController.next();
            }
        });

        System.out.println("#### Retrofit Calling");
        controller.call();
        System.out.println("#### Retrofit Finish");
    }

    private int twoCallTest = 0;

    @Test
    public void twoCallTest() {

        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        Retrofit retrofit = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);

        XpeRetrofitController controller = new XpeRetrofitController();

        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 01");


                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();
                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallTest++;
                    retrofitController.next();
                }
            }
        });
        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 02");

                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();

                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallTest++;
                    retrofitController.next();
                }
            }
        });

        controller.call();
        Assert.assertEquals(2, twoCallTest);
    }

    private int twoCallOnePriorityTest = 0;

    @Test
    public void twoCallOnePriorityTest() {

        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        Retrofit retrofit = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);

        XpeRetrofitController controller = new XpeRetrofitController();

        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 01");


                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();
                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallOnePriorityTest++;
                    retrofitController.next();
                }
            }
        });
        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 03");

                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();

                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallOnePriorityTest++;
                    retrofitController.next();
                }
            }

            @Override
            public void onPrepareCall(XpeRetrofitController retrofitController) {

                controller.pushToFirst(new XpeRequestCallbackZipCode() {
                    @Override
                    public void onResponse(XpeRetrofitController retrofitController) {
                        System.out.println("##------: " + status);
                        System.out.println("#### CALL 02");

                        if(status == XpeRequestStatus.ERROR)
                            Assert.fail();

                        if(status == XpeRequestStatus.SUCCESS) {
                            twoCallOnePriorityTest++;
                            retrofitController.next();
                        }
                    }
                });

            }
        });

        controller.call();
        Assert.assertEquals(3, twoCallOnePriorityTest);
    }



    public class XpeRequestCallbackZipCode extends XpeRequestCallback {

        @Override
        public void onPrepareCall(XpeRetrofitController retrofitController) {
        }

        @Override
        public Call onCreateCall(XpeRetrofitController retrofitController) {
            CEPServiceAPI cepServiceAPI = RetrofitTest.provideCEPApi(retrofitCep);
            return cepServiceAPI.checkCEP("74230-110");
        }

        @Override
        public void onResponse(XpeRetrofitController retrofitController) {

        }
    }

}
