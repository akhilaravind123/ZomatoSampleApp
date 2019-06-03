package com.services;

import android.content.Context;

import com.data.pref.AppPreferenceHelper;
import com.data.pref.PreferenceHelper;

/**
 * This StorageService class is used to Store the user details and use the details inside the application.
 *
 * @author Akhil Aravind
 */
public class StorageService {

    /**
     * Initialising the local variables
     */
    private Context context;

    /**
     * This is an constructor used to initialise the context of the class.
     * @param context context of the class
     */
    public StorageService(Context context) {
        this.context = context;
    }

    /**
     * This method stores the Dhwani tracks
     * @param name url of the track
     */
    public void storeAudioTracks(String name) {
        PreferenceHelper preferences = new AppPreferenceHelper(context, "MY_SP_FILE");
        preferences.storeAudioTracks(name);
    }

    /**
     * This method fetches the Dhwani tracks
     */
    public String getAudioTracks() {
        PreferenceHelper preferencesHelper = new AppPreferenceHelper(context, "MY_SP_FILE");
        return preferencesHelper.getAudioTracks();
    }
}