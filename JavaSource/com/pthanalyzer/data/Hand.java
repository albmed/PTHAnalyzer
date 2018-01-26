package com.pthanalyzer.data;

import java.util.ArrayList;
import java.util.List;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Wrapper for PDB table HAND 
 * 
 * @author albgonza
 *
 */
public class Hand implements ObjectSqlite {
	private Integer handId; 
	private Integer uniqueGameId; 
	private Integer dealerSeat; 
	private Long sbAmount; 
	private Integer sbSeat;
	private Long bbAmount; 
	private Integer bbSeat;
	
	private List<SeatData> seats; 

	private Integer boardCard1; 
	private Integer boardCard2; 
	private Integer boardCard3; 
	private Integer boardCard4; 
	private Integer boardCard5;

	public Integer getHandId() {
		return handId;
	}

	public void setHandId(Integer handId) {
		this.handId = handId;
	}

	public Integer getUniqueGameId() {
		return uniqueGameId;
	}

	public void setUniqueGameId(Integer uniqueGameId) {
		this.uniqueGameId = uniqueGameId;
	}

	public Integer getDealerSeat() {
		return dealerSeat;
	}

	public void setDealerSeat(Integer dealerSeat) {
		this.dealerSeat = dealerSeat;
	}

	public Long getSbAmount() {
		return sbAmount;
	}

	public void setSbAmount(Long sbAmount) {
		this.sbAmount = sbAmount;
	}

	public Integer getSbSeat() {
		return sbSeat;
	}

	public void setSbSeat(Integer sbSeat) {
		this.sbSeat = sbSeat;
	}

	public Long getBbAmount() {
		return bbAmount;
	}

	public void setBbAmount(Long bbAmount) {
		this.bbAmount = bbAmount;
	}

	public Integer getBbSeat() {
		return bbSeat;
	}

	public void setBbSeat(Integer bbSeat) {
		this.bbSeat = bbSeat;
	}

	public List<SeatData> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatData> seats) {
		this.seats = seats;
	}

	public Integer getBoardCard1() {
		return boardCard1;
	}

	public void setBoardCard1(Integer boardCard1) {
		this.boardCard1 = boardCard1;
	}

	public Integer getBoardCard2() {
		return boardCard2;
	}

	public void setBoardCard2(Integer boardCard2) {
		this.boardCard2 = boardCard2;
	}

	public Integer getBoardCard3() {
		return boardCard3;
	}

	public void setBoardCard3(Integer boardCard3) {
		this.boardCard3 = boardCard3;
	}

	public Integer getBoardCard4() {
		return boardCard4;
	}

	public void setBoardCard4(Integer boardCard4) {
		this.boardCard4 = boardCard4;
	}

	public Integer getBoardCard5() {
		return boardCard5;
	}

	public void setBoardCard5(Integer boardCard5) {
		this.boardCard5 = boardCard5;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append(this.getClass().getName()).append(" = {");
		sb.append("handId = ").append(handId).append("; ");
		sb.append("uniqueGameId = ").append(uniqueGameId).append("; ");
		sb.append("dealerSeat = ").append(dealerSeat).append("; ");
		sb.append("sbAmount = ").append(sbAmount).append("; ");
		sb.append("sbSeat = ").append(sbSeat).append("; ");
		sb.append("bbAmount = ").append(bbAmount).append("; ");
		sb.append("bbSeat = ").append(bbSeat).append("; ");

		sb.append("seats = {");
		for (SeatData seat : seats) { 
			sb.append(seat.toString());
		}
		sb.append("}; "); 
		
		sb.append("boardCard1 = ").append(boardCard1).append("; ");
		sb.append("boardCard2 = ").append(boardCard2).append("; ");
		sb.append("boardCard3 = ").append(boardCard3).append("; ");
		sb.append("boardCard4 = ").append(boardCard4).append("; ");
		sb.append("boardCard5 = ").append(boardCard5);
		sb.append("}");

		return sb.toString();
	}
	
	@Override
	public void loadResultSet(SQLiteStatement st) throws SQLiteException {
		int pos = 0; 
		
		this.handId = st.columnInt(pos++); 
		this.uniqueGameId = st.columnInt(pos++);
		this.dealerSeat = st.columnInt(pos++); 
		this.sbAmount = st.columnLong(pos++); 
		this.sbSeat = st.columnInt(pos++); 
		this.bbAmount = st.columnLong(pos++); 
		this.bbSeat = st.columnInt(pos++); 

		
		// TODO: Check what happens in table with less than 10 players
		List<SeatData> seats = new ArrayList<>(); 
		for (int i = 0; i < 10; i++) {
			SeatData seatData = new SeatData(); 
			seatData.setIdSeat(Integer.valueOf(i+1));
			seatData.setCash(st.columnNull(pos) ? null : st.columnLong(pos)); pos++; 
			seatData.setCard1(st.columnNull(pos) ? null : st.columnInt(pos)); pos++; 
			seatData.setCard2(st.columnNull(pos) ? null : st.columnInt(pos)); pos++;
			seatData.setHandTxt(st.columnNull(pos) ? null : st.columnString(pos)); pos++; 
			seatData.setHandInt(st.columnNull(pos) ? null : st.columnInt(pos)); pos++; 
			
			seats.add(i, seatData);
		}
		this.seats = seats;
		
		this.boardCard1 = st.columnNull(pos) ? null : st.columnInt(pos); pos++;
		this.boardCard2 = st.columnNull(pos) ? null : st.columnInt(pos); pos++;
		this.boardCard3 = st.columnNull(pos) ? null : st.columnInt(pos); pos++;
		this.boardCard4 = st.columnNull(pos) ? null : st.columnInt(pos); pos++;
		this.boardCard5 = st.columnNull(pos) ? null : st.columnInt(pos); pos++;
	}

	
	public class SeatData { 
		private Integer idSeat; 
		private Long cash; 
		private Integer card1; 
		private Integer card2; 
		private String handTxt; 
		private Integer handInt;

		public Integer getIdSeat() {
			return idSeat;
		}

		public void setIdSeat(Integer idSeat) {
			this.idSeat = idSeat;
		}

		public Long getCash() { 
			return cash; 
		}
		
		public void setCash(Long cash) {
			this.cash = cash;
		}
		
		public Integer getCard1() {
			return card1;
		}

		public void setCard1(Integer card1) {
			this.card1 = card1;
		}

		public Integer getCard2() {
			return card2;
		}

		public void setCard2(Integer card2) {
			this.card2 = card2;
		}

		public String getHandTxt() {
			return handTxt;
		}

		public void setHandTxt(String handTxt) {
			this.handTxt = handTxt;
		}

		public Integer getHandInt() {
			return handInt;
		}

		public void setHandInt(Integer handInt) {
			this.handInt = handInt;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(); 

			sb.append(this.getClass().getName()).append(" = {");
			sb.append("idSeat = ").append(idSeat).append("; ");
			sb.append("cash = ").append(cash).append("; ");
			sb.append("card1 = ").append(card1).append("; ");
			sb.append("card2 = ").append(card2).append("; ");
			sb.append("handTxt = ").append(handTxt).append("; ");
			sb.append("handInt = ").append(handInt);
			sb.append("}");

			return sb.toString();
		}
		
	}
}

