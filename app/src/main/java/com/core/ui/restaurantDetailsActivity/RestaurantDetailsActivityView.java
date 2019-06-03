package com.core.ui.restaurantDetailsActivity;

import com.core.models.restaurantModel.RestaurantModel;

/**
 * This is the view interface for the MainActivity.
 *
 * @author Akhil Aravind
 */
public interface RestaurantDetailsActivityView {

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
     * @param restaurantModel the track list
     */
    void onRestaurantDataReceived(RestaurantModel restaurantModel);

    void init();
}

