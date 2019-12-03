package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.Motivation
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel

class MotivationMapper : IModelMapper<Motivation, MotivationModel> {

    override fun Map(motivation: Motivation): MotivationModel {
        return MotivationModel()
    }
}
