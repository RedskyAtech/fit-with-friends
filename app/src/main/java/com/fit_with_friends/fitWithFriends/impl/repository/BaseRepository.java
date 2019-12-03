package com.fit_with_friends.fitWithFriends.impl.repository;

import com.fit_with_friends.common.contracts.tool.*;
import com.fit_with_friends.fitWithFriends.impl.models.BodyModel;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

public abstract class BaseRepository<TEntity, TModel extends IModel> implements IRepository<TModel> {

    private final IModelMapper<TEntity, TModel> mapper;
    private final AbstractDao<TEntity, Long> dao;

    public BaseRepository(IModelMapper<TEntity, TModel> mapper, AbstractDao<TEntity, Long> dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    protected abstract QueryBuilder<TEntity> query(Long id);

    protected abstract QueryBuilder<TEntity> query(PageQuery query);

    protected abstract void map(TModel model);

    protected abstract void map(TEntity entity, TModel model);

    protected abstract TEntity newEntity();

    @Override
    public TModel get(Long id) {
        TEntity entity = query(id).unique();

        if (entity == null)
            return null;

        TModel model = mapper.Map(entity);
        map(model);
        return model;
    }

    @Override
    public TModel get(PageQuery query) {
        TEntity entity = query(query).unique();

        if (entity == null)
            return null;

        TModel model = mapper.Map(entity);

        map(model);
        return model;
    }

    @Override
    public Page<TModel> page(PageInput input) {
        return Pager.get(query(input.query), input, mapper);
        //return null;
    }

    @Override
    public BodyModel<TModel> body(PageInput input) {
        return PagerPagination.get(query(input.query), input, mapper);
        //return null;
    }

    @Override
    public TModel update(Long id, TModel tModel) {
        TEntity entity = query(id).unique();
        map(entity, tModel);
        dao.update(entity);
        return mapper.Map(entity);
    }

    @Override
    public TModel create(TModel tModel) {
        Long id = tModel.getTableId();

        if (id != null) {
            return update(id, tModel);
        }

        TEntity entity = newEntity();
        map(entity, tModel);
        dao.insert(entity);
        return mapper.Map(entity);
    }

    @Override
    public boolean any(PageQuery query) {
        return query(query).count() > 0;
    }

    @Override
    public void remove(Long id) {
        TEntity entity = query(id).unique();
        dao.delete(entity);
    }

    @Override
    public TModel getByServerId(Long serverId) {
        return get(new PageQuery("serverId", serverId));
    }

    @Override
    public TModel getByServerCode(String code) {
        return get(new PageQuery("code", code));
    }

    @Override
    public void removeAll() {
        dao.deleteAll();
    }
}