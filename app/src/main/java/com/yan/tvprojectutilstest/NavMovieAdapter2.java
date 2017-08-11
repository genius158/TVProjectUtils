package com.yan.tvprojectutilstest;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yan on 2017/7/21.
 */

public class NavMovieAdapter2 extends RecyclerView.Adapter<NavMovieHolder> {
    protected final Context context;
    private final List<String> stringList;

    public NavMovieAdapter2(Context context, List<String> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_all_movie2, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.ivPoster.setImageURI(Uri.parse("res:// /" + R.drawable.bg));
        holder.ivPoster.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationHelper animationHelper = new AnimationHelper();
                animationHelper.setRatioX(1.2f);
                animationHelper.setRatioY(1.2f);
                if (hasFocus) {
                    animationHelper.starLargeAnimation(v);
                } else {
                    animationHelper.starSmallAnimation(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

}
