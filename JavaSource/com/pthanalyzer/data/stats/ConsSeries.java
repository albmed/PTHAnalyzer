package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

/**
 * Represents Series of Loses/Winnings 
 * 
 * @author albgonza
 *
 */
public class ConsSeries { 

	private Integer hands; 
	private Long amount;
	private Player player; 
	private Integer startHand;
	private Integer endHand;

	/**
	 * @return the hands
	 */
	public Integer getHands() {
		return hands;
	}

	/**
	 * @param hands the hands to set
	 */
	public void setHands(Integer hands) {
		this.hands = hands;
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
	 * @return the startHand
	 */
	public Integer getStartHand() {
		return startHand;
	}

	/**
	 * @param startHand the startHand to set
	 */
	public void setStartHand(Integer startHand) {
		this.startHand = startHand;
	}

	/**
	 * @return the endHand
	 */
	public Integer getEndHand() {
		return endHand;
	}

	/**
	 * @param endHand the endHand to set
	 */
	public void setEndHand(Integer endHand) {
		this.endHand = endHand;
	}


}
