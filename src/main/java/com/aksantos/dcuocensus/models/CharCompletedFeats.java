package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharCompletedFeats {
    private List<CharCompletedFeat> featList;
    private long returned;

    @JsonProperty("characters_completed_feat_list")
    public List<CharCompletedFeat> getFeatList() {
        return featList;
    }

    public void setFeatList(List<CharCompletedFeat> featList) {
        this.featList = featList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
