package com.fit_with_friends.common.contracts.tool;

import com.fit_with_friends.fitWithFriends.impl.models.BodyModel;

public interface IRepository<TEntity> {

    TEntity get(Long id);

    TEntity get(PageQuery query);

    Page<TEntity> page(PageInput input);

    BodyModel<TEntity> body(PageInput input);

    TEntity update(Long id, TEntity entity);

    TEntity create(TEntity entity);

    boolean any(PageQuery query);

    void remove(Long id);

    TEntity getByServerId(Long serverId);

    TEntity getByServerCode(String code);

    void removeAll();
}

