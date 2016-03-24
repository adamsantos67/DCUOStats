package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersItems extends TypeHolder<CharactersItem> {
    @JsonProperty("characters_item_list")
    public List<CharactersItem> getObjectList() {
        return super.getObjectList();
    }
}
