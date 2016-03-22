package com.aksantos.dcuocensus.models.enums;

public enum Alignment {
    Hero(2330),
    Villain(2331);
    
    private int id;
    
    private Alignment(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public static Alignment getById(long id) {
        Alignment retval = null;
        for (Alignment value : Alignment.values()) {
            if (value.getId() == id) {
                retval = value;
                break;
            }
        }
        return retval;
    }    
}
