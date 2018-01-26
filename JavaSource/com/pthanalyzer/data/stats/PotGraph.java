package com.pthanalyzer.data.stats;

/**
 * Class that represents the pot is bet in any hand
 * 
 * @author albgonza
 *
 */
public class PotGraph { 

	private Integer handId; 
	private Long pot; 
	private Long sb;

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
	 * @return the pot
	 */
	public Long getPot() {
		return pot;
	}

	/**
	 * @param pot the pot to set
	 */
	public void setPot(Long pot) {
		this.pot = pot;
	}

	/**
	 * @return the sb
	 */
	public Long getSb() {
		return sb;
	}

	/**
	 * @param sb the sb to set
	 */
	public void setSb(Long sb) {
		this.sb = sb;
	}

}
