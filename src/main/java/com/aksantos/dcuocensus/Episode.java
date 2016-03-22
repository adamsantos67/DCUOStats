package com.aksantos.dcuocensus;

import java.util.List;

public class Episode {
    private int number;
    private String name;
    private List<String> missions;

    public Episode(int number, String name, List<String> missions) {
        super();
        this.number = number;
        this.name = name;
        this.missions = missions;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMissions() {
        return missions;
    }

    public void setMissions(List<String> missions) {
        this.missions = missions;
    }
}
