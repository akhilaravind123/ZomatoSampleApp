package com.data.pref;

public interface PreferenceHelper {

    /**
     * This method stores the Dhwani tracks
     * @param name  the tracks to be saved in json format
     */
    void storeAudioTracks(String name);

    /**
     * This method fetches the Dhwani tracks
     */
    String getAudioTracks();
}