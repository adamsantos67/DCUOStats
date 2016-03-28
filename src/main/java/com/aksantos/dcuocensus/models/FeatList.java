package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatList extends TypeHolder<Feat> {
    @JsonProperty("feat_list")
    public List<Feat> getObjectList() {
        return super.getObjectList();
    }
}
