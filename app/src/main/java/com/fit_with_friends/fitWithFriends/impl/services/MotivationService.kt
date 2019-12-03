package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.IMotivationService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.IAsyncRemoteApi
import com.fit_with_friends.common.contracts.tool.IRepository
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel

class MotivationService(
    mLocal: IRepository<MotivationModel>,
    private val mRemoteApi: IAsyncRemoteApi<MotivationModel>
) :
    BaseService<MotivationModel>(mLocal), IMotivationService {

    override fun motivationNotiOnOff(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    ) {
        mRemoteApi.initialStep(url, action, motivationModel, result)
    }

    override fun todayMotivation(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    ) {
        mRemoteApi.initialStep(url, action, motivationModel, result)
    }

    override fun yesterdayMotivation(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    ) {
        mRemoteApi.initialStep(url, action, motivationModel, result)
    }

}