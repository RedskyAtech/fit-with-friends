package com.fit_with_friends.fitWithFriends.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.fit_with_friends.common.contracts.tool.*;
import com.fit_with_friends.common.repositories.AsyncRemoteApi;
import com.fit_with_friends.fitWithFriends.impl.entities.*;
import com.fit_with_friends.fitWithFriends.impl.models.*;
import com.fit_with_friends.fitWithFriends.impl.repository.*;
import com.google.gson.reflect.TypeToken;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    IRepository<UserModel> providesUserRepository(UserDao userDao, IModelMapper<User, UserModel> mapper) {
        return new UserRepository(mapper, userDao);
    }

    @Provides
    IAsyncRemoteApi<UserModel> providesAsyncUserRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "users", new TypeToken<UserModel>() {
        }.getType(),
                new TypeToken<Page<UserModel>>() {
                }.getType(),
                new TypeToken<DataModel<UserModel>>() {
                }.getType(), sqLiteDatabase);
    }

    @Provides
    IRepository<ChallengeModel> providesChallengeRepository(ChallengeDao challengeDao, IModelMapper<Challenge, ChallengeModel> mapper) {
        return new ChallengeRepository(mapper, challengeDao);
    }

    @Provides
    IAsyncRemoteApi<ChallengeModel> providesAsyncChallengeRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "challenge", new TypeToken<ChallengeModel>() {
        }.getType(),
                new TypeToken<PagePagination<ChallengeModel>>() {
                }.getType(),
                new TypeToken<DataModel<ChallengeModel>>() {
                }.getType(), sqLiteDatabase);
    }

    @Provides
    IRepository<NotificationModel> providesNotificationRepository(NotificationDao notificationDao, IModelMapper<Notification, NotificationModel> mapper) {
        return new NotificationRepository(mapper, notificationDao);
    }

    @Provides
    IAsyncRemoteApi<NotificationModel> providesAsyncNotificationRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "notification", new TypeToken<NotificationModel>() {
        }.getType(),
                new TypeToken<Page<NotificationModel>>() {
                }.getType(),
                new TypeToken<DataModel<NotificationModel>>() {
                }.getType(), sqLiteDatabase);
    }

    @Provides
    IRepository<MotivationModel> providesMotivationRepository(MotivationDao motivationDao, IModelMapper<Motivation, MotivationModel> mapper) {
        return new MotivationRepository(mapper, motivationDao);
    }

    @Provides
    IAsyncRemoteApi<MotivationModel> providesAsyncMotivationRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "notivation", new TypeToken<MotivationModel>() {
        }.getType(),
                new TypeToken<Page<MotivationModel>>() {
                }.getType(),
                new TypeToken<DataModel<MotivationModel>>() {
                }.getType(), sqLiteDatabase);
    }

    @Provides
    IRepository<ConnectionModel> providesConnectionRepository(ConnectionDao connectionDao, IModelMapper<Connection, ConnectionModel> mapper) {
        return new ConnectionRepository(mapper, connectionDao);
    }

    @Provides
    IAsyncRemoteApi<ConnectionModel> providesAsyncConnectionRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "connection", new TypeToken<ConnectionModel>() {
        }.getType(),
                new TypeToken<Page<ConnectionModel>>() {
                }.getType(),
                new TypeToken<DataModel<ConnectionModel>>() {
                }.getType(), sqLiteDatabase);
    }

    @Provides
    IRepository<WeightLogModel> providesWeightLogRepository(WeightLogDao weightLogDao, IModelMapper<WeightLog, WeightLogModel> mapper) {
        return new WeightLogRepository(mapper, weightLogDao);
    }

    @Provides
    IAsyncRemoteApi<WeightLogModel> providesAsyncWeightLogRepository(Context context, SQLiteDatabase sqLiteDatabase) {
        return new AsyncRemoteApi<>(context, "weightLog", new TypeToken<WeightLogModel>() {
        }.getType(),
                new TypeToken<Page<WeightLogModel>>() {
                }.getType(),
                new TypeToken<DataModel<WeightLogModel>>() {
                }.getType(), sqLiteDatabase);
    }
}