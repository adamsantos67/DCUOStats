package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AugSocketColor extends Type {
    private static final long serialVersionUID = 8720638206843568856L;

    private String name;

    @JsonProperty("aug_socket_color_id")
    public long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
