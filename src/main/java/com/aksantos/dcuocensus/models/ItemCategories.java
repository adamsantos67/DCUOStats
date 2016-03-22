package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCategories {
    private List<ItemCategory> itemCategoryList;
    private long returned;

    public ItemCategories() {
        itemCategoryList = new ArrayList<ItemCategory>();
    }

    @JsonProperty("item_category_list")
    public List<ItemCategory> getItemCategoryList() {
        return itemCategoryList;
    }

    public void setItemCategoryList(List<ItemCategory> itemCategoryList) {
        this.itemCategoryList = itemCategoryList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }
}
