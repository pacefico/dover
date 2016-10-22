package com.example.fugliara.ccs002.server;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;

abstract public class AzureResponse<T> {
    private static final String TAG = "TAG_AzureResponse";
    public boolean exception = false;
    public boolean success = false;

    public abstract void receive(MobileServiceList<T> result);

    public abstract void fail();
}
