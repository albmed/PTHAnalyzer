package com.pthanalyzer.data;

import static com.pthanalyzer.db.DBConstants.ALL_IN;
import static com.pthanalyzer.db.DBConstants.BETS;
import static com.pthanalyzer.db.DBConstants.BIG_BLIND;
import static com.pthanalyzer.db.DBConstants.BLIND;
import static com.pthanalyzer.db.DBConstants.WINS_HAND;
import static com.pthanalyzer.db.DBConstants.WINS_SIDE_POT;
import static com.pthanalyzer.utils.Constants.LOSER_SERIES;
import static com.pthanalyzer.utils.Constants.WINNER_SERIES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.pthanalyzer.data.Hand.SeatData;
import com.pthanalyzer.data.stats.AllInStat;
import com.pthanalyzer.data.stats.BasicDataStats;
import com.pthanalyzer.data.stats.BestHand;
import com.pthanalyzer.data.stats.ConsSeries;
import com.pthanalyzer.data.stats.FinalResult;
import com.pthanalyzer.data.stats.HandPlayed;
import com.pthanalyzer.data.stats.HandWon;
import com.pthanalyzer.data.stats.HandsPlayed;
import com.pthanalyzer.data.stats.HighestWin;
import com.pthanalyzer.data.stats.MostRaiseStats;
import com.pthanalyzer.data.stats.PotGraph;
import com.pthanalyzer.data.stats.StackedGraph;

/**
 * Create basic stats per game
 * 
 * <p>Represents the same information (with some exceptions) that log-file does, in next way:
 * 
 * <ol type ="I"> 
 * 	<li> Basic Data. {@link StatsGame#getGameBasicData}
 * 	<li> Ranking {@link StatsGame#getFinalResult} 
 * 	<li> Course of the Game
 * 		<ol type="a">
 * 			<li> Line {@link StatsGame#getCashByPlayers} // Això hauria de portar a pintar la gràfica
 * 			<li> Stacked {@link StatsGame#getHandsPerPercentage} // Anàleg
 * 			<li> Pot per Hand  {@link StatsGame#getPotPerHand} // Anàleg 
 * 		</ol>
 * 	<li> Most Hands Played {@link StatsGame#getHandsPlayedByPlayer}
 * 	<li> Best Hands {@link StatsGame#getBestHandsGame} 
 * 	<li> Most Wins {@link StatsGame#getMostWinsByPlayer}
 * 	<li> Highest wins {@link StatsGame#getHighestWinsGame}
 * 	<li> Longest Series of wins {@link StatsGame#getLongestSeries} // Map with 2 keys: one for gains
 * 	<li> Longest series of losses {@link StatsGame#getLongestSeries} // .. the other for loses
 * 	<li> Most bet/raise {@link StatsGame#getMostRaises}
 * 	<li> Most all in {@link StatsGame#getAllInsByPlayer}
 * </ol>
 * 
 * @author albgonza
 *
 */
public class StatsGame {

	private DataGame dg;  
	
	// Stats 
	private BasicDataStats basicDataStats; 
	private List<FinalResult> finalResults; 
	private Map<Position, List<Long>> lineGraph; 
	private Map<Hand, List<StackedGraph>> stackedGraph; 
	private List<PotGraph> potGraph;
	private List<HandsPlayed> handsPlayed; 
	private List<BestHand> bestHands; 
	private List<HandWon> mostWins; 
	private List<HighestWin> highestWins;
	private List<ConsSeries> seriesWin; 
	private List<ConsSeries> seriesLose; 
	private List<MostRaiseStats> mostBetsRaises; 
	private List<AllInStat> allIns; 
	
	public StatsGame(DataGame dg) {
		this.dg = dg;
		
		// Calculate stats
		this.basicDataStats = getGameBasicData(); 
		this.finalResults = getFinalResult(); 
		this.lineGraph = getCashByPlayers(); 
		this.stackedGraph = getHandsPerPercentage(); 
		this.potGraph = getPotPerHand(); 
		this.handsPlayed = getHandsPlayedByPlayer();
		this.bestHands = getBestHandsGame();
		this.mostWins = getMostWinsByPlayer();
		this.highestWins = getHighestWinsGame();
		this.seriesWin = getLongestSeries().get(WINNER_SERIES); 
		this.seriesLose = getLongestSeries().get(LOSER_SERIES);
		this.mostBetsRaises = getMostRaises();
		this.allIns = getAllInsByPlayer();
	}
	
	// Getters 
	public BasicDataStats getBasicDataStats() {
		return this.basicDataStats; 
	}
	
	public List<FinalResult> getFinalResults() { 
		return this.finalResults;
	}
	
	public Map<Position, List<Long>> getLineGraph() { 
		return this.lineGraph;
	}
	
	public Map<Hand, List<StackedGraph>> getStackedGraph() { 
		return this.stackedGraph;
	}
	
	public List<PotGraph> getPotGraph() {
		return this.potGraph;
	}

	public List<HandsPlayed> getHandsPlayed() {
		return this.handsPlayed;
	}
	
	public List<BestHand> getBestHands() { 
		return this.bestHands;
	}
	
	public List<HandWon> getMostWins() {
		return this.mostWins;
	}
	
	public List<HighestWin> getHighestWins() {
		return this.highestWins;
	}
	
	public List<ConsSeries> getSeriesWin() {
		return this.seriesWin; 
	}
	
	public List<ConsSeries> getSeriesLose() {
		return this.seriesLose;
	}
	
	public List<MostRaiseStats> getMostBetsRaises() {
		return this.mostBetsRaises;
	}
	
	public List<AllInStat> getAllIns() { 
		return this.allIns;
	}
	
	// FUNCTIONS 
	private BasicDataStats getGameBasicData() { 
		BasicDataStats bds = new BasicDataStats();  
		
		bds.setWinner(dg.isWinner() ? dg.getWinner() : null); 
		bds.setPlayers(Integer.valueOf(dg.getPlayers().size()));
		bds.setHands(dg.getTotalHands()); 
		bds.setStartMoney(dg.getGame().getInitialStack()); 
		bds.setStartBB(dg.getGame().getInitialSb() * 2L);
		bds.setEndBB(dg.getActions().stream().
				filter(a -> 
					a.getHandId().intValue() == dg.getTotalHands().intValue() && 
					a.getAction().contains(BIG_BLIND) && 
					a.getAmount() != null && a.getAmount().longValue() > 0L).
				findFirst().get().getAmount().longValue()); 
		
		return bds; 
	}
	
	/**
	 * FIXME: 
	 * Cal revisar què passa quan hi tres o més jugadors, un guanya, un altre és eliminat i l'últim(s), 
	 * 	pren side pot. En aquest cas caldria veure qui se suposa que ha eliminat el jugador. 
	 * 	Sobretot, en el cas on tant el que guanya com el que pren "side pot", guanyen la mà al que 
	 * 	ha estat eliminat. 
	 * 	En algún d'aquests casos potser caldria canviar  <code>a.getAction().equals("wins")</code> per 
	 * 	<code>a.getAction().contais("wins")</code> o quelcom semblant. 
	 * 
	 * @param dataGame
	 * @return
	 */
	private List<FinalResult> getFinalResult() { 
		List<FinalResult> frs = new ArrayList<>();
		
		for (Position pos : dg.getPositions()) { 
			FinalResult fr = new FinalResult(); 
			fr.setPos(pos); 
			if (dg.isWinner() && pos.getPlayer().getSeat().intValue() == dg.getWinner().getSeat().intValue()) { // winner
				// find out winner pocket cards
				SeatData sdWinner = dg.getHands().stream().
						filter(h -> h.getHandId().intValue() == dg.getTotalHands().intValue()).
						findFirst().get().getSeats().stream().
						filter(s -> s.getIdSeat().intValue() == pos.getPlayer().getSeat().intValue()).
						findFirst().get(); 
				
				Integer[] winnerPocket = new Integer[2]; 
				winnerPocket[0] = sdWinner.getCard1(); 
				winnerPocket[1] = sdWinner.getCard2(); 

				fr.setWinnerPocket(winnerPocket);
				fr.setWinnerHand(sdWinner.getHandTxt());
			}
			else { 
				if (pos.isEliminated()) { // find out who eliminated
					List<Action> actionsHandWin = dg.getActions().stream().
							filter(a -> 
								a.getHandId().intValue() == pos.getHandOut().intValue() && 
								a.getAction().equals(WINS_HAND)).
							collect(Collectors.toList());
					
					Player[] eliminatedBy = new Player[actionsHandWin.size()];
					for (int i = 0; i < actionsHandWin.size(); i++) {
						Action action = actionsHandWin.get(i); 
						eliminatedBy[i] = dg.getPlayers().stream().
								filter(p -> p.getSeat().intValue() == action.getPlayer().intValue()).
								findFirst().get(); 
					}
					
					fr.setEliminatedBy(eliminatedBy);
				}
				else {
					fr.setEliminatedBy(null); 
				}
			}
		
			frs.add(fr);
		}
		
		return frs; 
	}
	
	/**
	 * 
	 * TODO: 
	 * 		Cal veure si s'ha de recalcular la manera de com s'obté l'stack quan s'és winner i és l'última mà. 
	 * 		El problema pot estar a que no és necessari arribar a BeRo = 4 per guanyar una mà. 
	 * 		De totes formes, en aquest cas, és possible que es sigui correcte, ja que al ser l'última mà, un dels dos 
	 * 		haurà fet all-in, i per tant es mostren totes les mans fins a BeRo = 4.  
	 * 
	 * <p>Returns a Sorted Map by final Position. Each Position returns (value) a List of cash sorted by Hand      
	 * 
	 * @return a Sorted {@code Map}, whose keys are {@code Position} (sorted), and whose values are a {@code List} 
	 * of cash a player has, sorted by {@code Hand}  
	 */
	private Map<Position, List<Long>> getCashByPlayers() {
		// Cash per player 
		Map<Position, List<Long>> cashPlayer = new TreeMap<>(new Comparator<Position>() {
			@Override
			public int compare(Position o1, Position o2) {
				return o1.getPos().compareTo(o2.getPos()); 
			}
		});
		
		for (Position pos : dg.getPositions()) {
			Integer idSeat = pos.getPlayer().getSeat();

			List<Long> cashHandPlayer = new ArrayList<>(); 
			boolean firstZero = true;
			boolean addLast = false;
			Hand lastHand = null;
			
			int i = 0; 
			int handsSize = dg.getHands().size();

			// FIXME: Caldria comprovar que realment això funciona quan un jugador abandona la partida sense haver estat eliminat. 
			//	Cal comprovar també que no s'afegeix 2 cops (1: cashHandPlayer.add(cash), quan firstZero == true; 
			//	2: addLast == true && !isWinner).  

			outterloop:
			for (Hand hand: dg.getHands()) { 
				for (SeatData seatData : hand.getSeats()) {
					Long cash = seatData.getCash(); 
					if (seatData.getIdSeat().intValue() == idSeat.intValue() && ((cash != null && cash.compareTo(0L) > 0) || firstZero)) {
						cashHandPlayer.add(cash != null ? cash : Long.valueOf(0L));
						if (cash == null || cash.compareTo(0L) == 0) break outterloop; // No cal setejar firstZero a false, ja que fem break de tot.
						if (++i == handsSize) { // hem arribat al final i no té 0, cal afegir l'últim a mà.  
							addLast = true; 
							lastHand = hand; 
						}
						break; // Hem trobat el player, cal continuar a la següent hand, si existeix  
					}
				}
			}

			if (addLast) {
				final Integer lastHandIdFinal = lastHand.getHandId(); // Hem de crearla final. 
				List<Action> lastAction = dg.getActions().stream().
						filter(a -> a.getHandId().intValue() == lastHandIdFinal.intValue()).
						collect(Collectors.toList());
				
				// Is winner? 
				boolean isWinner = dg.isWinner() && dg.getGame().getWinnerSeat().intValue() == pos.getPlayer().getSeat().intValue(); 
				
				if (isWinner) { 
					// Get amountBet in this hand (last one) by this player 
					Long amountBet = lastAction.stream().filter(action -> action.getAmount() != null && action.getAmount() > 0 && 
							action.getPlayer().intValue() == pos.getPlayer().getSeat().intValue() && 
							!action.getAction().equalsIgnoreCase(WINS_HAND)).mapToLong(action -> action.getAmount()).sum(); 
					// Get initial amount in this hand and player
					Long amountInitial = lastHand.getSeats().stream().filter(
							seat -> seat.getIdSeat().intValue() == pos.getPlayer().getSeat().intValue()).findFirst().get().getCash();
					
					// Get amount Won: FIXME: Caution!! We should check in this operation, betting round 4 (post-River) isn't included.  
					Long amountWon = lastAction.stream().filter(
							action -> action.getBeRo().intValue() == 4 && 
							action.getPlayer().intValue() == pos.getPlayer().getSeat().intValue() && 
							action.getAmount() != null && action.getAmount() > 0).findFirst().get().getAmount().longValue(); 
					
					Long cash = amountInitial - amountBet + amountWon;
					cashHandPlayer.add(cash);
				}
				else { 
					if (pos.isEliminated()) cashHandPlayer.add(0L);
				}
			}

			cashPlayer.put(pos, cashHandPlayer);
		}
		
		return cashPlayer; 
	}
	
	/**
	 * Get amount per Hand and Player to print stacked graph
	 * 
	 * @return a map that represents all hands as key, and a {@link StackedGraph} as value, which contains amount and pct per player
	 */
	private Map<Hand, List<StackedGraph>> getHandsPerPercentage() {
		Map<Hand, List<StackedGraph>> handsCash = new TreeMap<>(new Comparator<Hand>() {
			@Override
			public int compare(Hand h1, Hand h2) {
				return h1.getHandId().compareTo(h2.getHandId()); 
			}
		});

		for (Hand hand : dg.getHands()) { 
			Long total = hand.getSeats().stream().
					filter(s -> s.getCash() != null && s.getCash().longValue() > 0L).
					mapToLong(s -> s.getCash()).reduce(0, Long::sum);
			
			List<StackedGraph> pctHand = new ArrayList<>();
			for (SeatData seat : hand.getSeats()) {
				Long cash = seat.getCash(); 
				if (cash != null && seat.getCash().longValue() > 0L) {
					BigDecimal pct = BigDecimal.valueOf(cash).multiply(BigDecimal.valueOf(100L)).divide(BigDecimal.valueOf(total), 3, RoundingMode.HALF_UP); 
					Player player = dg.getPlayers().stream().
							filter(p -> 
								p.getSeat().intValue() == seat.getIdSeat().intValue()).
							findFirst().get();
					
					StackedGraph sg = new StackedGraph(); 
					sg.setPlayer(player); 
					sg.setPct(pct); 
					sg.setStack(cash); 
					
					pctHand.add(sg);
				}
			}
			
			handsCash.put(hand, pctHand); 
		}

		// We need to add last one. 
		Game game = dg.getGame();
		if (dg.isWinner()) { 
			Hand lastHand = new Hand();
			lastHand.setHandId(Integer.valueOf(dg.getTotalHands().intValue() + 1));

			StackedGraph sg = new StackedGraph(); 
			Player player = dg.getPlayers().stream().filter(p -> p.getSeat().intValue() == game.getWinnerSeat().intValue()).findFirst().get();
			
			sg.setPlayer(player); 
			
			Long amountWon = getAmountPlayerHand(player.getSeat().intValue(), dg.getTotalHands().intValue());
			Long initialStack = dg.getHands().stream().filter(h -> 
						h.getHandId().intValue() == dg.getTotalHands().intValue()).
					findFirst().get().getSeats().stream().filter(s -> 
						s.getIdSeat().intValue() == player.getSeat().intValue()).
					findFirst().get().getCash(); 

			sg.setStack(amountWon + initialStack); 
			sg.setPct(BigDecimal.valueOf(100L));

			List<StackedGraph> pctHand = new ArrayList<>();
			pctHand.add(sg); 
			
			handsCash.put(lastHand, pctHand); 
		}

		return handsCash; 
	}
	
	/**
	 * Return data to plot pot graph. 
	 *  
	 * @param dataGame
	 */
	private List<PotGraph> getPotPerHand() { 
		List<PotGraph> listPF = new ArrayList<>(); 
		
		for (Hand hand : dg.getHands()) { 
			PotGraph pg = new PotGraph(); 
			pg.setHandId(hand.getHandId()); 
			pg.setSb(hand.getSbAmount()); 
			pg.setPot(dg.getActions().stream().filter(a ->
						a.getHandId().intValue() == hand.getHandId().intValue() &&
						a.getAmount() != null && a.getAmount().longValue() > 0L && 
						!a.getAction().contains(WINS_HAND)).
					mapToLong(Action::getAmount).sum()); 

			listPF.add(pg); 
		}

		return listPF; 
	}
	
	/**
	 * Get lists of hands played per player
	 * 
	 * @return
	 */
	private List<HandsPlayed> getHandsPlayedByPlayer() { 
		List<HandsPlayed> handsPlayed = new ArrayList<>(); 
		
		Map<Player, List<HandPlayed>> map = getHandsPlayedPlayer(); 

		for (Entry<Player, List<HandPlayed>> entry : map.entrySet()) { 
			long totalHandsPlayed = entry.getValue().stream().filter(h -> h.getPlayed().booleanValue() == true).count();  
			long totalHands = (long) entry.getValue().size();
			
			long totalHandsPlayed10_7 = entry.getValue().stream().
					filter(h -> 
						h.getPlayed().booleanValue() == true && 
						h.getTotalPlayers().intValue() <= 10 && 
						h.getTotalPlayers().intValue() >=7).
					count(); 

			long totalHands10_7 = entry.getValue().stream().
					filter(h -> 
						h.getTotalPlayers().intValue() <= 10 && 
						h.getTotalPlayers().intValue() >=7).
					count();  

			long totalHandsPlayed6_4 = entry.getValue().stream().
					filter(h -> 
						h.getPlayed().booleanValue() == true && 
						h.getTotalPlayers().intValue() <= 6 && 
						h.getTotalPlayers().intValue() >=4).
					count();
			
			long totalHands6_4 = entry.getValue().stream().
					filter(h -> 
						h.getTotalPlayers().intValue() <= 6 && 
						h.getTotalPlayers().intValue() >=4).
					count();
			
			long totalHandsPlayed3_1 = entry.getValue().stream().
					filter(h -> 
						h.getPlayed().booleanValue() == true && 
						h.getTotalPlayers().intValue() <= 3 && 
						h.getTotalPlayers().intValue() >=1).
					count();
			
			long totalHands3_1 = entry.getValue().stream().
					filter(h -> 
						h.getTotalPlayers().intValue() <= 3 && 
						h.getTotalPlayers().intValue() >=1).
					count();
			
			HandsPlayed hp = new HandsPlayed();
			
			hp.setHandsPlayedT(Integer.valueOf((int) totalHandsPlayed)); 
			hp.setTotalHandsT(Integer.valueOf((int) totalHands)); 
			hp.setPctT(totalHands > 0L ?
					BigDecimal.valueOf(totalHandsPlayed*100D/totalHands).setScale(0, RoundingMode.HALF_UP).intValue() : 
					null);
			
			hp.setHandsPlayedT_7(Integer.valueOf((int) totalHandsPlayed10_7));
			hp.setTotalHandsT_7(Integer.valueOf((int) totalHands10_7));
			hp.setPctT_7(totalHands10_7 > 0L ? 
					BigDecimal.valueOf(totalHandsPlayed10_7*100D/totalHands10_7).setScale(0, RoundingMode.HALF_UP).intValue() :
					null);
					
			hp.setHandsPlayed6_4(Integer.valueOf((int) totalHandsPlayed6_4));
			hp.setTotalHands6_4(Integer.valueOf((int) totalHands6_4));
			hp.setPct6_4(totalHands6_4 > 0L ?
					BigDecimal.valueOf(totalHandsPlayed6_4*100D/totalHands6_4).setScale(0, RoundingMode.HALF_UP).intValue(): 
					null);
					
			hp.setHandsPlayed3_1(Integer.valueOf((int) totalHandsPlayed3_1)); 
			hp.setTotalHands3_1(Integer.valueOf((int) totalHands3_1));
			hp.setPct3_1(totalHands3_1 > 0L ?
					BigDecimal.valueOf(totalHandsPlayed3_1*100D/totalHands3_1).setScale(0, RoundingMode.HALF_UP).intValue() :
					null);
					
			hp.setPlayer(entry.getKey()); 
			
			handsPlayed.add(hp);
		}
		
		Collections.sort(handsPlayed, new Comparator<HandsPlayed>() {
			@Override
			public int compare(HandsPlayed o1, HandsPlayed o2) {
				BigDecimal bg1 = BigDecimal.valueOf(o1.getHandsPlayedT()*100D/o1.getTotalHandsT());
				BigDecimal bg2 = BigDecimal.valueOf(o2.getHandsPlayedT()*100D/o2.getTotalHandsT());
				
				return bg2.compareTo(bg1); 
			}
		});

		return handsPlayed; 
	}
	
	/**
	 * Get list of best hands
	 * 
	 * @return
	 */
	private List<BestHand> getBestHandsGame() { 
		List<BestHand> bhs = new ArrayList<>(); 
		
		for (Hand hand : dg.getHands()) {
			for (SeatData sd : hand.getSeats()) {
				if (sd.getHandInt() != null && sd.getHandInt().intValue() > 0) {
					BestHand bh = new BestHand(); 
					bh.setHandId(hand.getHandId());
					bh.setHandInt(sd.getHandInt());
					bh.setHandTxt(sd.getHandTxt()); 
					bh.setPlayer(dg.getPlayers().stream().filter(p -> p.getSeat().longValue() == sd.getIdSeat().longValue()).findFirst().get());
					bh.setAmount(getAmountPlayerHand(sd.getIdSeat().intValue(), hand.getHandId().intValue()));

					bhs.add(bh); 
				}
			}
		}

		Collections.sort(bhs, new Comparator<BestHand>() {
			@Override
			public int compare(BestHand o1, BestHand o2) {
				return o2.getHandInt().compareTo(o1.getHandInt()); 
			}
		});
		
		return bhs; 
	}
	
	/**
	 * Get win hands per Player
	 * 
	 * @return
	 */
	private List<HandWon> getMostWinsByPlayer() { 
		Map<Player, List<HandPlayed>> map = getHandsPlayedPlayer();
		List<HandWon> handsWon = new ArrayList<>();
		
		for (Entry<Player, List<HandPlayed>> entry : map.entrySet()) {
			Player player = entry.getKey(); 

			long highestGain = 0L; 
			int handWon = 0;
			int played = 0;
			for (HandPlayed hp : entry.getValue()) {
				if (hp.getPlayed().booleanValue()) {
					
					if (playerWinsHand(player.getSeat().intValue(), hp.getHandId().intValue())) { 
						Long amountHand = getAmountPlayerHand(player.getSeat().intValue(), hp.getHandId().intValue());
						if (amountHand > highestGain) highestGain = amountHand;
						handWon++;
					}
					played++;
				}
			}
			
			HandWon hw = new HandWon(); 
			hw.setHighestWin(Long.valueOf(highestGain)); 
			hw.setPlayer(player);
			hw.setWins(Integer.valueOf(handWon)); 
			hw.setPlayed(Integer.valueOf(played));
			
			handsWon.add(hw); 
		}

		Collections.sort(handsWon, new Comparator<HandWon>() {

			@Override
			public int compare(HandWon o1, HandWon o2) {
				int retVal = Integer.compare(o2.getWins(), o1.getWins());
				if (retVal == 0) {
					BigDecimal bg1 = BigDecimal.valueOf(o1.getWins()*100D/o1.getPlayed());
					BigDecimal bg2 = BigDecimal.valueOf(o2.getWins()*100D/o2.getPlayed());

					return bg2.compareTo(bg1); 
				}
				return retVal; 
			}
		});

		return handsWon; 
	}
	
	/**
	 * Get Hightest wins in Game
	 * 
	 * @return
	 */
	private List<HighestWin> getHighestWinsGame() { 
		List<HighestWin> listHW = new ArrayList<>();
		
		for (Hand hand : dg.getHands()) { 
			List<Action> winActions = dg.getActions().stream().
					filter(a -> 
						a.getHandId().longValue() == hand.getHandId().longValue() && 
						a.getAction().equals("wins") &&
						a.getAmount() != null && a.getAmount().longValue() > 0L).
					collect(Collectors.toList());
			
			for (Action action : winActions) {
				HighestWin hw = new HighestWin(); 
				Long amount = getAmountPlayerHand(action.getPlayer().intValue(), hand.getHandId().intValue());
				
				hw.setAmount(amount); 
				hw.setPlayer(dg.getPlayers().stream().
						filter(p -> p.getSeat().intValue() == action.getPlayer().intValue()).
						findFirst().get()); 
				hw.setHandId(hand.getHandId()); 
				
				listHW.add(hw);
				
			}
		}

		Collections.sort(listHW, (HighestWin o1, HighestWin o2) ->  o2.getAmount().compareTo(o1.getAmount()));

		return listHW; 
	}
	

	/**
	 * Calculates series of wins and loses per {@code DataGame}
	 * @param dataGame {@code DataGame} to process
	 * 
	 * @return a {@code Map} whose keys are {@code WINNER_SERIES} or {@code LOSER_SERIES}, and whose values
	 * are a List of {@code ConsSeries}, that has all data for series
	 * 
	 */
	private Map<Integer, List<ConsSeries>> getLongestSeries() {
		
		Map<Integer, List<ConsSeries>> results = new HashMap<>();
		Map<Position, List<Long>> map = getCashByPlayers(); 
		
		List<ConsSeries> winSeries = new ArrayList<>();
		List<ConsSeries> loseSeries = new ArrayList<>();

		int winnerSeat = dg.getGame().getWinnerSeat().intValue();
		int totalHands = dg.getTotalHands().intValue(); 
		
		for (Entry<Position, List<Long>> entry : map.entrySet()) {
			Position pos = entry.getKey(); 
			List<Long> cashPlayer = entry.getValue(); 
			
			Player player = pos.getPlayer();
			
			long previousCash = -1L;
			boolean losing = false;
			boolean winning = false;
			
			int handNum = 0;
			
			ConsSeries wins = null; 
			ConsSeries loses = null; 

			for (Long cash : cashPlayer) {
				if (previousCash != -1L) { // No és la primera mà.  
					handNum++; 
					if (losing) { // està perdent
						if (cash <= previousCash) { // Segueix perdent
							loses.setAmount(Long.valueOf(loses.getAmount().longValue() + previousCash - cash.longValue()));
							loses.setEndHand(Integer.valueOf(handNum));
							loses.setHands(Integer.valueOf(loses.getHands().intValue() + 1));
							
//							loses.amount += previousCash - cash; 
//							loses.endHand=handNum; 
//							loses.hands +=1; 
						}
						else { // ie: (cash > previousCash) Be de perdre i ara guanya 
							loseSeries.add(loses);
							loses = null; 
							losing = false;
							
							winning = true; 
							wins = new ConsSeries(); 
							wins.setAmount(Long.valueOf(cash.longValue() - previousCash));
							wins.setStartHand(Integer.valueOf(handNum));
							wins.setEndHand(Integer.valueOf(handNum));
							wins.setHands(Integer.valueOf(1));
							wins.setPlayer(player);
							
//							wins.amount = cash - previousCash; 
//							wins.startHand = handNum; 
//							wins.endHand = handNum; 
//							wins.hands = 1; 
//							wins.player = player; 
						}
					}
					else if (winning) { // està guanyant
						if (cash < previousCash) { // Bé de guanyar, ara perd! 
							winSeries.add(wins);
							wins = null; 
							winning = false; 
							
							losing = true; 
							loses = new ConsSeries(); 
							
							loses.setAmount(Long.valueOf(previousCash - cash.longValue()));
							loses.setStartHand(Integer.valueOf(handNum));
							loses.setEndHand(Integer.valueOf(handNum));
							loses.setHands(Integer.valueOf(1));
							loses.setPlayer(player);
							
//							loses.amount = previousCash - cash; 
//							loses.startHand = handNum; 
//							loses.endHand = handNum; 
//							loses.hands = 1; 
//							loses.player = player; 
						}
						else if (cash == previousCash) { // Ni guanya ni perd, es ressetejen tots els flags  
							winSeries.add(wins); 
							wins = null; 
							winning = false; 
						}
						else { // ie: (cash > previousCash) Segueix guanyant  
							wins.setAmount(Long.valueOf(wins.getAmount().longValue() + cash.longValue() - previousCash));
							wins.setEndHand(Integer.valueOf(handNum));
							wins.setHands(Integer.valueOf(wins.getHands().intValue() + 1));
							
//							wins.amount += (cash - previousCash); 
//							wins.endHand=handNum; 
//							wins.hands +=1; 
						}
					}
					else { 
						if (cash < previousCash) { // Comença a perdre
							losing = true; 
							loses = new ConsSeries(); 
							loses.setAmount(Long.valueOf(previousCash - cash.longValue()));
							loses.setStartHand(Integer.valueOf(handNum));
							loses.setEndHand(Integer.valueOf(handNum));
							loses.setHands(Integer.valueOf(1));
							loses.setPlayer(player);
							
//							loses.amount = previousCash - cash; 
//							loses.startHand = handNum; 
//							loses.endHand = handNum; 
//							loses.hands = 1; 
//							loses.player = player; 
						}
						else if (cash == previousCash) { // Ni guanya ni perd, es ressetejen tots els flags  
							;
						}
						else { // ie: (cash > previousCash) Comença a guanyar  
							winning = true; 
							wins = new ConsSeries();
							wins.setAmount(Long.valueOf(cash.longValue() - previousCash));
							wins.setStartHand(Integer.valueOf(handNum));
							wins.setEndHand(Integer.valueOf(handNum));
							wins.setHands(Integer.valueOf(1));
							wins.setPlayer(player);
							
//							wins.amount = cash - previousCash; 
//							wins.startHand = handNum; 
//							wins.endHand = handNum; 
//							wins.hands = 1; 
//							wins.player = player; 
						}
					}
					if (cash == 0L && loses != null) { // // Eliminat 
						loseSeries.add(loses);
						loses = null; 
						losing = false;
					}
					else if (winnerSeat == player.getSeat().intValue() && 
							totalHands == handNum && 
							wins != null) { // Guanyador 
						winSeries.add(wins); 
						wins = null; 
						winning = false;
						
					}
				}
				previousCash = cash; 
			}
		}

		Comparator<ConsSeries> cmp = new Comparator<ConsSeries>() {
			public int compare(ConsSeries o1, ConsSeries o2) {
				if (o1.getHands().intValue() == o2.getHands().intValue()) return o2.getAmount().compareTo(o1.getAmount());
				return o2.getHands().compareTo(o1.getHands()); 
			}
		};
		
		Collections.sort(winSeries, cmp);
		Collections.sort(loseSeries, cmp);
		
		results.put(WINNER_SERIES, winSeries);
		results.put(LOSER_SERIES, loseSeries); 
		
		return results; 
	}
	
	/**
	 * Should return most bet/raise by player. 
	 * 
	 * <p> REMARK: Because of some reason that I'm not able to determine, some results differ from the ones in the log-file. 
	 * 
	 * @return
	 */
	private List<MostRaiseStats> getMostRaises() {
		List<MostRaiseStats> listMR = new ArrayList<>();
		
		Map<Player, List<HandPlayed>> map = getHandsPlayedPlayer();
		
		for (Entry<Player, List<HandPlayed>> entry : map.entrySet()) { 
			Player player = entry.getKey(); 

			int played = 0;
			int numTotalRaises = 0; // num total bet/raises 
			int numHandsRaised = 0; // hands with at least one bet/raise 
			for (HandPlayed hp : entry.getValue()) { 
				if (hp.getPlayed().booleanValue()) {
					played++;
					
					int numRaises = 0; 
					List<Action> actionsBet = dg.getActions().stream().filter(a -> 
								a.getAction().equals(BETS) &&
								a.getAmount() != null && a.getAmount().longValue() > 0L && 
								a.getPlayer().intValue() == player.getSeat().intValue() && 
								a.getHandId().intValue() == hp.getHandId().intValue()).
							collect(Collectors.toList());
					
					Action actionAllIn = dg.getActions().stream().filter(a -> 
								a.getAction().contains(ALL_IN) &&
								a.getAmount() != null && a.getAmount().longValue() > 0L && 
								a.getPlayer().intValue() == player.getSeat().intValue() && 
								a.getHandId().intValue() == hp.getHandId().intValue()).
							findFirst().orElse(null); 
					
					if (actionAllIn != null) { // existeix all in per la mà i jugador actual  
						// Obtenim la suma de les apostes en la ronda actual agrupat per jugador, abans del "all in" 
						Map<Integer, Long> map3 = dg.getActions().stream().filter(a -> 
									a.getHandId().intValue() == hp.getHandId().intValue() && // mateixa mà
									a.getBeRo().intValue() == actionAllIn.getBeRo().intValue() && // mateixa BeRo 
									a.getPlayer().intValue() != actionAllIn.getPlayer().intValue() && // els altres jugadors 
									a.getActionId().intValue() < actionAllIn.getActionId().intValue() && // només apostes prèvies  
									a.getAmount() != null && a.getAmount().longValue() > 0L). // hi ha aposta (possiblement redundant amb getAction())  
								collect(Collectors.groupingBy(Action::getPlayer, Collectors.summingLong(Action::getAmount)));

						boolean isCall = false; 
						// Comprovem si cap de les sumes es superior a la suma amb l'"all in" 
						// En tal cas serà un call 
						for (Entry<Integer, Long> entry3 : map3.entrySet()) {
							Long acum = dg.getActions().stream().filter(a -> 
										a.getHandId().intValue() == hp.getHandId().intValue() && // mateixa mà
										a.getBeRo().intValue() == actionAllIn.getBeRo().intValue() && // mateixa BeRo 
										a.getPlayer().intValue() == actionAllIn.getPlayer().intValue() &&  
										a.getAmount() != null && a.getAmount().longValue() > 0L &&
										a.getActionId().intValue() <= actionAllIn.getActionId().intValue()).
									mapToLong(Action::getAmount).sum();
							if (entry3.getValue().longValue() >= acum) { 
								// Es un call!! per al jugador X
								isCall = true; 
								break;
							}
						}
						if (!isCall) numRaises = 1; 
					}
					
					numRaises += actionsBet.size(); 

					if (numRaises > 0) {
						numHandsRaised++;
						numTotalRaises += numRaises; 
					}
				}
			}
			
			MostRaiseStats mr = new MostRaiseStats();
			mr.setNumHandsRaised(Integer.valueOf(numHandsRaised));
			mr.setNumPlayed(Integer.valueOf(played));
			mr.setNumRaises(Integer.valueOf(numTotalRaises));
			mr.setPlayer(player);
			
//			mr.numHandsRaised = numHandsRaised;  
//			mr.numPlayed = played; 
//			mr.numRaises = numTotalRaises;
//			mr.player = player;

			listMR.add(mr); 
		}

		Collections.sort(listMR, (MostRaiseStats mr1,  MostRaiseStats mr2) -> mr2.getNumRaises().compareTo(mr1.getNumRaises()));

		return listMR;
	}

	/**
	 * Get all-in bets by player  
	 * 
	 * @param dataGame
	 * @return
	 */
	private List<AllInStat> getAllInsByPlayer() { 
		List<AllInStat> allInsStat = new ArrayList<>();
		
		Map<Player, List<HandPlayed>> map = getHandsPlayedPlayer();
		for (Entry<Player, List<HandPlayed>> entry : map.entrySet()) {
			Player player = entry.getKey(); 
			List<Action> allIns = dg.getActions().stream().
					filter(a -> 
						a.getPlayer().longValue() == player.getSeat().longValue() && 
						a.getAction().contains(ALL_IN)).
					collect(Collectors.toList()); 
			
			AllInStat ai = new AllInStat(); 
			ai.setPlayer(player);
			ai.setTotalAllIns(Integer.valueOf(allIns.size()));
			ai.setTotalHandsPlayed(Integer.valueOf((int) entry.getValue().stream().filter(hp -> hp.getPlayed()).count()));
			ai.setFirst5Hands(Integer.valueOf((int) allIns.stream().filter(a -> a.getHandId().intValue() <= 5).count()));
			ai.setInPreFlop(Integer.valueOf((int) allIns.stream().filter(a -> a.getBeRo().intValue() == 0).count()));
			ai.setTotalWon(Long.valueOf(allIns.stream().filter(a -> playerWinsHand(a.getPlayer().intValue(), a.getHandId().intValue())).count()));
			
//			ai.player = player; 
//			ai.totalAllIns = Long.valueOf((long)allIns.size()); 
//			ai.totalHandsPlayed = Long.valueOf(entry.getValue().stream().filter(hp -> hp.isPlayed()).count());
//			ai.first5Hands = Long.valueOf(allIns.stream().filter(a -> a.getHandId().longValue() <= 5L).count());
//			ai.inPreFlop = Long.valueOf(allIns.stream().filter(a -> a.getBeRo().longValue() == 0L).count());
//			ai.totalWon = allIns.stream().filter(a -> 
//					playerWinsHand(a.getPlayer().longValue(), a.getHandId().longValue(), dataGame)).count();
			
			allInsStat.add(ai);
		}
		
		Collections.sort(allInsStat, new Comparator<AllInStat>() {
			@Override
			public int compare(AllInStat o1, AllInStat o2) {
				int retVal = o2.getTotalAllIns().compareTo(o1.getTotalAllIns());
				if (retVal == 0) { 
					BigDecimal bg1 = BigDecimal.valueOf(o1.getTotalAllIns()*100D/o1.getTotalHandsPlayed());
					BigDecimal bg2 = BigDecimal.valueOf(o2.getTotalAllIns()*100D/o2.getTotalHandsPlayed());
					return bg2.compareTo(bg1); 
				}
				return retVal;
			}
		});
		
		return allInsStat;
	}

 
	//////// Utility functions 
	
	private boolean playerWinsHand(int player, int hand) {
		return dg.getActions().stream().
				anyMatch(a -> 
					a.getPlayer().intValue() == player && 
					a.getHandId().intValue() == hand &&
					a.getAmount() != null && a.getAmount().longValue() > 0L &&
					a.getAction().equals(WINS_HAND)
				); 
	}
	
	
	private Long getAmountPlayerHand(int player, int hand) { 
		List<Action> actionsPlayerHand = dg.getActions().stream().
				filter(a -> 
					a.getPlayer().intValue() == player && 
					a.getHandId().intValue() == hand &&
					a.getAmount() != null && a.getAmount().longValue() > 0L
				).collect(Collectors.toList());
		
		
		long actionPot = actionsPlayerHand.stream().
				filter(a -> !a.getAction().contains(WINS_HAND)).mapToLong(Action::getAmount).sum(); 
		
		long winTot = actionsPlayerHand.stream().
				filter(a -> a.getAction().equals(WINS_HAND)).mapToLong(Action::getAmount).sum();
		
		long winSide = actionsPlayerHand.stream().
				filter(a -> a.getAction().contains(WINS_SIDE_POT)).mapToLong(Action::getAmount).sum();

		if (winTot > 0L) {
			return Long.valueOf(winTot - actionPot); 
		}
		else if (winSide > 0L) {
			return Long.valueOf(winSide - actionPot);
		}
		else { 
			return Long.valueOf(-actionPot); 
		}
	}

	private Map<Player, List<HandPlayed>> getHandsPlayedPlayer() {
		Map<Player, List<HandPlayed>> map = new HashMap<>();
		
		for (Player player : dg.getPlayers()) { 
			map.put(player, new ArrayList<>()); 
		}
		
		for (Hand hand : dg.getHands()) { 
			long playersAlive = hand.getSeats().stream().
					filter(s -> s.getCash() != null && s.getCash().longValue() > 0L).count(); 
			
			for (SeatData sd : hand.getSeats()) { 
				if (sd.getCash() == null || sd.getCash().longValue() == 0L) continue; // player out 
				List<Action> actionsPlayer = dg.getActions().stream().
						filter(a -> 
							a.getHandId().intValue() == hand.getHandId().intValue() && 
							a.getPlayer().intValue() == sd.getIdSeat().intValue() && 
							a.getAmount() != null && a.getAmount().longValue() > 0L).
						collect(Collectors.toList()); 
				
				Player player = dg.getPlayers().stream().
						filter(p -> p.getSeat().intValue() == sd.getIdSeat().intValue()).findFirst().get(); 
				
				HandPlayed hp = new HandPlayed(); 
				hp.setHandId(hand.getHandId());
				hp.setTotalPlayers(Integer.valueOf((int) playersAlive));
				hp.setPlayed(Boolean.FALSE); // default
				
				if (actionsPlayer.size() > 0) { // player has played
					if (!forced2Play(player, hand, actionsPlayer)) {
						hp.setPlayed(Boolean.TRUE);
					}
				}
				
				map.get(player).add(hp); 
			}
		}
		
		
		return map; 
	}
	
	private boolean forced2Play(Player player, Hand hand, List<Action> actionsHandPlayer) { 
		Action blindAction = actionsHandPlayer.stream().
				filter(a -> a.getBeRo().intValue() == 0 && a.getAction().contains(BLIND)).findFirst().orElse(null);
		
		if (blindAction != null) {
			if (blindAction.getAmount().longValue() == 
					actionsHandPlayer.stream().
							filter(a -> a.getBeRo().intValue() != 4 && a.getAmount() != null && a.getAmount().longValue() > 0L).
							filter(a -> a.getAmount() != null && a.getAmount().longValue() > 0L).
							mapToLong(Action::getAmount).sum()) {
				return true;
			}
		}

		return false; 
	}
	
}
