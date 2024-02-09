package com.trust.home.security.ui.register;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.utils.Constance;
import com.trust.home.security.utils.Gender;

public class RegisterPresenter extends BasePresenter<RegisterView> {
    public void createUser(String username, String password) {
        view.showLoading();
        getRef(Constance.Reference.USERS)
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            view.hideLoading();
                            view.showMessage("userName has been registered");
                        } else {
                            User user = new User(null, username, password, "", "0", Gender.MALE.getValue(), "");
                            getRef(Constance.Reference.USERS).child(username).setValue(user).addOnSuccessListener(unused -> {
                                view.hideLoading();
                                view.showToast("Register successfully");
                                view.onBackPress();
                            }).addOnFailureListener(e -> {
                                view.hideLoading();
                                view.showMessage("Register error, please try again");
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.hideLoading();
                        view.showMessage(error.getMessage());
                    }
                });
//        database.checkUserIsRegistered(username, isRegistered -> {
//            if(isRegistered) {
//                view.onRegisterFailure("userName has been registered");
//            } else {
//                User user = new User(null, username, password, "", "0", Gender.MALE.getValue(), "");
//                database.insertUser(user);
//                AppPrefsManager.getInstance().putUser(user);
//                view.onRegisterSuccess();
//            }
//
//            return Unit.INSTANCE;
//        });
    }
}
