package com.example.fugliara.ccs002.views.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.InviteItem;
import com.example.fugliara.ccs002.server.dataobjects.UserItem;
import com.example.fugliara.ccs002.views.fragments.Facebook;
import com.example.fugliara.ccs002.views.fragments.FacebookResults;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.facebook.login.widget.LoginButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class RegisterActivity extends AppCompatActivity {

    private Facebook facebook;

    private String tokenClass;
    private String nameClass;
    private String emailClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RegisterActivity passThis = this;

        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText editTextName = (EditText) findViewById(R.id.editTextName);
                EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

                String name = editTextName.getText().toString();
                String pass = editTextPassword.getText().toString();
                final String email = getIntent().getStringExtra("email");

                if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                    final ProgressWait progress;
                    progress = new ProgressWait(passThis, "Registrando");
                    progress.show();

                    AzureService.getInstance().getUserController().addUser(new AzureResponse<UserItem>() {
                        @Override
                        public void receive(MobileServiceList<UserItem> result) {
                            AzureService.getInstance().getInviteController().deleteInvite(new AzureResponse<InviteItem>() {
                                @Override
                                public void receive(MobileServiceList<InviteItem> result) {
                                    Intent intent = new Intent(passThis, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Registrado", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void fail() {
                                    Toast.makeText(getApplicationContext(), "Falha ao deletar invite", Toast.LENGTH_SHORT).show();
                                }
                            }, ProviderService.getInstance().getSessionItemView().inviteItem);
                            progress.dismiss();
                        }

                        @Override
                        public void fail() {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Falha ao registrar", Toast.LENGTH_SHORT).show();
                        }
                    }, name, email, pass, nameClass, emailClass, tokenClass);
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        facebook = new Facebook(loginButton, this, new FacebookResults() {
            @Override
            public void checkFacebookResult(String token, String name, String email) {
                tokenClass = token;
                nameClass = name;
                emailClass = email;
            }

            @Override
            public void checkFacebookFail() {

            }

            @Override
            public void logoutFacebook() {

            }
        });
        facebook.checkLoginFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.resultAction(requestCode, resultCode, data);
    }
}
