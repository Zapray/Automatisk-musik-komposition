import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class MidiManager {

	private List<List<Note>> songList;
	private ArrayList<Float> unconPitchList; //All the songs unconverted from textfile, only the pitch
	private ArrayList<Float> unconDurationList; //All the songs unconverted from textfile, only the duration
	private ArrayList<Float> convertTablePitch; // Convert table used to convert between midivalues and model values for the pitch
	private ArrayList<Float> convertTableDuration; //Convert table used to convert between midivalues and model values for the duration
	private ArrayList<Integer> convertedPitchList; // All the songs converted to model values, only the pitch
	private ArrayList<Integer> convertedDurationList; // All the songs converted to model values, only the duration

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
		List<Integer> tempPitch;
		List<Integer> tempDuration;
		int step=0;
		
		for(int i=0;i<convertedPitchList.size();i++){
			if(convertedPitchList.get(i)==-1){
				tempPitch = convertedPitchList.subList(step,i-1);
				tempDuration = convertedDurationList.subList(step,i-1);
					for(int j=0; j<tempPitch.size();j++){
					Note note = new Note(tempDuration.get(j),tempPitch.get(j));
					song.add(note);
					}
			}
			songList.add(song);
		}
		
		
		
	}
	public List<List<Note>> getData(){
		return songList;
	}

	
	public static void createMidi(List<List<Note>> newSongList){
		
		
		
	}
		
	
}
