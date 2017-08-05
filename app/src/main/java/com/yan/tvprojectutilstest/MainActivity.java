package com.yan.tvprojectutilstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NavMovieAdapter dataAdapter;
    NavMovieAdapter2 dataAdapter2;

    private List<String> stringList = new ArrayList<>();
    private List<String> stringList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        final FocusRecyclerView rvData = (FocusRecyclerView) findViewById(R.id.rv_data);
        GridLayoutManager focusGridLayoutManager = new GridLayoutManager(getApplicationContext(), 7);
        rvData.setLayoutManager(focusGridLayoutManager);
        rvData.setAdapter(dataAdapter = new NavMovieAdapter(this, stringList));
        rvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (rvData.isRecyclerViewToBottom()) {
                        addData();
                    }
                }
            }
        });

        addData();

        final FocusRecyclerView rvData2 = (FocusRecyclerView) findViewById(R.id.rv_data2);
        GridLayoutManager focusGridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 3);
        focusGridLayoutManager2.setOrientation(GridLayoutManager.HORIZONTAL);
        rvData2.setLayoutManager(focusGridLayoutManager2);
        rvData2.setAdapter(dataAdapter2 = new NavMovieAdapter2(this, stringList2));
        rvData2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (rvData2.isRecyclerViewToBottom()) {
                        addData2();
                    }
                }
            }
        });

        addData2();


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvData.setVisibility(View.GONE);
                rvData2.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvData.setVisibility(View.VISIBLE);
                rvData2.setVisibility(View.GONE);
            }
        });
    }

    private void addData() {
        findViewById(R.id.rv_data).postDelayed(new Runnable() {
            @Override
            public void run() {
                int tempSize = stringList.size();
                stringList.add("SDFASDFSDFADFSDAF");
                stringList.add("测试测试测试测试测试测试测试测试");
                stringList.add("测试测试测试测试");
                stringList.add("测试");
                stringList.add("SDFASDFSDFADFSDAF");
                stringList.add("测试测试测试测试测试测试测试测试");
                stringList.add("测试测试测试测试");
                stringList.add("测试");
                stringList.add("SDFASDFSDFADFSDAF");
                stringList.add("测试测试测试测试测试测试测试测试");
                stringList.add("测试测试测试测试");
                stringList.add("测试");
                stringList.add("SDFASDFSDFADFSDAF");
                stringList.add("测试测试测试测试测试测试测试测试");
                stringList.add("测试测试测试测试");
                stringList.add("测试");

                dataAdapter.notifyItemRangeInserted(tempSize, stringList.size() - tempSize);
            }
        }, 400);

    }

    private void addData2() {
        findViewById(R.id.rv_data).postDelayed(new Runnable() {
            @Override
            public void run() {
                int tempSize = stringList2.size();
                stringList2.add("SDFASDFSDFADFSDAF");
                stringList2.add("测试测试测试测试测试测试测试测试");
                stringList2.add("测试测试测试测试");
                stringList2.add("测试");
                stringList2.add("SDFASDFSDFADFSDAF");
                stringList2.add("测试测试测试测试测试测试测试测试");
                stringList2.add("测试测试测试测试");
                stringList2.add("测试");
                stringList2.add("SDFASDFSDFADFSDAF");
                stringList2.add("测试测试测试测试测试测试测试测试");
                stringList2.add("测试测试测试测试");
                stringList2.add("测试");
                stringList2.add("SDFASDFSDFADFSDAF");
                stringList2.add("测试测试测试测试测试测试测试测试");
                stringList2.add("测试测试测试测试");
                stringList2.add("测试");

                dataAdapter2.notifyItemRangeInserted(tempSize, stringList2.size() - tempSize);

            }
        }, 400);
    }
}
