package com.example.fugliara.ccs002.views.fragments;

/**
 * Created by fugliara on 11/7/2015.
 */
public interface FacebookResults {
    void checkFacebookResult(String token, String name, String email);

    void checkFacebookFail();

    void logoutFacebook();
}
