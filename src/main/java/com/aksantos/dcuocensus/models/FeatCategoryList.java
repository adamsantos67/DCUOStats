package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatCategoryList extends TypeHolder<FeatCategory> {
    @JsonProperty("feat_category_list")
    public List<FeatCategory> getObjectList() {
        return super.getObjectList();
    }
}
