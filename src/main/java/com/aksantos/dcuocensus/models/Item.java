package com.aksantos.dcuocensus.models;

import java.io.Serializable;

import com.aksantos.dcuocensus.models.enums.Alignment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item implements Serializable {
    private static final long serialVersionUID = -5090113400631636783L;

    private long id;
    private String codeName;
    private Name name;
    private int minLevel = 0;
    private int itemLevel = 0;
    private int charges = 0;
    private boolean magic = true;
    private boolean meta = true;
    private boolean tech = true;
    private long alignmentId = 0;
    private Alignment alignment = null;
    private boolean controller = true;
    private boolean tank = true;
    private boolean healer = true;
    private boolean male = true;
    private boolean female = true;
    private long categoryId = 0;
    private String category = "";
    private String subCategory = "";
    private int quality = 0;
    private long iconId = 0;
    private String imagePath = null;

    private boolean noTrade = false;
    private boolean noSale = false;

    private long saleValue = 0;
    private double dps = 0.0;
    private long health = 0;
    private long fatigue = 0;
    private long basicAttack = 0;
    private long finisherAttack = 0;
    private long movement = 0;
    private long mitigation = 0;
    private long resilience = 0;
    private long power = 0;
    private long heal = 0;
    private long powerHeal = 0;
    private long dominance = 0;

    private int manufacturer = 0;
    private int maxStackSize = 0;
    private boolean bindOnEquip = false;
    private boolean unique = false;
    private int itemType = 0;
    private int requiredCR = 0;
    private int pvpLevel = 0;
    private int socketColorId1 = 0;
    private int socketColorId2 = 0;
    private boolean currency = false;
    private long factionId = 0;

    @JsonProperty("item_id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("code_name")
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getNameEn() {
        return name == null ? "" : name.getEn();
    }

    public void setNameEn(String name) {
        this.name = new Name();
        this.name.setEn(name);
    }

    @JsonProperty("minimum_level")
    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    @JsonProperty("item_level")
    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public boolean isMagic() {
        return magic;
    }

    public void setMagic(boolean magic) {
        this.magic = magic;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public boolean isTech() {
        return tech;
    }

    public void setTech(boolean tech) {
        this.tech = tech;
    }

    @JsonProperty("alignment_id")
    public long getAlignmentId() {
        return alignmentId;
    }

    public void setAlignmentId(long alignmentId) {
        this.alignmentId = alignmentId;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public boolean isTank() {
        return tank;
    }

    public void setTank(boolean tank) {
        this.tank = tank;
    }

    public boolean isHealer() {
        return healer;
    }

    public void setHealer(boolean healer) {
        this.healer = healer;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    @JsonProperty("item_category_id")
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long itemCategoryId) {
        this.categoryId = itemCategoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    @JsonProperty("icon_id")
    public long getIconId() {
        return iconId;
    }

    public void setIconId(long iconId) {
        this.iconId = iconId;
    }

    @JsonProperty("image_path")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @JsonProperty("no_trade")
    public boolean isNoTrade() {
        return noTrade;
    }

    public void setNoTrade(boolean noTrade) {
        this.noTrade = noTrade;
    }

    @JsonProperty("no_sale")
    public boolean isNoSale() {
        return noSale;
    }

    public void setNoSale(boolean noSale) {
        this.noSale = noSale;
    }

    @JsonProperty("sale_value")
    public long getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(long saleValue) {
        this.saleValue = saleValue;
    }

    public double getDps() {
        return dps;
    }

    public void setDps(double dps) {
        this.dps = dps;
    }

    @JsonProperty("stat_health_pool")
    public long getHealth() {
        return health;
    }

    public void setHealth(long health) {
        this.health = health;
    }

    @JsonProperty("stat_fatigue_pool")
    public long getFatigue() {
        return fatigue;
    }

    public void setFatigue(long fatigue) {
        this.fatigue = fatigue;
    }

    @JsonProperty("stat_basic_attack")
    public long getBasicAttack() {
        return basicAttack;
    }

    public void setBasicAttack(long basicAttack) {
        this.basicAttack = basicAttack;
    }

    @JsonProperty("stat_finisher_attack")
    public long getFinisherAttack() {
        return finisherAttack;
    }

    public void setFinisherAttack(long finisherAttack) {
        this.finisherAttack = finisherAttack;
    }

    @JsonProperty("stat_movement")
    public long getMovement() {
        return movement;
    }

    public void setMovement(long movement) {
        this.movement = movement;
    }

    @JsonProperty("stat_mitigation")
    public long getMitigation() {
        return mitigation;
    }

    public void setMitigation(long mitigation) {
        this.mitigation = mitigation;
    }

    @JsonProperty("stat_resilience")
    public long getResilience() {
        return resilience;
    }

    public void setResilience(long resilience) {
        this.resilience = resilience;
    }

    @JsonProperty("stat_power_pool")
    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    @JsonProperty("stat_heal")
    public long getHeal() {
        return heal;
    }

    public void setHeal(long heal) {
        this.heal = heal;
    }

    @JsonProperty("stat_power_heal")
    public long getPowerHeal() {
        return powerHeal;
    }

    public void setPowerHeal(long powerHeal) {
        this.powerHeal = powerHeal;
    }

    @JsonProperty("stat_dominance")
    public long getDominance() {
        return dominance;
    }

    public void setDominance(long dominance) {
        this.dominance = dominance;
    }

    public int getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(int manufacturer) {
        this.manufacturer = manufacturer;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @JsonProperty("max_stack_size")
    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @JsonProperty("bind_on_equip")
    public boolean isBindOnEquip() {
        return bindOnEquip;
    }

    public void setBindOnEquip(boolean bindOnEquip) {
        this.bindOnEquip = bindOnEquip;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @JsonProperty("item_type")
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @JsonProperty("required_combat_rating")
    public int getRequiredCR() {
        return requiredCR;
    }

    public void setRequiredCR(int requiredCR) {
        this.requiredCR = requiredCR;
    }

    @JsonProperty("pvp_level")
    public int getPvpLevel() {
        return pvpLevel;
    }

    public void setPvpLevel(int pvpLevel) {
        this.pvpLevel = pvpLevel;
    }

    @JsonProperty("aug_socket_color_id_1")
    public int getSocketColorId1() {
        return socketColorId1;
    }

    public void setSocketColorId1(int socketColorId1) {
        this.socketColorId1 = socketColorId1;
    }

    @JsonProperty("aug_socket_color_id_2")
    public int getSocketColorId2() {
        return socketColorId2;
    }

    public void setSocketColorId2(int socketColorId2) {
        this.socketColorId2 = socketColorId2;
    }

    @JsonProperty("is_currency")
    public boolean isCurrency() {
        return currency;
    }

    public void setCurrency(boolean currency) {
        this.currency = currency;
    }

    @JsonProperty("faction_id")
    public long getFactionId() {
        return factionId;
    }

    public void setFactionId(long factionId) {
        this.factionId = factionId;
    }
}
