package com.example.fugliara.ccs002.server.controllers;

import android.util.Log;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.ItineraryItem;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

public class ItineraryController {
    private static final String TAG = "TAG_ItineraryController";

    private MobileServiceTable<ItineraryItem> serviceTable = null;

    public ItineraryController(MobileServiceClient mClient) {
        serviceTable = mClient.getTable(ItineraryItem.class);
    }

    public void getItinerary(final AzureResponse<ItineraryItem> result, final RouteItem routeItem) {
        AzureRequest<ItineraryItem> request = new AzureRequest<ItineraryItem>() {
            @Override
            public MobileServiceList<ItineraryItem> send() {
                MobileServiceList<ItineraryItem> queryResult = null;
                try {
                    queryResult = serviceTable.parameter("routeId", routeItem.id).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getItinerary() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void deleteItinerary(final AzureResponse<ItineraryItem> result, final ItineraryItem itinerary) {
        AzureRequest<ItineraryItem> request = new AzureRequest<ItineraryItem>() {
            @Override
            public MobileServiceList<ItineraryItem> send() {
                MobileServiceList<ItineraryItem> queryResult = null;
                try {
                    serviceTable.delete(itinerary).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "deleteItinerary() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void createItinerary(final AzureResponse<ItineraryItem> result, final RouteItem routeItem, final String date, final String time) {
        AzureRequest<ItineraryItem> request = new AzureRequest<ItineraryItem>() {
            @Override
            public MobileServiceList<ItineraryItem> send() {
                MobileServiceList<ItineraryItem> queryResult = null;
                try {
                    ItineraryItem itineraryItem = new ItineraryItem();
                    itineraryItem.date = date;
                    itineraryItem.time = time;
                    itineraryItem.route = routeItem;
                    serviceTable.insert(itineraryItem).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "createItinerary() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
