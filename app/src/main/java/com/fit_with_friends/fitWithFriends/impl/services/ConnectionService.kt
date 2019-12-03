package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel

class ConnectionService(
    private val mLocal: IRepository<ConnectionModel>,
    private val mRemoteApi: IAsyncRemoteApi<ConnectionModel>
) : BaseService<ConnectionModel>(mLocal), IConnectionService {

    override fun getAllConnectionsFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    ) {
        mRemoteApi.page(url, input, result)
    }

    override fun getPendingInviteListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    ) {
        mRemoteApi.page(url, input, result)
    }

    override fun inviteFriend(
        url: String,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<ConnectionModel>
    ) {
        mRemoteApi.create(url, connectionModel, input, result)
    }

    override fun searchFriend(
        url: String,
        userId: String?,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    ) {
        mRemoteApi.create(url, userId, connectionModel, input, result)
    }

    override fun sendRequest(
        url: String,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<ConnectionModel>
    ) {
        mRemoteApi.create(url, connectionModel, input, result)
    }

    override fun acceptRequest(
        url: String,
        action: String?,
        connectionModel: ConnectionModel,
        result: AsyncResult<ConnectionModel>
    ) {
        mRemoteApi.initialStep(url, action, connectionModel, result)
    }

    override fun declineRequest(
        url: String,
        action: String?,
        connectionModel: ConnectionModel,
        result: AsyncResult<ConnectionModel>
    ) {
        mRemoteApi.initialStep(url, action, connectionModel, result)
    }
}
