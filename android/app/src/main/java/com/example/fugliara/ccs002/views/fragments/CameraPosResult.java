package com.example.fugliara.ccs002.views.fragments;

import com.example.fugliara.ccs002.server.dataobjects.ImageItem;

import java.io.File;

abstract public class CameraPosResult {
    public abstract void action(final File to, final ImageItem img);

    public abstract void fail();
}
