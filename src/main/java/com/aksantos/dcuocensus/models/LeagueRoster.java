package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueRoster extends Type {
    private static final long serialVersionUID = 371660094113238136L;

    private long worldId;
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
        return super.getId();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (characterId ^ (characterId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeagueRoster other = (LeagueRoster) obj;
        if (characterId != other.characterId)
            return false;
        return true;
    }

}
