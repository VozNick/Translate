package com.example.vmm408.translatefeb_27.interfaces;

import com.example.vmm408.translatefeb_27.model.ResponseModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface Query {
    @GET("/api/v1.5/tr.json/getLangs")
    Call<Map<String, ArrayList<String>>> getLangs(@QueryMap Map<String, String> map);

    @GET("/api/v1.5/tr.json/translate")
    Call<ResponseModel> translate(@QueryMap Map<String, String> map);

    @GET("/api/v1.5/tr.json/detect")
    Call<Map<String, String>> detect(@QueryMap Map<String, String> map);
}
