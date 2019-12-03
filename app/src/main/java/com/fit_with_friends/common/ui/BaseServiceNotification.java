package com.fit_with_friends.common.ui;

import android.app.Service;

public abstract class BaseServiceNotification extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        setupServiceComponent();
    }

    protected abstract void setupServiceComponent();
}