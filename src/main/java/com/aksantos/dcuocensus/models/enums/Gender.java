package com.aksantos.dcuocensus.models.enums;

public enum Gender {
    male(0), 
    female(1);
    
    private int id;
    
    private Gender(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static Gender getById(long id) {
        Gender retval = null;
        for (Gender value : Gender.values()) {
            if (value.getId() == id) {
                retval = value;
                break;
            }
        }
        return retval;
    }    
    
}
