package com.core.ui.restaurantListActivity;

import android.util.Log;

import com.core.models.locationModel.LocationModel;
import com.core.models.searchModel.SearchModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the interactor implementation for the mainActivity.
 *
 * @author Akhil Aravind
 */
public class RestaurantListActivityInteractorImpl implements RestaurantListActivityInteractor, NetworkService.NetworkServiceListener {

    private NetworkService networkService;
    private StorageService storageService;
    private FetchLocationDataListener locationDataListener;
    private FetchSearchDataListener searchDataListener;

    /**
     * This method will fetch the Dhwani tracks.
     * @param listener FetchRestaurantDataListener instance
     */
    @Override
    public void fetchLocationData(String query, FetchLocationDataListener listener) {
        this.locationDataListener = listener;
        this.searchDataListener = null;

        if(networkService.haveNetworkAccess())
        {
            networkService.sendLocationDataRequest(query, this);
        }else
        {
            listener.onNetworkError();
        }
    }

    /**
     * This method will fetch the Dhyana tracks.
     * @param listener FetchSearchDataListener instance
     */
    @Override
    public void fetchSearchData(String lat, String lon,FetchSearchDataListener listener) {
        this.searchDataListener = listener;
        this.locationDataListener = null;

        if(networkService.haveNetworkAccess())
        {
            networkService.sendSearchDataRequest(lat, lon, this);
        }else
        {
            listener.onNetworkError();
        }
    }

    /**
     * This method will initialise the network and storage services
     * @param networkService the Network service
     * @param storageService the Storage service
     */
    @Override
    public void inject(NetworkService networkService, StorageService storageService) {
        this.networkService = networkService;
        this.storageService = storageService;
    }

    /**
     * This method will be called when the request is failed.
     * @param response The failure response
     */
    @Override
    public void onFailure(String response) {

        if (locationDataListener != null) {
            locationDataListener.onLocationDataFailure(response);
        } else if(searchDataListener != null) {
            searchDataListener.onSearchDataFailure(response);
        }
    }

    /**
     * This method will be called when the request is success.
     * @param response The success response
     * @param cancelFlag The flag to recognise whether user has intentionally cancelled or not
     */
    @Override
    public void onSuccess(String response, Boolean cancelFlag) {
//        Log.e(this.getClass().getSimpleName(),response);
        Log.e("onSuccess", "1111111111111");

        if (locationDataListener != null) {
            try {

                Gson gson = new GsonBuilder().create();
                LocationModel locationModel = gson.fromJson(response, LocationModel.class);

                locationDataListener.onLocationDataReceived(locationModel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (searchDataListener != null) {
            try {

                Gson gson = new GsonBuilder().create();
                SearchModel searchModel = gson.fromJson(response, SearchModel.class);

                searchDataListener.onSearchDataReceived(searchModel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
