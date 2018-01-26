package com.pthanalyzer.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.pthanalyzer.data.DataGame;
import com.pthanalyzer.data.Game;
import com.pthanalyzer.data.Hand;
import com.pthanalyzer.data.Player;
import com.pthanalyzer.data.Position;
import com.pthanalyzer.data.stats.PotGraph;
import com.pthanalyzer.data.stats.StackedGraph;
import com.pthanalyzer.db.PDBFile;
import com.pthanalyzer.exception.GeneralPTHException;

public class TestGraphs {

	public static void main(String[] args) {
		
//		String urlFile = "D:/Downloads/PTH/logData/20151021_BBCS2/pokerth-log-2015-10-21_212337.pdb";
//		String urlFile = "D:/Downloads/PTH/logData/20151028_BBCS2/pokerth-log-2015-10-28_212613.pdb";
		String urlFile = "D:/Downloads/PTH/logData/WeC_2908/pokerth-log-2015-11-18_215849.pdb";
		
		final String savedGraphsPath = "D:/Downloads/PTH/logData/graphs/temp/";
		String gameName = ""; 

		if (args != null && args.length > 0) { 
			urlFile = args[0]; 
		}
		
		PDBFile pdbFile = null;
		try {
			pdbFile = new PDBFile(urlFile).loadPDB();
			for (Game game : pdbFile.getGames()) { 
				final long timeInMilis = System.currentTimeMillis(); 
				if (pdbFile.getGames().size() > 1) gameName = "_Game_" + game.getUniqueId(); 

				DataGame dataGame = pdbFile.getMapGame().get(game.getUniqueId());
				Dimension dim = new Dimension(736, 240);
				
				String fileName = urlFile.substring(urlFile.lastIndexOf('/') + 1, urlFile.lastIndexOf('.')); 
				fileName = savedGraphsPath + fileName + gameName + "_" + timeInMilis; 
				
				saveCashPlot(dataGame, fileName + "_Cash.png", dim); 
				saveStackedPlot(dataGame, fileName + "_Stacked.png", dim);
				savePotPlot(dataGame, fileName + "_Pot.png", dim);
			}
		} 
		catch (GeneralPTHException e) {
			e.printStackTrace();
		} 
	}
	
	private static void saveCashPlot(DataGame dataGame, String fileName, Dimension dim) { 
		Map<Position, List<Long>> mapCash = dataGame.getStatsGame().getLineGraph(); // Get data
		JFreeChart chart = getChartCashPlot(mapCash, dataGame, null); // Create chart
		saveChart2File(chart, dim, fileName); // Save to file 
	}
	
	private static void saveStackedPlot(DataGame dataGame, String fileName, Dimension dim) { 
		JFreeChart chart = getChartStackedPlot(dataGame);
		saveChart2File(chart, dim, fileName); 
	}
	
	private static void savePotPlot(DataGame dataGame, String fileName, Dimension dim) { 
		JFreeChart chart = getChartPotPlot(dataGame);
		saveChart2File(chart, dim, fileName); 
	}

	/**
	 * Utility to store {@link JFreeChart} into a PNG file 
	 * 
	 * @param chart
	 * @param dim
	 * @param fileName
	 */
	private static void saveChart2File(JFreeChart chart, Dimension dim, String fileName) { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		try { 
			ChartUtilities.writeChartAsPNG(baos, chart, dim.width, dim.height);
		}
		catch(IOException e) {
			System.err.println("Error");
			e.printStackTrace(); 
		}
		finally { 
			if (baos != null) try {baos.close();} catch (IOException e) {e.printStackTrace();} 
		}

		// Write to a file
		FileOutputStream fos = null; 
		File file = null; 
		
		try { 
			file = new File(fileName);
			if (file.exists()) file.delete(); 
			
			fos = new FileOutputStream(file); 
			fos.write(baos.toByteArray());
		}
		catch (IOException e) { 
			e.printStackTrace();
		}
	
	}

	// Creates JFreeChart for Cash per Player 
	private static JFreeChart getChartCashPlot(Map<Position, List<Long>> mapCash, DataGame dataGame, Player highlight) {
		
		JFreeChart chart = null; 
		XYSeriesCollection xyCol = new XYSeriesCollection();
		
		for (Entry<Position, List<Long>> entry : mapCash.entrySet()) { 
			Player player = entry.getKey().getPlayer();
			XYSeries xy = new XYSeries(player.getPlayerName());
			for (int i = 0; i < entry.getValue().size(); i++) {
				xy.add(i, entry.getValue().get(i));
			}
			xyCol.addSeries(xy);
		}
		
		chart = ChartFactory.createXYLineChart("Course of the game", "Hand", 
				"Cash", xyCol, PlotOrientation.VERTICAL, false, true, false);
		
		// Chart BG Color 
		chart.setBackgroundPaint(new Color(240, 240, 240));

		// Plot
		XYPlot xyplot = chart.getXYPlot();
		
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setBaseShapesFilled(true);
		
		Color[] colors = new Color[] { 
				new Color(240, 5, 1), new Color(13, 191, 0), new Color(9, 8, 210), new Color(255, 0, 255), 
				new Color(11, 252, 246), new Color(0, 0, 0), new Color(236, 208, 21), 
				new Color(1, 149, 255), new Color(151, 153, 148), new Color(255, 145, 103) 
		};
		
		
		// Add Legend (shape and color)  
		int i = 0;
		LegendItemCollection items = new LegendItemCollection(); 
		for (Map.Entry<Position, List<Long>> entry : mapCash.entrySet()) {
			xylineandshaperenderer.setSeriesShapesVisible(i, false);
			xylineandshaperenderer.setSeriesPaint(i, colors[i]);
			items.add(new LegendItem(entry.getKey().getPlayer().getPlayerName(), null, null, null, 
					new Rectangle2D.Double(-6.0, -3.0, 12.0, 6.0), colors[i]));
			i++; 
		}

		xyplot.setFixedLegendItems(items);
		xyplot.setInsets(new RectangleInsets(5, 5, 5, 20));
		LegendTitle legend = new LegendTitle(xyplot); 
		legend.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(legend);
		
		// To make line stroke thicker (if player selected) 
		if (highlight != null) { 
			Position posSel = mapCash.keySet().stream().
					filter(pos -> pos.getPlayer().getSeat().intValue() == highlight.getSeat().intValue()).
					findFirst().get(); 
			xylineandshaperenderer.setSeriesStroke(posSel.getPos(), new BasicStroke(4.0f));
		}

		// Plot BG Color
		xyplot.setBackgroundPaint(Color.white);
		
		// Rang X (graella iterna)
		xyplot.setRangeGridlinePaint(Color.lightGray);
		xyplot.setRangeGridlinesVisible(true);
		// Rang Y (graella iterna)
		xyplot.setDomainGridlinePaint(Color.lightGray);
		xyplot.setDomainGridlinesVisible(true);
		
		RangeGame rg = new RangeGame(dataGame);
		
		// Eix Y
		NumberAxis rangeAxis = (NumberAxis)xyplot.getRangeAxis();
		rangeAxis.setRange(rg.rangeY);
		rangeAxis.setTickUnit(rg.unitY);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// Eix X
		NumberAxis domainAxis = (NumberAxis) xyplot.getDomainAxis(); 
		domainAxis.setRange(rg.rangeX);
		domainAxis.setTickUnit(rg.unitX);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		return chart;
	}

	// Get cash related to other players per hand
	public static JFreeChart getChartStackedPlot(DataGame dataGame) { 
		Map<Hand, List<StackedGraph>> mapStack = dataGame.getStatsGame().getStackedGraph();

		Color[] colors = new Color[] { 
				new Color(240, 5, 1), new Color(13, 191, 0), new Color(9, 8, 210), new Color(255, 0, 255), 
				new Color(11, 252, 246), new Color(0, 0, 0), new Color(236, 208, 21), 
				new Color(1, 149, 255), new Color(151, 153, 148), new Color(255, 145, 103) 
		};

		DefaultTableXYDataset tableXY = new DefaultTableXYDataset(); 
		XYSeries xys = null; 

		for (Position pos : dataGame.getPositions()) {
			xys = new XYSeries(pos.getPlayer().getPlayerName(), true, false); 

			for (Entry<Hand, List<StackedGraph>> entry : mapStack.entrySet()) {
				Hand hand = entry.getKey(); 
				StackedGraph sg = entry.getValue().stream().filter(s -> 
							s.getPlayer().getSeat().intValue() == pos.getPlayer().getSeat().intValue()).
						findFirst().orElse(null);

				if (sg == null) continue; 
				else { 
					xys.add(hand.getHandId(), sg.getStack());
				}
			}
			tableXY.addSeries(xys);
		}
		
		JFreeChart chart = ChartFactory.createStackedXYAreaChart("Stacked", "Hand", 
				"Player Cash", tableXY, PlotOrientation.VERTICAL, false, false, false);
		
		chart.setBackgroundPaint(new Color(240, 240, 240));
		
		XYPlot plot = chart.getXYPlot(); 

		
		StackedXYAreaRenderer2 render = (StackedXYAreaRenderer2) plot.getRenderer();
		render.setBaseItemLabelsVisible(true);

		LegendItemCollection items = new LegendItemCollection(); 
		for (int i = 0; i < dataGame.getPositions().size(); i++) {
			render.setSeriesPaint(i, colors[i]);
			items.add(new LegendItem(dataGame.getPositions().get(i).getPlayer().getPlayerName(), null, null, null, 
					new Rectangle2D.Double(-6.0, -3.0, 12.0, 6.0), colors[i]));
		}
		
		plot.setFixedLegendItems(items);
		plot.setInsets(new RectangleInsets(5, 5, 5, 20));
		LegendTitle legend = new LegendTitle(plot); 
		legend.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(legend);
		
		
		// Plot BG Color
		plot.setBackgroundPaint(Color.white);
		
		// Rang X (graella iterna)
		plot.setRangeGridlinesVisible(false);
		// Rang Y (graella iterna)
		plot.setDomainGridlinesVisible(false);
//
		RangeGame rg = new RangeGame(dataGame);
		
		// Eix Y
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(rg.rangeY);
		rangeAxis.setTickUnit(rg.unitY);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setLowerMargin(0.0D);
		rangeAxis.setUpperMargin(0.0D);

		// Eix X
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); 
//		domainAxis.setRange(rg.rangeX);
		domainAxis.setTickUnit(rg.unitX);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		domainAxis.setLowerMargin(0.0D);
		domainAxis.setUpperMargin(0.0D);

		// Returns object 
		return chart; 
	}
	
	private static JFreeChart getChartPotPlot(DataGame dataGame) {
		List<PotGraph> potData = dataGame.getStatsGame().getPotGraph();
		
		XYSeries xy = new XYSeries("Pot");
		for (PotGraph pg : potData) {
			xy.add(pg.getHandId(), pg.getPot());
		}

		JFreeChart chart = ChartFactory.createXYBarChart("Pot", "Hand", false, "Pot Size", 
				new XYSeriesCollection(xy), PlotOrientation.VERTICAL, false, false, false); 
		chart.setBackgroundPaint(new Color(240, 240, 240));
		
		XYPlot plot = chart.getXYPlot(); 
		
		// Plot BG Color
		plot.setBackgroundPaint(Color.white);
		
		// Rang X (graella iterna)
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlineStroke(new BasicStroke(0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f, 10.0f }, 0.0f));
		
		// this(1.0f, CAP_SQUARE, JOIN_MITER, 10.0f, null, 0.0f);
		
		// Rang Y (graella iterna)
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setDomainGridlineStroke(new BasicStroke(0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f, 10.0f }, 0.0f));
		plot.setDomainGridlinesVisible(true);

		XYBarRenderer render = (XYBarRenderer) plot.getRenderer();
		
//		GradientPaint gp = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 64));
		GradientPaint gp = new GradientPaint(0.0F, 0.0F, new Color(124, 146, 36), 0.0F, 0.0F, new Color(178, 221, 107));
		render.setSeriesPaint(0, gp);
		render.setDrawBarOutline(false);

		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); 		
		domainAxis.setLowerMargin(0.0D);
		domainAxis.setUpperMargin(0.0D);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		numberaxis.setLowerMargin(0.0D);
		numberaxis.setUpperMargin(0.0D);
		
		long previousSB = -1L;
		long previousMark = -1L;
		
//		Color[] intervalColors = {new Color(223, 223, 223, 50), new Color(236, 236, 236, 50)};
		Color[] intervalColors = {new Color(160, 160, 160, 100), new Color(160, 160, 160, 75)};
		int intervalSet = 0; 

		// how many marks? 
		int marks= potData.stream().collect(Collectors.groupingBy(PotGraph::getSb)).size();
		System.out.println("Marks: " + marks + ": " + potData.stream().collect(Collectors.groupingBy(PotGraph::getSb)).keySet());
		
		
		
		for (PotGraph pg : potData) { 
			ValueMarker vm = null; 
			if (previousSB != pg.getSb().longValue()) { 
				if (previousSB != -1L) {
					vm = new ValueMarker(pg.getHandId().doubleValue(), Color.darkGray, 
							new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] { 1.0f, 3.0f }, 0.0f));
					
					plot.addDomainMarker(vm);
					
					IntervalMarker im = new IntervalMarker((double) previousMark, pg.getHandId().doubleValue());
					im.setPaint(intervalColors[intervalSet%2]); intervalSet++;

					// Set label
					String textLabel = String.valueOf(previousSB) + "/" + String.valueOf(previousSB*2); 
					
					double pctHands = ((double) pg.getHandId() - previousMark)/dataGame.getTotalHands().doubleValue(); 

					if (pctHands < 0.06D) { // set vertical
						double xPos = ((double) (pg.getHandId() - previousMark)/2D) + (double) previousMark; 
						double yPos = 
								(dataGame.getGame().getInitialStack().doubleValue() * (double) dataGame.getPlayers().size())/2D; 
						
						XYTextAnnotation label = 
								new XYTextAnnotation(textLabel, xPos, yPos);
						label.setFont(new Font(Font.SANS_SERIF, 0, 12));
						label.setRotationAnchor(TextAnchor.CENTER);
						label.setTextAnchor(TextAnchor.CENTER);
						label.setRotationAngle(-3.14 / 2);
						label.setPaint(Color.white);
						plot.addAnnotation(label);
					}
					else {  
						im.setLabel(textLabel);
						im.setLabelFont(new Font(Font.SANS_SERIF, 0, 12));
						im.setLabelAnchor(RectangleAnchor.CENTER);
						im.setLabelTextAnchor(TextAnchor.CENTER);
						im.setLabelPaint(Color.white);
					}
					
					plot.addDomainMarker(im);
				}
				previousSB = pg.getSb().longValue(); 
				previousMark = pg.getHandId().longValue(); 
			}
		}
		
		// We need to add last one 
		if (dataGame.getTotalHands().longValue() -  previousMark > 0L) { 
			IntervalMarker im = new IntervalMarker((double) previousMark, dataGame.getTotalHands().doubleValue());
			im.setPaint(intervalColors[intervalSet%2]); intervalSet++;

			// Set label
			String textLabel = String.valueOf(previousSB) + "/" + String.valueOf(previousSB*2); 
			
			double pctHands = ((double) dataGame.getTotalHands().longValue() - previousMark)/dataGame.getTotalHands().doubleValue(); 

			if (pctHands < 0.06D) { // set vertical
				double xPos = ((double) (dataGame.getTotalHands().longValue() - previousMark)/2D) + (double) previousMark; 
				double yPos = 
						(dataGame.getGame().getInitialStack().doubleValue() * (double) dataGame.getPlayers().size())/2D; 
				
				XYTextAnnotation label = 
						new XYTextAnnotation(textLabel, xPos, yPos);
				label.setFont(new Font(Font.SANS_SERIF, 0, 12));
				label.setRotationAnchor(TextAnchor.CENTER);
				label.setTextAnchor(TextAnchor.CENTER);
				label.setRotationAngle(-3.14 / 2);
				label.setPaint(Color.white);
				plot.addAnnotation(label);
			}
			else {  
				im.setLabel(textLabel);
				im.setLabelFont(new Font(Font.SANS_SERIF, 0, 12));
				im.setLabelAnchor(RectangleAnchor.CENTER);
				im.setLabelTextAnchor(TextAnchor.CENTER);
				im.setLabelPaint(Color.white);
			}
			
			plot.addDomainMarker(im);
		}

		return chart; 
	}
	

	@SuppressWarnings("unused")
	private static class RangeGame { 
		Range rangeX; 
		Range rangeY;
		NumberTickUnit unitX;
		NumberTickUnit unitY; 
		
		public RangeGame(PDBFile pdbFile, Integer idGame) { 
			DataGame dataGame = pdbFile.getMapGame().get(idGame);
			Game currentGame = pdbFile.getGames().stream().filter(game -> game.getUniqueId().intValue() == idGame.intValue()).findFirst().get();

			double hands = 0D; 
			if (dataGame.getTotalHands() > 0)
				hands = (double)dataGame.getTotalHands();
			else
				hands = (double) pdbFile.getMapGame().get(idGame).getHands().size();

			// Ranges
			rangeX = new Range(0D, Math.ceil((hands)/10D)*10D); 
			rangeY = new Range(0D, (double) dataGame.getPlayers().size()*currentGame.getInitialStack());
			
			// units 
			unitX = (hands > 300D ? new NumberTickUnit(20D) : new NumberTickUnit(10D));
			unitY = new NumberTickUnit(((double)currentGame.getInitialStack()*dataGame.getPlayers().size())/5D);
		}
		
		public RangeGame(DataGame dataGame) { 
			Game currentGame = dataGame.getGame(); 

			double hands = (double)dataGame.getTotalHands(); 

			// Ranges
			rangeX = new Range(0D, Math.ceil((hands)/10D)*10D); 
			rangeY = new Range(0D, (double) dataGame.getPlayers().size()*currentGame.getInitialStack());
			
			// units 
			unitX = (hands > 300D ? new NumberTickUnit(20D) : new NumberTickUnit(10D));
			unitY = new NumberTickUnit(((double)currentGame.getInitialStack()*dataGame.getPlayers().size())/5D);

		}
	}
	
}
