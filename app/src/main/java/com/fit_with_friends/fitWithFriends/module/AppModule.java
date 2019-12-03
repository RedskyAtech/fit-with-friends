package com.fit_with_friends.fitWithFriends.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    Context providesContext(Application application) {
        return application;
    }
}