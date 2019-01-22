package com.xpenatan.android.retrofit.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Natan Guilherme on 8/26/2016.
 */
public class CEPResponse {
    @SerializedName("erro")
    boolean erro;
    @SerializedName("cep")
    String cep;
    @SerializedName("logradouro")
    String logradouro;
    @SerializedName("complemento")
    String complemento;
    @SerializedName("bairro")
    String bairro;
    @SerializedName("localidade")
    String localidade;
    @SerializedName("uf")
    String uf;
    @SerializedName("unidade")
    String unidade;
    @SerializedName("ibge")
    String ibge;
    @SerializedName("gia")
    String gia;

    public String getGia() {
        return gia;
    }

    public boolean isError() {
        return erro;
    }
    public boolean isSuccess() {
        return !erro;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getUf() {
        return uf;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getIbge() {
        return ibge;
    }
}
