package com.yan.tvprojectutilstest;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yan.tvprojectutils.MarqueeText;

/**
 * Created by yan on 2017/7/21.
 */
public class NavMovieHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView ivPoster;
    public MarqueeText tvTitle;
    public View pflContainer;

    public NavMovieHolder(View itemView) {
        super(itemView);
        ivPoster = (SimpleDraweeView) itemView.findViewById(R.id.iv_poster);
        if (itemView.findViewById(R.id.tv_movie_nav) != null) {
            tvTitle = (MarqueeText) itemView.findViewById(R.id.tv_movie_nav);
            pflContainer = itemView.findViewById(R.id.pfl_container);
        }
    }

}
