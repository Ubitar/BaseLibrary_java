package com.common.network.model;

import com.common.network.NetworkManager;

public abstract class BaseModel<T> {

    protected T model;

    public BaseModel() {
        model = NetworkManager.getRequest(getApi());
    }

    protected abstract <A extends Class> A getApi();

}
