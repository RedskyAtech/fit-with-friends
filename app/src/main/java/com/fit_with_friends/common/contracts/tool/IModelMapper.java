package com.fit_with_friends.common.contracts.tool;

public interface IModelMapper<TEntity, TModel> {

    TModel Map(TEntity entity);
}
