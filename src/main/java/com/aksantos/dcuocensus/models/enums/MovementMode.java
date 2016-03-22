package com.aksantos.dcuocensus.models.enums;

public enum MovementMode {
    Acrobat(3527, "Acrobatics"),
    Flight(3313, "Flight"),
    Speed(3317, "Super-Speed");
    
    private int id;
    private String name;
    
    private MovementMode(int id, String altName) {
        this.id = id;
        this.name = altName;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public static MovementMode getById(long id) {
        MovementMode retval = null;
        for (MovementMode value : MovementMode.values()) {
            if (value.getId() == id) {
                retval = value;
                break;
            }
        }
        return retval;
    }    
}
