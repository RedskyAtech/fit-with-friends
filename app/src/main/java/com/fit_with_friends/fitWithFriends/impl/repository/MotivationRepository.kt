package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.Motivation
import com.fit_with_friends.fitWithFriends.impl.entities.MotivationDao
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class MotivationRepository(
    mapper: IModelMapper<Motivation, MotivationModel>,
    private val dao: AbstractDao<Motivation, Long>
) :
    BaseRepository<Motivation, MotivationModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<Motivation> {
        return dao.queryBuilder().where(MotivationDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<Motivation> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: MotivationModel) {}

    override fun map(motivation: Motivation, model: MotivationModel) {
        motivation.id = model.tableId
    }

    override fun newEntity(): Motivation {
        return Motivation()
    }
}
