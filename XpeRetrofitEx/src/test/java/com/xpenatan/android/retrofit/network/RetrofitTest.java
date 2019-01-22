package com.xpenatan.android.retrofit.network;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.xpenatan.android.retrofit.network.api.CEPServiceAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTest {


    public static OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                if(message != null && !message.isEmpty()) {
                    String msg = message;
                    try {
                        msg = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(message));
                        msg = " \n" + msg;
                    } catch (JsonSyntaxException m) {}
                    System.out.println(msg);
                }
            }
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.dispatcher(new Dispatcher(new SynchronousExecutorService()));
        // add logging as last interceptor
//        httpClient.addInterceptor(authorizationInterceptor);
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.writeTimeout(1, TimeUnit.MINUTES);
        return httpClient.build();
    }

    public static Retrofit provideRetrofit(String baseUrl, OkHttpClient httpClient) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
//                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(httpClient)
                .build();
    }

    public static CEPServiceAPI provideCEPApi(Retrofit retrofit){
        return retrofit.create(CEPServiceAPI.class);
    }


}
