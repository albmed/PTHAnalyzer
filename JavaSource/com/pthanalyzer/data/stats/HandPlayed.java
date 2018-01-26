package com.pthanalyzer.data.stats;

public class HandPlayed { 
	private Integer handId; 
	private Integer totalPlayers; 
	private Boolean played;

	/**
	 * @return the handId
	 */
	public Integer getHandId() {
		return handId;
	}

	/**
	 * @param handId the handId to set
	 */
	public void setHandId(Integer handId) {
		this.handId = handId;
	}

	/**
	 * @return the totalPlayers
	 */
	public Integer getTotalPlayers() {
		return totalPlayers;
	}

	/**
	 * @param totalPlayers the totalPlayers to set
	 */
	public void setTotalPlayers(Integer totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

	/**
	 * @return the played
	 */
	public Boolean getPlayed() {
		return played;
	}

	/**
	 * @param played the played to set
	 */
	public void setPlayed(Boolean played) {
		this.played = played;
	}

}
