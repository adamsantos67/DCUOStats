package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharIdMappingList extends TypeHolder<CharIdMapping> {
    @JsonProperty("char_id_mapping_list")
    public List<CharIdMapping> getObjectList() {
        return super.getObjectList();
    }
}
