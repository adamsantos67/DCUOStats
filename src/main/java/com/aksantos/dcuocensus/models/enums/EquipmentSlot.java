package com.aksantos.dcuocensus.models.enums;

public enum EquipmentSlot {
    Head(0),
    Neck(1),
    
    Shoulders(3),
    Back(4),
    Hands(5),
    Waist(6),
    Feet(7),
    
    Face(9),
    Chest(10),
    Legs(11),
    Ring1(12),
    Ring2(13),
    Consumable(14),
    Trinket(15),
    
    Weapon(17),
    Slot1(18),
    Slot2(19),
    Slot3(20),
    Slot4(21);
    
    private final int id;
    
    EquipmentSlot(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static EquipmentSlot getById(int id) {
        EquipmentSlot ret = null;
        for (EquipmentSlot slot: values()) {
            if (slot.getId() == id) {
                ret = slot;
                break;
            }
        }
        return ret;
    }
}
