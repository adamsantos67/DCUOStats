package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueList extends TypeHolder<League> {
    @JsonProperty("guild_list")
    public List<League> getItem_list() {
        return super.getObjectList();
    }

}
