package com.jm.newvista.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.jm.newvista.R;
import com.jm.newvista.mvp.model.MovieScheduleModel;
import com.jm.newvista.mvp.presenter.MovieSchedulePresenter;
import com.jm.newvista.mvp.view.MovieScheduleView;
import com.jm.newvista.receiver.FinishActivityReceiver;
import com.jm.newvista.ui.adapter.MovieScheduleRecyclerViewAdapter;
import com.jm.newvista.ui.base.BaseActivity;

public class MovieScheduleActivity
        extends BaseActivity<MovieScheduleModel, MovieScheduleView, MovieSchedulePresenter>
        implements MovieScheduleView {
    private Toolbar toolbar;
    private RecyclerView movieScheduleRecyclerView;
    private MovieScheduleRecyclerViewAdapter movieScheduleRecyclerViewAdapter;

    private FinishActivityReceiver finishActivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_schedule);

        finishActivityReceiver = new FinishActivityReceiver(this);
        registerReceiver(finishActivityReceiver, new IntentFilter("FinishActivity:PaymentDone"));

        initView();
        getPresenter().updateTooBarTitle();
        getPresenter().getAndDisplayMovieSchedule();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(finishActivityReceiver);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        movieScheduleRecyclerView = findViewById(R.id.movieScheduleRecyclerView);
        movieScheduleRecyclerViewAdapter = new MovieScheduleRecyclerViewAdapter();
        movieScheduleRecyclerView.setAdapter(movieScheduleRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        movieScheduleRecyclerView.setLayoutManager(layoutManager);
        movieScheduleRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public MovieScheduleView createView() {
        return this;
    }

    @Override
    public MovieSchedulePresenter createPresenter() {
        return new MovieSchedulePresenter();
    }

    @Override
    public Intent onGetIntent() {
        return getIntent();
    }

    @Override
    public void onSetToolBarTitle(String toolBarTitle) {
        toolbar.setSubtitle(toolBarTitle);
    }

    @Override
    public MovieScheduleRecyclerViewAdapter onGetMovieScheduleRecyclerViewAdapter() {
        return movieScheduleRecyclerViewAdapter;
    }

    @Override
    public void onNullResult() {
        Toast.makeText(this, R.string.no_movie_schedule, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMovieScheduleFailure() {
        Toast.makeText(this, R.string.movie_schedule_error, Toast.LENGTH_SHORT).show();
    }
}
