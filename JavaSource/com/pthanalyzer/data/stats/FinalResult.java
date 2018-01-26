package com.pthanalyzer.data.stats;

import com.pthanalyzer.data.Player;
import com.pthanalyzer.data.Position;

/**
 * Represents Final Result per Position as shown in Log-File under Ranking
 * 
 * @author albgonza
 *
 */
public class FinalResult { 
	
	private Position pos; 
	private Integer[] winnerPocket; 
	private String winnerHand; 
	private Player[] eliminatedBy;

	/**
	 * @return the pos
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}

	/**
	 * @return the winnerPocket
	 */
	public Integer[] getWinnerPocket() {
		return winnerPocket;
	}

	/**
	 * @param winnerPocket the winnerPocket to set
	 */
	public void setWinnerPocket(Integer[] winnerPocket) {
		this.winnerPocket = winnerPocket;
	}

	/**
	 * @return the winnerHand
	 */
	public String getWinnerHand() {
		return winnerHand;
	}

	/**
	 * @param winnerHand the winnerHand to set
	 */
	public void setWinnerHand(String winnerHand) {
		this.winnerHand = winnerHand;
	}

	/**
	 * @return the eliminatedBy
	 */
	public Player[] getEliminatedBy() {
		return eliminatedBy;
	}

	/**
	 * @param eliminatedBy the eliminatedBy to set
	 */
	public void setEliminatedBy(Player[] eliminatedBy) {
		this.eliminatedBy = eliminatedBy;
	}

}
