import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PianoRythm {

	private ArrayList<Chord> fixedChordList = new ArrayList<Chord>();
	private ArrayList<Float> rythm = new ArrayList<Float>();
	private ArrayList<String> ackord = new ArrayList<String>();
	private ArrayList<Integer> velocity = new ArrayList<Integer>();
	public PianoRythm(ArrayList<Chord> chordList, String partOfSong){
		

		chooseRythmFromDatabase(partOfSong);
		
		//for(int i=0;i<rythm.size();i++){
			//System.out.println(rythm.get(i));
		//}
		
		
		this.fixedChordList = adaptChordsToRythm(chordList);
		
		
		
		
	}

	private ArrayList<Chord> adaptChordsToRythm(ArrayList<Chord> chordList) {
		ArrayList<Chord> fixedChordList = new ArrayList<Chord>();
		
		int rythmcounter = 0;
		
		
		
		for(int i = 0; i<chordList.size(); i++){
			float totRythmDur = 0;
			int index = 0;
			while(totRythmDur < 0.5){
				totRythmDur = totRythmDur + rythm.get(index + rythmcounter);
				index++;
				if(index+rythmcounter ==rythm.size()){
					break;
				}
			}
			
			for(int j = rythmcounter; j<index+rythmcounter;j++){
				
				if(ackord.get(j)==null){
					fixedChordList.add(new Chord(null,rythm.get(j),velocity.get(j)));
				
				}else{
					fixedChordList.add(new Chord(chordList.get(i).getLabel(),rythm.get(j),velocity.get(j)));
				}
				
				
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

	private void chooseRythmFromDatabase(String partOfSong) {
		
		
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
							boolean comma1 = true;
							boolean comma2 = true;
							int index1 = 0;
							int index2 = 0;
						
							for(int k = 0; k<line.length(); k++){
								if(line.charAt(k)==',' && comma1){
									index1=k;
									rythm.add(Float.parseFloat(line.substring(0,k)));
									comma1= false;
								}else if(line.charAt(k)==',' && comma2){
									index2=k;
									ackord.add(line.substring(index1+1,k));
									velocity.add(Integer.parseInt(line.substring(k+1, line.length())));
									comma2=false;
								}
								
							}
							
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
		
	
		
		
		
	}
	
	public static void main(String[] args){
		ArrayList<Chord> listan = new ArrayList<Chord>();
		PianoRythm piano = new PianoRythm(listan,"test");
		
		}

	public ArrayList<Chord> getFixedRythm() {
		return fixedChordList;
	}
}
