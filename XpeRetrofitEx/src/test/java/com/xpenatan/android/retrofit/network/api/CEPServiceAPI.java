package com.xpenatan.android.retrofit.network.api;

import com.xpenatan.android.retrofit.network.model.CEPResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Natan Guilherme on 1/13/17.
 */

public interface CEPServiceAPI {
    @GET("/ws/{cep}/json/")
    Call<CEPResponse> checkCEP(@Path("cep") String cep);
}