package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

public class MostRaiseStats {
	
	private Player player; 
	private Integer numPlayed; 
	private Integer numHandsRaised; 
	private Integer numRaises;

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
	 * @return the numPlayed
	 */
	public Integer getNumPlayed() {
		return numPlayed;
	}

	/**
	 * @param numPlayed the numPlayed to set
	 */
	public void setNumPlayed(Integer numPlayed) {
		this.numPlayed = numPlayed;
	}

	/**
	 * @return the numHandsRaised
	 */
	public Integer getNumHandsRaised() {
		return numHandsRaised;
	}

	/**
	 * @param numHandsRaised the numHandsRaised to set
	 */
	public void setNumHandsRaised(Integer numHandsRaised) {
		this.numHandsRaised = numHandsRaised;
	}

	/**
	 * @return the numRaises
	 */
	public Integer getNumRaises() {
		return numRaises;
	}

	/**
	 * @param numRaises the numRaises to set
	 */
	public void setNumRaises(Integer numRaises) {
		this.numRaises = numRaises;
	}

}
