package com.sarvex.ribbit;

import android.app.Application;

import com.parse.Parse;

public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Parse.com bindings
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "syH4EfBHCjeMHs9zuYE00hqB1bYHKaSrkbkbIDJt", "CiGuyeKlfnfWY49JcTVSHGPBzIC09Lv4QYJTJMVX");
    }
}
