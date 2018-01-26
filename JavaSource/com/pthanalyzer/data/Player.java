package com.pthanalyzer.data;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Wrapper for PDB table PLAYER
 * 
 * @author albgonza
 *
 */
public class Player implements ObjectSqlite {

	private Integer uniqueId; 
	private Integer seat;
	private String playerName;

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 

		sb.append(this.getClass().getName()).append(" = {");
		sb.append("uniqueId = ").append(uniqueId).append("; ");
		sb.append("seat = ").append(seat).append("; ");
		sb.append("playerName = ").append(playerName);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public void loadResultSet(SQLiteStatement st) throws SQLiteException {
		this.uniqueId = st.columnInt(0);
		this.seat = st.columnInt(1);
		this.playerName = st.columnString(2);
	} 
	
	
	
	
}
