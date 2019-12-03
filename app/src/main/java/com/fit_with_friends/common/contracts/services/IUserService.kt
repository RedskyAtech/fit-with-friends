package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.ICRUDService
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.UserModel

interface IUserService : ICRUDService<UserModel> {

    fun getProfile(url: String, action: String?, input: PageInput, result: AsyncResult<UserModel>)

    fun createUser(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun createUser(
        url: String,
        userModel: UserModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<UserModel>
    )

    fun loginUser(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun forgotPassword(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun changePassword(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun addWeight(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun deleteWeight(url: String, userModel: UserModel, result: AsyncResult<UserModel>)

    fun logout(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>)

    fun editProfile(
        url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>
    )

    fun editProfile(
        url: String,
        userModel: UserModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<UserModel>
    )

    fun policyType(url: String, action: String?, input: PageInput, result: AsyncResult<UserModel>)

}
