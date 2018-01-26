package com.pthanalyzer.db;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.almworks.sqlite4java.SQLiteException;
import com.pthanalyzer.data.Action;
import com.pthanalyzer.data.DataGame;
import com.pthanalyzer.data.Game;
import com.pthanalyzer.data.Hand;
import com.pthanalyzer.data.Player;
import com.pthanalyzer.exception.GeneralPTHException;

public class PDBFile {
	protected static final Logger logger = Logger.getLogger(PDBFile.class);
	
	private String fileName; 
	private List<Game> games; 
	private Map<Integer, DataGame> mapGame;

	public PDBFile(String fileName) {
		this.fileName = fileName;
	}
	
	// Getters and setters
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public Map<Integer, DataGame> getMapGame() {
		return mapGame;
	}

	public void setMapGame(Map<Integer, DataGame> mapGame) {
		this.mapGame = mapGame;
	}
	
	// Mètode princial de la classe. Carrega tota la informació del fitxer 
	// 	1. Carrega tota la informació del fitxer PDB
	//	2. Crea un Map amb la relació idGame/DataGame on DataGame conté la informació d'un Game
	public PDBFile loadPDB() throws GeneralPTHException { 
		if (fileName == null)  return null; 
		
		File file = new File(fileName); 
		if (!file.exists()) {
			throw new GeneralPTHException("Unable to locate file \"" + fileName + "\"");
//			logger.error("Unable to locate file \"" + fileName + "\"");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Error: File " + fileName + " not found");
//			box.open();
//			return null; 
		}
		
		LoadDB db = null; 
		try { 
			mapGame = new HashMap<Integer, DataGame>(); 
			
			db = new LoadDB(file); 
			this.games = db.loadGames();

			for (Game game : games) { 
				Integer idGame = game.getUniqueId();  
				List<Player> playersGame =  db.loadPlayers(idGame); 
				List<Hand> handsGame = db.loadHands(idGame);
				List<Action> actionsGame = db.loadActions(idGame); 
				
				DataGame dg = new DataGame(); 
				dg.setPlayers(playersGame);
				dg.setHands(handsGame);
				dg.setActions(actionsGame);
				dg.setGame(game);

				dg.analyzeGame(); 

				mapGame.put(idGame, dg);
			}
		}
		catch (SQLiteException e) { 
			throw new GeneralPTHException("Unable to load DB dataFile");
//			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//			box.setMessage("Unable to load DB dataFile");
//			box.open();
//			return null; 
		}
		finally {
			if (db != null ) db.close();
		}

//		if (__ISDEBUG__) printGames(); 
		
//		// Printing amount per hand
//		for (Game game : games) {
//			System.out.println("PRINTING AMOUNT PER GAME \"" + game.getUniqueId() + "\" AND PLAYER");
//			new Graphs(this).generalGraph(game.getUniqueId());
//			new Graphs(this).generalGraph2(game.getUniqueId());
//		}
		
		return this; 
	}
	
}
