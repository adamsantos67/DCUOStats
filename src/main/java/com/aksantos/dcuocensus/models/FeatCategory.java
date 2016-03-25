package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatCategory extends Type {
    private static final long serialVersionUID = -2437889911114680056L;

    private Name name;
    private long parentId = 0;
    private int order = 0;

    @JsonProperty("feat_category_id")
    public long getId() {
        return super.getId();
    }

    @JsonProperty("category_name")
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @JsonProperty("parent_category_id")
    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("ui_order_number")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
