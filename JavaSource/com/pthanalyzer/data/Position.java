package com.pthanalyzer.data;

/**
 * Results for player  
 * 
 * @author albgonza
 *
 */
public class Position {

	private Integer pos; 
	private Player player; 
	private Integer handOut;
	private boolean eliminated = false; // check if player has been eliminated or not (winner, left game, ...)  

	public Position(Integer pos, Player player, Integer handOut) {
		this.pos = pos;
		this.player = player;
		this.handOut = handOut;
	}

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getHandOut() {
		return handOut;
	}

	public void setHandOut(Integer handOut) {
		this.handOut = handOut;
	}

	public boolean isEliminated() {
		return eliminated;
	}

	public void setEliminated(boolean eliminated) {
		this.eliminated = eliminated;
	}

}
