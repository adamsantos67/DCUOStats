package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class League  extends Type {
    private static final long serialVersionUID = -3769961501832352388L;

    private long id;
    private long worldId;
    private String name;
    private String lowerName;
    private long characterAlignmentId;

    @JsonProperty("guild_id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("world_id")
    public long getWorldId() {
        return worldId;
    }

    public void setWorldId(long worldId) {
        this.worldId = worldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("lower_name")
    public String getLowerName() {
        return lowerName;
    }

    public void setLowerName(String lowerName) {
        this.lowerName = lowerName;
    }

    @JsonProperty("character_alignment_id")
    public long getCharacterAlignmentId() {
        return characterAlignmentId;
    }

    public void setCharacterAlignmentId(long characterAlignmentId) {
        this.characterAlignmentId = characterAlignmentId;
    }

}
