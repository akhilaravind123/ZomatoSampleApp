package com.core.ui.restaurantListActivity;

/**
 * This adapter is used to display the list of Dhwani tracks.
 *
 * @author Akhil Aravind
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.core.models.searchModel.Restaurant;
import com.zomatoapi.R;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.NowPlayingAdapterViewHolder> {

    /**
     * Initialising the local variables
     */
    private List<Restaurant> restaurantsList;
    Context context;
    RecyclerViewClickInterface listener;

    /**
     * This is an constructor used to initialise the arraylist and the context
     *  @param context      This param is used to specify the context.
     * @param restaurantsList      This is an array list holds the list of tracks url
     * @param listener recyclerView click listener
     */
    public RestaurantListAdapter(Context context, List<Restaurant> restaurantsList, RecyclerViewClickInterface listener) {
        this.restaurantsList = restaurantsList;
        this.context = context;
        this.listener = listener;

    }
    @Override
    public NowPlayingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View guideListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resturant_list, parent, false);
        NowPlayingAdapterViewHolder holder = new NowPlayingAdapterViewHolder(guideListView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NowPlayingAdapterViewHolder holder, final int position) {

        holder.ivRestaurant.setClipToOutline(true);

        holder.tvName.setText(restaurantsList.get(position).getRestaurant().getName());

        holder.tvLocation.setText(restaurantsList.get(position).getRestaurant().getLocation().getLocalityVerbose());
        holder.tvDishes.setText(restaurantsList.get(position).getRestaurant().getCuisines());
        holder.tvCost.setText(String.valueOf(restaurantsList.get(position).getRestaurant().getAverageCostForTwo()/2) + " per person");
        holder.tvRating.setText(String.valueOf(restaurantsList.get(position).getRestaurant().getUserRating().getAggregateRating()));
        holder.tvRating.getBackground().setColorFilter(Color.parseColor("#" +
                restaurantsList.get(position).getRestaurant().getUserRating().getRatingColor()), PorterDuff.Mode.SRC_ATOP);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context)
                .load(restaurantsList.get(position).getRestaurant().getThumb())
                .apply(options)
                .into(holder.ivRestaurant);

        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.recyclerviewOnClick(position, restaurantsList.get(position).getRestaurant().getId());
            }
        });

    }

    /**
     * This method is used to show the count of the views.
     *
     * @return the size of the list.
     */
    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    /**
     * ViewHolder class is used to initialise the custom adapter objects.
     */
    public class NowPlayingAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRestaurant;
        TextView tvRating, tvCost, tvDishes, tvLocation, tvName;
        ConstraintLayout clParent;

        public NowPlayingAdapterViewHolder(View view) {
            super(view);
            clParent = view.findViewById(R.id.cl_parent_lcl);
            ivRestaurant = view.findViewById(R.id.iv_resturant_lcl);
            tvName = view.findViewById(R.id.tv_resturant_name_lcl);
            tvRating = view.findViewById(R.id.tv_rating_lcl);
            tvLocation = view.findViewById(R.id.tv_location_lcl);
            tvDishes = view.findViewById(R.id.tv_dishes_lcl);
            tvCost = view.findViewById(R.id.tv_cost_lcl);
        }
    }
}
