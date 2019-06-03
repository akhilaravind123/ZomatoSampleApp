package com.core.ui.restaurantListActivity;

import com.core.models.locationModel.LocationModel;
import com.core.models.searchModel.SearchModel;
import com.services.NetworkService;
import com.services.StorageService;

/**
 * This is the presenter implementation for the mainActivity.
 *
 * @author Akhil Aravind
 */
public class RestaurantListActivityPresenterImpl implements RestaurantListActivityPresenter,
        RestaurantListActivityInteractor.FetchLocationDataListener, RestaurantListActivityInteractor.FetchSearchDataListener {
    private RestaurantListActivityView view;
    private RestaurantListActivityInteractor interactor;
    private int position = 0;

    RestaurantListActivityPresenterImpl(RestaurantListActivityView view) {
        this.view = view;
        interactor = new RestaurantListActivityInteractorImpl();
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
    public void fetchLocationData(String query) {
        if (view != null) {
            if (interactor != null) {
                interactor.fetchLocationData(query,this);
            }
        }
    }

    /**
     * This method will fetch the Dhyana tracks from server
     */
    @Override
    public void fetchSearchData(String lat, String lon) {
        if (view != null) {
            if (interactor != null) {
                view.showProgressDialog();
                interactor.fetchSearchData(lat, lon, this);
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

    /**
     * This method will give the tracks
     * @param locationModel the arraylist of TrackListModel
     */
    @Override
    public void onLocationDataReceived(LocationModel locationModel) {
        if (view != null) {
            view.onLocationDataReceived(locationModel);
        }
    }

    /**
     * This method will give failure message
     * @param message the reason for the failure
     */
    @Override
    public void onLocationDataFailure(String message) {
        if (view != null) {
            view.showMessage("Please check your network connection");
        }
    }

    /**
     * This method will give the tracks
     * @param searchModel the arraylist of TrackListModel
     */
    @Override
    public void onSearchDataReceived(SearchModel searchModel) {
        if (view != null) {

            view.hideProgressDialog();
            view.onSearchDataReceived(searchModel);
        }
    }

    /**
     * This method will give failure message
     * @param message the reason for the failure
     */
    @Override
    public void onSearchDataFailure(String message) {
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
