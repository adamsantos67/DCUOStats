package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemList extends TypeHolder<Item> {
    @JsonProperty("item_list")
    public List<Item> getItem_list() {
        return super.getObjectList();
    }
}
