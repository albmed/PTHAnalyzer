package com.pthanalyzer.data.stats;

import java.math.BigDecimal;

import com.pthanalyzer.data.Player;

/**
 * @author albgonza
 *
 */
public class StackedGraph {
	private Player player; 
	private BigDecimal pct;
	private Long stack;

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
	 * @return the pct
	 */
	public BigDecimal getPct() {
		return pct;
	}

	/**
	 * @param pct the pct to set
	 */
	public void setPct(BigDecimal pct) {
		this.pct = pct;
	}

	/**
	 * @return the stack
	 */
	public Long getStack() {
		return stack;
	}

	/**
	 * @param stack the stack to set
	 */
	public void setStack(Long stack) {
		this.stack = stack;
	} 

}
