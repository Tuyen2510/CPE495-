package com.trust.home.security.ui.home;

import com.trust.home.security.base.BasePresenter;
import com.trust.home.security.database.entity.User;
import com.trust.home.security.utils.AppFlow;

import kotlinx.coroutines.flow.Flow;

public class HomePresenter extends BasePresenter<HomeView> {
    public Flow<User> userFlow() {
        return AppFlow.INSTANCE.getUserFlow();
    }
}
