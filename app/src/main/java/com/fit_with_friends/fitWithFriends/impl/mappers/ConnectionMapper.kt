package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.Connection
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel

class ConnectionMapper : IModelMapper<Connection, ConnectionModel> {

    override fun Map(connection: Connection): ConnectionModel {
        return ConnectionModel()
    }
}