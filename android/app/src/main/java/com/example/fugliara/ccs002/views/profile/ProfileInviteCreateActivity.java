package com.example.fugliara.ccs002.views.profile;

import android.content.Intent;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class ProfileInviteCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProfileInviteCreateActivity passThis = this;
        setContentView(R.layout.activity_profile_invite_create);

        final Button button = (Button) findViewById(R.id.btInviteCreate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View passV = v;
                EditText editTextEmail = (EditText) findViewById(R.id.txtInviteCreateEmail);

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
                    final ProgressWait progress;
                    progress = new ProgressWait(passThis, "Gerando");
                    progress.show();

                    AzureService.getInstance().getInviteController().generateInvite(new AzureResponse<InviteItem>() {
                        @Override
                        public void receive(MobileServiceList<InviteItem> result) {
                            progress.dismiss();
                            Intent intent = new Intent(passV.getContext(), ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Convidado", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Falha ao gerar", Toast.LENGTH_SHORT).show();
                        }
                    }, editTextEmail.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
