package com.trust.home.security.ui.editProfile;

import android.net.Uri;
import android.os.Build;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.utils.AppFlow;
import com.trust.home.security.utils.AppPrefsManager;
import com.trust.home.security.utils.Constance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class EditProfilePresenter extends BasePresenter<EditProfileView> {
    private User currentUser;

    public void getLoggedUser() {
        currentUser = AppFlow.INSTANCE.getCurrentUser();

        if(currentUser != null) {
            view.onGetUserSuccess(currentUser);
        }
    }

    public void saveNewProfile(
            String filePath,
            String fullName,
            String age,
            int gender
    ) {
        view.showLoading();
        User user = AppPrefsManager.getInstance().getUser();
        File f = new File(filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String fileId = UUID.randomUUID().toString();
                storageReference
                        .getReference(Constance.Storage.PROFILES)
                        .child(user.getUserName())
                        .child(fileId)
                        .putBytes(Files.readAllBytes(f.toPath()))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                getAvatar(fileId, fullName, age, gender);
                            } else {
                                view.hideLoading();
                                view.showMessage("Upload Avatar Failed");
                            }
                        });
            } catch (IOException e) {
                view.hideLoading();
                throw new RuntimeException(e);
            }
        }
    }

    private void getAvatar(
            String fileId,
            String fullName,
            String age,
            int gender
    ) {
        storageReference
                .getReference(Constance.Storage.PROFILES)
                .child(currentUser.getUserName())
                .child(fileId)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    saveNewProfileToDb(
                            uri.toString(),
                            fullName,
                            age,
                            gender
                    );
                });
    }

    public void saveNewProfileToDb(
            String avatarPath,
            String fullName,
            String age,
            int gender) {
        if(currentUser != null) {
            if(avatarPath == null) {
                view.showLoading();
            }

            currentUser.setAge(age);
            currentUser.setFullName(fullName);
            currentUser.setGender(gender);
            if(avatarPath != null) {
                currentUser.setAvatar(avatarPath);
            }

            getRef(Constance.Reference.USERS)
                    .child(currentUser.getUserName())
                    .setValue(currentUser)
                    .addOnCompleteListener(task -> {
                        view.hideLoading();
                        if(task.isSuccessful()) {
                            view.onSaveSuccess();
                        } else {
                            view.showMessage("Save failed");
                        }
                    });
        } else {
            view.hideLoading();
            view.showMessage("Save failed");
        }
    }
}
