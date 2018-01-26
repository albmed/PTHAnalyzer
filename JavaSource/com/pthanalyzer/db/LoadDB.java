package com.pthanalyzer.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.pthanalyzer.data.Action;
import com.pthanalyzer.data.Game;
import com.pthanalyzer.data.Hand;
import com.pthanalyzer.data.Player;
import com.pthanalyzer.data.Session;
import com.pthanalyzer.exception.GeneralPTHException;

public class LoadDB {

//	private Shell shell;
	private File file;  
	private SQLiteConnection db = null;  
	
	public LoadDB(File file) throws SQLiteException {
//		this.shell = shell;
		this.file = file; 
		init(); 
	}
	
	private void init() throws SQLiteException { 
		this.db = new SQLiteConnection(this.file);
		db.open(false);
	}
	
	public void close() { 
		if (this.db != null) db.dispose(); 
	}
	
	public List<Session> loadSessions() throws SQLiteException, GeneralPTHException {
		if (db == null) throw new SQLiteException(-1, "DB conection not stablished");
		
		List<Session> sessions = new ArrayList<Session>(); 
		SQLiteStatement st = null;
		
		try { 
			st = db.prepare("SELECT PokerTH_Version, Date, Time, LogVersion FROM Session"); 
			
			while(st.step()) {
				Session session = new Session(); 
				session.loadResultSet(st);
				sessions.add(session);
			}
		}
		catch (SQLiteException e) { 
			throw new GeneralPTHException("Error retrieving SESSION data");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error retrieving SESSION data");
//			box.open();
//			return null; 
		}
		finally {
			if (st != null) st.dispose(); 
		}
		
		 return sessions;
	}
	
	public List<Game> loadGames() throws SQLiteException, GeneralPTHException {
		if (db == null) throw new SQLiteException(-1, "DB conection not stablished");
		
		List<Game> games = new ArrayList<Game>(); 
		SQLiteStatement st = null;
		
		try { 
			
			String query = 
					"SELECT UniqueGameID, GameID, Startmoney, StartSb, DealerPos, Winner_Seat " + 
					"FROM Game " + 
					"ORDER BY UniqueGameID ";

			st = db.prepare(query);
			
			while(st.step()) {
				Game game = new Game(); 
				game.loadResultSet(st);
				games.add(game);
			}
		}
		catch (SQLiteException e) { 
			throw new GeneralPTHException("Error retrieving GAME data");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error retrieving GAME data");
//			box.open();
//			return null; 
		}
		finally { 
			if (st != null) st.dispose(); 
		}
		
		return games; 
	}
	
	public List<Player> loadPlayers(Integer id) throws SQLiteException, GeneralPTHException { 
		if (db == null) throw new SQLiteException(-1, "DB conection not stablished");
		
		List<Player> players = new ArrayList<Player>(); 
		SQLiteStatement st = null;
		
		try {
			
			String query = 
					"SELECT UniqueGameID, Seat, Player " + 
					"FROM Player " + 
					"WHERE UniqueGameID = ? " + 
					"ORDER BY UniqueGameID, Seat ";
			
			st = db.prepare(query);
			st.bind(1, id);
			
			while(st.step()) {
				Player player = new Player(); 
				player.loadResultSet(st);
				players.add(player);
			}
		}
		catch (SQLiteException e) { 
			throw new GeneralPTHException("Error retrieving PLAYER data");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error retrieving PLAYER data");
//			box.open();
//			return null; 
		}
		finally { 
			if (st != null) st.dispose(); 
		}
		
		return players; 
	}

	public List<Hand> loadHands(Integer id) throws SQLiteException, GeneralPTHException { 
		if (db == null) throw new SQLiteException(-1, "DB conection not stablished");
		
		List<Hand> hands = new ArrayList<Hand>(); 
		SQLiteStatement st = null;
		
		try {
			String query = 
					"SELECT HandID, UniqueGameID, Dealer_Seat, Sb_Amount, Sb_Seat, Bb_Amount, Bb_Seat, "  +
					"Seat_1_Cash, Seat_1_Card_1, Seat_1_Card_2, Seat_1_Hand_text, Seat_1_Hand_int, " +
					"Seat_2_Cash, Seat_2_Card_1, Seat_2_Card_2, Seat_2_Hand_text, Seat_2_Hand_int, " +
					"Seat_3_Cash, Seat_3_Card_1, Seat_3_Card_2, Seat_3_Hand_text, Seat_3_Hand_int, " +
					"Seat_4_Cash, Seat_4_Card_1, Seat_4_Card_2, Seat_4_Hand_text, Seat_4_Hand_int, " +
					"Seat_5_Cash, Seat_5_Card_1, Seat_5_Card_2, Seat_5_Hand_text, Seat_5_Hand_int, " +
					"Seat_6_Cash, Seat_6_Card_1, Seat_6_Card_2, Seat_6_Hand_text, Seat_6_Hand_int, " +
					"Seat_7_Cash, Seat_7_Card_1, Seat_7_Card_2, Seat_7_Hand_text, Seat_7_Hand_int, " +
					"Seat_8_Cash, Seat_8_Card_1, Seat_8_Card_2, Seat_8_Hand_text, Seat_8_Hand_int, " +
					"Seat_9_Cash, Seat_9_Card_1, Seat_9_Card_2, Seat_9_Hand_text, Seat_9_Hand_int, " +
					"Seat_10_Cash, Seat_10_Card_1, Seat_10_Card_2, Seat_10_Hand_text, Seat_10_Hand_int, " +
					"BoardCard_1, BoardCard_2, BoardCard_3, BoardCard_4, BoardCard_5 " + 
					"FROM Hand " + 
					"WHERE UniqueGameID = ? " + 
					"ORDER BY UniqueGameID, HandID "; 

			st = db.prepare(query);
			st.bind(1, id); 

			while(st.step()) {
				Hand hand = new Hand(); 
				hand.loadResultSet(st);
				hands.add(hand); 
			}
		}
		catch (SQLiteException e) {
			throw new GeneralPTHException("Error retrieving HAND data"); 
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error retrieving HAND data");
//			box.open();
//			return null; 
		}
		finally { 
			if (st != null) st.dispose(); 
		}
		
		return hands; 
	}

	public List<Action> loadActions(Integer id) throws SQLiteException, GeneralPTHException { 
		if (db == null) throw new SQLiteException(-1, "DB conection not stablished");
		
		List<Action> actions = new ArrayList<Action>(); 
		SQLiteStatement st = null;
		
		try { 
			String query = 
					"SELECT ActionID, HandID, UniqueGameID, BeRo, Player, Action, Amount " +
					"FROM Action " + 
					"WHERE UniqueGameID = ? " + 
					"ORDER BY UniqueGameID, HandID, ActionID ";
			
			st = db.prepare(query);
			st.bind(1, id);
			
			while(st.step()) {
				Action action = new Action();  
				action.loadResultSet(st);
				actions.add(action);
			}
		}
		catch (SQLiteException e) { 
			throw new GeneralPTHException("Error retrieving ACTION data");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error retrieving ACTION data");
//			box.open();
//			return null; 
		}
		finally { 
			if (st != null) st.dispose(); 
		}
		
		return actions; 
	}

}
