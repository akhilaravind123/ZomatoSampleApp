package com.core.ui.restaurantListActivity;

import com.core.models.locationModel.LocationModel;
import com.core.models.searchModel.SearchModel;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the interactor interface for the mainActivity.
 *
 * @author Akhil Aravind
 */
public interface RestaurantListActivityInteractor
{
    /**
     * This method will initialise the network and storage services
     * @param networkService the Network service
     * @param storageService the Storage service
     */
    void inject(NetworkService networkService, StorageService storageService);

    /**
     * This method will fetch the location data.
     * @param listener FetchLocationDataListener instance
     */
    void fetchLocationData(String query, FetchLocationDataListener listener);

    /**
     * This method will fetch the Dhyana tracks.
     * @param listener FetchSearchDataListener instance
     */
    void fetchSearchData(String lat, String lon, FetchSearchDataListener listener);

    /**
     * Interface to handle the fetch Dhwani track data
     */
    interface FetchLocationDataListener
    {
        /**
         * This method will give the tracks
         * @param locationModel the arraylist of TrackListModel
         */
        void onLocationDataReceived(LocationModel locationModel);

        /**
         * This method will give failure message
         * @param message the reason for the failure
         */
        void onLocationDataFailure(String message);

        /**
         * This method will be called if there is a network error.
         */
        void onNetworkError();
    }

    /**
     * Interface to handle the fetch Dhyana track data
     */
    interface FetchSearchDataListener
    {
        /**
         * This method will give the tracks
         * @param searchModel the arraylist of TrackListModel
         */
        void onSearchDataReceived(SearchModel searchModel);

        /**
         * This method will give failure message
         * @param message the reason for the failure
         */
        void onSearchDataFailure(String message);

        /**
         * This method will be called if there is a network error.
         */
        void onNetworkError();
    }
}
