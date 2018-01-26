package com.pthanalyzer.data;

import static com.pthanalyzer.db.DBConstants.SITS_OUT;
import static com.pthanalyzer.db.DBConstants.WINS_GAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.pthanalyzer.data.Hand.SeatData;
import com.pthanalyzer.exception.GeneralPTHException; 

/**
 * Data for Game 
 * 
 * @author albgonza
 *
 */
public class DataGame {

	private static final Logger logger = Logger.getLogger(DataGame.class); 
	
	private Game game; 
	private Player winner;
	private Integer totalHands;

	private List<Player> players;
	private List<Hand> hands; 
	private List<Action> actions;
	private List<Position> positions;
	private StatsGame statsGame; 

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
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
	 * @return the totalHands
	 */
	public Integer getTotalHands() {
		return totalHands;
	}

	/**
	 * @param totalHands the totalHands to set
	 */
	public void setTotalHands(Integer totalHands) {
		this.totalHands = totalHands;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * @return the hands
	 */
	public List<Hand> getHands() {
		return hands;
	}

	/**
	 * @param hands the hands to set
	 */
	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}

	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	/**
	 * @return the positions
	 */
	public List<Position> getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	} 

	/**
	 * Returns whether this game has a winner 
	 * @return
	 */
	public boolean isWinner() { 
		return winner != null ? true : false; 
	}
	
	/**
	 * @return the statsGame
	 */
	public StatsGame getStatsGame() {
		return statsGame;
	}

	/**
	 * @param statsGame the statsGame to set
	 */
	public void setStatsGame(StatsGame statsGame) {
		this.statsGame = statsGame;
	}

	/**
	 * Calculates all data necessary for Game 
	 * 
	 * @throws GeneralPTHException
	 */
	public void analyzeGame() throws GeneralPTHException { 
		Integer lastHand = hands.get(hands.size() - 1).getHandId(); 
		List<Action> lastActions = actions.stream().
				filter(a -> a.getHandId().intValue() == lastHand.intValue()).collect(Collectors.toList()); 
		
		// Find out whether exists a winner
		boolean isWinner = false; 
		
		for (Action action : lastActions) { 
			if (action.getAction().equals(WINS_GAME)) { 
				game.setWinnerSeat(action.getPlayer());
				this.totalHands = lastHand;
				this.winner = players.stream().filter(p -> p.getSeat().intValue() == action.getPlayer().intValue()).findFirst().get(); 
				isWinner = true; 
				if (logger.isDebugEnabled()) {
					logger.debug("Winner found for game " + game.getUniqueId() + ": " + this.winner.toString());
				}
				break;
			}
		}
		
		if (!isWinner) { // No winner found 
			if (logger.isInfoEnabled()) logger.info("No winner found for game " + game.getUniqueId());
			game.setWinnerSeat(-1);
			this.winner = null; 
			this.totalHands = lastHand;
		}
		
		// Set positions
		this.setPositions(calculateFinalPositions());
		
		// Generate stats; 
		this.statsGame = new StatsGame(this); 
	}
	
	/**
	 * Get final Positions of game
	 * 
	 * Iterate over Hands (reversed) to check whether a player still have money to play. 
	 * 
	 * Note: Iterate over actions finding "sits out" don't work if player leave game  
	 *  
	 * @return
	 * @throws GeneralPTHException 
	 */
	private List<Position> calculateFinalPositions() throws GeneralPTHException { 
		List<Position> positions = new ArrayList<>(10);
		
		// Indicates hand in which a player is out (eliminated or left)
		Integer[] playersSaw = new Integer[players.size()];
		
		int curPos = 0; 
		ListIterator<Hand> handsRev =  hands.listIterator(hands.size());
		while (handsRev.hasPrevious()) {
			Hand hd = handsRev.previous();
			
			List<SeatData> sds = hd.getSeats().stream().filter(sd -> sd.getCash() != null && sd.getCash().longValue() > 0L).collect(Collectors.toList());
			
			// We sort by amount in reverse order to be able to sort positions when two players are out in same 
			//	hand. Also to determine who is in first position.  
			Collections.sort(sds, (SeatData sd1, SeatData sd2) -> sd2.getCash().compareTo(sd1.getCash()));
			
			for (SeatData sd : sds) {
				int seat = sd.getIdSeat().intValue();
				if (playersSaw[seat -1] == null) { 
					playersSaw[seat -1] = hd.getHandId();
					
					Player player = players.stream().filter(p -> p.getSeat().intValue() == seat).findFirst().get(); 
					
					Position pos = new Position(++curPos, player, hd.getHandId());
					// Check if player is eliminated or left game;  
					if (actions.stream().filter(a -> a.getPlayer().intValue() == seat && 
							a.getHandId().longValue() == hd.getHandId().longValue() && 
							a.getAction() != null && a.getAction().contains(SITS_OUT)).collect(Collectors.toList()).size() > 0) 
						pos.setEliminated(true); 

					positions.add(pos);
					
					if (curPos == 1 && isWinner() && game.getWinnerSeat().longValue() != player.getSeat().longValue()) { 
						throw new GeneralPTHException("Winner Conflict!!"); 
					}
				}
			}
		}
		
		Collections.sort(positions, (Position p1, Position p2) -> p1.getPos().compareTo(p2.getPos()));
		
		return positions; 
	}
	
	
}
