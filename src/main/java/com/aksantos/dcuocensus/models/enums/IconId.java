package com.aksantos.dcuocensus.models.enums;

public enum IconId {
    EXPLORATION(109110),
    METROPOLIS(109111),
    GOTHAM(109112),
    EXPLORE_ALERTS(109113),
    STYLES(109115),
    ICONIC_STYLES(109119),
    RENOWN(109122),
    GENERAL(109123),
    COLLECTIBLES(109124),
    RACES(109125),
    SOLOS(109128),
    DUOS(109130),
    PVP(109132),
    PVP_LEGENDS(109134),
    RAIDS(109135),
    ICONIC_BATTLES(109136),
    ALERTS(109138),
    VALENTINES(109143),
    ST_PATRICKS(109144),
    SPRING(109148),
    DUELING(109156),
    FALL(109157),
    RND(109158),
    WINTER(109161),
    SUMMER(109168),
    BASES(109192),
    ICONIC_SOLO(109196);
    
    private final int id;
    
    private IconId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
