package com.jm.newvista.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jm.newvista.R;
import com.jm.newvista.bean.MovieScheduleEntity;
import com.jm.newvista.ui.activity.MovieScheduleDetailActivity;
import com.jm.newvista.util.NetworkUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Johnny on 2/21/2018.
 */

public class MovieScheduleRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieScheduleRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<MovieScheduleEntity> movieSchedules;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_schedule, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MovieScheduleEntity entity = movieSchedules.get(position);

        // Set properties
        holder.theaterName.setText(entity.getTheaterName());
        holder.location.setText(entity.getLocation());
        // Format datetime
        Date date = entity.getShowtime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss aa MMM d, yyyy", Locale.ENGLISH);
        String dateStr = simpleDateFormat.format(date);
        // Set properties
        holder.showtime.setText(dateStr);
        holder.price.setText(String.valueOf(entity.getPrice()));
        Glide.with(context).load(NetworkUtil.GET_THEATER_LOGO_URL + entity.getAuditoriumTheaterId())
                .transition(withCrossFade()).into(holder.logo);

        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieScheduleDetailActivity.class);
            intent.putExtra("movieTitle", movieSchedules.get(position).getMovieTitle());
            intent.putExtra("auditoriumTheaterId", movieSchedules.get(position).getAuditoriumTheaterId());
            intent.putExtra("theaterName", movieSchedules.get(position).getTheaterName());
            intent.putExtra("location", movieSchedules.get(position).getLocation());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieSchedules == null ? 0 : movieSchedules.size();
    }

    public void setMovieSchedules(List<MovieScheduleEntity> movieSchedules) {
        this.movieSchedules = movieSchedules;
    }

    public List<MovieScheduleEntity> getMovieSchedules() {
        return movieSchedules;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        CardView cardView;
        TextView theaterName;
        TextView location;
        TextView showtime;
        TextView price;
        ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnTouchListener(this);
            theaterName = itemView.findViewById(R.id.theaterName);
            location = itemView.findViewById(R.id.location);
            showtime = itemView.findViewById(R.id.showtime);
            price = itemView.findViewById(R.id.price);
            logo = itemView.findViewById(R.id.logo);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator upAnim = ObjectAnimator.ofFloat(v, "translationZ", 8);
                    upAnim.setDuration(50);
                    upAnim.setInterpolator(new DecelerateInterpolator());
                    upAnim.start();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    ObjectAnimator downAnim = ObjectAnimator.ofFloat(v, "translationZ", 0);
                    downAnim.setDuration(50);
                    downAnim.setInterpolator(new AccelerateInterpolator());
                    downAnim.start();
                    break;
            }
            return false;
        }
    }
}
