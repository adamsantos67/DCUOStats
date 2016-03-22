package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Characters {
    private List<Character> characterList;
    private long returned;

    public Characters() {
        characterList = new ArrayList<Character>();
    }

    @JsonProperty("character_list")
    public List<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
