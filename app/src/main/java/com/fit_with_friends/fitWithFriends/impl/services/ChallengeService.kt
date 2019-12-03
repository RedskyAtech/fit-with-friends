package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel

class ChallengeService(mLocal: IRepository<ChallengeModel>, private val mRemoteApi: IAsyncRemoteApi<ChallengeModel>) :
    BaseService<ChallengeModel>(mLocal), IChallengeService {

    override fun createChallenge(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    ) {
        mRemoteApi.initialStep(url, action, challengeModel, result)
    }

    override fun createChallenge(
        url: String,
        challengeModel: ChallengeModel,
        action: String?,
        input: PageInput,
        filePath: String,
        result: AsyncResult<ChallengeModel>
    ) {
        mRemoteApi.create(url, challengeModel, action, input, filePath, result)
    }

    override fun getAllChallengesFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<PagePagination<ChallengeModel>>
    ) {
        mRemoteApi.page(url, null, input, null, result)
    }

    override fun challengeDetail(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    ) {
        mRemoteApi.initialStep(url, action, challengeModel, result)
    }

    override fun acceptRequest(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    ) {
        mRemoteApi.initialStep(url, action, challengeModel, result)
    }

    override fun declineRequest(
        url: String,
        action: String?,
        challengeModel: ChallengeModel,
        result: AsyncResult<ChallengeModel>
    ) {
        mRemoteApi.initialStep(url, action, challengeModel, result)
    }
}