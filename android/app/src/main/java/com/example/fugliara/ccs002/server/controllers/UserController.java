package com.example.fugliara.ccs002.server.controllers;

import android.util.Base64;
import android.util.Log;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.UserFacebookItem;
import com.example.fugliara.ccs002.server.dataobjects.UserItem;
import com.example.fugliara.ccs002.server.dataobjects.UserRatingItem;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

public class UserController {
    private static final String TAG = "TAG_UserController";

    private MobileServiceTable<UserItem> serviceTableUser = null;
    private MobileServiceTable<UserRatingItem> serviceTableUserRating = null;

    public UserController(MobileServiceClient mClient) {
        serviceTableUser = mClient.getTable(UserItem.class);
        serviceTableUserRating = mClient.getTable(UserRatingItem.class);
    }

    public void checkLogin(final AzureResponse<UserItem> result, final UserItem userItem) {
        AzureRequest<UserItem> request = new AzureRequest<UserItem>() {
            @Override
            public MobileServiceList<UserItem> send() {
                MobileServiceList<UserItem> queryResult = null;
                try {
                    byte[] data;

                    data = userItem.password.getBytes("UTF-8");
                    String passBase64 = Base64.encodeToString(data, Base64.DEFAULT).trim();
                    Log.d(TAG, "checkLogin() email: '" + userItem.email + "', pass64: '" + passBase64 + "'");

                    String email = userItem.email;
                    if (!userItem.facebookItem.email.isEmpty()) {
                        email = userItem.facebookItem.email;
                    }
                    email = email.isEmpty() ? "null" : email;
                    passBase64 = passBase64.isEmpty() ? "null" : passBase64;
                    String token = userItem.facebookItem.token.isEmpty() ? "null" : userItem.facebookItem.token;

                    queryResult = serviceTableUser.parameter("email", email).parameter("pass", passBase64).parameter("facebookToken", token).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "checkLogin() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    // limpar isso muitos parametros
    public void addUser(final AzureResponse<UserItem> result, final String name, final String email, final String pass, final String nameFace, final String emailFace, final String tokenFace) {
        AzureRequest<UserItem> request = new AzureRequest<UserItem>() {
            @Override
            public MobileServiceList<UserItem> send() {
                MobileServiceList<UserItem> queryResult = null;
                try {
                    UserItem user = new UserItem();
                    user.name = name;
                    user.email = email;
                    user.facebookItem = new UserFacebookItem();
                    user.facebookItem.name = nameFace;
                    user.facebookItem.email = emailFace;
                    user.facebookItem.token = tokenFace;
                    // adicionar a data hora no servidor

                    byte[] data;
                    data = pass.trim().getBytes("UTF-8");
                    String passBase64 = Base64.encodeToString(data, Base64.DEFAULT);

                    user.password = passBase64.trim();
                    serviceTableUser.insert(user).get(); // post
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "addUser() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void addUserRating(final AzureResponse<UserRatingItem> result, final UserRatingItem rating) {
        AzureRequest<UserRatingItem> request = new AzureRequest<UserRatingItem>() {
            @Override
            public MobileServiceList<UserRatingItem> send() {
                MobileServiceList<UserRatingItem> queryResult = null;
                try {
                    serviceTableUserRating.insert(rating).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "addUserRating() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void getUserRating(final AzureResponse<UserRatingItem> result) {
        AzureRequest<UserRatingItem> request = new AzureRequest<UserRatingItem>() {
            @Override
            public MobileServiceList<UserRatingItem> send() {
                MobileServiceList<UserRatingItem> queryResult = null;
                try {
                    queryResult = serviceTableUserRating.parameter("userId", ProviderService.getInstance().getSessionItemView().userItem.id).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getUserRating() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
