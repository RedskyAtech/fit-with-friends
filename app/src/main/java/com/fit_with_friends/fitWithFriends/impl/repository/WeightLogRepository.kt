package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.WeightLog
import com.fit_with_friends.fitWithFriends.impl.entities.WeightLogDao
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class WeightLogRepository(
    mapper: IModelMapper<WeightLog, WeightLogModel>,
    private val dao: AbstractDao<WeightLog, Long>
) :
    BaseRepository<WeightLog, WeightLogModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<WeightLog> {
        return dao.queryBuilder().where(WeightLogDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<WeightLog> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: WeightLogModel) {}

    override fun map(weightLog: WeightLog, model: WeightLogModel) {
        weightLog.id = model.tableId
    }

    override fun newEntity(): WeightLog {
        return WeightLog()
    }
}