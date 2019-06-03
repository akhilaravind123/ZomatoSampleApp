package com.core.ui.restaurantDetailsActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.core.models.restaurantModel.RestaurantModel;
import com.services.NetworkService;
import com.services.StorageService;
import com.zomatoapi.R;

public class RestaurantDetailsActivity extends AppCompatActivity implements View.OnClickListener, RestaurantDetailsActivityView {


    private RestaurantDetailsActivityPresenter presenter;
    private String resId = "";
    ImageView ivImage;
    ImageButton ivBack;
    TextView tvRestaurantName, tvRating, tvLocation, tvDishes, tvVotes, tvCost, tvReviews;
    private String deepLink = "";
    Button btnDeepLink;
    private ConstraintLayout clParent;
    private ShimmerRecyclerView rvShimmer;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_details);

        presenter = new RestaurantDetailsActivityPresenterImpl(this);
        NetworkService networkService = new NetworkService();
        networkService.inject(RestaurantDetailsActivity.this);
        presenter.inject(networkService, new StorageService(RestaurantDetailsActivity.this));

        presenter.init();

        presenter.fetchRestaurantData(resId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_ard:
                finish();
                break;
            case R.id.btn_deeplink_ard:

                Uri myAction = Uri.parse(deepLink);

                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.application.zomato");

                if (intent != null) {
                    Intent deepIntent = new Intent(Intent.ACTION_VIEW, myAction);
                    startActivity(deepIntent);
                } else {
                    intent= new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.application.zomato"));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void init() {
        clParent = findViewById(R.id.cl_parent_ard);
        rvShimmer = findViewById(R.id.shimmer_recycler_view);
        ivBack = findViewById(R.id.iv_back_ard);
        ivImage = findViewById(R.id.iv_resturant_image_ard);
        btnDeepLink = findViewById(R.id.btn_deeplink_ard);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name_ard);
        tvRating = findViewById(R.id.tv_rating_ard);
        tvLocation = findViewById(R.id.tv_location_ard);
        tvDishes = findViewById(R.id.tv_dishes_name_ard);
        tvCost = findViewById(R.id.tv_cost_ard);
        tvVotes = findViewById(R.id.tv_votes_ard);
        tvReviews = findViewById(R.id.tv_reviews_ard);

        resId = getIntent().getExtras().getString("res_id");

        ivBack.setOnClickListener(this);
        btnDeepLink.setOnClickListener(this);
    }

    @Override
    public void showProgressDialog() {

        rvShimmer.showShimmerAdapter();
        btnDeepLink.setVisibility(View.GONE);
        clParent.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressDialog() {
        rvShimmer.hideShimmerAdapter();
        btnDeepLink.setVisibility(View.VISIBLE);
        clParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(RestaurantDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRestaurantDataReceived(RestaurantModel restaurantModel) {
        Log.e("onRestaurantData", restaurantModel.getPhotosUrl());

        deepLink = restaurantModel.getDeeplink();

        tvRestaurantName.setText(restaurantModel.getName());
        tvLocation.setText(restaurantModel.getLocation().getLocalityVerbose());
        tvDishes.setText(restaurantModel.getCuisines());
        tvCost.setText(String.valueOf(restaurantModel.getAverageCostForTwo()/2));
        tvVotes.setText(String.valueOf(restaurantModel.getUserRating().getVotes()));
        tvRating.setText(String.valueOf(restaurantModel.getUserRating().getAggregateRating()));
        tvReviews.setText(String.valueOf("'" + restaurantModel.getUserRating().getRatingText()) + "'");
        tvRating.getBackground().setColorFilter(Color.parseColor("#" + restaurantModel.getUserRating().getRatingColor()), PorterDuff.Mode.SRC_ATOP);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(restaurantModel.getThumb())
                .apply(options)
                .into(ivImage);

    }

}