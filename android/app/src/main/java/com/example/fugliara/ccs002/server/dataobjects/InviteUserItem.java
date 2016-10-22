package com.example.fugliara.ccs002.server.dataobjects;

/**
 * Created by Paulo on 10/7/2015.
 */
public class InviteUserItem {

    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("hostUserId")
    public String hostuserid;
    @com.google.gson.annotations.SerializedName("availableInvites")
    public int availableinvites;
    @com.google.gson.annotations.SerializedName("invitations")
    public int invitations;

    public InviteUserItem() {
        id = "";
        hostuserid = "";
        availableinvites = 0;
        invitations = 0;
    }
}
