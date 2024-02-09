package com.trust.home.security.ui.login;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.trust.home.security.HomeSecurityApplication;
import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.ui.loginWithFaceId.LoginWithFaceIdFragment;
import com.trust.home.security.utils.AppFlow;
import com.trust.home.security.utils.AppPrefsManager;
import com.trust.home.security.utils.Constance;
import com.trust.home.security.utils.Gender;

import kotlin.Unit;

public class LoginPresenter extends BasePresenter<LoginView> {
    public void login(String username, String password) {
        getRef(Constance.Reference.USERS)
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if(user!= null && user.getPassword() != null && password.equals(user.getPassword())) {
                                AppPrefsManager.getInstance().putUser(user);
                                checkUserRegisteredFace(username);
                            }
                        } else {
                            view.onLoginFailure("Username already exists");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.hideLoading();
                        view.showMessage(error.getMessage());
                    }
                });
//        database.selectUserWhere(username, password, user -> {
//            if(user != null) {
//                AppPrefsManager.getInstance().putUser(user);
//                checkUserRegisteredFace(username);
//            } else view.onLoginFailure("username or password is incorrect");
//            return Unit.INSTANCE;
//        });
    }

    private void checkUserRegisteredFace(String username) {
//        database.checkUserIsRegisteredFace(username, isRegistered -> {
//            if (isRegistered) {
//                view.onLoginSuccess();
//            } else view.goToRegisterFace();
//            return Unit.INSTANCE;
//        });
        getRef(Constance.Reference.FACES)
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            AppFlow.INSTANCE.initialize();
                            view.onLoginSuccess();
                        } else {
                            view.goToRegisterFace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        view.hideLoading();
                        view.showMessage(error.getMessage());
                    }
                });
    }

    public void checkUserLoggedIn() {
        User user = AppPrefsManager.getInstance().getUser();
        if(user != null) {
            checkUserRegisteredFace(user.getUserName());
        }
    }
}
