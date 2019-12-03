package com.fit_with_friends.common.repositories;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import com.fit_with_friends.BuildConfig;
import com.fit_with_friends.common.contracts.tool.*;
import com.fit_with_friends.common.helpers.DateHelper;
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel;
import com.fit_with_friends.fitWithFriends.impl.models.UserModel;
import com.fit_with_friends.fitWithFriends.utils.CommonMethods;
import com.fit_with_friends.fitWithFriends.utils.Constants;
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public class RemoteRepository<TModel extends IModel> {
    private final Gson _gson;
    private final Type _dataType;
    private final Type _pageType;
    private final MediaType _mediaType = MediaType.parse("application/json; charset=utf-8");
    private final Context _context;
    private final String _key;
    private final String _url;
    private String myVersionName;
    private OkHttpClient _httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public RemoteRepository(Context context, String key, Type pageType, Type dataType) {
        _dataType = dataType;
        _gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateHelper()).create();
        _context = context;
        _key = key;
        _pageType = pageType;
        _url = Constants.BASE_URL + "/" + _key;
        myVersionName = BuildConfig.VERSION_NAME;
        Log.d("version name", myVersionName);
    }

    public String rawPost(String data) {
        return rawPost(null, null, data);
    }

    public String rawPost(String url, String data, String userId, String action) {
        Log.i("POST:START", url);
        Log.d("POST:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public String rawPost(String userId, String action, String data) {
        String url = _url;
        if (action != null) {
            url = _url + "/" + action;
        }
        Log.i("POST:START", url);
        Log.d("POST:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public TModel get(String url, String id, String userId, PageInput input) throws RemoteException {
        String response = getRaw(url, id, userId, input);
        DataModel<TModel> responseData = _gson.fromJson(response, _dataType);
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public String getRaw(String url, String action, String userId, PageInput input) {
        return remoteGet(url, input.getParams(), userId);
    }

    private String rawPut(String url, String data, String userId) {
        Log.i("PUT:START", url);
        Log.d("PUT:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("PUT:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public String delete(Long id, String userId) {
        String url = _url + "/" + id;
        Log.i("DELETE:START", url);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("x-app-version", "android-" + myVersionName)
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("DELETE:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public String delete(String url, String userId) {
        Log.i("DELETE:START", url);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .delete()
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("DELETE:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public TModel delete(String url, TModel model) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawDelete(url, data);
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public String rawDelete(String url, String data) {
        Log.i("DELETE:START", url);
        Log.d("DELETE:DATA", data);

        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("DELETE:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public String delete(String url, String userId, PageInput input) {
        return remoteDelete(url, userId, input.getParams());
    }

    private String remoteDelete(String url, String userId, Hashtable<String, String> params) {
        String query = "";
        if (params != null) {
            Uri.Builder uriBuilder = new Uri.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                assert value != null;
                if (!value.equals(""))
                    uriBuilder.appendQueryParameter(key, value);
            }
            query = uriBuilder.build().getEncodedQuery();
        }
        assert query != null;
        String getUrl = url + (query.equals("") ? "" : "?" + query);
        Log.i("DELETE:START", getUrl);
        try {
            Request request = new Request.Builder()
                    .url(getUrl)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .delete()
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("DELETE:DATA[" + getUrl + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    private String remoteGet(Hashtable<String, String> params, String url, String userId) {
        return remoteGet(url, params, userId);
    }

    private String remoteGet(Hashtable<String, String> params, String userId) {
        return remoteGet(_url, params, userId);
    }

    private String remoteGet(String url, Hashtable<String, String> params, String userId) {
        String query = "";
        if (params != null) {
            Uri.Builder uriBuilder = new Uri.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                assert value != null;
                if (!value.equals(""))
                    uriBuilder.appendQueryParameter(key, value);
            }
            query = uriBuilder.build().getEncodedQuery();
        }
        assert query != null;
        String getUrl = url + (query.equals("") ? "" : "?" + query);
        Log.i("GET:START", getUrl);
        try {
            Request request = new Request.Builder()
                    .url(getUrl)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String body = response.body().string();
            Log.d("GET:DATA[" + getUrl + "]", body);
            return body;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public TModel get(String id) throws RemoteException {
        return get(id, null);
    }

    public TModel get(String id, String action) throws RemoteException {
        return get(id, action, null);
    }

    public TModel get(String id, String action, String userId) throws RemoteException {
        String response = getRaw(id, action, userId);
        DataModel<TModel> responseData = _gson.fromJson(response, _dataType);
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public String getRaw(String id) {
        return getRaw(id, null);
    }

    public String getRaw(String id, String action, String userId) {
        return action == null ?
                remoteGet(_url + "/" + id, null, userId) :
                remoteGet(_url + "/" + action + "/" + id, null, userId);
    }

    public String getRaw(String id, String action) {
        return getRaw(id, action, null);
    }

    public Page<TModel> page(PageInput input) throws RemoteException {
        return page(input, _key);
    }

    public Page<TModel> page(PageInput input, String action) throws RemoteException {
        String response = pageRaw(input, action, " ");
        Page<TModel> responseData = _gson.fromJson(response, _pageType);
        responseData.Total = (long) responseData.body.size();
        if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData;
        }
        throw new RemoteException(responseData.getError());
    }

    public Page<TModel> page(String url, PageInput input) throws RemoteException {
        return page(url, input, _key);
    }

    public PagePagination<TModel> page(String url, String action, PageInput input, String userId) throws RemoteException {
        String response = pageRaw(input, url, userId);
        PagePagination<TModel> responseData = _gson.fromJson(response, _pageType);
        responseData.body.total = (long) responseData.body.data.size();
        if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData;
        }
        throw new RemoteException(responseData.getError());
    }

    public Page<TModel> page(String url, PageInput input, String userId) throws RemoteException {
        String response = pageRaw(input, url, userId);
        Page<TModel> responseData = _gson.fromJson(response, _pageType);
        responseData.Total = (long) responseData.body.size();
        if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData;
        }
        throw new RemoteException(responseData.getError());
    }

    public String pageRaw(PageInput input, String url, String userId) {
        return remoteGet(input.getParams(), url, userId);
    }

    public TModel update(String url, TModel model, String userId) throws RemoteException {
        String data = _gson.toJson(model);
        DataModel<TModel> responseData = _gson.fromJson(rawPut(url, data, userId), _dataType);
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public TModel update(TModel model, String action, String userId) throws RemoteException {
        String data = _gson.toJson(model);
        DataModel<TModel> responseData;
        responseData = _gson.fromJson(rawPut(action, data, userId), _dataType);
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public TModel update(String url, String action, PageInput input, TModel model) throws RemoteException {
        String data = _gson.toJson(model);
        String response = rawUpdate(url, action, input.getParams(), data);
        DataModel<TModel> responseData = _gson.fromJson(response, _dataType);
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    private String rawUpdate(String url, String action, Hashtable<String, String> params, String data) {
        String query = "";
        if (params != null) {
            Uri.Builder uriBuilder = new Uri.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                assert value != null;
                if (!value.equals(""))
                    uriBuilder.appendQueryParameter(key, value);
            }
            query = uriBuilder.build().getEncodedQuery();
        }
        assert query != null;
        String getUrl = url + (query.equals("") ? "" : "?" + query);
        Log.i("PUT:START", getUrl);
        Log.d("PUT:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data);
            Request request = new Request.Builder()
                    .url(getUrl)
                    .put(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("PUT:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }


    public TModel create(TModel model, String userId) throws RemoteException {
        return create("", model, userId);
    }

    public TModel initialCreation(String url, String userId, TModel model) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPost(url, data);
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public TModel create(String url, TModel model) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPost(url, data);
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    private String rawPost(String url, String data) {
        Log.i("POST:START", url);
        Log.d("POST:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public TModel create(String url, TModel model, String userId) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPost(url, data, userId, "");
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public Page<TModel> create(String url, Page<TModel> model, String userId) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPost(url, data, userId, "");
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.page;
        } else if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.page;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.page;
        }
        throw new RemoteException(responseData.getError());
    }

    public Page<TModel> create(String url, String userId, TModel model, PageInput input) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPostQuery(url, data, input.getParams());
        Page<TModel> responseData = _gson.fromJson(responseJson, _pageType);
        responseData.Total = (long) responseData.body.size();
        if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData;
        }
        throw new RemoteException(responseData.getError());
    }

    public TModel create(String url, TModel model, PageInput input) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPostQuery(url, data, input.getParams());
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    public TModel create(String url, TModel model, String action, PageInput input, String filePath) throws RemoteException {
        String data = _gson.toJson(model);
        String responseJson = rawPostUpload(url, data, action, input.getParams(), filePath);
        DataModel<TModel> responseData = _gson.fromJson(responseJson, _dataType);
        assert responseData != null;
        if (responseData.message.equals(Constants.UNAUTHENTICATED)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.code.equals(Constants.BLOCK_CODE)) {
            CommonMethods.Companion.startNextActivity(_context);
            return responseData.body;
        } else if (responseData.success || responseData.code.equals(Constants.SUCCESS_CODE)) {
            return responseData.body;
        }
        throw new RemoteException(responseData.getError());
    }

    private String rawPostUpload(String url, String data, String action, Hashtable<String, String> params, String filePath) {
        String query = "";
        if (params != null) {
            Uri.Builder uriBuilder = new Uri.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                assert value != null;
                if (!value.equals(""))
                    uriBuilder.appendQueryParameter(key, value);
            }
            query = uriBuilder.build().getEncodedQuery();
        }
        assert query != null;
        String getUrl = url + (query.equals("") ? "" : "?" + query);
        Log.i("POST:START", getUrl);
        Log.d("POST:DATA", data);
        try {
            File file = new File(filePath);
            MediaType mediaType = null;
            if (!filePath.isEmpty()) {
                if (filePath.endsWith("png"))
                    mediaType = MediaType.parse("image/png");
                else
                    mediaType = MediaType.parse("image/jpeg");
            }
            RequestBody body = null;
            switch (action) {
                case Constants.CREATE_CHALLENGE:
                    ChallengeModel challengeModel = _gson.fromJson(data, ChallengeModel.class);
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(mediaType, file))
                            .addFormDataPart("name", challengeModel.getName())
                            .addFormDataPart("add_friend", challengeModel.getAdd_friend())
                            .addFormDataPart("description", challengeModel.getDescription())
                            .addFormDataPart("start_date", challengeModel.getStart_date())
                            .addFormDataPart("end_date", challengeModel.getEnd_date())
                            .addFormDataPart("weight", challengeModel.getWeight())
                            .addFormDataPart("weight_type", challengeModel.getWeight_type())
                            .addFormDataPart("security_key", Constants.SECURITY_KEY_VALUE)
                            .build();
                    break;
                case Constants.EDIT_CHALLENGE:
                    ChallengeModel challengeModelEdit = _gson.fromJson(data, ChallengeModel.class);
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(mediaType, file))
                            .addFormDataPart("name", challengeModelEdit.getName())
                            .addFormDataPart("add_friend", challengeModelEdit.getAdd_friend())
                            .addFormDataPart("description", challengeModelEdit.getDescription())
                            .addFormDataPart("start_date", challengeModelEdit.getStart_date())
                            .addFormDataPart("end_date", challengeModelEdit.getEnd_date())
                            .addFormDataPart("weight", challengeModelEdit.getWeight())
                            .addFormDataPart("weight_type", challengeModelEdit.getWeight_type())
                            .addFormDataPart("challenge_id", challengeModelEdit.getChallenge_id())
                            .addFormDataPart("security_key", Constants.SECURITY_KEY_VALUE)
                            .build();
                    break;
                case Constants.EDIT_PROFILE: {
                    UserModel userModel = _gson.fromJson(data, UserModel.class);
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(mediaType, file))
                            .addFormDataPart("dob", userModel.getDob())
                            .addFormDataPart("gender", userModel.getGender())
                            .addFormDataPart("height", userModel.getHeight())
                            .addFormDataPart("name", userModel.getName())
                            .addFormDataPart("weight", userModel.getWeight())
                            .addFormDataPart("weight_type", userModel.getWeight_type())
                            .addFormDataPart("height_type", userModel.getHeight_type())
                            .addFormDataPart("security_key", Constants.SECURITY_KEY_VALUE)
                            .build();
                    break;
                }
                case Constants.REGISTER: {
                    UserModel userModel = _gson.fromJson(data, UserModel.class);
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(mediaType, file))
                            .addFormDataPart("name", userModel.getName())
                            .addFormDataPart("email", userModel.getEmail())
                            .addFormDataPart("password", userModel.getPassword())
                            .addFormDataPart("confirmPassword", userModel.getConfirmPassword())
                            .addFormDataPart("dob", userModel.getDob())
                            .addFormDataPart("weight", userModel.getWeight())
                            .addFormDataPart("height", userModel.getHeight())
                            .addFormDataPart("weight_type", userModel.getWeight_type())
                            .addFormDataPart("height_type", userModel.getHeight_type())
                            .addFormDataPart("gender", userModel.getGender())
                            .addFormDataPart("device_type", userModel.getDevice_type())
                            .addFormDataPart("device_token", userModel.getDevice_token())
                            .addFormDataPart("security_key", Constants.SECURITY_KEY_VALUE)
                            .build();
                    break;
                }
                case Constants.ADD_WEIGHT: {
                    UserModel userModel = _gson.fromJson(data, UserModel.class);
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(mediaType, file))
                            .addFormDataPart("weight", userModel.getWeight())
                            .addFormDataPart("weight_type", userModel.getWeight_type())
                            .addFormDataPart("date", userModel.getDate())
                            .addFormDataPart("security_key", Constants.SECURITY_KEY_VALUE)
                            .build();
                    break;
                }
            }
            Request request = new Request.Builder()
                    .url(getUrl)
                    .post(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    private String rawPostQuery(String url, String data, Hashtable<String, String> params) {
        String query = "";
        if (params != null) {
            Uri.Builder uriBuilder = new Uri.Builder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                assert value != null;
                if (!value.equals(""))
                    uriBuilder.appendQueryParameter(key, value);
            }
            query = uriBuilder.build().getEncodedQuery();
        }
        assert query != null;
        String getUrl = url + (query.equals("") ? "" : "?" + query);
        Log.i("POST:START", getUrl);
        Log.d("POST:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(getUrl)
                    .post(body)
                    .addHeader(Constants.AUTHORIZATION, Constants.BEARER + PreferenceHandler.readString(_context, Constants.TOKEN, ""))
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }

    public String create(String url, String data, String userId) {
        Log.i("POST:START", url);
        Log.d("POST:DATA", data);
        try {
            RequestBody body = RequestBody.create(_mediaType, data == null ? "" : data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader(Constants.SECURITY_KEY, Constants.SECURITY_KEY_VALUE)
                    .addHeader(Constants.ACCEPT, Constants.TYPE)
                    .build();
            Response response = _httpClient.newCall(request).execute();
            assert response.body() != null;
            String dataBody = response.body().string();
            Log.d("POST:DATA[" + url + "]", dataBody);
            return dataBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteData error = new RemoteData();
            error.success = false;
            error.error = ex.getMessage();
            return _gson.toJson(error);
        }
    }
}