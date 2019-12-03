package com.fit_with_friends.common.contracts.tool;


import java.util.ArrayList;
import java.util.List;

public class Page<TModel> extends RemoteData {

    public Long pageNo;
    public Long Total = 0L;
    public List<TModel> body;
    private Boolean _isCached = false;
    private Boolean _isFinal = true;

    public Boolean isFinal() {
        return _isFinal;
    }

    public void setFinal() {
        _isFinal = true;
    }

    public Boolean isCached() {
        return _isCached;
    }

    public void setCached() {
        _isCached = true;
        _isFinal = false;
    }

    public Page() {
        body = new ArrayList<>();
    }
}