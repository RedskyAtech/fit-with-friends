package com.fit_with_friends.fitWithFriends.module;

import com.fit_with_friends.common.contracts.services.*;
import com.fit_with_friends.common.contracts.tool.IAsyncRemoteApi;
import com.fit_with_friends.common.contracts.tool.IRepository;
import com.fit_with_friends.fitWithFriends.impl.models.*;
import com.fit_with_friends.fitWithFriends.impl.services.*;
import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    IUserService providesUserService(IRepository<UserModel> local, IAsyncRemoteApi<UserModel> remoteApi) {
        return new UserService(local, remoteApi);
    }

    @Provides
    IChallengeService providesChallengeService(IRepository<ChallengeModel> local, IAsyncRemoteApi<ChallengeModel> remoteApi) {
        return new ChallengeService(local, remoteApi);
    }

    @Provides
    INotificationService providesNotificationService(IRepository<NotificationModel> local, IAsyncRemoteApi<NotificationModel> remoteApi) {
        return new NotificationService(local, remoteApi);
    }

    @Provides
    IMotivationService providesMotivationService(IRepository<MotivationModel> local, IAsyncRemoteApi<MotivationModel> remoteApi) {
        return new MotivationService(local, remoteApi);
    }

    @Provides
    IConnectionService providesConnectionService(IRepository<ConnectionModel> local, IAsyncRemoteApi<ConnectionModel> remoteApi) {
        return new ConnectionService(local, remoteApi);
    }

    @Provides
    IWeightLogService providesWeightLogService(IRepository<WeightLogModel> local, IAsyncRemoteApi<WeightLogModel> remoteApi) {
        return new WeightLogService(local, remoteApi);
    }
}
