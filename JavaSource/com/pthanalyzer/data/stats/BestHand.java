package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

public class BestHand {
	
	private Player player; 
	private Integer handInt; 
	private String handTxt; 
	private Integer handId; 
	private Long amount; // if < 0 loss, if > 0 gain

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
	 * @return the handInt
	 */
	public Integer getHandInt() {
		return handInt;
	}

	/**
	 * @param handInt the handInt to set
	 */
	public void setHandInt(Integer handInt) {
		this.handInt = handInt;
	}

	/**
	 * @return the handTxt
	 */
	public String getHandTxt() {
		return handTxt;
	}

	/**
	 * @param handTxt the handTxt to set
	 */
	public void setHandTxt(String handTxt) {
		this.handTxt = handTxt;
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
