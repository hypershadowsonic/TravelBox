package com.edge.work.travelbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class CollectionTitleAdapter extends RecyclerView.Adapter<CollectionTitleAdapter.MyViewHolder> {

    Context context;
    ArrayList<CollectionTitleInfo> data;
    LayoutInflater inflater;
    private static RecyclerView shoplist, placelist;
    Boolean[] isExpand = new Boolean[10];
    FragmentManager fm;


    public CollectionTitleAdapter(Context context, ArrayList<CollectionTitleInfo> data, FragmentManager fm) {
        this.context = context;
        this.data = data;
        this.fm = fm;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.collection_container, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(data.get(position).collectionName);
        ImageLoader.getInstance().displayImage(data.get(position).titleImg,holder.title);
        isExpand[position]=false;


        final float scale = context.getResources().getDisplayMetrics().density;
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpand[position]){
                    startExpandAnimation(holder.colContainer, (int) (590 * scale + 0.5f), (int) (150 * scale + 0.5f));
                    startRotateAnimation(holder.expand, 0, 180);
                    Log.d("Collectiontitle", "Closing");
                    isExpand[position]=false;
                } else {
                    startExpandAnimation(holder.colContainer, (int) (150 * scale + 0.5f), (int) (590 * scale + 0.5f));
                    startRotateAnimation(holder.expand, 180, 0);
                    Log.d("Collectiontitle", "Expanding");
                    isExpand[position]=true;
                }
            }
        });

        CollectionItemAdapter shopadapter = new CollectionItemAdapter(context,CollectionItemData.getData(context,"x",data.get(position).collectionID),fm);
        CollectionItemAdapter placeadapter = new CollectionItemAdapter(context,CollectionItemData.getData(context,"y",data.get(position).collectionID),fm);
        shoplist.setHasFixedSize(true);
        shoplist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        shoplist.setAdapter(shopadapter);
        shoplist.setNestedScrollingEnabled(false);
        placelist.setHasFixedSize(true);
        placelist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        placelist.setAdapter(placeadapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView title, expand;
        TextView name;
        RelativeLayout colContainer;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (ImageView) itemView.findViewById(R.id.collection_title_img);
            expand = (ImageView) itemView.findViewById(R.id.collection_title_btn_expand);
            name = (TextView) itemView.findViewById(R.id.collection_title_name);
            shoplist = (RecyclerView) itemView.findViewById(R.id.collection_title_shop);
            placelist = (RecyclerView) itemView.findViewById(R.id.collection_title_place);
            colContainer = (RelativeLayout) itemView.findViewById(R.id.collection_container);
        }
    }

    private static void startExpandAnimation(final RelativeLayout relativeLayout, int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                relativeLayout.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }
    private static void startRotateAnimation(final ImageView imageView, int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                imageView.setRotation((int) animation.getAnimatedValue());
                //relativeLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                /*ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                relativeLayout.setLayoutParams(layoutParams);*/
            }
        });
        animator.start();
    }
}
