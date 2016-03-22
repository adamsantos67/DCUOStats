package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersItems {
    private List<CharactersItem> charactersItemList;
    private long returned;

    @JsonProperty("characters_item_list")
    public List<CharactersItem> getCharactersItemList() {
        return charactersItemList;
    }

    public void setCharactersItemList(List<CharactersItem> charactersItemList) {
        this.charactersItemList = charactersItemList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
