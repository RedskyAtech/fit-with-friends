package com.fit_with_friends.fitWithFriends.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.fit_with_friends.fitWithFriends.impl.entities.*;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    private static SQLiteDatabase _database;
    private static DaoMaster _daoMaster;
    private static DaoSession _daoSession;

    @Provides
    SQLiteDatabase providesSQLiteDatabase(Context context) {
        if (_database == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "admin-db", null);
            _database = helper.getWritableDatabase();
        }

        return _database;
    }

    @Provides
    DaoMaster providesDaoMaster(SQLiteDatabase database) {
        if (_daoMaster == null) {
            _daoMaster = new DaoMaster(database);
        }
        return _daoMaster;
    }

    @Provides
    DaoSession providesDaoSession(DaoMaster daoMaster) {
        if (_daoSession == null) {
            _daoSession = daoMaster.newSession();
        }
        return _daoSession;
    }

    @Provides
    UserDao providesUserDao(DaoSession daoSession) {
        return daoSession.getUserDao();
    }

    @Provides
    ChallengeDao providesChallengeDao(DaoSession daoSession) {
        return daoSession.getChallengeDao();
    }

    @Provides
    NotificationDao providesNotificationDao(DaoSession daoSession) {
        return daoSession.getNotificationDao();
    }

    @Provides
    MotivationDao providesMotivationDao(DaoSession daoSession) {
        return daoSession.getMotivationDao();
    }

    @Provides
    ConnectionDao providesConnectionDao(DaoSession daoSession) {
        return daoSession.getConnectionDao();
    }

    @Provides
    WeightLogDao providesWeightLogDao(DaoSession daoSession) {
        return daoSession.getWeightLogDao();
    }
}