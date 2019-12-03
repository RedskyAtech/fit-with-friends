package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel

interface IWeightLogService : ICRUDService<WeightLogModel> {

    fun getWeightLogsFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<WeightLogModel>
    )

    fun weightLogDetail(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<WeightLogModel>
    )

}