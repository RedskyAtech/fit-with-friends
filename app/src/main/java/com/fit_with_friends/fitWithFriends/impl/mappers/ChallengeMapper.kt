package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.Challenge
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel

class ChallengeMapper : IModelMapper<Challenge, ChallengeModel> {

    override fun Map(challenge: Challenge): ChallengeModel {
        return ChallengeModel()
    }
}
