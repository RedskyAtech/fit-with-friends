package com.fit_with_friends.fitWithFriends.impl.repository

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.common.contracts.tool.PageQuery
import com.fit_with_friends.fitWithFriends.impl.entities.User
import com.fit_with_friends.fitWithFriends.impl.entities.UserDao
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import de.greenrobot.dao.AbstractDao
import de.greenrobot.dao.query.QueryBuilder

class UserRepository(mapper: IModelMapper<User, UserModel>, private val dao: AbstractDao<User, Long>) :
    BaseRepository<User, UserModel>(mapper, dao) {

    override fun query(id: Long?): QueryBuilder<User> {
        return dao.queryBuilder().where(UserDao.Properties.Id.eq(id))
    }

    override fun query(query: PageQuery): QueryBuilder<User> {
        val queryBuilder = dao.queryBuilder()
        if (query.contains("all")) {
            queryBuilder.list()
        }
        return queryBuilder
    }

    override fun map(model: UserModel) {}

    override fun map(user: User, model: UserModel) {
        user.id = model.tableId
    }

    override fun newEntity(): User {
        return User()
    }
}
