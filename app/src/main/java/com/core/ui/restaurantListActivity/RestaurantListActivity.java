package com.core.ui.restaurantListActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.core.models.locationModel.LocationModel;
import com.core.models.searchModel.Restaurant;
import com.core.models.searchModel.SearchModel;
import com.core.ui.restaurantDetailsActivity.RestaurantDetailsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.services.NetworkService;
import com.services.StorageService;
import com.zomatoapi.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener, RestaurantListActivityView, RecyclerViewClickInterface {

    protected static final String TAG = "location-updates-sample";

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

    Context context;
    private RestaurantListActivityPresenter presenter;

    AutoCompleteTextView atvSearch;
    RecyclerView recyclerView;
    ImageView ivClear;
    private ArrayAdapter<String> autoAdapter;
    private List<String> placeList = new ArrayList<>();
    private String[] places = new String[placeList.size()];
    private RecyclerViewClickInterface listner;
    private ShimmerRecyclerView rvShimmer;
    TextView tvNoRestaurants;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_list);

        presenter = new RestaurantListActivityPresenterImpl(this);
        NetworkService networkService = new NetworkService();
        networkService.inject(RestaurantListActivity.this);
        presenter.inject(networkService, new StorageService(RestaurantListActivity.this));

        presenter.init();
        buildGoogleApiClient();

    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkPermissions();
    }

    public void checkPermissions() {

        if (!isPlayServicesAvailable(this)) return;

        if (Build.VERSION.SDK_INT < 23) {
            startLocationUpdates();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @SuppressLint("NewApi")
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ContextCompat.checkSelfPermission(RestaurantListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    mGoogleApiClient, mLocationRequest, RestaurantListActivity.this);

                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(RestaurantListActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    presenter.fetchSearchData("", "");
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        presenter.fetchSearchData("", "");
                        break;
                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
//            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
//        stopLocationUpdates();
//        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "onLocationChanged");
        mCurrentLocation = location;
        stopLocationUpdates();
        presenter.fetchSearchData(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_arl:
                atvSearch.setText("");
                break;
        }
    }

    @Override
    public void init() {
        context = this;
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        listner = this;

        recyclerView = findViewById(R.id.rv_restaurants_arl);
        tvNoRestaurants = findViewById(R.id.tv_no_restaurants_arl);
        rvShimmer = findViewById(R.id.shimmer_recycler_view);
        atvSearch = findViewById(R.id.atv_search_arl);
        ivClear = findViewById(R.id.iv_clear_arl);

        autoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, places);
        atvSearch.setAdapter(autoAdapter);

        ivClear.setOnClickListener(this);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, 0));

        atvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 0) {
                    ivClear.setVisibility(View.GONE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }

                if (s.toString().length()>2) {
                    presenter.fetchLocationData(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        atvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hideKeyboardActivity(RestaurantListActivity.this);
                recyclerView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void showProgressDialog() {
        rvShimmer.showShimmerAdapter();
    }

    @Override
    public void hideProgressDialog() {
        rvShimmer.hideShimmerAdapter();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(RestaurantListActivity.this, message, Toast.LENGTH_SHORT).show();
        tvNoRestaurants.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationDataReceived(final LocationModel locationModel) {

        placeList.clear();

        for (int i = 0; i < locationModel.getLocationSuggestions().size(); i++) {
            placeList.add(locationModel.getLocationSuggestions().get(i).getTitle());

            Log.e("Location", placeList.get(i));
            if (atvSearch.getText().toString().equalsIgnoreCase(locationModel.getLocationSuggestions().get(i).getTitle())) {
                presenter.fetchSearchData(String.valueOf(locationModel.getLocationSuggestions().get(i).getLatitude()),
                        String.valueOf(locationModel.getLocationSuggestions().get(i).getLongitude()));
            }
        }

        autoAdapter.clear();
        autoAdapter.addAll(placeList);
        atvSearch.setAdapter(autoAdapter);
    }

    @Override
    public void onSearchDataReceived(SearchModel searchModel) {

        tvNoRestaurants.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        List<Restaurant> restaurantList = new ArrayList<>();
        for (int i = 0; i<searchModel.getRestaurants().size(); i++) {
            restaurantList.add(searchModel.getRestaurants().get(i));
        }
        restaurantList.addAll(searchModel.getRestaurants());

        recyclerView.setAdapter(new RestaurantListAdapter(this, restaurantList, listner));
    }

    public static void hideKeyboardActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isPlayServicesAvailable(Context context) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    public void recyclerviewOnClick(int position, String resId) {

        Intent intent = new Intent(this, RestaurantDetailsActivity.class);
        intent.putExtra("res_id", resId);
        startActivity(intent);
    }
}