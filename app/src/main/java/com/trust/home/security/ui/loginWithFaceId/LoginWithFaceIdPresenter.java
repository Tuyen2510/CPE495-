package com.trust.home.security.ui.loginWithFaceId;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.Face;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.utils.AppPrefsManager;
import com.trust.home.security.utils.Constance;

public class LoginWithFaceIdPresenter extends BasePresenter<LoginWithFaceIdView> {
    public void getFace() {
        view.showLoading();
        User user = AppPrefsManager.getInstance().getUser();
        getRef(Constance.Reference.FACES)
                .child(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        view.hideLoading();
                        if(snapshot.exists()) {
                            view.getFaceSuccess(snapshot.getValue(Face.class));
                        } else {
                            view.showMessage("Data is not invalid");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.hideLoading();
                        view.showMessage("Data is not invalid");
                    }
                });
    }
    public void logout() {
        AppPrefsManager.getInstance().logout();
    }
}
