package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;

/**
 * Basic Data Stats
 * 
 * @author albgonza
 *
 */
public class BasicDataStats {
	
	private Integer players;
	private Player winner;
	private Integer hands;
	private Long startMoney;
	private Long startBB;
	private Long endBB;

	/**
	 * @return the players
	 */
	public Integer getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(Integer players) {
		this.players = players;
	}

	/**
	 * @return the winner
	 */
	public Player getWinner() {
		return winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void setWinner(Player winner) {
		this.winner = winner;
	}

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
	 * @return the startMoney
	 */
	public Long getStartMoney() {
		return startMoney;
	}

	/**
	 * @param startMoney the startMoney to set
	 */
	public void setStartMoney(Long startMoney) {
		this.startMoney = startMoney;
	}

	/**
	 * @return the startBB
	 */
	public Long getStartBB() {
		return startBB;
	}

	/**
	 * @param startBB the startBB to set
	 */
	public void setStartBB(Long startBB) {
		this.startBB = startBB;
	}

	/**
	 * @return the endBB
	 */
	public Long getEndBB() {
		return endBB;
	}

	/**
	 * @param endBB the endBB to set
	 */
	public void setEndBB(Long endBB) {
		this.endBB = endBB;
	}

}
