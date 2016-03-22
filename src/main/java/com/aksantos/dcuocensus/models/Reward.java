package com.aksantos.dcuocensus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reward {
	private long id;
	private long difficulty;
	private long xp;

	@JsonProperty("feat_reward_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
