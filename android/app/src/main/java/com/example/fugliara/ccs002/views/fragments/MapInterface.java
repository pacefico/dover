package com.example.fugliara.ccs002.views.fragments;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by fugliara on 9/22/2015.
 */
public interface MapInterface {
    void handleGetDirectionsResult(ArrayList<LatLng> directionPoints);

    Context getActivity();
}
