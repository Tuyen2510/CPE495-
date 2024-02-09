package com.trust.home.security;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trust.home.security.database.DatabaseJobViewModel;
import com.trust.home.security.utils.AppPrefsManager;

public class HomeSecurityApplication extends Application {
    public static HomeSecurityApplication instance;
    public static DatabaseJobViewModel database;
    public static FirebaseDatabase firebaseDatabase;

    public static DatabaseReference getRef(String refName) {
        return firebaseDatabase.getReference(refName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = new DatabaseJobViewModel(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        AppPrefsManager.initialize(this);
    }
}
