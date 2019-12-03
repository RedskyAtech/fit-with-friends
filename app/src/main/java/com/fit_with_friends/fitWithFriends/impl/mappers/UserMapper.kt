package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.User
import com.fit_with_friends.fitWithFriends.impl.models.UserModel

class UserMapper : IModelMapper<User, UserModel> {

    override fun Map(user: User): UserModel {
        return UserModel()
    }
}
