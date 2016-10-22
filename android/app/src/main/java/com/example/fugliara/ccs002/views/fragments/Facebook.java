package com.example.fugliara.ccs002.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class Facebook {
    private static final String TAG = "TAG_Facebook";
    private CallbackManager callbackManager;
    private FacebookResults results;

    public Facebook(LoginButton loginButton, final AppCompatActivity passThis, FacebookResults res) {
        this.results = res;

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) { // descobriri um mode de verificar se esta logado
                    LoginManager.getInstance().logOut();
                    ProviderService.getInstance().getSessionItemView().userItem = null;
                    results.logoutFacebook();
                }
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                checkLoginFacebook();
            }

            @Override
            public void onCancel() {
                Toast.makeText(passThis.getApplicationContext(), "Cancelado ao Logar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(passThis.getApplicationContext(), "Falha ao Logar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkLoginFacebook() {
        if (ProviderService.getInstance().getSessionItemView().userItem == null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code

                            try {
                                // Verificar com o server o login e se necessario registrar o facebookId
                                Log.d(TAG, response.getJSONObject().getString("email"));

                                String token = response.getJSONObject().getString("id");
                                String name = response.getJSONObject().getString("name");
                                String email = response.getJSONObject().getString("email");

                                results.checkFacebookResult(token, name, email);
                            } catch (Exception exception) {
                                results.checkFacebookFail();
                                exception.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    public void resultAction(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
