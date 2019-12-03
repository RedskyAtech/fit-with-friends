package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.IAsyncRemoteApi
import com.fit_with_friends.common.contracts.tool.IRepository
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.UserModel

class UserService(mLocal: IRepository<UserModel>, private val mRemoteApi: IAsyncRemoteApi<UserModel>) :
    BaseService<UserModel>(mLocal), IUserService {

    override fun getProfile(url: String, action: String?, input: PageInput, result: AsyncResult<UserModel>) {
        mRemoteApi.get(url, action, input, result)
    }

    override fun createUser(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun createUser(
        url: String,
        userModel: UserModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<UserModel>
    ) {
        mRemoteApi.create(url, userModel, action, input, filePath, result)
    }

    override fun loginUser(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun forgotPassword(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun changePassword(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun addWeight(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun deleteWeight(url: String, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, null, userModel, result)
    }

    override fun logout(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun editProfile(url: String, action: String?, userModel: UserModel, result: AsyncResult<UserModel>) {
        mRemoteApi.initialStep(url, action, userModel, result)
    }

    override fun editProfile(
        url: String,
        userModel: UserModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<UserModel>
    ) {
        mRemoteApi.create(url, userModel, action, input, filePath, result)
    }

    override fun policyType(url: String, action: String?, input: PageInput, result: AsyncResult<UserModel>) {
        mRemoteApi.get(url, action, input, result)
    }
}
