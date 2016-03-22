package com.aksantos.dcuocensus.models.enums;

public enum Power {
    Fire(2666, Role.Tank), 
    Ice(2324, Role.Tank), 
    Earth(2784, Role.Tank), 
    Rage(1992462, Role.Tank), 
    Atomic(6902, Role.Tank),
    Gadgets(175798, Role.Controller), 
    Mental(7019, Role.Controller), 
    Light(2667, Role.Controller), 
    Quantum(1810455, Role.Controller), 
    Munitions(2636096, Role.Controller),
    Nature(74779, Role.Healer), 
    Sorcery(197247,Role.Healer), 
    Electricity(2325, Role.Healer), 
    Celestial(1932154, Role.Healer), 
    Water(1, Role.Healer);
    
    private final long id;
    private final Role role;
    
    private Power(long id, Role role) {
        this.role = role;
        this.id = id;
    }
    
    public Role getRole() {
        return role;
    }
    
    public long getId() {
        return id;
    }
    
    public static Power getById(long id) {
        Power retval = null;
        for (Power value : Power.values()) {
            if (value.getId() == id) {
                retval = value;
                break;
            }
        }
        return retval;
    }    
}
