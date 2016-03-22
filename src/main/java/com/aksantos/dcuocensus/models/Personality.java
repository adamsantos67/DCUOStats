package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Personality {
    private long id;
    private Name name;

    @JsonProperty("personality_id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
