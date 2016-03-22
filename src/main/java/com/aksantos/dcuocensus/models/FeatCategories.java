package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatCategories {
    private List<FeatCategory> featCategoryList;
    private long returned;

    public FeatCategories() {
        featCategoryList = new ArrayList<FeatCategory>();
    }

    @JsonProperty("feat_category_list")
    public List<FeatCategory> getFeatCategoryList() {
        return featCategoryList;
    }

    public void setFeatCategoryList(List<FeatCategory> featCategoryList) {
        this.featCategoryList = featCategoryList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }
}
