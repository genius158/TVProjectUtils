package com.yan.tvprojectutilstest;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yan.tvprojectutils.MarqueeText;

import java.util.List;

/**
 * Created by yan on 2017/7/21.
 */

public class NavMovieAdapter extends RecyclerView.Adapter<NavMovieHolder> {
    protected final Context context;
    private final List<String> stringList;

    public NavMovieAdapter(Context context, List<String> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_all_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.ivPoster.setImageURI(Uri.parse("res:// /" + R.drawable.bg));

        holder.tvTitle.setText(stringList.get(position));
        holder.pflContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationHelper animationHelper = new AnimationHelper();

                animationHelper.setRatioX((v.getWidth() - dipToPx(context, 10)) / ((ViewGroup) v).getChildAt(0).getWidth());
                animationHelper.setRatioY((v.getHeight() - dipToPx(context, 10)) / ((ViewGroup) v).getChildAt(0).getHeight());
                MarqueeText tvTitle = (MarqueeText) v.findViewById(R.id.tv_movie_nav);
                tvTitle.setSelected(hasFocus);
                if (hasFocus) {
                    animationHelper.starLargeAnimation(((ViewGroup) v).getChildAt(0));
                    tvTitle.startScroll();
                } else {
                    animationHelper.starSmallAnimation(((ViewGroup) v).getChildAt(0));
                    tvTitle.stopScroll();
                }
            }

        });
    }

    private float dipToPx(Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

}
