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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Type other = (Type) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
