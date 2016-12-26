package com.edge.work.travelbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Super on 12/26/2016.
 */

public class UserIslandListAdapter extends RecyclerView.Adapter<UserIslandListAdapter.MyViewHolder>{

    Context context;
    ArrayList<UserIslandListInfo> data;

    LayoutInflater inflater;



    public UserIslandListAdapter(Context context, ArrayList<UserIslandListInfo> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.userarchlist_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView.setText(data.get(position).type);
        ImageLoader.getInstance().displayImage(data.get(position).url,holder.imageView);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.text_row);
            imageView = (ImageView)itemView.findViewById(R.id.img_row);
        }
    }
}
