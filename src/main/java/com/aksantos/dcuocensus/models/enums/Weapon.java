package com.aksantos.dcuocensus.models.enums;

public enum Weapon {
    DualWield(3316, "Dual Wield"),
    TwoHanded(3312, "Two-Handed"),
    HandBlast(503870, "Hand Blast"),
    OneHanded(4498, "One-Handed"),
    Staff(4521, "Staff"),
    MartialArts(2336, "Martial Arts"),
    Bow(3314, "Bow"),
    Brawling(17740, "Brawling"),
    Rifle(9111, "Rifle"),
    DualPistol(3315, "Dual Pistol"),
    Shield(1479215, "Shield");
    
    private int id;
    private String name;
    
    private Weapon(int id, String name) {
        this.id = id;
        this.name= name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static Weapon getById(long id) {
        Weapon retval = null;
        for (Weapon value : Weapon.values()) {
            if (value.getId() == id) {
                retval = value;
                break;
            }
        }
        return retval;
    }    
    
    public String toString() {
        return name;
    }
}
