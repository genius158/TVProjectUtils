package com.yan.tvprojectutilstest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yan on 2017/7/21.
 */

public class NavMovieAdapter2 extends NavMovieAdapter {


    public NavMovieAdapter2(Context context, List<String> objectList) {
        super(context, objectList);
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_all_movie2, parent, false) );
    }

}
