package com.core.ui.restaurantDetailsActivity;

import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the presenter interface for the mainActivity.
 *
 * @author Akhil Aravind
 */
public interface RestaurantDetailsActivityPresenter {

    /**
     * This method will initialise the network and storage services
     * @param networkService the Network service
     * @param storageService the Storage service
     */
    void inject(NetworkService networkService, StorageService storageService);

    /**
     * This method will show progress dialogs.
     */
    void showProgressDialog();

    /**
     * This method will hide progress dialogs.
     */
    void hideProgressDialog();

    /**
     * This method will fetch the Dhwani tracks from server
     */
    void fetchRestaurantData(String resId);

    void init();
}
