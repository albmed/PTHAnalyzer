package com.pthanalyzer.data;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Wrapper for PDB table GAME 
 * 
 * @author albgonza
 *
 */
public class Game implements ObjectSqlite {

	private Integer uniqueId; 
	private Integer id; 
	private Long initialStack; 
	private Long initialSb; 
	private Integer dealerSeat;
	private Integer winnerSeat;

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getInitialStack() {
		return initialStack;
	}

	public void setInitialStack(Long initialStack) {
		this.initialStack = initialStack;
	}

	public Long getInitialSb() {
		return initialSb;
	}

	public void setInitialSb(Long initialSb) {
		this.initialSb = initialSb;
	}

	public Integer getDealerSeat() {
		return dealerSeat;
	}

	public void setDealerSeat(Integer dealerSeat) {
		this.dealerSeat = dealerSeat;
	}

	public Integer getWinnerSeat() {
		return winnerSeat;
	}

	public void setWinnerSeat(Integer winnerSeat) {
		this.winnerSeat = winnerSeat;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 

		sb.append(this.getClass().getName()).append(" = {");
		sb.append("uniqueId = ").append(uniqueId).append("; ");
		sb.append("id = ").append(id).append("; ");
		sb.append("initialStack = ").append(initialStack).append("; ");
		sb.append("initialSb = ").append(initialSb).append("; ");
		sb.append("dealerSeat = ").append(dealerSeat).append("; ");
		sb.append("winnerSeat = ").append(winnerSeat);
		sb.append("}");

		return sb.toString();
	}
	
	@Override
	public void loadResultSet(SQLiteStatement st) throws SQLiteException {
		this.uniqueId = st.columnInt(0);
		this.id = st.columnInt(1);
		this.initialStack = st.columnLong(2);
		this.initialSb = st.columnLong(3);
		this.dealerSeat = st.columnInt(4);
		this.winnerSeat = st.columnNull(5) ? null : st.columnInt(5); 
	} 

}
