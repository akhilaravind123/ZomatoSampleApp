package com.core.ui.restaurantDetailsActivity;

import com.core.models.restaurantModel.RestaurantModel;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the interactor interface for the mainActivity.
 *
 * @author Akhil Aravind
 */
public interface RestaurantDetailsActivityInteractor
{
    /**
     * This method will initialise the network and storage services
     * @param networkService the Network service
     * @param storageService the Storage service
     */
    void inject(NetworkService networkService, StorageService storageService);

    /**
     * This method will fetch the Dhwani tracks.
     * @param resId
     * @param listener FetchRestaurantDataListener instance
     */
    void fetchRestaurantData(String resId, FetchRestaurantDataListener listener);

    /**
     * Interface to handle the fetch Dhwani track data
     */
    interface FetchRestaurantDataListener
    {
        /**
         * This method will give the tracks
         * @param restaurantModel the arraylist of TrackListModel
         */
        void onRestaurantDataReceived(RestaurantModel restaurantModel);

        /**
         * This method will give failure message
         * @param message the reason for the failure
         */
        void onRestaurantDataFailure(String message);

        /**
         * This method will be called if there is a network error.
         */
        void onNetworkError();
    }
}
