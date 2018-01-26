package com.pthanalyzer.test;

import static com.pthanalyzer.utils.Constants.LOSER_SERIES;
import static com.pthanalyzer.utils.Constants.WINNER_SERIES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pthanalyzer.data.DataGame;
import com.pthanalyzer.data.Game;
import com.pthanalyzer.data.Hand;
import com.pthanalyzer.data.Player;
import com.pthanalyzer.data.Position;
import com.pthanalyzer.data.StatsGame;
import com.pthanalyzer.data.stats.AllInStat;
import com.pthanalyzer.data.stats.BasicDataStats;
import com.pthanalyzer.data.stats.BestHand;
import com.pthanalyzer.data.stats.ConsSeries;
import com.pthanalyzer.data.stats.FinalResult;
import com.pthanalyzer.data.stats.HandWon;
import com.pthanalyzer.data.stats.HandsPlayed;
import com.pthanalyzer.data.stats.HighestWin;
import com.pthanalyzer.data.stats.MostRaiseStats;
import com.pthanalyzer.data.stats.PotGraph;
import com.pthanalyzer.data.stats.StackedGraph;
import com.pthanalyzer.db.PDBFile;
import com.pthanalyzer.exception.GeneralPTHException;
import com.pthanalyzer.utils.CardUtils;
import com.pthanalyzer.utils.Constants;

public class TestStats {

	public static void main(String[] args) {
		
//		String urlFile = "D:/Downloads/PTH/logData/20151021_BBCS2/pokerth-log-2015-10-21_212337.pdb";
//		String urlFile = "D:/Downloads/PTH/logData/20151028_BBCS2/pokerth-log-2015-10-28_212613.pdb";
		String urlFile = "D:/Downloads/PTH/logData/WeC_2908/pokerth-log-2015-11-18_215849.pdb";
		
		if (args != null && args.length > 0) { 
			urlFile = args[0]; 
		}
		
		PDBFile pdbFile = null;
		try {
			pdbFile = new PDBFile(urlFile).loadPDB();
			for (Game game : pdbFile.getGames()) { 
				DataGame dataGame = pdbFile.getMapGame().get(game.getUniqueId()); 
//				getPlotData(dataGame, dataGame.getPlayers(), null);
				
				StatsGame sg = dataGame.getStatsGame(); 
				
				BasicDataStats bds = sg.getBasicDataStats(); 
				analyzeBasicData(bds); 
				
				List<FinalResult> listRanking = sg.getFinalResults(); 
				analyzeRanking(listRanking);
				
				Map<Position, List<Long>> mapCash = sg.getLineGraph();
				analyzeMapCash(mapCash);
				
				Map<Hand, List<StackedGraph>> mapStack = sg.getStackedGraph();
				analyzeMapStack(mapStack);
				
				List<PotGraph> listPG = sg.getPotGraph(); 
				analyzerPotGraph(listPG);
				
				List<HandsPlayed> listHP = sg.getHandsPlayed(); 
				analyzeHandsPlayer(listHP); 

				List<BestHand> listBH = sg.getBestHands(); 
				analyzeBestHands(listBH);
				
				List<HandWon> listHW = sg.getMostWins(); 
				analyzeHandsWin(listHW); 

				List<HighestWin> listHiW = sg.getHighestWins();
				analyzeHighestWins(listHiW);
				
				List<ConsSeries> seriesWin = sg.getSeriesWin();
				List<ConsSeries> seriesLose = sg.getSeriesLose();
				Map<Integer, List<ConsSeries>> mapSeries = new HashMap<>(); 
				mapSeries.put(WINNER_SERIES, seriesWin);
				mapSeries.put(LOSER_SERIES, seriesLose);
				analyzeMapSeries(mapSeries);

				List<MostRaiseStats> listMR = sg.getMostBetsRaises(); 
				analyzeMostRaises(listMR); 

				List<AllInStat> listAI = sg.getAllIns(); 
				analyzeAllIns(listAI);
			}
			
		} catch (GeneralPTHException e) {
			e.printStackTrace();
		} 
	}
	
	private static void analyzeBasicData(BasicDataStats bds) {
		System.out.println(" ============== BASIC DATA ============== ");
		
		System.out.println("Number of players: " + bds.getPlayers());
		System.out.println("Winner:            " + bds.getWinner().getPlayerName());
		System.out.println("Hands:             " + bds.getHands());
		System.out.println("Startmoney:        " + bds.getStartMoney());
		System.out.println("Start big blind:   " + bds.getStartBB());
		System.out.println("End big blind:     " + bds.getEndBB());
	}
	
	public static void analyzeRanking(List<FinalResult> listRanking) {
		System.out.println(" ============== RANKING ============== ");
		
		for (FinalResult fr : listRanking) { 
			if (fr.getWinnerPocket() != null) { // winner 
				System.out.println(fr.getPos().getPos() + ": " + 
						fr.getPos().getPlayer().getPlayerName() + "; " + fr.getPos().getHandOut() + "; " + 
						"wins with [" + CardUtils.getStringCards(fr.getWinnerPocket()[0], false) + ", " + 
						CardUtils.getStringCards(fr.getWinnerPocket()[1], false) + "] " + 
						fr.getWinnerHand()); 
			}
			else { 
				if (fr.getEliminatedBy() == null) {
					System.out.println();
				}
				else { 
					Player[] elim = fr.getEliminatedBy();
					String elimBy = ""; 
					if (elim.length > 1) { 
						for (int i = 0; i < elim.length; i++) { 
							elimBy += elim[i].getPlayerName() + 
									((i == elim.length -1) ? "" : " and "); 
						}
					}
					else { 
						elimBy = elim[0].getPlayerName(); 
					}
					System.out.println(fr.getPos().getPos() + ": " + 
							fr.getPos().getPlayer().getPlayerName() + "; " + fr.getPos().getHandOut() + "; " + 
							"eliminated by " + elimBy); 
				}
			}
		}
	}
	

	public static void analyzeMapCash(Map<Position, List<Long>> map) { 
		for (Entry<Position, List<Long>> entry : map.entrySet()) {
			Position pos = entry.getKey();
			System.out.println("Position " + pos.getPos() + " por player " + pos.getPlayer());
			System.out.println("\tCash: " + entry.getValue().toString());
		}
		
	}
	
	public static void analyzeMapSeries(Map<Integer, List<ConsSeries>> map) { 
		List<ConsSeries> winSeries = map.get(Constants.WINNER_SERIES);
		List<ConsSeries> loseSeries = map.get(Constants.LOSER_SERIES); 
		
		int i = 0; 
		System.out.println(" === WIN SERIES === ");
		for (ConsSeries cons : winSeries) {
			System.out.println("\tPos " + (++i) +"=> Duration: " + cons.getHands() + "; Player: " + cons.getPlayer().getPlayerName() + 
					"; Hands: " + cons.getStartHand() + "-" + cons.getEndHand() + "; Gain: " + cons.getAmount());
		}

		i = 0;
		System.out.println(" === LOSE SERIES === ");
		for (ConsSeries cons : loseSeries) {
			System.out.println("\tPos " + (++i) +"=> Duration: " + cons.getHands() + "; Player: " + cons.getPlayer().getPlayerName() + 
					"; Hands: " + cons.getStartHand() + "-" + cons.getEndHand() + "; Loss: " + cons.getAmount());
		}
	}
	
	private static void analyzeMapStack(Map<Hand, List<StackedGraph>> mapStack) {
		for (Entry<Hand, List<StackedGraph>> entry : mapStack.entrySet()) { 
			Hand hand = entry.getKey(); 
			List<StackedGraph> list = entry.getValue(); 
			
			System.out.println("=== HAND " + hand.getHandId() + " === ");
			
			for (StackedGraph sg : list) { 
				System.out.println("\tPlayer: {" + sg.getPlayer().getSeat() + ", " + sg.getPlayer().getPlayerName() + 
						"} = {" + sg.getPct().toPlainString() + ", " + sg.getPct().setScale(1, RoundingMode.HALF_UP).toPlainString() + ", " + sg.getStack() + "}");
			}
		}
	}
	
	private static void analyzerPotGraph(List<PotGraph> listPG) {
		System.out.println(" ============== POT GRAPH ============== ");

		long lastSB = 0L; 
		for (PotGraph pg : listPG) { 
			if (lastSB != pg.getSb().longValue()) {
				System.out.println("\t SB: " + pg.getSb().longValue() + "; BB: " + pg.getSb().longValue()*2L);
				lastSB = pg.getSb().longValue(); 
			}
			System.out.println("\t\tHand: " + pg.getHandId() + "; Pot: " + pg.getPot());
		}
	}

	
	
	private static void analyzeBestHands(List<BestHand> listBH) {
		int i = 0; 
		System.out.println(" ============== BEST HANDS ============== ");
		for (BestHand bh : listBH) { 
			System.out.println((++i) + ": " + bh.getHandTxt() + "; " + bh.getPlayer().getPlayerName() + "; " + 
					bh.getHandId().longValue() + "; " + (bh.getAmount().longValue() > 0L ? 
							"wins " + bh.getAmount().longValue() : "loses " + (-bh.getAmount().longValue())));  
		}
		
	}

	private static void analyzeHandsPlayer(List<HandsPlayed> listHP) {
		int i = 0; 
		
		System.out.println(" ============== HANDS PLAYED per PLAYER ============== ");
		for (HandsPlayed hp : listHP) { 
			System.out.println((++i) + ": " + hp.getPlayer().getPlayerName() + " ==> " + 
					(hp.getPctT() == null ? " - " : hp.getPctT() + "% ") + "(" + hp.getHandsPlayedT() + "/" + hp.getTotalHandsT() + "); " + 
					(hp.getPctT_7() == null ? " - " : hp.getPctT_7() + "% ") + "(" + hp.getHandsPlayedT_7() + "/" + hp.getTotalHandsT_7() + "); " +
					(hp.getPct6_4() == null ? " - " : hp.getPct6_4() + "% ") + "(" + hp.getHandsPlayed6_4() + "/" + hp.getTotalHands6_4() + "); " +
					(hp.getPct3_1() == null ? " - " : hp.getPct3_1() + "% ") + "(" + hp.getHandsPlayed3_1() + "/" + hp.getTotalHands3_1()+ "); ");
		}
	}

	private static void analyzeHandsWin(List<HandWon> listHW) {
		int i = 0; 

		System.out.println(" ============== MOST WINS ============== ");
		for (HandWon hw : listHW) { 
			System.out.println((++i) + ": " + hw.getPlayer().getPlayerName()  + " " + hw.getWins() + "(" + 
					(hw.getPlayed() == 0 ? " - %) " : BigDecimal.valueOf(hw.getWins()*100D/hw.getPlayed()).
							setScale(0, RoundingMode.HALF_UP).longValue() + "%)") + 
					" ==> " + hw.getHighestWin());
		}
	}

	private static void analyzeHighestWins(List<HighestWin> listHiW) {
		int i = 0; 
		
		System.out.println(" ============== HIGHEST WINS ============== ");
		for (HighestWin hw : listHiW) { 
			System.out.println((++i) + ": " + hw.getAmount() + "; " + hw.getPlayer().getPlayerName()  + "; " + 
					hw.getHandId());
		}
	}

	private static void analyzeMostRaises(List<MostRaiseStats> listMR) {
		int i = 0; 
		
		System.out.println(" ============== MOST BET/RAISE ============== ");
		for (MostRaiseStats mr : listMR) {
			System.out.println((++i) + ": " + mr.getPlayer().getPlayerName()  + " ==> " + 
					mr.getNumRaises() + " (" +
					(mr.getNumPlayed() == 0L ? "0%)" :
					BigDecimal.valueOf(mr.getNumHandsRaised()*100D/mr.getNumPlayed()).
						setScale(0, RoundingMode.HALF_UP).longValue() + "%) "));
		}

		System.out.println("[Player, TotalRaises, HandsRaised, HandsPlayed]");
		for (MostRaiseStats mr : listMR) { 
			System.out.println("[" + mr.getPlayer().getPlayerName() + "; " + 
					mr.getNumRaises() + "; " + mr.getNumHandsRaised() + "; " +   
					mr.getNumPlayed() + "]");
			
		}
		
	}

	private static void analyzeAllIns(List<AllInStat> listAI) {
		int i = 0; 
		
		System.out.println(" ============== ALL INS ============== ");
		for (AllInStat ai : listAI) { 
			System.out.println((++i) + ": " + ai.getPlayer().getPlayerName()  + " ==> " + 
					ai.getTotalAllIns() + " ("  + 
					(ai.getTotalHandsPlayed() == 0L ? "0%); " : 
						BigDecimal.valueOf(ai.getTotalAllIns()*100D/ai.getTotalHandsPlayed()).
							setScale(0, RoundingMode.HALF_UP).longValue() + "%); ") +
					ai.getInPreFlop() + "; " + ai.getFirst5Hands() + "; " + ai.getTotalWon());
		}
		
	}


}
