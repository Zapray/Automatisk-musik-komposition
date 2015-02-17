import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class MidiManager {

	private List<ArrayList<Note>> songList = new ArrayList<ArrayList<Note>>();
	//All the songs unconverted from textfile, only the pitch
	private ArrayList<Float> unconPitchList = new ArrayList<Float>();
	 //All the songs unconverted from textfile, only the duration
	private ArrayList<Float> unconDurationList = new ArrayList<Float>();
	// Convert table used to convert between midivalues and model values for the pitch
	private ArrayList<Float> convertTablePitch = new ArrayList<Float>();
	//Convert table used to convert between midivalues and model values for the duration
	private ArrayList<Float> convertTableDuration = new ArrayList<Float>(); 
	// All the songs converted to model values, only the pitch
	private ArrayList<Integer> convertedPitchList= new ArrayList<Integer>();
	// All the songs converted to model values, only the duration
	private ArrayList<Integer> convertedDurationList = new ArrayList<Integer>(); 

	private int pMax;
	private int dMax;


	public MidiManager(String filePath){
		
		try{
		BufferedReader in =new BufferedReader(new FileReader(filePath));
		createUnconvertedArrays(in);
		createConvertTables();
		pMax=convertTablePitch.size();
		dMax=convertTableDuration.size();
		convertArraysToModelFormat();
		createSongList();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		




	}





	//Makes two ArrayList, one which contains all the the note pitches in the songs and one which contains
	//all the durations in the song. Nothing is converted 
	private void createUnconvertedArrays(BufferedReader  in){


		Float unconvertedNotePitch;
		Float unconvertedNoteDuration;
		try{
			String line=in.readLine();
			while(line !=null){			
				for(int i=0; i<line.length(); i++){
					if(line.charAt(i)=='-'){
						unconPitchList.add((float)-1);
						unconDurationList.add((float)-1);
					}
					if(line.charAt(i)==','){
						unconvertedNotePitch=Float.parseFloat(line.substring(0,i));
						unconvertedNoteDuration=Float.parseFloat(line.substring(i+1,line.length()));
						unconPitchList.add(unconvertedNotePitch);
						unconDurationList.add(unconvertedNoteDuration);

					}	
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


	}

	//Creating convering tables that show which pitch is which number in the model.
	//Same for duration
	private void createConvertTables(){
		for(int i=0;i<unconPitchList.size();i++){

			if(!convertTablePitch.contains(unconPitchList.get(i))){
				convertTablePitch.add(unconPitchList.get(i));
			}
			if(!convertTableDuration.contains(unconDurationList.get(i))){
				convertTableDuration.add(unconDurationList.get(i));
			}	
		}

		Collections.sort(convertTablePitch);
		Collections.sort(convertTableDuration);

	}

	//Change from unconverted to converted for the songs
	private void convertArraysToModelFormat(){
		for(int i = 0;i < unconPitchList.size();i++){
			if(unconPitchList.get(i)==-1){
				convertedPitchList.add(-1);
				convertedDurationList.add(-1);
			}else{
				convertedPitchList.add(convertTablePitch.indexOf(unconPitchList.get(i)));
				convertedDurationList.add(convertTableDuration.indexOf(unconDurationList.get(i)));
			}
		}	

	}




	private void createSongList(){
		ArrayList<Note> song=new ArrayList<Note>(0);
		List<Integer> tempPitch = new ArrayList<Integer>();
		List<Integer> tempDuration = new ArrayList<Integer>();
		int step=0;
		for(int i=0;i<convertedPitchList.size();i++){
			if(convertedPitchList.get(i)==-1){
				tempPitch = convertedPitchList.subList(step,i);
				tempDuration = convertedDurationList.subList(step,i);
					for(int j=0; j<tempPitch.size();j++){
					Note note = new Note(tempDuration.get(j),tempPitch.get(j));
					song.add(note);
					}
					songList.add(song);
					song=new ArrayList<Note>(0);
					step=i+1;
			}
		}
		
		
		
	}
	public List<ArrayList<Note>> getData(){
		return songList;
	}

	
	public static void createMidi(List<List<Note>> newSongList){
		
		
		
	}
	// -1 because we don't count the notation -1
	public int getPMax(){
		return pMax-1;
	}
	public int getDMax(){
		return dMax-1;
	}
	
}
