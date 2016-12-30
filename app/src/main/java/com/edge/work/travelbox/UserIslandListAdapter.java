package com.edge.work.travelbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_userarchlist, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(data.get(position).type.endsWith("S")||data.get(position).type.endsWith("s")){
            holder.textView.setText("2X2");
        } else if(data.get(position).type.endsWith("B")||data.get(position).type.endsWith("b")){
            holder.textView.setText("2X3");
        }
        ImageLoader.getInstance().displayImage(data.get(position).url,holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyislandIslandFragment.setCurrentShopID(data.get(position).arch);
                if (data.get(position).type.endsWith("S")){
                    MyislandIslandFragment.pickStatusOpen(false);
                } else {
                    MyislandIslandFragment.pickStatusOpen(true);
                }

            }
        });
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
