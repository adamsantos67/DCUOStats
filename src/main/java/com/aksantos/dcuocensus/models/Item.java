package com.aksantos.dcuocensus.models;

import java.io.Serializable;

import com.aksantos.dcuocensus.models.enums.Alignment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item extends Type implements Serializable {
    private static final long serialVersionUID = 9123423321411964569L;

    private String codeName;
    private Name name;              // Name
    private int minLevel = 0;
    private int itemLevel = 0;      // Level
    private int charges = 0;
    private boolean magic = true;
    private boolean meta = true;
    private boolean tech = true;
    private long alignmentId = 0;  
    private Alignment alignment = null;  // Alignment
    private boolean controller = true;   // Role
    private boolean tank = true;         // Role
    private boolean healer = true;       // Role
    private boolean male = true;
    private boolean female = true;
    private long categoryId = 0;
    private ItemCategory category = null;
    private ItemCategory subCategory = null;
    private int quality = 0;
    private long iconId = 0;
    private String imagePath = null;

    private boolean noTrade = false;
    private boolean noSale = false;

    private long saleValue = 0;
    private double dps = 0.0;
    private long health = 0;
    private long power = 0;
    private long precision = 0;
    private long might = 0;
    private long movement = 0;
    private long defense = 0;
    private long resilience = 0;
    private long powerPool = 0;
    private long restoration = 0;
    private long vitalization = 0;
    private long dominance = 0;

    private int manufacturer = 0;
    private int maxStackSize = 0;
    private boolean bindOnEquip = false;
    private boolean unique = false;
    private int itemType = 0;
    private int requiredCR = 0;
    private int pvpLevel = 0;
    private long socketColorId1 = 0;
    private String socketColor1 = "";
    private long socketColorId2 = 0;
    private String socketColor2 = "";
    private boolean currency = false;
    private long factionId = 0;

    @JsonProperty("item_id")
    public long getId() {
        return super.getId();
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

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public ItemCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ItemCategory subCategory) {
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
    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    @JsonProperty("stat_basic_attack")
    public long getPrecision() {
        return precision;
    }

    public void setPrecision(long precision) {
        this.precision = precision;
    }

    @JsonProperty("stat_finisher_attack")
    public long getMight() {
        return might;
    }

    public void setMight(long might) {
        this.might = might;
    }

    @JsonProperty("stat_movement")
    public long getMovement() {
        return movement;
    }

    public void setMovement(long movement) {
        this.movement = movement;
    }

    @JsonProperty("stat_mitigation")
    public long getDefense() {
        return defense;
    }

    public void setDefense(long defense) {
        this.defense = defense;
    }

    @JsonProperty("stat_resilience")
    public long getResilience() {
        return resilience;
    }

    public void setResilience(long resilience) {
        this.resilience = resilience;
    }

    @JsonProperty("stat_power_pool")
    public long getPowerPool() {
        return powerPool;
    }

    public void setPowerPool(long powerPool) {
        this.powerPool = powerPool;
    }

    @JsonProperty("stat_heal")
    public long getRestoration() {
        return restoration;
    }

    public void setRestoration(long restoration) {
        this.restoration = restoration;
    }

    @JsonProperty("stat_power_heal")
    public long getVitalization() {
        return vitalization;
    }

    public void setVitalization(long vitalization) {
        this.vitalization = vitalization;
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
    public long getSocketColorId1() {
        return socketColorId1;
    }

    public void setSocketColorId1(long socketColorId1) {
        this.socketColorId1 = socketColorId1;
    }

    @JsonProperty("aug_socket_color_id_2")
    public long getSocketColorId2() {
        return socketColorId2;
    }

    public void setSocketColorId2(long socketColorId2) {
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

    public String getSocketColor1() {
        return socketColor1;
    }

    public void setSocketColor1(String socketColor1) {
        this.socketColor1 = socketColor1;
    }

    public String getSocketColor2() {
        return socketColor2;
    }

    public void setSocketColor2(String socketColor2) {
        this.socketColor2 = socketColor2;
    }
}
