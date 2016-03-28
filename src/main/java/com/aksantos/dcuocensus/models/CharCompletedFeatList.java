package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharCompletedFeatList extends TypeHolder<CharCompletedFeat> {
    @JsonProperty("characters_completed_feat_list")
    public List<CharCompletedFeat> getObjectList() {
        return super.getObjectList();
    }
}
