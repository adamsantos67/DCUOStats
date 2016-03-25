package com.aksantos.dcuocensus.models;

import java.io.Serializable;

public abstract class Type implements Serializable {
    private static final long serialVersionUID = 1374830829973928458L;

    private long id = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
