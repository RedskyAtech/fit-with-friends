package com.fit_with_friends.common.contracts.tool;

public interface ICRUDService<TModel>{

    TModel get(Long id);

    TModel get(PageQuery query);

    Page<TModel> search(PageInput input);

    TModel  update(TModel model);

    TModel create(TModel entity);

    void delete(Long id);

    void deleteAll();
}
