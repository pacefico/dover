package com.example.fugliara.ccs002.server.controllers;

import android.util.Log;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.InviteItem;
import com.example.fugliara.ccs002.server.dataobjects.InviteUserItem;
import com.example.fugliara.ccs002.server.dataobjects.UserItem;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.Date;
import java.util.Random;

public class InviteController {

    private static final String TAG = "TAG_InviteController";

    private MobileServiceTable<InviteUserItem> mInviteUserItemTable = null;
    private MobileServiceTable<InviteItem> mInviteItemTable = null;

    public InviteController(MobileServiceClient mClient) {
        mInviteUserItemTable = mClient.getTable(InviteUserItem.class);
        mInviteItemTable = mClient.getTable(InviteItem.class);
    }

    private String generateCode() {
        Log.d(TAG, "generateCode()");

        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(9);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void generateInvite(final AzureResponse<InviteItem> result, final String email) {
        AzureRequest<InviteItem> request = new AzureRequest<InviteItem>() {
            @Override
            public MobileServiceList<InviteItem> send() {
                try {
                    InviteItem invite = new InviteItem();
                    invite.invitecode = generateCode();
                    Log.d(TAG, "generateInvite() Code: " + invite.invitecode);
                    invite.guestuseremail = email;
                    invite.hostuserid = ProviderService.getInstance().getSessionItemView().userItem.id;
                    invite.invitedate = new Date();
                    mInviteItemTable.insert(invite).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "generateInvite() Error: " + exception.getMessage());
                }
                return null;
            }
        };

        request.execute(result);
    }

    public void deleteInvite(final AzureResponse<InviteItem> result, final InviteItem inviteItem) {
        AzureRequest<InviteItem> request = new AzureRequest<InviteItem>() {
            @Override
            public MobileServiceList<InviteItem> send() {
                try {
                    mInviteItemTable.delete(inviteItem).get();
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "deleteInvite() Error: " + exception.getMessage());
                }
                return null;
            }
        };

        request.execute(result);
    }

    public void checkInvite(final AzureResponse<InviteItem> result, final String email, final String number) {
        AzureRequest<InviteItem> request = new AzureRequest<InviteItem>() {
            @Override
            public MobileServiceList<InviteItem> send() {
                MobileServiceList<InviteItem> queryResult = null;
                try {
                    Log.d(TAG, "code invite: '" + number + "' email: '" + email + "'");
                    queryResult = mInviteItemTable.where().field("inviteCode").eq(number).and().field("guestUserEmail").eq(email).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "checkInvite() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void getInviteUserItem(final AzureResponse<InviteUserItem> result, final UserItem userItem) {
        AzureRequest<InviteUserItem> request = new AzureRequest<InviteUserItem>() {
            @Override
            public MobileServiceList<InviteUserItem> send() {
                MobileServiceList<InviteUserItem> queryResult = null;
                try {
                    queryResult = mInviteUserItemTable.where().field("hostUserId").eq(userItem.id).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getInviteUserItem() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void updateInviteUserItem(final AzureResponse<InviteUserItem> result, final InviteUserItem inviteUserItem) {
        AzureRequest<InviteUserItem> request = new AzureRequest<InviteUserItem>() {
            @Override
            public MobileServiceList<InviteUserItem> send() {
                MobileServiceList<InviteUserItem> queryResult = null;
                try {
                    mInviteUserItemTable.update(inviteUserItem);
                    result.success = true;
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "updateInviteUserItem() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
