package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

public class AllInStat { 
	private Player player; 
	private Integer totalAllIns;
	private Integer totalHandsPlayed; 
	private Integer inPreFlop; 
	private Integer first5Hands; 
	private Long totalWon;

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
	 * @return the totalAllIns
	 */
	public Integer getTotalAllIns() {
		return totalAllIns;
	}

	/**
	 * @param totalAllIns the totalAllIns to set
	 */
	public void setTotalAllIns(Integer totalAllIns) {
		this.totalAllIns = totalAllIns;
	}

	/**
	 * @return the totalHandsPlayed
	 */
	public Integer getTotalHandsPlayed() {
		return totalHandsPlayed;
	}

	/**
	 * @param totalHandsPlayed the totalHandsPlayed to set
	 */
	public void setTotalHandsPlayed(Integer totalHandsPlayed) {
		this.totalHandsPlayed = totalHandsPlayed;
	}

	/**
	 * @return the inPreFlop
	 */
	public Integer getInPreFlop() {
		return inPreFlop;
	}

	/**
	 * @param inPreFlop the inPreFlop to set
	 */
	public void setInPreFlop(Integer inPreFlop) {
		this.inPreFlop = inPreFlop;
	}

	/**
	 * @return the first5Hands
	 */
	public Integer getFirst5Hands() {
		return first5Hands;
	}

	/**
	 * @param first5Hands the first5Hands to set
	 */
	public void setFirst5Hands(Integer first5Hands) {
		this.first5Hands = first5Hands;
	}

	/**
	 * @return the totalWon
	 */
	public Long getTotalWon() {
		return totalWon;
	}

	/**
	 * @param totalWon the totalWon to set
	 */
	public void setTotalWon(Long totalWon) {
		this.totalWon = totalWon;
	}

}
