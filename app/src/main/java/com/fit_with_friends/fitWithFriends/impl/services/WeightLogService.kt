package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.IWeightLogService
import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel

class WeightLogService(mLocal: IRepository<WeightLogModel>, private val mRemoteApi: IAsyncRemoteApi<WeightLogModel>) :
    BaseService<WeightLogModel>(mLocal), IWeightLogService {

    override fun getWeightLogsFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<WeightLogModel>
    ) {
        mRemoteApi.get(url, null, input, result)
    }

    override fun weightLogDetail(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<WeightLogModel>
    ) {
        mRemoteApi.get(url, null, input, result)
    }
}