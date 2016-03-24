package com.aksantos.dcuocensus.models;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.Gender;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Power;
import com.aksantos.dcuocensus.models.enums.Role;
import com.aksantos.dcuocensus.models.enums.Weapon;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Character {
    private long id;
    private String name;
    private int level;
    private int combatRating;
    private int skillPoints;
    private int pvpCombatRating;
    private int genderId;
    private Gender gender;
    private int powerTypeId;
    private Power power;
    private Role role;
    private long movementModeId;
    private MovementMode movementMode;
    private long personalityId;
    private Personality personality;
    private long alignmentId;
    private Alignment alignment;
    private String world;
    private long originId;
    private Origin origin;
    private String mentor;
    private int powerSourceId;
    private Weapon weapon;
    private int maxFeats;
    private long currentHealth;
    private long currentPower;
    private long maxHealth;
    private long maxPower;
    private long defense;
    private long toughness;
    private long might;
    private long precision;
    private long restoration;
    private long vitalization;
    private long dominance;
    private long imageId;

    private int worldId;
    private int databaseId;
    private long regionId;
    private boolean active;
    private boolean deleted;
    private String hash;

    private Set<Long> completedFeats;
    private Set<Long> unlockableFeats = new TreeSet<Long>();

    private Map<Role, Set<Long>> unlockableRoleFeats = new TreeMap<Role, Set<Long>>();
    private Map<MovementMode, Set<Long>> unlockableMovementFeats = new TreeMap<MovementMode, Set<Long>>();

    private List<CharactersItem> characterItems;

    @JsonProperty("character_id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonProperty("combat_rating")
    public int getCombatRating() {
        return combatRating;
    }

    public void setCombatRating(int combatRating) {
        this.combatRating = combatRating;
    }

    @JsonProperty("skill_points")
    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    @JsonProperty("pvp_combat_rating")
    public int getPvpCombatRating() {
        return pvpCombatRating;
    }

    public void setPvpCombatRating(int pvpCombatRating) {
        this.pvpCombatRating = pvpCombatRating;
    }

    @JsonProperty("gender_id")
    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonProperty("movement_mode_id")
    public long getMovementModeId() {
        return movementModeId;
    }

    public void setMovementModeId(long movementModeId) {
        this.movementModeId = movementModeId;
    }

    public MovementMode getMovementMode() {
        return movementMode;
    }

    public void setMovementMode(MovementMode movementMode) {
        this.movementMode = movementMode;
    }

    public void setMovementModeStr(String movementMode) {
        for (MovementMode mode : MovementMode.values()) {
            if (mode.getName() != null && mode.getName().equals(movementMode)) {
                this.movementMode = mode;
                break;
            }
        }
    }

    @JsonProperty("personality_id")
    public long getPersonalityId() {
        return personalityId;
    }

    public void setPersonalityId(long personalityId) {
        this.personalityId = personalityId;
    }

    public Personality getPersonality() {
        return personality;
    }

    public void setPersonality(Personality personality) {
        this.personality = personality;
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

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    @JsonProperty("origin_id")
    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @JsonProperty("max_feats")
    public int getMaxFeats() {
        return maxFeats;
    }

    public void setMaxFeats(int maxFeats) {
        this.maxFeats = maxFeats;
    }

    @JsonProperty("current_health")
    public long getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(long currentHealth) {
        this.currentHealth = currentHealth;
    }

    @JsonProperty("current_power")
    public long getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(long currentPower) {
        this.currentPower = currentPower;
    }

    @JsonProperty("max_health")
    public long getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(long maxHealth) {
        this.maxHealth = maxHealth;
    }

    @JsonProperty("max_power")
    public long getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(long maxPower) {
        this.maxPower = maxPower;
    }

    public long getDefense() {
        return defense;
    }

    public void setDefense(long defense) {
        this.defense = defense;
    }

    public long getToughness() {
        return toughness;
    }

    public void setToughness(long toughness) {
        this.toughness = toughness;
    }

    public long getMight() {
        return might;
    }

    public void setMight(long might) {
        this.might = might;
    }

    public long getPrecision() {
        return precision;
    }

    public void setPrecision(long precision) {
        this.precision = precision;
    }

    public long getRestoration() {
        return restoration;
    }

    public void setRestoration(long restoration) {
        this.restoration = restoration;
    }

    public long getVitalization() {
        return vitalization;
    }

    public void setVitalization(long vitalization) {
        this.vitalization = vitalization;
    }

    public long getDominance() {
        return dominance;
    }

    public void setDominance(long dominance) {
        this.dominance = dominance;
    }

    public Set<Long> getCompletedFeats() {
        return completedFeats;
    }

    public void setCompletedFeats(Set<Long> completedFeats) {
        this.completedFeats = completedFeats;
    }

    public Set<Long> getUnlockableFeats() {
        return unlockableFeats;
    }

    public void setUnlockableFeats(Set<Long> unlockableFeats) {
        this.unlockableFeats = unlockableFeats;
    }

    public List<CharactersItem> getCharacterItems() {
        return characterItems;
    }

    public void setCharacterItems(List<CharactersItem> characterItems) {
        this.characterItems = characterItems;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    @JsonProperty("power_type_id")
    public int getPowerTypeId() {
        return powerTypeId;
    }

    public void setPowerTypeId(int powerTypeId) {
        this.powerTypeId = powerTypeId;
    }

    @JsonProperty("power_source_id")
    public int getPowerSourceId() {
        return powerSourceId;
    }

    public void setPowerSourceId(int powerSourceId) {
        this.powerSourceId = powerSourceId;
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

    @JsonProperty("region_id")
    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Map<Role, Set<Long>> getUnlockableRoleFeats() {
        return unlockableRoleFeats;
    }

    public Set<Long> getUnlockableRoleFeatSet(Role role) {
        Set<Long> roleFeats = unlockableRoleFeats.get(role);
        if (roleFeats == null) {
            roleFeats = new TreeSet<Long>();
            unlockableRoleFeats.put(role, roleFeats);
        }
        return roleFeats;
    }

    public void setUnlockableRoleFeats(Map<Role, Set<Long>> unlockableRoleFeats) {
        this.unlockableRoleFeats = unlockableRoleFeats;
    }

    public void addUnlockableRoleFeat(Role role, Long featId) {
        Set<Long> roleFeats = getUnlockableRoleFeatSet(role);
        roleFeats.add(featId);
    }

    public Map<MovementMode, Set<Long>> getUnlockableMovementFeats() {
        return unlockableMovementFeats;
    }

    public Set<Long> getUnlockableMovementFeatSet(MovementMode movement) {
        Set<Long> movementFeats = unlockableMovementFeats.get(movement);
        if (movementFeats == null) {
            movementFeats = new TreeSet<Long>();
            unlockableMovementFeats.put(movement, movementFeats);
        }
        return movementFeats;
    }

    public void setUnlockableMovementFeats(Map<MovementMode, Set<Long>> unlockableMovementFeats) {
        this.unlockableMovementFeats = unlockableMovementFeats;
    }

    public void addUnlockabeMovementFeat(MovementMode movement, Long featId) {
        Set<Long> movementFeats = getUnlockableMovementFeatSet(movement);
        movementFeats.add(featId);
    }

}
