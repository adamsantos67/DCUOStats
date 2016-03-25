package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reward extends Type {
    private static final long serialVersionUID = -5765909930494690735L;

    private long difficulty;
	private long xp;

	@JsonProperty("feat_reward_id")
	public long getId() {
		return super.getId();
	}

	public long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(long difficulty) {
		this.difficulty = difficulty;
	}

	public long getXp() {
		return xp;
	}

	public void setXp(long xp) {
		this.xp = xp;
	}
}
