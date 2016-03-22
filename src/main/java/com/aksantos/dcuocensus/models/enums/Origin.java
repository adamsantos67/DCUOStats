package com.aksantos.dcuocensus.models.enums;

public enum Origin {
    Meta(21783),
    Tech(21784),
    Magic(21785);
    
    private long id;
    
    private Origin(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
    public static Origin getById(long id) {
        Origin retval = null;
        for (Origin origin : Origin.values()) {
            if (origin.getId() == id) {
                retval = origin;
                break;
            }
        }
        return retval;
    }
}
