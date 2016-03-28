package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonalityList extends TypeHolder<Personality> {
    @JsonProperty("personality_list")
    public List<Personality> getObjectList() {
        return super.getObjectList();
    }
}
