package com.example.fugliara.ccs002.server.controllers;

import android.util.Log;
import android.util.Pair;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.ImageItem;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fugliara on 11/8/2015.
 */
public class ImageController {
    private static final String TAG = "TAG_ImageController";

    private MobileServiceTable<ImageItem> mImageItemTable = null;

    public ImageController(MobileServiceClient mClient) {
        mImageItemTable = mClient.getTable(ImageItem.class);
    }

    public void getImageUrl(final AzureResponse<ImageItem> result, final String type) {
        AzureRequest<ImageItem> request = new AzureRequest<ImageItem>() {
            @Override
            public MobileServiceList<ImageItem> send() {
                MobileServiceList<ImageItem> queryResult = null;
                try {
                    ImageItem image;
                    List<Pair<String, String>> par = new ArrayList<Pair<String, String>>();
                    par.add(new Pair<String, String>("type", type));
                    image = mImageItemTable.lookUp("0", par).get();
                    queryResult = new MobileServiceList<ImageItem>(new ArrayList<ImageItem>(), 0);
                    queryResult.add(image);
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getImageUrl() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
