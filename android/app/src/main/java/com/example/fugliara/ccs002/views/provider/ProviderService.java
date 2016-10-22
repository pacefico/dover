package com.example.fugliara.ccs002.views.provider;

import com.example.fugliara.ccs002.views.provider.dataviews.SessionItemView;

public class ProviderService {
    private static ProviderService ourInstance = new ProviderService();

    private SessionItemView sessionItemView = null;

    private ProviderService() {
    }

    public static ProviderService getInstance() {
        return ourInstance;
    }

    public SessionItemView getSessionItemView() {
        if (sessionItemView == null) {
            sessionItemView = new SessionItemView();
        }
        return sessionItemView;
    }
}
