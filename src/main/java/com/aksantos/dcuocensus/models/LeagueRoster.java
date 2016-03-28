package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueRoster extends Type {
    private static final long serialVersionUID = 371660094113238136L;

    private long worldId;
    private long id;
    private long characterId;
    private int rank;

    @JsonProperty("world_id")
    public long getWorldId() {
        return worldId;
    }

    public void setWorldId(long worldId) {
        this.worldId = worldId;
    }

    @JsonProperty("guild_id")
    public long getId() {
        return id;
    }

    public void setId(long leagueId) {
        this.id = leagueId;
    }

    @JsonProperty("character_id")
    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}
