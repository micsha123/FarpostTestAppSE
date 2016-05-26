package ru.farpost.dmitry.farposttestappse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.farpost.dmitry.farposttestappse.Constants;
import ru.farpost.dmitry.farposttestappse.R;
import ru.farpost.dmitry.farposttestappse.adapter.RecyclerViewAdapter;
import ru.farpost.dmitry.farposttestappse.api.GitHubRepos;
import ru.farpost.dmitry.farposttestappse.model.Response;
import ru.farpost.dmitry.farposttestappse.utils.EndlessRecyclerOnScrollListener;

public class ResultListFragment extends Fragment {

    private GitHubRepos gitHubRepos;
    private Call<Response> callResponse;
    private RecyclerViewAdapter adapter;
    private Response responseGithub;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    public ResultListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resultlist, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gitHubRepos = retrofit.create(GitHubRepos.class);

        adapter = new RecyclerViewAdapter(getActivity());
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                adapter.setCount(adapter.getCount() + 10);
                adapter.notifyDataSetChanged();
            }
        };

        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                } else {
                    if(callResponse != null && callResponse.isExecuted()){
                        callResponse.cancel();
                    }
                    loadDataFromGitHub(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataFromGitHub(String keyword){
        callResponse = gitHubRepos.getRepos(keyword);

        callResponse.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null) {
                    responseGithub = response.body();
                    showDataOnScreen(responseGithub);
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t) {
            }
        });
    }

    private void showDataOnScreen(Response response){
        adapter.setCount(10);
        mEndlessRecyclerOnScrollListener.reset(0, true);
        adapter.setResponse(response);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            responseGithub = (Response) savedInstanceState.getParcelable("response");
            adapter.setResponse(responseGithub);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putParcelable("response", responseGithub);
    }
}
