package com.trust.home.security.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.trust.home.security.base.BaseDialogFragment;
import com.trust.home.security.databinding.DialogUploadAvatarBinding;
import com.trust.home.security.databinding.LayoutNoticeDialogBinding;

public class DialogUpload extends BaseDialogFragment<DialogUploadAvatarBinding> {

    public DialogUpload(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogUploadAvatarBinding getBinding(LayoutInflater inflater) {
        return DialogUploadAvatarBinding.inflate(inflater);
    }

    @Override
    protected void initViews() {
        mBinding.tvProgress.setText("0 %");
    }

    @SuppressLint("SetTextI18n")
    public void uploadPercent(int percent) {
        mBinding.tvProgress.setText(percent + " %");
    }
}
