package ru.farpost.dmitry.farposttestappse.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.farpost.dmitry.farposttestappse.model.Response;

public interface GitHubRepos {

    @GET("search/repositories?")
    Call<Response> getRepos(@Query("q") String keyword);

}
