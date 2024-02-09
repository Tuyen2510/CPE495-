package com.trust.home.security.ui.registerFaceId;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.Face;
import com.trust.home.security.utils.Constance;

public class RegisterFaceIdPresenter extends BasePresenter<RegisterFaceIdView> {
    public void putFace(String userName, Face face) {
        view.showLoading();
        getRef(Constance.Reference.FACES)
                .child(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            view.hideLoading();
                            view.registerFaceSuccess();
                        } else {
                            getRef(Constance.Reference.FACES)
                                    .child(userName)
                                    .setValue(face).addOnCompleteListener(task -> {
                                        view.hideLoading();
                                        if(task.isSuccessful()) {
                                            view.registerFaceSuccess();
                                        } else view.registerFaceFailure(task.getException().getMessage());
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.hideLoading();
                        view.registerFaceFailure(error.getMessage());
                    }
                });
    }
}
