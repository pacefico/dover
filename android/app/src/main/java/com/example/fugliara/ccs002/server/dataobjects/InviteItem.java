package com.example.fugliara.ccs002.server.dataobjects;

import java.util.Date;

public class InviteItem {

    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("hostUserId")
    public String hostuserid;
    @com.google.gson.annotations.SerializedName("inviteCode")
    public String invitecode;
    @com.google.gson.annotations.SerializedName("inviteDate")
    public Date invitedate;
    @com.google.gson.annotations.SerializedName("guestUserEmail")
    public String guestuseremail;

    public InviteItem() {
        id = "";
        hostuserid = "";
        invitecode = "";
        invitedate = null;
        guestuseremail = "";
    }
}
