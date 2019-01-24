package com.xpenatan.android.retrofit.tests;

import com.xpenatan.android.retrofit.XpeRequestCallback;
import com.xpenatan.android.retrofit.XpeRequestStatus;
import com.xpenatan.android.retrofit.XpeRetrofitController;
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
            }
        });
    }

    private int twoCallTest = 0;

    public void twoCallTest(boolean onHold) {
        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        Retrofit retrofit = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);

        XpeRetrofitController controller = new XpeRetrofitController();

        if(onHold)
            controller.holdCall();

        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 01");


                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();
                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallTest++;
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
                }
            }
        });
        if(onHold)
            controller.call();
        Assert.assertEquals(2, twoCallTest);
    }

    @Test
    public void twoCallTest() {
        twoCallTest(false);
    }

    @Test
    public void twoCallTestOnHold() {
        twoCallTest(true);
    }

    private int twoCallOnePriorityTest = 0;


    public void twoCallOnePriorityTest(boolean onHold) {

        OkHttpClient okHttpClient = RetrofitTest.provideHttpClient();
        Retrofit retrofit = RetrofitTest.provideRetrofit(ApiPathTests.PATH_SERVER_VALIDATE_CEP, okHttpClient);

        XpeRetrofitController controller = new XpeRetrofitController();

        if(onHold)
            controller.holdCall();

        controller.push(new XpeRequestCallbackZipCode() {
            @Override
            public void onResponse(XpeRetrofitController retrofitController) {
                System.out.println("##------: " + status);
                System.out.println("#### CALL 01");


                if(status == XpeRequestStatus.ERROR)
                    Assert.fail();
                if(status == XpeRequestStatus.SUCCESS) {
                    twoCallOnePriorityTest++;
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
                        }
                    }
                });

            }
        });

        if(onHold)
            controller.call();

        Assert.assertEquals(3, twoCallOnePriorityTest);

    }

    @Test
    public void twoCallOnePriorityTest() {
        twoCallOnePriorityTest(false);
    }

    @Test
    public void twoCallOnePriorityTestOnHold() {
        twoCallOnePriorityTest(true);
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
