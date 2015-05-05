import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.MetaMessage;

public class MidiManager2 {

	private List<ArrayList<Note>> songList = new ArrayList<ArrayList<Note>>();
	private List<ArrayList<Frame>> listOfFramesList = new ArrayList<ArrayList<Frame>>();
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
	
	//
	private ArrayList<String> unconChordList = new ArrayList<String>();
	//
	private ArrayList<String> convertTableChords = new ArrayList<String>();
	//
	private ArrayList<Integer> convertedChordList= new ArrayList<Integer>();
	
	private ArrayList<Frame> fixedListOfFrameList = new ArrayList<Frame>();

	private int pMax;
	private int dMax;
	private int cMax;

	public MidiManager2(String filePath){
		
		try{
		BufferedReader in =new BufferedReader(new FileReader(filePath));
		createUnconvertedArrays(in);
		createConvertTables();
	
		convertArraysToModelFormat();
		fixConvertTables();
		pMax=convertTablePitch.size();
		dMax=convertTableDuration.size();
		cMax=convertTableChords.size();
		//createSongList();
		createlistOfFramesList();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getConvertTableChords(){
		return convertTableChords;
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
					if(line.charAt(i)=='?'){
						unconChordList.add(line.substring(1));
						unconPitchList.add((float)-2);
						unconDurationList.add((float)-2);		
						}
					
					if(line.charAt(i)=='-'){
						unconChordList.add("-");
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
	/**
	private void fixListOfListOfFrames(){
		for(int i = 0; i< fixedListOfFrameList.size();i++){
			Frame tempFrame = fixedListOfFrameList.get(i);
			for(int j=0; j<tempFrame.getMelodyPackage().size();j++){
				tempFrame.getMelodyPackage().get(j)
				
				
				
			}
			
		}
		
		
		
		
	}
	*/
	
	
	//Creating convering tables that show which pitch is which number in the model.
	//Same for duration
	private void createConvertTables(){
		convertTableChords.add("-");
		
		for(int j =0;j<unconChordList.size();j++){
			if(!convertTableChords.contains(unconChordList.get(j))){
				convertTableChords.add(unconChordList.get(j));
			}
			
		}
		
		
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
			}else if(unconPitchList.get(i)==-2){
				convertedPitchList.add(-2);
				convertedDurationList.add(-2);
			}else{
				convertedPitchList.add(convertTablePitch.indexOf(unconPitchList.get(i))-1);
				convertedDurationList.add(convertTableDuration.indexOf(unconDurationList.get(i))-1);
			}
		}	
		for(int j = 0; j < unconChordList.size();j++){
			if(unconChordList.get(j) == "-"){
				convertedChordList.add(-1);	
			}else{
				convertedChordList.add(convertTableChords.indexOf(unconChordList.get(j)));
			}
			
		}
	}
	private void fixConvertTables(){
		convertTablePitch.remove(1);
		convertTableDuration.remove(1);
	}
	
	private ArrayList<Float> getConvertTablePitch(){
		return convertTablePitch;
	}
	private void createlistOfFramesList(){
		ArrayList<Frame> frameList = new ArrayList<Frame>();
		ArrayList<Note> melodypack=new ArrayList<Note>(0);
		List<Integer> tempPitch = new ArrayList<Integer>();
		List<Integer> tempDuration = new ArrayList<Integer>();
		boolean firstChord = false;
		int step=1;
		int counter1=0;
		for(int i=0;i<convertedPitchList.size();i++){
		
			//System.out.println(i + " " + step + " "+ convertedChordList.get(counter1) +  "  " + convertedPitchList.get(i));
			if(convertedChordList.get(counter1) == -1 && convertedChordList.size()-1 !=counter1){
				counter1++;
			}
			if(convertedPitchList.get(i)==-1){
				tempPitch = convertedPitchList.subList(step,i);
				tempDuration = convertedDurationList.subList(step,i);
					for(int j=0; j<tempPitch.size();j++){
					Note note = new Note(tempDuration.get(j),tempPitch.get(j));
					melodypack.add(note);
					}
				Frame melodyframe = new Frame(melodypack, convertedChordList.get(counter1));
				frameList.add(melodyframe);	
				counter1++;
				step=i+2;
				listOfFramesList.add(frameList);
				frameList = new ArrayList<Frame>();
				melodypack=new ArrayList<Note>(0);
				firstChord=false;
			}
			if(convertedPitchList.get(i)==-2 && firstChord){	
				
				tempPitch = convertedPitchList.subList(step,i);
				tempDuration = convertedDurationList.subList(step,i);
					for(int j=0; j<tempPitch.size();j++){
					Note note = new Note(tempDuration.get(j),tempPitch.get(j));
					melodypack.add(note);
					}
			Frame melodyframe = new Frame(melodypack, convertedChordList.get(counter1));
			frameList.add(melodyframe);	
			counter1++;
			melodypack=new ArrayList<Note>(0);
			step=i+1;
					
			}
			if(convertedPitchList.get(i)!=-1){
			firstChord=true;
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
	public byte string2Byte(String hexString){
		int val = Integer.parseInt( hexString, 16);
		byte[] byteArray = new byte[1];
		return byteArray[0] = (byte) ( ( val >>> 24 ) & 0xff );
		//byteArray[1] = (byte) ( ( val >>> 16 ) & 0xff );
		//byteArray[2] = (byte) ( ( val >>> 8 ) & 0xff );
		//byteArray[3] = (byte) ( ( val >>> 0 ) & 0xff );
	}
	//public List<ArrayList<Note>> getData(){
		//return songList;
	//}
	


	public List<ArrayList<Frame>> getData(){
		return listOfFramesList;
	}
	public byte[] dec2Hex(int number){
		int result=number;
		ArrayList<String> index = new ArrayList<String>();
		int count=0;
		//char[] hex={'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		String[] hex={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
		
		while(result!=0){
			count++;
			result=number/16;
			index.add(hex[number-(result*16)]);
			number=result;
		}
		byte[] data = new byte[3];
		
		data[0]=string2Byte("0"+index.get(count-1));
		System.out.print("0"+"x"+"0"+index.get(count-1));
		int a=1;
		for(int i=count-2; i>-1; i=i-2){
			data[a]=string2Byte(index.get(i)+index.get(i-1));
			System.out.print(index.get(i)+index.get(i-1));
			a++;
		}
		
		return data; 
		
	}
	
	public void createMidi(List<Frame> newFrameList,int nbrOfFrames) throws Exception{
		 Sequence sequence;//need sequencer to create midi
         int resolution = 192;
		 sequence=new Sequence(Sequence.PPQ,resolution); //Sets divisiontype and resolution. 
         long tick =0;      			//varible for converting to ticks
         long tickMeter=0;				//varible for keeping track of what tick the song is on
         MidiEvent NoteOn;				//
         MidiEvent NoteOff;
         //File outputFile = new File(System.getProperty("user.dir")+"/songs/TestSong.mid");
         File outputFile = new File(System.getProperty("user.dir")+"/Turing/Johan-chorus-X.mid");
         Track track = sequence.createTrack();
         MetaMessage bpm = new MetaMessage();
         byte[] data = {(byte)0x07, (byte)0xA1, (byte)0x20};//120bpm
         //{(byte)0x07, (byte)0xA1, (byte)0x20};//120bpm 
         Random random=new Random();
         int randomBpm = random.nextInt(5);
         //System.out.println(randomBpm);
         //byte[] data=dec2Hex(60000000/(120));
         //System.out.println(data);
         if(randomBpm==0){
        	 data[0]=(byte)0x0B; 
        	 data[1]=(byte)0x71;
        	 data[2]=(byte)0xB0;
        	 //80bpm
         }if(randomBpm==1){
        	 data[0]=(byte)0x0A; 
        	 data[1]=(byte)0x2C;
        	 data[2]=(byte)0x2B;
        	 //90bpm ish
         }if(randomBpm==2){
        	 data[0]=(byte)0x09; 
        	 data[1]=(byte)0x27;
        	 data[2]=(byte)0xC0;
        	 //100bpm
         }if(randomBpm==3){
        	 data[0]=(byte)0x08; 
        	 data[1]=(byte)0x52;
        	 data[2]=(byte)0xAF;
        	 //110bpm ish
         }if(randomBpm==4){
        	 data[0]=(byte)0x07; 
        	 data[1]=(byte)0xA1;
        	 data[2]=(byte)0x20;
        	 //120bpm 
         }
//         }if(randomBpm==5){
//        	 data[0]=(byte)0x07; 
//        	 data[1]=(byte)0x0A;
//        	 data[2]=(byte)0xE2;
//        	 //130bpm ish
//         }if(randomBpm==6){
//        	 data[0]=(byte)0x06; 
//        	 data[1]=(byte)0x8A;
//        	 data[2]=(byte)0x1B;
//        	 //120bpm 
//         }
         
 		 bpm.setMessage(0x51 ,data, data.length);
 		 MidiEvent newBpm = new MidiEvent(bpm,(long)0);
 		 track.add(newBpm);
         
     
        
         
         //We start with creating the melodytrack
         for(int  i=0; i<newFrameList.size();i++ ){
        	 List<Note> noteList = newFrameList.get(i).getMelodyPackage();
        	 
        	 for(int j =0; j<noteList.size(); j++){
        		 
        		// System.out.println(convertTableDuration.get((noteList.get(j)).getDuration()));
            	 //System.out.println(convertTablePitch.get((noteList.get(j)).getPitch()));
            	 tick=convertNoteLengthToTicks(convertTableDuration.get((noteList.get(j)).getDuration()),192);
            	 if(convertTablePitch.get((noteList.get(j)).getPitch())==0){
            		tickMeter=tickMeter + tick;	 
            	 }
            	 if(convertTablePitch.get((noteList.get(j)).getPitch())!=0){
             		
             		//System.out.println("Command1=   " +ShortMessage.NOTE_ON);
             		//System.out.println("Command2=   " + ShortMessage.NOTE_OFF);
             		ShortMessage	shortMessage1 = new ShortMessage();
             		shortMessage1.setMessage(ShortMessage.NOTE_ON,0,(convertTablePitch.get((noteList.get(j)).getPitch())).intValue(), 114 );
             		NoteOn=new MidiEvent(shortMessage1,tickMeter);
             		//System.out.println("commad:" +shortMessage.getCommand() + "     " + shortMessage.getData1() + "     " + shortMessage.getData2());
             		track.add(NoteOn);
             		ShortMessage	shortMessage2 = new ShortMessage();
             		tickMeter=tickMeter + tick;	
             		shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,(convertTablePitch.get((noteList.get(j)).getPitch())).intValue(), 0 );
             		//System.out.println("commad:" +shortMessage.getCommand() + "     " + shortMessage.getData1() + "     " + shortMessage.getData2());
             		NoteOff=new MidiEvent(shortMessage2,tickMeter);
             		
             		track.add(NoteOff);
             			
             	 }
        	 }
        	  
         }
        
         //Now we do it for the chords
         Track track2 = sequence.createTrack();
           
         
         //tick = convertNoteLengthToTicks((float)0.5, resolution);      			//varible for converting to ticks
    									//varible for keeping track of what tick the song is on
         MidiEvent NoteOn1;	
         MidiEvent NoteOn2;	
         MidiEvent NoteOn3;	
         MidiEvent NoteOff1;
         MidiEvent NoteOff2;
         MidiEvent NoteOff3;
         tickMeter=0;
         
         ArrayList<String> chordsForBassLine = new ArrayList<String>(); 
         ArrayList<Chord> chordsForPianoRythm = new ArrayList<Chord>();
         for(int p = 0; p<newFrameList.size();p++){
        	 int nbrchord = newFrameList.get(p).getChord();
        	 String chord = convertTableChords.get(nbrchord);
        	 Chord ackord = new Chord(chord,(float)0.5);
        	 chordsForPianoRythm.add(ackord);
        	 chordsForBassLine.add(chord);
         }
         
         PianoRythm pianoRythm = new PianoRythm(chordsForPianoRythm,"chorus");
         
         /**
          * Remove Pianorythm. Comment out next phrase. 
          */  
         chordsForPianoRythm = pianoRythm.getFixedRythm();
         
         for(int  i=0; i<chordsForPianoRythm.size();i++ ){
        	 
        	 Chord ackord = chordsForPianoRythm.get(i);
        	 //System.out.println(ackord.getNote1() +  "  " + ackord.getNote2() + "  "+ ackord.getNote3());
        	 
        	 
        	ShortMessage	shortMessage1 = new ShortMessage();
        	ShortMessage	shortMessage2 = new ShortMessage();
        	ShortMessage	shortMessage3 = new ShortMessage();
      		shortMessage1.setMessage(ShortMessage.NOTE_ON,0,ackord.getNote1(), 114 );
      		shortMessage2.setMessage(ShortMessage.NOTE_ON,0,ackord.getNote2(), 114 );
      		shortMessage3.setMessage(ShortMessage.NOTE_ON,0,ackord.getNote3(), 114 );
      		NoteOn1=new MidiEvent(shortMessage1,tickMeter);
      		track2.add(NoteOn1);
      		NoteOn2=new MidiEvent(shortMessage2,tickMeter);
      		track2.add(NoteOn2);
      		NoteOn3=new MidiEvent(shortMessage3,tickMeter);
      		track2.add(NoteOn3);
      		
      		ShortMessage	shortMessage4 = new ShortMessage();
      		ShortMessage	shortMessage5 = new ShortMessage();
      		ShortMessage	shortMessage6 = new ShortMessage();
      		
      		tickMeter=tickMeter + convertNoteLengthToTicks(ackord.getDuration(), resolution);	
      		shortMessage4.setMessage(ShortMessage.NOTE_OFF,0,ackord.getNote1(), 0 );
      		shortMessage5.setMessage(ShortMessage.NOTE_OFF,0,ackord.getNote2(), 0 );
      		shortMessage6.setMessage(ShortMessage.NOTE_OFF,0,ackord.getNote3(), 0 );
  
      		NoteOff1=new MidiEvent(shortMessage4,tickMeter);
      		track2.add(NoteOff1);
      		NoteOff2=new MidiEvent(shortMessage5,tickMeter);
      		track2.add(NoteOff2);
      		NoteOff3=new MidiEvent(shortMessage6,tickMeter);
      		track2.add(NoteOff3);
      			
        	 
        	 
         }
       /**
         //Write the bassline
         Track track4 = sequence.createTrack();
         
         BassLine bass = new BassLine(chordsForPianoRythm);
      
         ShortMessage sm = new ShortMessage( );
         sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 34, 0); //9 ==> is the channel 10.
         track4.add(new MidiEvent(sm, 0));
         for (int i=0;i<bass.getBassLine().size();i++){ 
         	track4.add(bass.getBassLine().get(i)); 	 
         }
         	
         
         //Write the drumtrack
         Track track3 = sequence.createTrack();
         

         // nextInt is normally exclusive of the top value,
         // so add 1 to make it inclusive
         int randomNum = analyzeSong(newFrameList);
         DrumBeat drumbeat = new DrumBeat(randomNum,nbrOfFrames/2);
         sm = new ShortMessage();
         sm.setMessage(ShortMessage.PROGRAM_CHANGE, 9, 35, 0); //9 ==> is the channel 10.
         track3.add(new MidiEvent(sm, 0));
        
         
         
     
        	 for (int i=0;i<drumbeat.getDrumList().size();i++){ 
        		 //drumbeat.getDrumList().get(i).setTick(ticksPerFourBars + drumbeat.getDrumList().get(i).getTick());
        		 track3.add(drumbeat.getDrumList().get(i)); 	 
        	 }
         */
         
         
      
        	 
        	 
        	 
         
         MidiSystem.write(sequence, 1, outputFile);
         System.out.println("Song is created");
         //for(int nEvent = 0; nEvent < track.size()-1; nEvent++){	 
        	 //MidiEvent event = track.get(nEvent);
        	 //MidiMessage message = event.getMessage();
         //}

        

         
         

		
		
	}

	public int analyzeSong(List<Frame> newFrameList){
		int[] counter = new int[convertTableDuration.size()];
		Random rand = new Random();
//		Looks at the durations in the song to decide if its fast or slow.
		for(int i=0;i<newFrameList.size();i++){
			List<Note>  melody= newFrameList.get(i).getMelodyPackage();
			for(int j=0;j<melody.size();j++){
			int a = melody.get(j).getDuration();
			//			Starts with -2
			counter[a]++;
			}
		}
		float sum = 0;
		//Count through the number of notes, which should be an index of how fast the song goes
		for(int i=0;i<counter.length;i++){
			if(convertTableDuration.get(i)<=(float)0.125){
				sum+=counter[i];
			}
			else{
				sum-=counter[i];
			}
		}
		
		if(sum>20){
			return (int) rand.nextInt(5)+1; 
		}
		else{
			return rand.nextInt(5)+6;
		}
	}
	
	public static long convertNoteLengthToTicks(float noteLength,int res) throws Exception{
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks

	// -1 because we don't count the notation -1
	public int getPMax(){
		return pMax-1;
	}
	public int getDMax(){
		return dMax-1;
	}
	public int getCMax(){
		return cMax-1;
	}

	public static void main(String[] args) throws Exception{
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/database_verse.txt");
		//MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/database/Chorus/Hooktheory-2015-02-26-08-00-10.mid");
		List<ArrayList<Frame>> listOfFramesList = mm.getData();
		ArrayList<Float> convertTablePitch = mm.getConvertTablePitch();
		for(int i=0;i<listOfFramesList.size();i++){
			for(int j=0; j<listOfFramesList.get(i).size();j++){
				//System.out.println("Ackord:  " + listOfFramesList.get(i).get(j).getChord());
				
				for(int k = 0; k<listOfFramesList.get(i).get(j).getMelodyPackage().size();k++){
					//System.out.println("Not:  " +convertTablePitch.get(listOfFramesList.get(i).get(j).getMelodyPackage().get(k).getPitch()));
				}
			}
		
//		mm.createMidi(listOfFramesList.get(0));
		
		
		
		}
		
		
		
	}

	public List<Float> getDurationConversionTable() {
		return convertTableDuration;
	}
	
	
	
}
