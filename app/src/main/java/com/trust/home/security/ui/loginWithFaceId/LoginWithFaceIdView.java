package com.trust.home.security.ui.loginWithFaceId;

import com.trust.home.security.base.BaseView;
import com.trust.home.security.database.entity.Face;

public interface LoginWithFaceIdView extends BaseView {
    void getFaceSuccess(Face face);
}
