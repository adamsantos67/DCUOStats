package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCategory extends Type {
    private static final long serialVersionUID = 974653102804035602L;

    private String categoryName = null;
    private long parentId = 0;
    private String codeName = null;
    private Name name = null;

    @JsonProperty("item_category_id")
    public long getId() {
        return super.getId();
    }

    @JsonProperty("category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @JsonProperty("parent_category_id")
    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
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

    public String getNameEn() {
        return name == null ? "" : name.getEn();
    }

    public void setName(Name name) {
        this.name = name;
    }

}
