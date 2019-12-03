package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.Challenge
import com.fit_with_friends.fitWithFriends.impl.entities.ChallengeDao
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class ChallengeRepository(
    mapper: IModelMapper<Challenge, ChallengeModel>,
    private val dao: AbstractDao<Challenge, Long>
) :
    BaseRepository<Challenge, ChallengeModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<Challenge> {
        return dao.queryBuilder().where(ChallengeDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<Challenge> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: ChallengeModel) {}

    override fun map(challenge: Challenge, model: ChallengeModel) {
        challenge.id = model.tableId
    }

    override fun newEntity(): Challenge {
        return Challenge()
    }
}