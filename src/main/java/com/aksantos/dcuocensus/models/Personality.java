package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Personality extends Type {
    private Name name;

    @JsonProperty("personality_id")
    public long getId() {
        return super.getId();
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
    
    public String getNameEn() {
        return name == null ? "" : name.getEn();
    }
    
}
