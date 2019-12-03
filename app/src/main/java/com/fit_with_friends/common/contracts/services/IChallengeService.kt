package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import java.io.File

interface IChallengeService : ICRUDService<ChallengeModel> {

    fun createChallenge(
        url: String, action: String?, challengeModel: ChallengeModel, result: AsyncResult<ChallengeModel>
    )

    fun createChallenge(
        url: String,
        challengeModel: ChallengeModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<ChallengeModel>
    )

    fun getAllChallengesFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<PagePagination<ChallengeModel>>
    )

    fun challengeDetail(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    )

    fun acceptRequest(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    )

    fun declineRequest(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    )

}
