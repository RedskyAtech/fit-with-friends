package com.fit_with_friends.fitWithFriends.module;

import com.fit_with_friends.common.contracts.tool.IModelMapper;
import com.fit_with_friends.fitWithFriends.impl.entities.*;
import com.fit_with_friends.fitWithFriends.impl.mappers.*;
import com.fit_with_friends.fitWithFriends.impl.models.*;
import dagger.Module;
import dagger.Provides;

@Module
public class MapperModule {

    @Provides
    IModelMapper<User, UserModel> providesUserMapper() {
        return new UserMapper();
    }

    @Provides
    IModelMapper<Challenge, ChallengeModel> providesChallengeMapper() {
        return new ChallengeMapper();
    }

    @Provides
    IModelMapper<Notification, NotificationModel> providesNotificationMapper() {
        return new NotificationMapper();
    }

    @Provides
    IModelMapper<Motivation, MotivationModel> providesMotivationMapper() {
        return new MotivationMapper();
    }

    @Provides
    IModelMapper<Connection, ConnectionModel> providesConnectionMapper() {
        return new ConnectionMapper();
    }
    @Provides
    IModelMapper<WeightLog, WeightLogModel> providesWeightLogMapper() {
        return new WeightLogMapper();
    }
}
