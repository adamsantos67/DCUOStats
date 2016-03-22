package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharIdMappings {
    private List<CharIdMapping> mappingList;
    private long returned;

    @JsonProperty("char_id_mapping_list")
    public List<CharIdMapping> getMappingList() {
        return mappingList;
    }

    public void setMappingList(List<CharIdMapping> mappingList) {
        this.mappingList = mappingList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
