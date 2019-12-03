package com.fit_with_friends.common.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public abstract class BaseLoaderFragment<T> extends BaseV4Fragment implements LoaderManager.LoaderCallbacks<T> {

    private Loader<T> loader;

    protected void initDataLoader(int id) {
        getLoaderManager().initLoader(id, null, this);
    }

    protected abstract Loader<T> createDataLoader();

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        loader = createDataLoader();
        return loader;
    }

    protected void onChangedData() {
        loader.onContentChanged();
    }

}
