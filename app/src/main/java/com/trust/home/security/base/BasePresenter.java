package com.trust.home.security.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.trust.home.security.HomeSecurityApplication;
import com.trust.home.security.database.DatabaseJobViewModel;
import com.trust.home.security.helpers.Preferences;

public class BasePresenter <V extends BaseView> {
    protected V view;
    protected final Preferences preferences;
    protected final DatabaseJobViewModel database;
    protected final FirebaseDatabase firebaseDatabase;
    protected final FirebaseStorage storageReference;

    public BasePresenter() {
        preferences = Preferences.getInstance();
        storageReference = FirebaseStorage.getInstance();
        firebaseDatabase = HomeSecurityApplication.firebaseDatabase;
        database = HomeSecurityApplication.database;
    }

    protected DatabaseReference getRef(String refName) {
        return firebaseDatabase.getReference(refName);
    }

    public void onAttach(V view) {
        this.view = view;
    }

    public void onDetach() {

    }
}
