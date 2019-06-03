package com.core.ui.restaurantListActivity;

import com.core.models.locationModel.LocationModel;
import com.core.models.searchModel.SearchModel;

/**
 * This is the view interface for the MainActivity.
 *
 * @author Akhil Aravind
 */
public interface RestaurantListActivityView {

    /**
     * This method will show progress dialogs.
     */
    void showProgressDialog();

    /**
     * This method will hide progress dialogs.
     */
    void hideProgressDialog();

    /**
     * This method will show toasts.
     * @param message the message to be toasted
     */
    void showMessage(String message);

    /**
     * This method get the Dhwani tracks
     * @param locationModel the track list
     */
    void onLocationDataReceived(LocationModel locationModel);

    /**
     * This method get the Dhyana tracks
     * @param searchModel the track list
     */
    void onSearchDataReceived(SearchModel searchModel);

    void init();
}

