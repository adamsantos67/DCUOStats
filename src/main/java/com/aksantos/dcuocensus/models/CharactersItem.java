package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharactersItem implements Comparable<CharactersItem> {
    private long characterId;
    private int worldId;
    private int databaseId;
    private long itemId;
    private int equipmentSlotId;
    private int quantity;
    private boolean bound;
    private int durability;
    private int itemNumber;
    private long augItemId1;
    private long augItemId2;
    private long augItemId3;

    @JsonProperty("character_id")
    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    @JsonProperty("world_id")
    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    @JsonProperty("database_id")
    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    @JsonProperty("item_id")
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("equipment_slot_id")
    public int getEquipmentSlotId() {
        return equipmentSlotId;
    }

    public void setEquipmentSlotId(int equipmentSlotId) {
        this.equipmentSlotId = equipmentSlotId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("is_bound")
    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getItem_number() {
        return itemNumber;
    }

    public void setItem_number(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    @JsonProperty("aug_item_id_1")
    public long getAugItemId1() {
        return augItemId1;
    }

    public void setAugItemId1(long augItemId1) {
        this.augItemId1 = augItemId1;
    }

    @JsonProperty("aug_item_id_2")
    public long getAugItemId2() {
        return augItemId2;
    }

    public void setAugItemId2(long augItemId2) {
        this.augItemId2 = augItemId2;
    }

    @JsonProperty("aug_item_id_3")
    public long getAugItemId3() {
        return augItemId3;
    }

    public void setAugItemId3(long augItemId3) {
        this.augItemId3 = augItemId3;
    }

    @Override
    public int compareTo(CharactersItem charItem) {
        int ret = this.getEquipmentSlotId() - charItem.getEquipmentSlotId();
        if (ret == 0) {
            ret = (int) (this.getItemId() - charItem.getItemId());
        }
        return ret;
    }
}
