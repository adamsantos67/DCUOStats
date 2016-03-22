package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Personalities {
    private List<Personality> personalityList;
    private long returned;

    public Personalities() {
        personalityList = new ArrayList<Personality>();
    }

    @JsonProperty("personality_list")
    public List<Personality> getPersonalityList() {
        return personalityList;
    }

    public void setPersonalityList(List<Personality> personalityList) {
        this.personalityList = personalityList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
