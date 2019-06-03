package com.core.ui.restaurantDetailsActivity;

import com.core.models.restaurantModel.RestaurantModel;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the presenter implementation for the mainActivity.
 *
 * @author Akhil Aravind
 */
public class RestaurantDetailsActivityPresenterImpl implements RestaurantDetailsActivityPresenter,
        RestaurantDetailsActivityInteractor.FetchRestaurantDataListener {
    private RestaurantDetailsActivityView view;
    private RestaurantDetailsActivityInteractor interactor;
    private int position = 0;

    RestaurantDetailsActivityPresenterImpl(RestaurantDetailsActivityView view) {
        this.view = view;
        interactor = new RestaurantDetailsActivityInteractorImpl();
    }

    /**
     * This method will fetch the Dhwani tracks from server
     */
    @Override
    public void init() {
        if (view != null) {
            view.init();
        }
    }

    /**
     * This method will fetch the Dhwani tracks from server
     */
    @Override
    public void fetchRestaurantData(String resId) {
        if (view != null) {
            if (interactor != null) {
                view.showProgressDialog();
                interactor.fetchRestaurantData(resId, this);
            }
        }
    }

    /**
     * This method will show progress dialogs.
     */
    @Override
    public void showProgressDialog() {
        if (view != null) {
            view.showProgressDialog();
        }
    }

    /**
     * This method will hide progress dialogs.
     */
    @Override
    public void hideProgressDialog() {
        if (view != null) {
            view.hideProgressDialog();
        }
    }

    /**
     * This method will initialise the network and storage services
     * @param networkService the Network service
     * @param storageService the Storage service
     */
    @Override
    public void inject(NetworkService networkService, StorageService storageService) {
        interactor.inject(networkService, storageService);
    }

    @Override
    public void onRestaurantDataReceived(RestaurantModel restaurantModel) {
        if (view != null) {
            view.hideProgressDialog();
            view.onRestaurantDataReceived(restaurantModel);
        }
    }

    /**
     * This method will give failure message
     * @param message the reason for the failure
     */
    @Override
    public void onRestaurantDataFailure(String message) {
        if (view != null) {
            view.hideProgressDialog();
            view.showMessage("Please check your network connection");
        }
    }

     /**
     * This method will be called if there is a network error.
     */
    @Override
    public void onNetworkError() {
        if (view != null) {
            view.hideProgressDialog();
            view.showMessage("Please check your network connection");
        }
    }
}
