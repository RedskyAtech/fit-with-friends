package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.Connection
import com.fit_with_friends.fitWithFriends.impl.entities.ConnectionDao
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class ConnectionRepository(
    mapper: IModelMapper<Connection, ConnectionModel>,
    private val dao: AbstractDao<Connection, Long>
) : BaseRepository<Connection, ConnectionModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<Connection> {
        return dao.queryBuilder().where(ConnectionDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<Connection> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: ConnectionModel) {}

    override fun map(connection: Connection, model: ConnectionModel) {
        connection.id = model.tableId
    }

    override fun newEntity(): Connection {
        return Connection()
    }
}
