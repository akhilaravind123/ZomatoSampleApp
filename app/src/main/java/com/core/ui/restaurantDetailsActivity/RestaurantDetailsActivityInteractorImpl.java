package com.core.ui.restaurantDetailsActivity;

import android.util.Log;

import com.core.models.restaurantModel.RestaurantModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the interactor implementation for the mainActivity.
 *
 * @author Akhil Aravind
 */
public class RestaurantDetailsActivityInteractorImpl implements RestaurantDetailsActivityInteractor, NetworkService.NetworkServiceListener {

    private NetworkService networkService;
    private StorageService storageService;
    private FetchRestaurantDataListener restaurantDataListener;

    /**
     * This method will fetch the Dhwani tracks.
     * @param resId
     * @param listener FetchRestaurantDataListener instance
     */
    @Override
    public void fetchRestaurantData(String resId, FetchRestaurantDataListener listener) {
        this.restaurantDataListener = listener;

        if(networkService.haveNetworkAccess())
        {
            networkService.sendRestaurantDataRequest(resId,this);
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

        if (restaurantDataListener != null) {
            restaurantDataListener.onRestaurantDataFailure(response);
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

        if (restaurantDataListener != null) {
            try {

                Gson gson = new GsonBuilder().create();
                RestaurantModel restaurantModel = gson.fromJson(response, RestaurantModel.class);

                restaurantDataListener.onRestaurantDataReceived(restaurantModel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
