package com.example.fugliara.ccs002.server.controllers;

import android.util.Log;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.LatLnItem;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

public class RouteController {
    private static final String TAG = "TAG_RouteController";

    private MobileServiceTable<RouteItem> serviceTable = null;

    public RouteController(MobileServiceClient mClient) {
        serviceTable = mClient.getTable(RouteItem.class);
    }

    public void getRoutes(final AzureResponse<RouteItem> result) {
        AzureRequest<RouteItem> request = new AzureRequest<RouteItem>() {
            @Override
            public MobileServiceList<RouteItem> send() {
                MobileServiceList<RouteItem> queryResult = null;
                try {
                    queryResult = serviceTable.parameter("userId", ProviderService.getInstance().getSessionItemView().userItem.id).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getRoutes() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void deleteRoute(final AzureResponse<RouteItem> result, final RouteItem delete) {
        AzureRequest<RouteItem> request = new AzureRequest<RouteItem>() {
            @Override
            public MobileServiceList<RouteItem> send() {
                MobileServiceList<RouteItem> queryResult = null;
                try {
                    serviceTable.delete(delete).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "deleteRoute() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void createRoute(final AzureResponse<RouteItem> result, final String nameFrom, final String nameTo, final LatLng from, final LatLng to) {
        AzureRequest<RouteItem> request = new AzureRequest<RouteItem>() {
            @Override
            public MobileServiceList<RouteItem> send() {
                MobileServiceList<RouteItem> queryResult = null;
                try {
                    RouteItem route = new RouteItem();
                    route.user = ProviderService.getInstance().getSessionItemView().userItem;
                    route.from = new LatLnItem(nameFrom, String.valueOf(from.latitude), String.valueOf(from.longitude));
                    route.to = new LatLnItem(nameTo, String.valueOf(to.latitude), String.valueOf(to.longitude));
                    serviceTable.insert(route).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "createRoute() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
