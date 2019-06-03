package com.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.zomatoapi.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.core.utilities.AppConstants.BASE_URL;
import static com.core.utilities.AppConstants.LOCATION;
import static com.core.utilities.AppConstants.RESTAURANT;
import static com.core.utilities.AppConstants.SEARCH;

/**
 * This NetworkService class holds the Base url and other urls used in the application.
 * All the requests to servers will be made from the NetworkService class
 * We are using Volley library to make API calls.
 *
 * @author Akhil Aravind
 */
public class NetworkService {

    OkHttpClient client = new OkHttpClient();
    Context context = null;
    Handler mainHandler = null;
    private NetworkServiceListener listener;
    Boolean cancelFlag =  false;
    private static int progressValue;
    private String LOG_TAG = "Network_Service";

    public void inject(Context context) {
        this.context = context;
        mainHandler = new Handler(context.getMainLooper());
    }

    public void sendLocationDataRequest(String query, final NetworkServiceListener listener) {
        String URL = BASE_URL + LOCATION + "query=" + query + "&count=10";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .get()
                .addHeader("user_key", context.getResources().getString(R.string.api_key))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure("FAIL");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String epResponse = response.body().string();
                Log.e(LOG_TAG, epResponse);
                Log.i(LOG_TAG, response.toString());

                if (response.isSuccessful()) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onSuccess(epResponse, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(epResponse);
                        }
                    });
                }
            }
        });
    }

    public void sendSearchDataRequest(String lat, String lon, final NetworkServiceListener listener) {
        String URL;

        if (lat.length() == 0 && lon.length() == 0) {
            URL = BASE_URL + SEARCH;
        } else {
            URL = BASE_URL + SEARCH + "lat=" + lat + "&lon=" + lon + "&sort=real_distance";
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .get()
                .addHeader("user_key", context.getResources().getString(R.string.api_key))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure("FAIL");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String epResponse = response.body().string();
                Log.e(LOG_TAG, epResponse);
                Log.i(LOG_TAG, response.toString());

                if (response.isSuccessful()) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onSuccess(epResponse, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(epResponse);
                        }
                    });
                }
            }

        });
    }

    public void sendRestaurantDataRequest(String resId, final NetworkServiceListener listener) {
        String URL = BASE_URL + RESTAURANT + "res_id=" + resId;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .get()
                .addHeader("user_key", context.getResources().getString(R.string.api_key))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure("FAIL");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String epResponse = response.body().string();
                Log.e(LOG_TAG, epResponse);
                Log.i(LOG_TAG, response.toString());

                if (response.isSuccessful()) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onSuccess(epResponse, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(epResponse);
                        }
                    });
                }
            }

        });
    }


    /**
     * Interface to handle success and failure response
     */
    public interface NetworkServiceListener {
        void onFailure(String response);

        void onSuccess(String response, Boolean cancelFlag);
    }

    /**
     * This method will check the network connection
     *
     * @return true or false
     */
    public boolean haveNetworkAccess() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo connection = manager.getActiveNetworkInfo();
            if (connection != null && connection.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}