package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

public class HighestWin {
	
	private Player player; 
	private Integer handId; 
	private Long amount;

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

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
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

}
