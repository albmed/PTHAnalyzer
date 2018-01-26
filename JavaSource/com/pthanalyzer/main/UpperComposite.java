package com.pthanalyzer.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.pthanalyzer.data.DataGame;
import com.pthanalyzer.data.Game;
import com.pthanalyzer.data.Player;
import com.pthanalyzer.db.PDBFile;
import com.pthanalyzer.language.Language;
import com.pthanalyzer.tabs.TabLayout;

public class UpperComposite extends Composite {
	
	protected static final Logger logger = Logger.getLogger(UpperComposite.class); 

	Combo comboGameSel = null; 
	Label labelGame = null;
	Label labelState = null; 
	Label labelStateTxt = null;
	Label labelWinner = null;
	Label labelWinnerTxt = null;
	Label labelHands = null;
	Label labelHandsTxt = null;
	
	PDBFile pdbFile = null;
	
	Composite parent = null; 
	UpperComposite upperComposite = null; 
	
	public int selectedGame = -1;
	
	public UpperComposite(Composite parent, int style, PDBFile pdbFile) {
		super(parent, style);
		this.parent = parent; 
		this.pdbFile = pdbFile; 
		this.upperComposite = this; 
		
		// Set layout
		GridLayout gridLayout = new GridLayout(10, true);
		this.setLayout(gridLayout);
		
		// Game Label 
		labelGame = new Label(this, SWT.NONE);
		labelGame.setAlignment(SWT.RIGHT);
//		labelGame.setText("Select Game: ");
		labelGame.setText(Language.getMessage(Language.UPPER_GAME_SELECT));
		
		// Combo 
		comboGameSel = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboGameSel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		comboGameSel.setToolTipText(Language.getMessage(Language.UPPER_GAME_SELECT_CHOOSE));
		setGameComboData(comboGameSel); 
		
		// State 
		labelState = new Label(this, SWT.NONE);
		labelState.setAlignment(SWT.RIGHT);
//		labelState.setText("State: ");
		labelState.setText(Language.getMessage(Language.UPPER_GAME_STATE));
		labelState.setVisible(false); 

		labelStateTxt = new Label(this, SWT.NONE);
		labelStateTxt.setAlignment(SWT.LEFT);
		labelStateTxt.setVisible(false);

		// Winner 
		labelWinner = new Label(this, SWT.NONE);
		labelWinner.setAlignment(SWT.RIGHT);
//		labelWinner.setText("Winner: ");
		labelWinner.setText(Language.getMessage(Language.UPPER_GAME_WINNER));
		labelWinner.setVisible(false); 
		
		labelWinnerTxt = new Label(this, SWT.NONE);
		labelWinnerTxt.setAlignment(SWT.LEFT);
		labelWinnerTxt.setVisible(false);
		
		// Hands 
		labelHands = new Label(this, SWT.NONE);
		labelHands.setAlignment(SWT.RIGHT);
//		labelHands.setText("Hands: ");
		labelHands.setText(Language.getMessage(Language.UPPER_GAME_HANDS));
		labelHands.setVisible(false); 
		
		labelHandsTxt = new Label(this, SWT.NONE);
		labelHandsTxt.setAlignment(SWT.LEFT);
		labelHandsTxt.setVisible(false);
	}
	
	public void setGameComboData(Combo combo) { 
		List<Game> games = pdbFile.getGames();
		String[] gamesStr = null; 
		
		if (games != null && !games.isEmpty()) {
			gamesStr = new String[games.size()];
			int i = 0; 
			for (Game game : games) { 
				//"GAME " + String.valueOf(game.getUniqueId());
				gamesStr[i] = Language.getMessage(Language.UPPER_GAME_SELECT_GAME, new Object[] {String.valueOf(game.getUniqueId())});
				i++; 
			}
			
			combo.setItems(gamesStr);
			combo.addSelectionListener(new GameComboSelector());
		}
		else {
			//FIXME: No system outs 
			System.out.println("NO Games found!");
		}
		
	}

	public void destroy() { 
		for (Control control : this.getChildren()) { 
			control.dispose();
			control = null; 
		}
		this.dispose();
	}
	
	class GameComboSelector extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			int sel = comboGameSel.getSelectionIndex(); 
			if (sel > -1 && sel < pdbFile.getGames().size()) { 
				DataGame dg = pdbFile.getMapGame().get(pdbFile.getGames().get(sel).getUniqueId());
				selectedGame = sel; 
				
				if (dg.isWinner()) { // Exists winner  
					labelState.setVisible(true);
					labelStateTxt.setText(Language.getMessage(Language.UPPER_GAME_STATE_COMPLETE));
					labelStateTxt.setForeground(new Color(null, 0, 168, 84));
					labelStateTxt.setVisible(true);
					
					Player player = dg.getWinner();
					labelWinner.setVisible(true);
					labelWinnerTxt.setText(player != null ? player.getPlayerName() : Language.getMessage(Language.WARNING_NOTFOUND));
					labelWinnerTxt.setVisible(true);
				}
				else { // No winner  
					labelState.setVisible(true);
					labelStateTxt.setText(Language.getMessage(Language.UPPER_GAME_STATE_INCOMPLETE));
					labelStateTxt.setForeground(new Color(null, 255, 0, 0));
					labelStateTxt.setVisible(true);

					labelWinner.setVisible(true);
					labelWinnerTxt.setText(Language.getMessage(Language.WARNING_NOVALUE));
					labelWinnerTxt.setVisible(true);
					
				}
				
				labelHands.setVisible(true);
				labelHandsTxt.setVisible(true);
				labelHandsTxt.setText(String.valueOf(dg.getTotalHands()));
				
				new TabLayout(parent); 
				
			}
			
			upperComposite.layout(true, true);
		} 
		
		
//		@Override
//		public void widgetSelected(SelectionEvent e) {
//			int sel = comboGameSel.getSelectionIndex();
//			List<Game> games = pdbFile.getGames();
//			
//			if (sel > -1 && sel < games.size()) { 
//				Game game = games.get(sel); 
////				logger.info("Selected game: " + sel);
//				selectedGame = sel; 
//
//				if (game.getWinnerSeat() != null && game.getWinnerSeat().longValue() > 0) { // Exists winner 
//					labelState.setVisible(true);
//					labelStateTxt.setText("Complete");
//					labelStateTxt.setForeground(new Color(null, 0, 168, 84));
//					labelStateTxt.setVisible(true);
//					
//					Player player = pdbFile.getPlayerPerGameSeat(game.getUniqueId(), game.getWinnerSeat());  
//					labelWinner.setVisible(true);
//					labelWinnerTxt.setText(player != null ? player.getPlayerName() : "NOT FOUND");
//					labelWinnerTxt.setVisible(true);
//				}
//				else { // No winner 
//					labelState.setVisible(true);
//					labelStateTxt.setText("Incomplete");
//					labelStateTxt.setForeground(new Color(null, 255, 0, 0));
//					labelStateTxt.setVisible(true);
//
//					labelWinner.setVisible(true);
//					labelWinnerTxt.setText(String.valueOf("--"));
//					labelWinnerTxt.setVisible(true);
//				}
//
//				labelHands.setVisible(true);
//				labelHandsTxt.setVisible(true);
//				int handsGame = pdbFile.getHandsPerGame(game.getUniqueId()).size(); 
//				labelHandsTxt.setText(String.valueOf(handsGame)); 
//			}
//			
//			upperComposite.layout(true, true);
//		}
		
	}
	
}
