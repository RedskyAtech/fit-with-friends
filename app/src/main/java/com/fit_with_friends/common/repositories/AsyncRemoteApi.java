package com.fit_with_friends.common.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.fit_with_friends.common.contracts.tool.*;
import com.fit_with_friends.fitWithFriends.utils.CommonMethods;
import com.fit_with_friends.fitWithFriends.utils.Constants;
import com.fit_with_friends.fitWithFriends.utils.NetworkDetector;
import com.fit_with_friends.fitWithFriends.utils.image.ToastUtils;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class AsyncRemoteApi<TModel extends IModel> implements IAsyncRemoteApi<TModel> {
    private static final String ERROR_NOT_CONNECTED = "Internet connection not available";
    private static final String ERROR_SLOW_INTERNET = "Internet connection is slow";
    private static final String ERROR_UNKNOWN = "Unknown error";
    private final RemoteRepository<TModel> _remoteRepository;
    private Gson _gson;
    private Type _dataType;
    private Context mContext;
    private final NetworkDetector _networkDetector;

    public AsyncRemoteApi(Context context, String key, Type modelType, Type pageType, Type dataType,
                          SQLiteDatabase database) {
        _remoteRepository = new RemoteRepository<>(context, key, pageType, dataType);
        _networkDetector = new NetworkDetector(context);
        _dataType = dataType;
        mContext = context;
        _gson = new Gson();
    }

    @Override
    public IAsyncResponse<TModel> get(final String id) {
        final ModelResponse response = new ModelResponse();
        response._isBusy = true;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response._model = _remoteRepository.get(id);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    response._error = ERROR_UNKNOWN;
                } finally {
                    response._isBusy = false;
                }
            }
        })).start();
        return response;
    }

    @Override
    public void get(AsyncResult<TModel> result) {
        get(null, null, result);
    }

    @Override
    public void get(String id, AsyncResult<TModel> result) {
        get(id, null, result);
    }

    @Override
    public void get(final String url, final String userId, final AsyncResult<TModel> result) {
        get(url, userId, null, result);
    }

    @Override
    public void get(final String url, final String action, final String userId, final PageInput input, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel model = _remoteRepository.get(url, action, userId, input);
                    result.success(model);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void get(final String url, final String userId, final PageInput input, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel model = _remoteRepository.get(url, null, userId, input);
                    result.success(model);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void update(final TModel model, final String action, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.update(model, action, "");
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public IAsyncResponse<TModel> update(final String url, final TModel model, final String userId) {
        final ModelResponse response = new ModelResponse();
        response._isBusy = true;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response._model = _remoteRepository.update(url, model, userId);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    response._error = ERROR_UNKNOWN;
                } finally {
                    response._isBusy = false;
                }
            }
        })).start();
        return response;
    }

    @Override
    public void update(final String url, final TModel model, final String userId, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.update(url, model, userId);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void update(final String url, final String action, final PageInput input, final TModel model, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.update(url, action, input, model);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public IAsyncResponse<Page<TModel>> page(final PageInput input) {
        final PageResponse response = new PageResponse();
        response._isBusy = true;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response._page = _remoteRepository.page(input);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    response._error = ERROR_UNKNOWN;
                } finally {
                    response._isBusy = false;
                }
            }
        })).start();
        return response;
    }

    @Override
    public void page(String url, PageInput input, AsyncResult<Page<TModel>> result) {
        page(url, input, "", result);
    }

    @Override
    public void page(final PageInput input, final String action, final AsyncResult<Page<TModel>> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Page<TModel> realPage = _remoteRepository.page(input, action);
                    result.success(realPage);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void page(final String url, final PageInput input, final String userId, final AsyncResult<Page<TModel>> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Page<TModel> realPage = _remoteRepository.page(url, input, userId);
                    result.success(realPage);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void page(final String url, final String action, final PageInput input, final String userId, final AsyncResult<PagePagination<TModel>> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PagePagination<TModel> realPage = _remoteRepository.page(url, action, input, userId);
                    result.success(realPage);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    switch (ex.getMessage()) {
                        case Constants.NULL_OBJECT_ERROR:
                            Log.d("exception", ex.getMessage());
                            break;
                        case Constants.NETWORK_ERROR:
                            Log.d("exception", ex.getMessage());
                            break;
                        case Constants.TOO_MANY_REQUEST_ERROR:
                            Log.d("exception", ex.getMessage());
                            break;
                        default:
                            result.error(ex.getMessage());
                            break;
                    }
                }
            }
        })).start();
    }

    @Override
    public void initialStep(final String url, final String userId, final TModel model, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.initialCreation(url, userId, model);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final TModel model, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.create(url, model);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final TModel model, final String userId, final AsyncResult<TModel> result) {
        create(url, model, userId, "", result);
    }

    @Override
    public void create(final String url, final TModel model, final String userId, final String action, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.create(url, model, userId);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final String userId, final Page<TModel> model, final AsyncResult<Page<TModel>> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Page<TModel> remoteModel = _remoteRepository.create(url, model, userId);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final String userId, final TModel model, final PageInput input, final AsyncResult<Page<TModel>> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Page<TModel> remoteModel = _remoteRepository.create(url, userId, model, input);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final TModel model, final PageInput input, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.create(url, model, input);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        if (url.equals(Constants.BASE_URL + Constants.INVITE_FRIEND)) {
                            result.error("");
                        } else {
                            Log.d("exception", ex.getMessage());
                        }
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void create(final String url, final TModel model, final String action, final PageInput input, final String filePath, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel response = _remoteRepository.create(url, model, action, input, filePath);
                    result.success(response);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void delete(final Long id, final String userId, final AsyncNotify result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseJson = _remoteRepository.delete(id, userId);
                    DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
                    if (responseData.success || responseData.code.equals("200"))
                        result.success();
                    else
                        result.error(responseData.message);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void delete(final String url, final String userId, final AsyncNotify result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseJson = _remoteRepository.delete(url, userId);
                    DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
                    if (responseData.success || responseData.code.equals("200"))
                        result.success();
                    else
                        result.error(responseData.message);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void delete(final String url, final String userId, final PageInput input, final AsyncNotify result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseJson = _remoteRepository.delete(url, userId, input);
                    DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
                    if (responseData.success || responseData.code.equals("200"))
                        result.success();
                    else
                        result.error(responseData.message);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    @Override
    public void delete(final String url, final TModel model, final AsyncResult<TModel> result) {
        if (!_networkDetector.isNetworkAvailable()) {
            ToastUtils.longToast(ERROR_NOT_CONNECTED);
            result.error(ERROR_NOT_CONNECTED);
            return;
        }
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TModel remoteModel = _remoteRepository.delete(url, model);
                    result.success(remoteModel);
                } catch (Exception ex) {
                    Log.d("exception", ex.getMessage());
                    ex.printStackTrace();
                    if (ex.getMessage().equals(Constants.NULL_OBJECT_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else if (ex.getMessage().equals(Constants.TOO_MANY_REQUEST_ERROR)) {
                        Log.d("exception", ex.getMessage());
                    } else {
                        result.error(ex.getMessage());
                    }
                }
            }
        })).start();
    }

    public class ModelResponse implements IAsyncResponse<TModel> {
        boolean _isBusy;
        String _error;
        TModel _model;

        @Override
        public boolean isBusy() {
            return _isBusy;
        }

        @Override
        public boolean hasError() {
            return _error != null;
        }

        @Override
        public String getError() {
            return _error;
        }

        @Override
        public void waitForResult() {
            int i = 0;
            while (_isBusy) {
                i++;
                System.out.println(i);
            }
        }

        @Override
        public TModel getResult() {
            return _model;
        }
    }

    private class PageResponse implements IAsyncResponse<Page<TModel>> {
        boolean _isBusy;
        String _error;
        Page<TModel> _page;

        @Override
        public boolean isBusy() {
            return _isBusy;
        }

        @Override
        public boolean hasError() {
            return _error != null;
        }

        @Override
        public String getError() {
            return _error;
        }

        @Override
        public void waitForResult() {

        }

        @Override
        public Page<TModel> getResult() {
            return _page;
        }
    }
}