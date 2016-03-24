package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Characters extends TypeHolder<Character> {
    @JsonProperty("character_list")
    public List<Character> getObjectList() {
        return super.getObjectList();
    }
}
