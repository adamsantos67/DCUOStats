package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feats {
    private List<Feat> featList;
    private long returned;

    public Feats() {
        featList = new ArrayList<Feat>();
    }
    
    @JsonProperty("feat_list")
    public List<Feat> getFeatList() {
        return featList;
    }

    public void setFeatList(List<Feat> feat_list) {
        this.featList = feat_list;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Feats [feat_list=" + featList + ", returned=" + returned + "]";
    }
}
