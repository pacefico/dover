package com.example.fugliara.ccs002.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.fugliara.ccs002.server.controllers.ImageController;
import com.example.fugliara.ccs002.server.controllers.InviteController;
import com.example.fugliara.ccs002.server.controllers.ItineraryController;
import com.example.fugliara.ccs002.server.controllers.OrderController;
import com.example.fugliara.ccs002.server.controllers.ProductController;
import com.example.fugliara.ccs002.server.controllers.RouteController;
import com.example.fugliara.ccs002.server.controllers.UserController;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

public class AzureService {

    private static final String TAG = "TAG_AzureService";
    private static AzureService ourInstance = new AzureService();
    private MobileServiceClient mClient;
    private UserController userController = null;
    private InviteController inviteController = null;
    private RouteController routeController = null;
    private ItineraryController itineraryController = null;
    private OrderController orderController = null;
    private ImageController imageController = null;
    private ProductController productController = null;
    private Context context;

    private AzureService() {
        context = null;
    }

    public static AzureService getInstance() {
        return ourInstance;
    }

    public void create(Context context) {
        Log.d("AzureService.java", "onCreate()");
        if (this.context == null) {
            this.context = context;
        }
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://transporter.azure-mobile.net/",
                    "YEDiKxLFOYnuqBpnnNaLRbDMgCSSIf73",
                    this.context);
//					this).withFilter(new ProgressFilter());
            //Get the Mobile Service Table instance to use

        } catch (MalformedURLException e) {
            Log.d(TAG, "Error: " + e.getMessage());
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
    }

    public UserController getUserController() {
        if (userController == null) {
            userController = new UserController(mClient);
        }
        return userController;
    }

    public InviteController getInviteController() {
        if (inviteController == null) {
            inviteController = new InviteController(mClient);
        }
        return inviteController;
    }

    public RouteController getRouteController() {
        if (routeController == null) {
            routeController = new RouteController(mClient);
        }
        return routeController;
    }

    public ItineraryController getItineraryController() {
        if (itineraryController == null) {
            itineraryController = new ItineraryController(mClient);
        }
        return itineraryController;
    }

    public OrderController getOrderController() {
        if (orderController == null) {
            orderController = new OrderController(mClient);
        }
        return orderController;
    }

    public ImageController getImageController() {
        if (imageController == null) {
            imageController = new ImageController(mClient);
        }
        return imageController;
    }

    public ProductController getProductController() {
        if (productController == null) {
            productController = new ProductController(mClient);
        }
        return productController;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
