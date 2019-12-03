package com.fit_with_friends.common.contracts.tool;

import com.fit_with_friends.fitWithFriends.impl.models.BodyModel;
import de.greenrobot.dao.query.QueryBuilder;

public class PagerPagination {
    public static <TEntity, TModel> BodyModel<TModel> get(QueryBuilder<TEntity> queryBuilder, PageInput input, IModelMapper<TEntity, TModel> mapper) {
        BodyModel<TModel> bodyModel = new BodyModel<>();

        bodyModel.total = queryBuilder.count();

        if (!input.noPaging) {
            queryBuilder.offset(input.pageNo * input.pageSize);
            queryBuilder.limit(input.pageSize);
        }

        for (TEntity entity : queryBuilder.list()) {
            bodyModel.data.add(mapper.Map(entity));
        }

        return bodyModel;
    }
}
