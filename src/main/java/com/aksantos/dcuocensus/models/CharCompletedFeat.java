package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharCompletedFeat {
    private long characterId;
    private long featId;

    @JsonProperty("character_id")
    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    @JsonProperty("feat_id")
    public long getFeatId() {
        return featId;
    }

    public void setFeatId(long featId) {
        this.featId = featId;
    }

}
