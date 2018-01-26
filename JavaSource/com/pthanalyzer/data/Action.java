package com.pthanalyzer.data;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Wrapper for PDB table ACTION
 * 
 * @author albgonza
 *
 */
public class Action implements ObjectSqlite {

	private Integer actionId; 
	private Integer handId; 
	private Integer uniqueId; 
	private Integer beRo; // Betting Round (0=PreFlop, 1=Flop, 2=Turn, 3=River, 4=Results)  
	private Integer player; // this points to seat?
	private String action;
	private Long amount;

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public Integer getHandId() {
		return handId;
	}

	public void setHandId(Integer handId) {
		this.handId = handId;
	}

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getBeRo() {
		return beRo;
	}

	public void setBeRo(Integer beRo) {
		this.beRo = beRo;
	}

	public Integer getPlayer() {
		return player;
	}

	public void setPlayer(Integer player) {
		this.player = player;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 

		sb.append(this.getClass().getName()).append(" = {");
		sb.append("actionId = ").append(actionId).append("; ");
		sb.append("handId = ").append(handId).append("; ");
		sb.append("uniqueId = ").append(uniqueId).append("; ");
		sb.append("beRo = ").append(beRo).append("; ");
		sb.append("player = ").append(player).append("; ");
		sb.append("action = ").append(action).append("; ");
		sb.append("amount = ").append(amount);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public void loadResultSet(SQLiteStatement st) throws SQLiteException {
		this.actionId = st.columnInt(0);
		this.handId = st.columnInt(1);
		this.uniqueId = st.columnInt(2);
		this.beRo = st.columnInt(3);
		this.player = st.columnInt(4);
		this.action = st.columnString(5);

//		this.amount = st.columnInt(6);
		this.amount = st.columnNull(6) ? null : st.columnLong(6); 
	} 

}
