package com.example.fugliara.ccs002.server;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public abstract class AzureRequest<T> extends AsyncTask<AzureResponse<T>, Void, Void> {
    private static final String TAG = "TAG_AzureRequest";
    MobileServiceList<T> queryResult = null;
    AzureResponse<T> response = null;

    @Override
    protected Void doInBackground(AzureResponse<T>[] params) {
        response = params[0];
        try {
            queryResult = send();
        } catch (Exception exception) {
            Log.d(TAG, "AzureRequest.doInBackground() Error: " + exception.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (response.exception == false) {
            if (response != null) {
                if (queryResult != null) {
                    response.receive(queryResult);
                } else {
                    if (response.success) {
                        response.receive(null);
                    } else {
                        response.fail();
                    }
                }
            }
        } else {
            Log.d(TAG, "AzureException re open azure");
            AzureService.getInstance().create(null);
            response.fail();
        }
    }


    public abstract MobileServiceList<T> send();
}
