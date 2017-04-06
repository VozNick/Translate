package com.example.vmm408.translatefeb_27.workwithnet;

import android.content.Context;
import android.content.Intent;

import com.example.vmm408.translatefeb_27.interfaces.Query;
import com.example.vmm408.translatefeb_27.model.ResponseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkWithNet {
    private final String API_KEY =
            "trnsl.1.1.20170227T171240Z.3f06d44dbc0f6794.2f488c0ffea585d60a5ee1a94f380260dcbfb5dc";
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://translate.yandex.net")
            .build();
    private Query query = retrofit.create(Query.class);
    private Context context;

    public WorkWithNet(Context context) {
        this.context = context;
    }

    public void getLangs() {
        query.getLangs(initArray()).enqueue(new Callback<Map<String, ArrayList<String>>>() {
            @Override
            public void onResponse(Call<Map<String, ArrayList<String>>> call,
                                   Response<Map<String, ArrayList<String>>> response) {
                context.sendBroadcast(new Intent().setAction("loadedLangsAction")
                        .putExtra("langs", langsArray(response.body().get("dirs"))));
            }

            @Override
            public void onFailure(Call<Map<String, ArrayList<String>>> call, Throwable t) {
            }
        });
    }

    private Map<String, String> initArray() {
        Map<String, String> map = new TreeMap<>();
        map.put("key", API_KEY);
        return map;
    }

    private ArrayList<String> langsArray(ArrayList<String> response) {
        ArrayList<String> arrayList = new ArrayList<>(response);
        Collections.sort(arrayList);
        return arrayList;
    }

    public void translate(String words, String lang) {
        query.translate(initArray(words, lang)).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                context.sendBroadcast(new Intent().setAction("responseAction")
                        .putExtra("textTranslated", response.body().getText()[0]));
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }

    private Map<String, String> initArray(String words, String lang) {
        Map<String, String> map = new TreeMap<>();
        map.put("key", API_KEY);
        map.put("text", words);
        map.put("lang", lang);
        return map;
    }

    public void detect(String text) {
        query.detect(initArray(text)).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call,
                                   Response<Map<String, String>> response) {
                context.sendBroadcast(new Intent().setAction("changeLangsAction")
                        .putExtra("lang", response.body().toString()));
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
            }
        });
    }

    private Map<String, String> initArray(String text) {
        Map<String, String> map = new TreeMap<>();
        map.put("key", API_KEY);
        map.put("text", text);
        return map;
    }
}
