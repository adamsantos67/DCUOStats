package com.aksantos.dcuocensus.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardList extends TypeHolder<Reward> {
    @JsonProperty("feat_reward_list")
    public List<Reward> getObjectList() {
        return super.getObjectList();
    }
}
