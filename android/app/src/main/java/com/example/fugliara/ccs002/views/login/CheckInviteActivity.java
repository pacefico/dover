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
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class CheckInviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CheckInviteActivity passThis = this;

        setContentView(R.layout.activity_check_invite);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button buttonRegister = (Button) findViewById(R.id.buttonCheck);
        buttonRegister.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText editTextEmail = (EditText) findViewById(R.id.editTextEmailInvite);
                EditText editTextNumber = (EditText) findViewById(R.id.editTextNumberInvite);

                final String email = editTextEmail.getText().toString();
                String number = editTextNumber.getText().toString();

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                        final ProgressWait progress;
                        progress = new ProgressWait(passThis, "Validando");
                        progress.show();

                        AzureService.getInstance().getInviteController().checkInvite(new AzureResponse<InviteItem>() {
                            @Override
                            public void receive(MobileServiceList<InviteItem> result) {
                                if (result.size() > 0) {
                                    ProviderService.getInstance().getSessionItemView().inviteItem = result.get(0);
                                    Intent intent = new Intent(passThis, RegisterActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(passThis, "Falha ao validar", Toast.LENGTH_SHORT).show();
                                }
                                progress.dismiss();
                            }

                            @Override
                            public void fail() {
                                progress.dismiss();
                                Toast.makeText(passThis, "Falha ao validar", Toast.LENGTH_SHORT).show();
                            }
                        }, email, number);
                    } else {
                        Toast.makeText(passThis, "Internet Offline!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(passThis, "Email inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
