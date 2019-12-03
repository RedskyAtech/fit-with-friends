package com.fit_with_friends.common.contracts.tool;

public interface IAsyncResponse<TData> {
    boolean isBusy();

    boolean hasError();

    String getError();

    void waitForResult();

    TData getResult();
}


