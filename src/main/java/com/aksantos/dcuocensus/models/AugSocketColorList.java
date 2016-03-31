package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AugSocketColorList extends TypeHolder<AugSocketColor> {
    @JsonProperty("aug_socket_color_list")
    public List<AugSocketColor> getObjectList() {
        return super.getObjectList();
    }
}
