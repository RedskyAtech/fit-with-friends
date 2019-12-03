package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.WeightLog
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel

class WeightLogMapper : IModelMapper<WeightLog, WeightLogModel> {

    override fun Map(weightLog: WeightLog): WeightLogModel {
        return WeightLogModel()
    }
}
