package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.ICRUDService
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel

interface IConnectionService : ICRUDService<ConnectionModel> {

    fun getAllConnectionsFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    )

    fun getPendingInviteListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    )

    fun inviteFriend(
        url: String,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<ConnectionModel>
    )

    fun searchFriend(
        url: String,
        userId: String?,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<Page<ConnectionModel>>
    )

    fun sendRequest(
        url: String,
        connectionModel: ConnectionModel,
        input: PageInput,
        result: AsyncResult<ConnectionModel>
    )

    fun acceptRequest(
        url: String,
        action: String?,
        connectionModel: ConnectionModel,
        result: AsyncResult<ConnectionModel>
    )

    fun declineRequest(
        url: String,
        action: String?,
        connectionModel: ConnectionModel,
        result: AsyncResult<ConnectionModel>
    )
}
