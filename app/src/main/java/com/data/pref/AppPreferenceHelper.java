package com.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This AppPreferencesHelper class is used to save the user details locally.
 * Once user uninstalls the APP or User Log's out the preference will be cleared.
 *
 * @author Akhil Aravind
 */
public class AppPreferenceHelper implements PreferenceHelper {

    /**
     * Initialising the local variables
     */
    private String APP_AUDIO_TRACKS = "audio_tracks";
    private String APP_DHYANA_TRACKS = "dhyana_tracks";
    private String APP_PRANA_TRACKS = "prana_tracks";

    Context context;
    SharedPreferences preferences;

    /**
     * This is an constructor used to initialise the context of the class.
     * @param context context of the class
     * @param prefFileName preference file name
     */
    public AppPreferenceHelper(Context context, String prefFileName) {
        this.context = context;
        preferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    /**
     * This method stores the Dhwani tracks
     * @param name url of the track
     */
    @Override
    public void storeAudioTracks(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_AUDIO_TRACKS, name);
        editor.apply();
    }

    /**
     * This method fetches the Dhwani tracks
     */
    @Override
    public String getAudioTracks() {
        return preferences.getString(APP_AUDIO_TRACKS, "");
    }

}