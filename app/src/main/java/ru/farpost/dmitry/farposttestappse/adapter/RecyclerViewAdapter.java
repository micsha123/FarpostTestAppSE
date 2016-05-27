package ru.farpost.dmitry.farposttestappse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akiniyalocts.pagingrecycler.PagingAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.farpost.dmitry.farposttestappse.R;
import ru.farpost.dmitry.farposttestappse.model.Repository;

public class RecyclerViewAdapter extends PagingAdapter {


    private Context context;
    private ArrayList<Repository> items;

    public RecyclerViewAdapter(Context context, ArrayList<Repository> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Picasso.with(context).load(items.get(position).owner.avatar_url).into(viewHolder.image);
        viewHolder.name.setText(items.get(position).full_name);
        viewHolder.description.setText(items.get(position).description);
    }

    @Override
    public int getPagingLayout() {
        return R.layout.item_recycler;
    }

    @Override
    public int getPagingItemCount() {
        return items.size();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            description = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}