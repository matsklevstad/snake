package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileHandler implements FileHandlerInterface{
	
	public static BoardController game;
	public static int topTenScore;
	public static int topFiveScore;
	public static int highscoreScore;
	
	public File getAppStateFile() {
		return new File(System.getProperty("user.home") + "/Applications/tdt4100-app/scorefile.txt");
	}

	public String getScoresFromFile(File filename){
		BufferedReader reader;
		File file = getAppStateFile();
		String output = "";
		List<Highscore> allScores = new ArrayList<Highscore>();
		
		StartMenuController.totalGames = 0;
		StartMenuController.totalScore = 0;
		
		try {
				if (!file.isFile() && !file.exists()) {
					file.createNewFile();
				
				}
			
				reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();
				
				
				while(line != null) {
					String[] parts = line.split(";");
					Integer points = Integer.valueOf(parts[0]);
					StartMenuController.totalScore += points;
					StartMenuController.totalGames += 1;

					String time = parts[1].substring(0, 16);
					String name = parts[2];
					Highscore newHighscore = new Highscore(name, points, time);
					
					allScores.add(newHighscore);
					line = reader.readLine();
				
			}
			reader.close();
			
			Collections.sort(allScores);
			Collections.reverse(allScores);
			int placement = 0;
			
			for (Highscore highscore : allScores) {
					placement ++;
					if(placement <= 10) {
						if(placement == 10) {
							topTenScore = highscore.getPoints();
						}
						
						if(placement == 5) {
							topFiveScore = highscore.getPoints();
						}
						
						if(placement == 1) {
							highscoreScore = highscore.getPoints();
						}
						
						// Litt "stygg" koding bak formateringen på highscores, men :
						
						// Case som oppstår hvis man er på topplista (1-9.plass), men har mindre enn 10 poeng.(Veldig sjeldent
						// dette vil skje). Da legges det til litt space her og der. Samt fjerne punktumen
						// i toString-en og heller lage et selv her. 
						if(highscore.getPoints() < 10 && placement != 10) {
							output += " "  + placement + ". " + highscore.toString().substring(1) + "\n";
						}
						
						// Samme case som ovenfor, men utligne nå lengden på tallet ti samt mindre enn ti poeng.
						else if(highscore.getPoints() < 10 && placement == 10) {
							output += placement + ". " + highscore.toString().substring(1) + "\n";
						}
						
						// Hvis man på 1-9.plass og ikke har mindre enn 10 poeng, så formateres det slik. Bare
						// legger på en space på plasseringen
						else if(placement != 10) {
							output += " " + placement + highscore.toString() + "\n";
						}
						// Unngår å legge til et space dersom man er på 10.plass.
						else {
							output += placement + highscore.toString() + "\n";
							
						}
					}
				}
			return output;
			
		} catch(Exception e) {
			System.out.println("Could not find the requested file....");
			e.printStackTrace();
		}
		return output;
		
	}	
	
	public void writeScoreToFile() {
		
		FileOutputStream fos = null;
		File file;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  
		String outputToFile =  BoardController.game.getFruitScore() +  ";" + formatter.format(date) + ";" + StartMenuController.getUsername() + "\n";
	    
	    try {
	    	file = getAppStateFile();
	    	fos = new FileOutputStream(file, true);
	    	
	    	if (!file.exists()) {
	    		file.createNewFile();
	    	}
	    	
	    	byte[] bytesArray = outputToFile.getBytes();
	    	fos.write(bytesArray);
	    	fos.flush();
	    	System.out.println("File written successfully.");
	    	
	    }
	    catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
	    
	    finally {
	    	try {
	    		if (fos != null);
	    		{
	    			fos.close();
	    		}
	    	}
	    	catch(IOException ioe) {
	    	System.out.println("Error in closing the stream");
	    	}
	    }
	}

	

//	public void writeScoreToFile(File filename) {
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
//	    Date date = new Date();  
//	    
//		try {
//			FileWriter fw = new FileWriter(filename, true);
//			
//			fw.write(BoardController.game.getFruitScore() + ";" + formatter.format(date) + ";" + StartMenuController.getUsername() + "\n");
//			fw.close();
//		}
//		catch(IOException ioe) 
//		{
//			System.err.println("IOException " + ioe.getMessage());
//		}
//	}	
}
