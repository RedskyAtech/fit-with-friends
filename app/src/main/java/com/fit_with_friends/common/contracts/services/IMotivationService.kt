package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.ICRUDService
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel

interface IMotivationService : ICRUDService<MotivationModel> {

    fun motivationNotiOnOff(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    )

    fun todayMotivation(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    )

    fun yesterdayMotivation(
        url: String,
        action: String?,
        motivationModel: MotivationModel,
        result: AsyncResult<MotivationModel>
    )
}