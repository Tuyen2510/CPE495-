package com.trust.home.security.ui.editProfile;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.trust.home.security.base.BaseFragment;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.databinding.FragmentEditProfileBinding;
import com.trust.home.security.utils.AppUtils;
import com.trust.home.security.utils.Gender;
import com.trust.home.security.utils.GlideUtils;
import com.trust.home.security.utils.StringUtils;
import com.trust.home.security.widgets.DialogUpload;

import kotlin.Unit;

public class EditProfileFragment
        extends BaseFragment<FragmentEditProfileBinding, EditProfilePresenter, EditProfileView>
        implements EditProfileView {
    private Uri currentUri;

    private final ActivityResultLauncher<String> pickPhotoLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), result -> {
                if(result != null) {
                    this.currentUri = result;
                    Glide.with(this)
                            .load(result)
                            .into(mBinding.imAvatar);
                }
    });

    @Override
    protected FragmentEditProfileBinding binding(LayoutInflater inflater, ViewGroup container) {
        return FragmentEditProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected EditProfilePresenter getPresenter() {
        return new EditProfilePresenter();
    }

    @Override
    protected void initViews() {
        mPresenter.getLoggedUser();
    }

    @Override
    public void onGetUserSuccess(User user) {
        mBinding.fieldName.setText(user.getFullName());
        mBinding.fieldAge.setText(user.getAge());
        Gender gender = Gender.getGender(user.getGender());
        if(gender == Gender.MALE) {
            mBinding.cbMale.setChecked(true);
        } else mBinding.cbFemale.setChecked(true);

        if(StringUtils.valid(user.getAvatar())) {
            GlideUtils.loadAvatar(mBinding.imAvatar, user.getAvatar());
        }
    }

    @Override
    protected void initActions() {
        mBinding.toolbar.setStartIconListener(this::onBackPressed);
        mBinding.btnSave.setOnClickListener(this::onSave);
        mBinding.imAvatar.setOnClickListener(this::onSelectAvatar);
    }

    private void onSelectAvatar(View view) {
        pickPhotoLauncher.launch("image/*");
    }

    private void onSave(View view) {
        String name = mBinding.fieldName.getText();
        String age = mBinding.fieldAge.getText();
        int gender = mBinding.cbMale.isChecked() ? Gender.MALE.getValue() : Gender.FEMALE.getValue();

        if(currentUri != null) {
            AppUtils.INSTANCE.compressImage(requireContext(), currentUri, result -> {
                if(StringUtils.valid(result)) {
                    mPresenter.saveNewProfile(
                            result, name, age, gender
                    );
                }
                return Unit.INSTANCE;
            });
        } else {
            mPresenter.saveNewProfileToDb(
                    null, name, age, gender
            );
        }
    }

    @Override
    public void onSaveSuccess() {
        showToast("Update successfully");
        onBackPressed();
    }

    @Override
    protected EditProfileView attachView() {
        return this;
    }
}
