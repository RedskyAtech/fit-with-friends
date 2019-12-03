package com.fit_with_friends.common.contracts.tool;

public interface IAsyncRemoteApi<TModel extends IModel> {

    IAsyncResponse<TModel> get(final String id);

    void get(AsyncResult<TModel> result);

    void get(final String url, AsyncResult<TModel> result);

    void get(final String url, String userId, AsyncResult<TModel> result);

    void get(final String url, String action, String userId, PageInput input, AsyncResult<TModel> result);

    void get(String url, String userId, PageInput input, AsyncResult<TModel> result);

    IAsyncResponse<TModel> update(final String url, final TModel model, String userId);

    void update(final TModel model, String action, AsyncResult<TModel> result);

    void update(final String url, final TModel model, final String userId, AsyncResult<TModel> result);

    void update(final String url, String action, PageInput input, TModel model, AsyncResult<TModel> result);

    IAsyncResponse<Page<TModel>> page(final PageInput input);

    void page(final String url, final PageInput input, AsyncResult<Page<TModel>> result);

    void page(final PageInput input, String action, AsyncResult<Page<TModel>> result);

    void page(final String url, final PageInput input, String userId, AsyncResult<Page<TModel>> result);

    void page(final String url, final String action, final PageInput input, final String userId, final AsyncResult<PagePagination<TModel>> result);

    void initialStep(final String url, final String userId, final TModel model, AsyncResult<TModel> result);

    void create(final String url, final TModel model, AsyncResult<TModel> result);

    void create(final String url, TModel model, String userId, AsyncResult<TModel> result);

    void create(final String url, TModel model, String userId, String action, AsyncResult<TModel> result);

    void create(final String url, String userId, Page<TModel> model, AsyncResult<Page<TModel>> result);

    void create(final String url, String userId, TModel model, final PageInput input, AsyncResult<Page<TModel>> result);

    void create(final String url, TModel model, final PageInput input, AsyncResult<TModel> result);

    void create(final String url, TModel model, String action, PageInput input, String filePath, AsyncResult<TModel> result);

    void delete(Long id, String userId, AsyncNotify result);

    void delete(String url, String userId, AsyncNotify result);

    void delete(String url, String userId, PageInput input, AsyncNotify result);

    void delete(final String url, TModel model, AsyncResult<TModel> result);

}