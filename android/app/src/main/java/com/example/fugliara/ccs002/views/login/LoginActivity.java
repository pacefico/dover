package com.example.fugliara.ccs002.views.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.UserFacebookItem;
import com.example.fugliara.ccs002.server.dataobjects.UserItem;
import com.example.fugliara.ccs002.views.fragments.Facebook;
import com.example.fugliara.ccs002.views.fragments.FacebookResults;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.home.HomeActivity;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.facebook.login.widget.LoginButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TAG_LoginActivity";

    private LoginActivity passThis = this;
    private boolean loginFacebook = false;

    private Facebook facebook;

    private UserFacebookItem userFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LoginActivity", "onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView textViewNotRegister = (TextView) findViewById(R.id.textViewNotRegister);
        textViewNotRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CheckInviteActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
                EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

                String email = editTextEmail.getText().toString();
                String pass = editTextPassword.getText().toString();

                if (loginFacebook) {
                    callLoginTask("", "");
                } else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                            callLoginTask(email, pass);
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        userFacebook = new UserFacebookItem();
        facebook = new Facebook(loginButton, this, new FacebookResults() {
            @Override
            public void checkFacebookResult(String token, String name, String email) {
                userFacebook.email = email;
                userFacebook.name = name;
                userFacebook.token = token;

                editTextEmail.setText("");
                editTextPassword.setText("");

                loginFacebook = true;
                editTextEmail.setVisibility(View.INVISIBLE);
                editTextPassword.setVisibility(View.INVISIBLE);
                buttonLogin.setText("Entrar");
            }

            @Override
            public void checkFacebookFail() {
                loginFacebook = false;
                editTextEmail.setVisibility(View.VISIBLE);
                editTextPassword.setVisibility(View.VISIBLE);
                buttonLogin.setText("Login");
            }

            @Override
            public void logoutFacebook() {
                loginFacebook = false;
                userFacebook = new UserFacebookItem();
                editTextEmail.setVisibility(View.VISIBLE);
                editTextPassword.setVisibility(View.VISIBLE);
                buttonLogin.setText("Login");
            }
        });
        facebook.checkLoginFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.resultAction(requestCode, resultCode, data);
    }

    public void callLoginTask(String email, String pass) {
        final UserItem userItem = new UserItem();
        userItem.email = email;
        userItem.password = pass;
        userItem.facebookItem = userFacebook;

        final ProgressWait progress = new ProgressWait(this, "Processando");
        progress.show();

        AzureService.getInstance().getUserController().checkLogin(new AzureResponse<UserItem>() {
            @Override
            public void receive(MobileServiceList<UserItem> result) {
                if (result.size() > 0) {
                    if (result.get(0) != null) {
                        ProviderService.getInstance().getSessionItemView().userItem = result.get(0);

                        Intent intent = new Intent(passThis, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        passThis.startActivity(intent);

                        Log.d(TAG, "callLoginTask() returning true");
                    } else {
                        Toast.makeText(passThis.getApplicationContext(), "Email ou Senha Invalidos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(passThis.getApplicationContext(), "Email ou Senha Invalidos", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }

            @Override
            public void fail() {
                progress.dismiss();
                Log.d(TAG, "callLoginTask() returning false");
                Toast.makeText(passThis.getApplicationContext(), "Falha ao Logar", Toast.LENGTH_SHORT).show();
            }
        }, userItem);
    }
}
