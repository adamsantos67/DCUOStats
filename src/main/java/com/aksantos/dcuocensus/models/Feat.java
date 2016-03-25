package com.aksantos.dcuocensus.models;

import java.util.List;

import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Feat extends Type {
    private static final long serialVersionUID = 5703361648827126045L;

    private Name name;
    private Name description;
    private long categoryId = 0;
    private String category = "";
    private String subCategory = "";
    private String predicate = "";
    private long difficulty = 0;
    private long rewardId = 0;
    private long reward = 0;
    private long iconId = 0;
    private String imagePath = "";
    private Alignment alignment = null;
    private MovementMode movementMode = null;
    private List<Role> roles = null;
    private Origin origin = null;
    private int order1 = 0;
    private int order2 = 0;
    private long completed = 0;

    @JsonProperty("feat_id")
    public long getId() {
        return super.getId();
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

    public Name getDescription() {
        return description;
    }

    public void setDescription(Name description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return description == null ? "" : description.getEn();
    }

    public void setDescriptionEn(String description) {
        this.description = new Name();
        this.description.setEn(description);
    }

    @JsonProperty("feat_category_id")
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
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

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setAlignments(List<Alignment> alignments) {
        if (alignments != null && !alignments.isEmpty()) {
            setAlignment(alignments.get(0));
        } else {
            setAlignment(null);
        }
    }

    public MovementMode getMovementMode() {
        return movementMode;
    }

    public void setMovementMode(MovementMode movementMode) {
        this.movementMode = movementMode;
    }

    public void setMovementModes(List<MovementMode> movementModes) {
        if (movementModes != null && !movementModes.isEmpty()) {
            setMovementMode(movementModes.get(0));
        } else {
            setMovementMode(null);
        }
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public void setOrigins(List<Origin> origins) {
        if (origins != null && !origins.isEmpty()) {
            setOrigin(origins.get(0));
        } else {
            setOrigin(null);
        }
    }

    public int getOrder1() {
        return order1;
    }

    public void setOrder1(int order1) {
        this.order1 = order1;
    }

    public int getOrder2() {
        return order2;
    }

    public void setOrder2(int order2) {
        this.order2 = order2;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    @JsonProperty("feat_reward_id")
    public long getRewardId() {
        return rewardId;
    }

    public void setRewardId(long feat_reward_id) {
        this.rewardId = feat_reward_id;
    }
}
