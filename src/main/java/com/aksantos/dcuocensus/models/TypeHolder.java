package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeHolder<T> {
    private List<T> objectList;
    private long returned;

    public TypeHolder() {
        objectList = new ArrayList<T>();
    }

    public List<T> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
