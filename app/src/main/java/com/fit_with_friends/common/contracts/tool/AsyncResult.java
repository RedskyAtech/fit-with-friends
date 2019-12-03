package com.fit_with_friends.common.contracts.tool;

public interface AsyncResult<TData> {
    void success(TData data);

    void error(String error);
}

