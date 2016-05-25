package ru.farpost.dmitry.farposttestappse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.farpost.dmitry.farposttestappse.R;
import ru.farpost.dmitry.farposttestappse.model.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Response response;
    private int count = 10;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(response.items.get(position).owner.avatar_url).into(holder.image);
        holder.name.setText(response.items.get(position).full_name);
        holder.description.setText(response.items.get(position).description);
    }

    @Override
    public int getItemCount() {
//        if (response != null){
//            return response.items.size();
//        }
//        return 0;
        if (response != null){
            if (response.items.size() < count){
                return response.items.size();
            } else {
                return count;
            }
        } else{
            return 0;
        }
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