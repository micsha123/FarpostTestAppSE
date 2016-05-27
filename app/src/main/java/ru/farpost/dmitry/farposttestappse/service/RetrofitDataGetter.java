package ru.farpost.dmitry.farposttestappse.service;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.farpost.dmitry.farposttestappse.Constants;
import ru.farpost.dmitry.farposttestappse.api.GitHubRepos;
import ru.farpost.dmitry.farposttestappse.model.EventResult;
import ru.farpost.dmitry.farposttestappse.model.Repository;
import ru.farpost.dmitry.farposttestappse.model.Response;

public class RetrofitDataGetter {

    private static RetrofitDataGetter instance = null;
    private Retrofit retrofit;
    private GitHubRepos gitHubRepos;
    private Call<Response> callResponse;
    private ArrayList<Repository> items = new ArrayList<Repository>();

    protected RetrofitDataGetter() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gitHubRepos = retrofit.create(GitHubRepos.class);

    }

    public static RetrofitDataGetter getInstance() {
        if (instance == null) {
            synchronized (RetrofitDataGetter.class) {
                if (instance == null) {
                    instance = new RetrofitDataGetter();
                }
            }
        }
        return instance;
    }

    public void getItems(String keyword) {

        if(callResponse != null && callResponse.isExecuted()){
            callResponse.cancel();
        }

        callResponse = gitHubRepos.getRepos(keyword);
        callResponse.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null) {
                    EventBus.getDefault().post(new EventResult(response.body().items));
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t) {
            }
        });
    }

}
