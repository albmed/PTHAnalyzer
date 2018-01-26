package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

public class HandWon { 

	private Player player; 
	private Integer played;
	private Integer wins;
	private Long highestWin;

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
	 * @return the played
	 */
	public Integer getPlayed() {
		return played;
	}

	/**
	 * @param played the played to set
	 */
	public void setPlayed(Integer played) {
		this.played = played;
	}

	/**
	 * @return the wins
	 */
	public Integer getWins() {
		return wins;
	}

	/**
	 * @param wins the wins to set
	 */
	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * @return the highestWin
	 */
	public Long getHighestWin() {
		return highestWin;
	}

	/**
	 * @param highestWin the highestWin to set
	 */
	public void setHighestWin(Long highestWin) {
		this.highestWin = highestWin;
	}

}
