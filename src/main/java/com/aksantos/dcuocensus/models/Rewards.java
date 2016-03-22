package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rewards {
    private List<Reward> featRewardList;
    private long returned;

    public Rewards() {
        featRewardList = new ArrayList<Reward>();
    }

    @JsonProperty("feat_reward_list")
    public List<Reward> getFeatRewardList() {
        return featRewardList;
    }

    public void setFeatRewardList(List<Reward> featRewardList) {
        this.featRewardList = featRewardList;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
