package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class BasicStructureGenerator {
	private ArrayList<Motive> listOfMotifs = new ArrayList<Motive>();
	
	
	public BasicStructureGenerator(String partOfSong, int nbrOfSections){
		
		String filePathStruc = System.getProperty("user.dir")+"/StructureParts/pianorythm_" + partOfSong + ".txt";
		String filePathMotif = System.getProperty("user.dir")+"/MotifParts/" + partOfSong + ".txt";
		ArrayList<String> struc = new ArrayList<String>();
		ArrayList<String> motifs = new ArrayList<String>();
		ArrayList<Integer> nbrOfSektionsStruc = new ArrayList<Integer>();
		ArrayList<Integer> nbrOfSektionsMotif = new ArrayList<Integer>();
		
		
		
		
		BufferedReader inStruc = null;
		BufferedReader inMotif = null;
		try{
			inStruc = new BufferedReader(new FileReader(filePathStruc));
			inMotif = new BufferedReader(new FileReader(filePathMotif));
		}catch (IOException e) {
			e.printStackTrace();
		}
		int nbrOfStruc = 0;
		int nbrOfMotif = 0;
		int nbrOfSec=0;
		
		try{
			String line=inStruc.readLine();
			while(line !=null){
				
				if(line.equals("-")){
					nbrOfStruc++;
					nbrOfSektionsStruc.add(nbrOfSec);
					nbrOfSec=0;
				}else{
					nbrOfSec++;
				}
				line=inStruc.readLine();
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStruc != null)inStruc.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		try{
			String line=inMotif.readLine();
			while(line !=null){
				
				if(line.equals("-")){
					
					nbrOfMotif++;
					nbrOfSektionsMotif.add(nbrOfMotif);
					nbrOfMotif=0;
				}else{
					nbrOfMotif++;
				}
				line=inMotif.readLine();
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inMotif != null)inMotif.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		Random randStruc = new Random();
		Random randMotif = new Random();
		int randomStruc = randStruc.nextInt((nbrOfStruc));
		int randomMotif = randMotif.nextInt((nbrOfMotif));
		
		while(nbrOfSections != nbrOfSektionsStruc.get(randomStruc)){
			randomStruc = randStruc.nextInt((nbrOfStruc));
		}
		
		while(nbrOfSections != nbrOfSektionsMotif.get(randomMotif)){
			randomMotif = randStruc.nextInt((nbrOfMotif));
		}
		
		try{
			inStruc =new BufferedReader(new FileReader(filePathStruc));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		int barCounter = 0;
		try{
			String line=inStruc.readLine();
			while(line !=null){
				if(line.equals("-")){
					barCounter++;
				}
				if(barCounter == randomStruc){
					line=inStruc.readLine();
						while(!line.equals("-")){
							struc.add(line);
							line=inStruc.readLine();
						}
					
				}else{
				line=inStruc.readLine();
				}
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStruc != null)inStruc.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		
		try{
			inMotif =new BufferedReader(new FileReader(filePathMotif));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		barCounter = 0;
		try{
			String line=inMotif.readLine();
			while(line !=null){
				if(line.equals("-")){
					barCounter++;
				}
				if(barCounter == randomMotif){
					line=inMotif.readLine();
						while(!line.equals("-")){
							motifs.add(line);
							line=inMotif.readLine();
						}
					
				}else{
				line=inMotif.readLine();
				}
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inMotif != null)inMotif.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		
		
		
	}
}
