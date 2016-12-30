package com.edge.work.travelbox;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Super on 12/30/2016.
 */

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<CollectionItemInfo> data;
    LayoutInflater inflater;
    FragmentManager fm;


    public CollectionItemAdapter(Context context, ArrayList<CollectionItemInfo> data, FragmentManager fm) {
        this.context = context;
        this.data = data;
        this.fm = fm;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.collection_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(data.get(position).imgUrl,holder.thumb);
        holder.name.setText(data.get(position).itemName);
        if(data.get(position).price!=null) {
            holder.price.setText("$ " + data.get(position).price);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("shopid",data.get(position).placeID);
                bundle.putString("category",data.get(position).category);
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.setArguments(bundle);

                fm.beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.main_container, infoFragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView thumb;
        TextView name,price;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.collection_item_img);
            name = (TextView) itemView.findViewById(R.id.collection_item_title);
            price = (TextView) itemView.findViewById(R.id.collection_item_price);
            card = (CardView) itemView.findViewById(R.id.collection_item);

        }
    }
}
