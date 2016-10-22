package com.example.fugliara.ccs002.views.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.InviteUserItem;
import com.example.fugliara.ccs002.server.dataobjects.UserItem;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class ProfileInviteActivity extends AppCompatActivity {
    InviteUserItem inviteUserItem = null;
    ProfileInviteActivity paasThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_invite);

        final Button buttonFacebook = (Button) findViewById(R.id.buttonInviteFacebook);
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLinkUrl, previewImageUrl;

                appLinkUrl = "https://fb.me/895446080509790";
                previewImageUrl = "http://fugliara.bitbucket.org/temp/pigeon.png";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(paasThis, content);
                }
            }
        });

        final Button button = (Button) findViewById(R.id.btProfileInvite);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View passV = v;
                if (inviteUserItem.availableinvites > 0) {
                    inviteUserItem.availableinvites -= 1;
                    inviteUserItem.invitations += 1;
                    AzureService.getInstance().getInviteController().updateInviteUserItem(new AzureResponse<InviteUserItem>() {
                        @Override
                        public void receive(MobileServiceList<InviteUserItem> result) {
                            Intent intent = new Intent(passV.getContext(), ProfileInviteCreateActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void fail() {
                            Toast.makeText(getApplicationContext(), "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                        }
                    }, inviteUserItem);
                } else {
                    Toast.makeText(getApplicationContext(), "Convite não disponível", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getViewData();
    }

    private void getViewData() {
        final TextView txtAvailable = (TextView) findViewById(R.id.lblAvailable);
        final TextView txtInvites = (TextView) findViewById(R.id.lblInvites);
        UserItem userItem = ProviderService.getInstance().getSessionItemView().userItem;

        final ProgressWait progress;
        progress = new ProgressWait(this, "Verifica Convite");
        progress.show();

        AzureService.getInstance().getInviteController().getInviteUserItem(new AzureResponse<InviteUserItem>() {
            @Override
            public void receive(MobileServiceList<InviteUserItem> result) {
                progress.dismiss();
                if (result.size() > 0) {
                    inviteUserItem = result.get(0);
                    txtAvailable.setText(Integer.toString(result.get(0).availableinvites));
                    txtInvites.setText(Integer.toString(result.get(0).invitations));
                } else {
                    inviteUserItem = new InviteUserItem();
                }
            }

            @Override
            public void fail() {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Falha ao buscar", Toast.LENGTH_SHORT).show();
            }
        }, userItem);
    }
}
