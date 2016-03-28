package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueRosterList extends TypeHolder<LeagueRoster> {
    @JsonProperty("guild_roster_list")
    public List<LeagueRoster> getItem_list() {
        return super.getObjectList();
    }

}
