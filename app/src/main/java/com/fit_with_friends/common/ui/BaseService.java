package com.fit_with_friends.common.ui;


import com.fit_with_friends.common.contracts.tool.*;

public class BaseService <TModel extends IModel> implements ICRUDService<TModel> {

    private IRepository<TModel> local;

    public BaseService(IRepository<TModel> local) {

        this.local = local;
    }

    @Override
    public TModel get(Long id) {
        return local.get(id);
    }

    @Override
    public TModel get(PageQuery query) {
        return local.get(query);
    }

    @Override
    public Page<TModel> search(PageInput input) {
        return local.page(input);
    }

    @Override
    public TModel update(TModel tModel) {
        return local.update(tModel.getTableId(), tModel);
    }

    @Override
    public TModel create(TModel entity) {
        return local.create(entity);
    }

    @Override
    public void delete(Long id) {
        local.remove(id);
    }

    @Override
    public void deleteAll() {
        local.removeAll();
    }
}
