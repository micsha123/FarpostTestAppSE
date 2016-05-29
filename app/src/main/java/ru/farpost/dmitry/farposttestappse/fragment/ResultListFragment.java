package ru.farpost.dmitry.farposttestappse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.akiniyalocts.pagingrecycler.PagingDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import ru.farpost.dmitry.farposttestappse.R;
import ru.farpost.dmitry.farposttestappse.adapter.RecyclerViewAdapter;
import ru.farpost.dmitry.farposttestappse.model.EventResult;
import ru.farpost.dmitry.farposttestappse.model.Repository;
import ru.farpost.dmitry.farposttestappse.service.RetrofitDataGetter;

public class ResultListFragment extends Fragment implements PagingDelegate.OnPageListener {

    private RecyclerViewAdapter adapter;
    private SearchView searchView;
    private CircularProgressBar progressbar;
    private ArrayList<Repository> items;
    private ArrayList<Repository> itemsForShowing;
    private RetrofitDataGetter dataGetter;

    private int visibleThreshold = 9;
    private int lastLoadedPos = 0;
    private CharSequence searchInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resultlist, container, false);

        progressbar = (CircularProgressBar) rootView.findViewById(R.id.progressbar);

        dataGetter = RetrofitDataGetter.getInstance();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        items = new ArrayList<>();
        itemsForShowing = new ArrayList<>();

        adapter = new RecyclerViewAdapter(getActivity(), itemsForShowing);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PagingDelegate pagingDelegate = new PagingDelegate.Builder(adapter)
                .attachTo(recyclerView)
                .listenWith(this)
                .build();

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        if(searchInput != null && searchInput.length() > 0){
            searchView.setQuery(searchInput, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsForShowing.clear();
                adapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(newText)) {
                    dataGetter.getItems(newText);
                    progressbar.setVisibility(View.VISIBLE);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Repository> arrayList = savedInstanceState.getParcelableArrayList("items");
            lastLoadedPos = savedInstanceState.getInt("lastLoadedPos");
            searchInput = savedInstanceState.getCharSequence("search");
            int a = savedInstanceState.getInt("progress");
            progressbar.setVisibility(savedInstanceState.getInt("progress") > 0 ? View.GONE : View.VISIBLE);
            items.addAll(arrayList);
            itemsForShowing.addAll(items.subList(0, lastLoadedPos));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", items);
        outState.putCharSequence("search", searchView.getQuery());
        outState.putInt("lastLoadedPos", lastLoadedPos);
        outState.putInt("progress", progressbar.getVisibility());
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventResult eventResult) {
        items = eventResult.getItems();
        if(items.size() < visibleThreshold){
            lastLoadedPos = items.size() - 1;
        } else {
            lastLoadedPos = visibleThreshold;
        }
        itemsForShowing.clear();
        itemsForShowing.addAll(items.subList(0, lastLoadedPos));
        progressbar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPage(int i) {
        if (i + visibleThreshold <= items.size()){
            itemsForShowing.addAll(items.subList(i, i + visibleThreshold));
            lastLoadedPos = i + visibleThreshold;
        } else {
            itemsForShowing.clear();
            itemsForShowing.addAll(items);
            lastLoadedPos = items.size() - 1;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDonePaging() {
    }
}
