package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCategories extends TypeHolder<ItemCategory> {
    @JsonProperty("item_category_list")
    public List<ItemCategory> getObjectList() {
        return super.getObjectList();
    }
}
