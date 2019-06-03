package com.services;

import android.content.Context;

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

}