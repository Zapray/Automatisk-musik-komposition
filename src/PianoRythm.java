import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PianoRythm {

	private ArrayList<Chord> fixedChordList = new ArrayList<Chord>();
	
	public PianoRythm(ArrayList<Chord> chordList, String partOfSong){
		

		ArrayList<Float> rythm = chooseRythmFromDatabase(partOfSong);
		
		//for(int i=0;i<rythm.size();i++){
			//System.out.println(rythm.get(i));
		//}
		
		
		this.fixedChordList = adaptChordsToRythm(chordList,rythm);
		
		
		
		
	}

	private ArrayList<Chord> adaptChordsToRythm(ArrayList<Chord> chordList,
			ArrayList<Float> rythm) {
		ArrayList<Chord> fixedChordList = new ArrayList<Chord>();
		
		int rythmcounter = 0;
		
		
		
		for(int i = 0; i<chordList.size(); i++){
			float totRythmDur = 0;
			int index = 0;
			//System.out.println("Loopiteration:  " + i);
			while(totRythmDur < 0.5){
				//System.out.println("totRytmDur:  " + totRythmDur + "   index:  " + index + "    rythmcounter: " + rythmcounter );
				totRythmDur = totRythmDur + rythm.get(index + rythmcounter);
				index++;
				if(index+rythmcounter ==rythm.size()){
					//System.out.println("JAG BREAKAR");
					break;
				}
			}
			
			for(int j = rythmcounter; j<index+rythmcounter;j++){
				fixedChordList.add(new Chord(chordList.get(i).getLabel(),rythm.get(j)));
			}
			
			
		    i = i + (int) Math.floor(2*totRythmDur) -1;
			
		    
		    rythmcounter = rythmcounter + index;
			
			if(rythmcounter == rythm.size()){
				//System.out.println("reset");
				rythmcounter = 0;
			}
			
			
			
			
			
		}
		
		
		
		
		
		
		
		return fixedChordList;
	}

	private ArrayList<Float> chooseRythmFromDatabase(String partOfSong) {
		
		
		String filePath = System.getProperty("user.dir")+"/PianoRythmDatabase/pianorythm_" + partOfSong + ".txt";
		ArrayList<Float> durations = new ArrayList<Float>();
		
		BufferedReader in = null;
		try{
			in =new BufferedReader(new FileReader(filePath));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int nbrOfRythms = 0;
		try{
			String line=in.readLine();
			while(line !=null){
				
				if(line.equals("-")){
					
					nbrOfRythms++;
				}
				line=in.readLine();
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		Random rand = new Random();
		int randomNum = rand.nextInt((nbrOfRythms));
		
		try{
			in =new BufferedReader(new FileReader(filePath));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		int barCounter = 0;
		try{
			String line=in.readLine();
			while(line !=null){
				if(line.equals("-")){
					barCounter++;
				}
				if(barCounter == randomNum){
					line=in.readLine();
						while(!line.equals("-")){
							durations.add(Float.parseFloat(line));
							line=in.readLine();
						}
					
				}else{
				line=in.readLine();
				}
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	
		
		
		
		return durations;
	}
	
	public static void main(String[] args){
		ArrayList<Chord> listan = new ArrayList<Chord>();
		PianoRythm piano = new PianoRythm(listan,"chorus");
		
		}

	public ArrayList<Chord> getFixedRythm() {
		return fixedChordList;
	}
}
