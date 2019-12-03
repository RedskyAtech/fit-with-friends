package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.Notification
import com.fit_with_friends.fitWithFriends.impl.entities.NotificationDao
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class NotificationRepository(
    mapper: IModelMapper<Notification, NotificationModel>,
    private val dao: AbstractDao<Notification, Long>
) : BaseRepository<Notification, NotificationModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<Notification> {
        return dao.queryBuilder().where(NotificationDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<Notification> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: NotificationModel) {}

    override fun map(notification: Notification, model: NotificationModel) {
        notification.id = model.tableId
    }

    override fun newEntity(): Notification {
        return Notification()
    }
}
