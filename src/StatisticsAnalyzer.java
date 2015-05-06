

/*
 * This is a Midi Analyzer that can analyzer midi files from the midi database Hooktheory.
 * The class finds the chords and the melody in the files and writes them into a textfile on the format
 * 
 * ?Chord
 * Pitch,Duration
 * Pitch,Duration
 * Pitch,Duration
 * ?Chord
 * Pitch,Duration
 * Pitch,Duration
 * Pitch,Duration
 * - (New Song)
 * 
 */



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.*;

import javax.sound.midi.*;


public class StatisticsAnalyzer {

	public static void main(String args[]) throws Exception{
		File[] files =new File (System.getProperty("user.dir")+"/database/Verseandchorus/").listFiles(); 

		int count = 0;
		int equalCount = 0;
		ArrayList<Integer> mostUsedChords=new ArrayList<Integer>(); 
		ArrayList<Integer> numberOfSongsWithChord = new ArrayList<Integer>();
		ArrayList<Integer> chordProgressions = new ArrayList<Integer>();
		
		
		String keys[] = {"C","F","G","Am","Dm","Em","Cm","D","E","G#","Gm","Fm","F#","A","B","Bm","C#","D#","A#","Cm#","Dm#","Fm#","Gm#","Am#","Cdim","Ddim","Edim","Fdim","Gdim","Adim","Bdim","C#dim","D#dim","F#dim","G#dim","A#dim","Caug","Daug","Eaug","Faug","Gaug","Aaug","Baug","C#aug","D#aug","F#aug","G#aug","A#aug","C7","D7","E7","F7","G7","A7","B7","C#7","D#7","F#7","G#7","A#7","Cm7","Dm7","Em7","Fm7","Gm7","Am7","Bm7","Cm#7","Dm#7","Fm#7","Gm#7","Am#7","Csus","Dsus","Esus","Fsus","Gsus","Asus","Bsus","C#sus","D#sus","F#sus","G#sus","A#sus"};
		String progres[]=new String[(keys.length)*(keys.length)];
		int p =0;
		System.out.println((keys.length)*(keys.length));
		for(String keyn : keys){
			for(String key : keys){
				progres[p] = keyn+"-"+key;
				p++;
			}	
		}
		System.out.println(Arrays.toString(progres));
		
		for(int o = 0;o<progres.length;o++){
			chordProgressions.add(0);
		}
		for(int j=0;j<keys.length;j++){
			mostUsedChords.add(0);
			numberOfSongsWithChord.add(0);
		}
		for(File file : files){
			//System.out.println("Nyl�t");
			String ext1 = FilenameUtils.getExtension(file.getName());
			if(ext1.equals("mid")){
				count++;
				Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
				sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
				
				InputStream is = new BufferedInputStream(new FileInputStream( new File(System.getProperty("user.dir")+"/database/Verseandchorus/" + file.getName())));
				
				Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
				float res = sequence.getResolution();
				Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
				int track0 = 0;
				int track1 = 1;
				Track   melodytrack = tracks[track0];
				Track   chordtrack = tracks[track1];
				List<ArrayList<FloatNote>> melodyList = findMelody(melodytrack, res);
				ArrayList<Chord> chordList = findChords(chordtrack, res);
		
				ArrayList<String> chordsInSong = new ArrayList<String>();
				
				for(int i = 0; i<chordList.size();i++){
					//System.out.println(chordList.get(i).getLabel());
					int a = 0;
					for(String ackord : keys){
						
						if(chordList.get(i).getLabel()!=null && chordList.get(i).getLabel().equals(ackord)){
							int b = mostUsedChords.get(a);
							b++;
							mostUsedChords.remove(a);
							mostUsedChords.add(a,b);
							if(!chordsInSong.contains(chordList.get(i).getLabel())){
								chordsInSong.add(chordList.get(i).getLabel());
								int c = numberOfSongsWithChord.get(a);
								c++;
								numberOfSongsWithChord.remove(a);
								numberOfSongsWithChord.add(a,c);
							}
						}
						
						a++;
					}
				}
				
				for(int i = 0; i<chordList.size()-1;i++){
					int a = 0;
					
					for(String prog : progres){
						if(chordList.get(i).getLabel()!=null && (chordList.get(i).getLabel()+"-"+chordList.get(i+1).getLabel()).equals(prog)){
							int b = chordProgressions.get(a);
							b++;
							chordProgressions.remove(a);
							chordProgressions.add(a,b);
						}
					
					a++;
					}
					
					
				}
				
				
				
				
				/**
				File filen = new File(System.getProperty("user.dir")+"/albinsstatistik.txt");


				//File filen = new File("database_intro.txt");


				// if file doesnt exists, then create it
				if (!filen.exists()) {
					filen.createNewFile();
				}

				
				
				
				

				PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/albinsstatistik.txt", true));


				//PrintWriter outFile = new PrintWriter(new FileWriter("database_intro.txt", true));



				count++;

				if(chordList.size() > melodyList.size()){
					int diff = chordList.size() - melodyList.size();
					do{
						chordList.remove(chordList.size()-diff);
						diff--;
					}while(diff > 0);

				}
				System.out.println(count+": " + chordList.size() + "  " + melodyList.size());

				boolean containsZeroDuration = false;
				for(int i = 0; i < melodyList.size(); i++){
					for(int j = 0; j < melodyList.get(i).size(); j++){
						if(melodyList.get(i).get(j).getDuration() == 0.0f){
							containsZeroDuration = true;
						}
						
					}
				}
				
				if(chordList.size()==melodyList.size() && chordList.size() != 0 && !containsZeroDuration){
					
					equalCount++;
					for (int i=0;i<chordList.size();i++){
						outFile.println("?"+chordList.get(i).getLabel());
						for (int j =0; j<melodyList.get(i).size();j++){
							outFile.println(melodyList.get(i).get(j).getPitch() + "," + melodyList.get(i).get(j).getDuration());
						}
					}
					outFile.println("-");
				}
				 	*/

				//outFile.close();
				
				sequencer.close();
			}
			//for(int k=0;k<mostUsedChords.size();k++){
				//System.out.println(keys[k] + "   " + mostUsedChords.get(k));
				//System.out.println(keys[k] + "   " + numberOfSongsWithChord.get(k));
				
				
			//}
			
			//System.out.println(count);
			
		}
		/**
		File filen = new File(System.getProperty("user.dir")+"/statistikackordsf�ljder.txt");


		//File filen = new File("database_intro.txt");


		// if file doesnt exists, then create it
		if (!filen.exists()) {
			filen.createNewFile();
		}
		
		PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/statistikackordsf�ljder.txt", true));
		*/
		
		File filen = new File(System.getProperty("user.dir")+"/statistikantalsangermedackord.txt");


		//File filen = new File("database_intro.txt");


		// if file doesnt exists, then create it
		if (!filen.exists()) {
			filen.createNewFile();
		}
		
		PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/statistikantalsangermedackord.txt", true));


		
		for(int t =0;t<numberOfSongsWithChord.size();t++){
			outFile.println(keys[t]+","+numberOfSongsWithChord.get(t));
			
			//System.out.println(progres[t]+"     " + chordProgressions.get(t));
		}
		outFile.close();
		//System.out.println((double)equalCount/count);
		
	}//end main
	

	public static float convertTicksToNoteLength(long tick1, long tick2, float res){
		res=res*4;

		return (tick2-tick1)/res;

	}//end convertTicksToNoteLength

	public static float convertTicksToDuration(long tick1, long tick2, float res){
		res=res*4;
		//System.out.println(tick1);
		//System.out.println(tick2);
		Integer tickone = (int) (long) tick1;
		Integer ticktwo = (int) (long) tick2;
		float durationindec = (tick1-tick2)/res;
		//float durationindec = (tickone-ticktwo)/res;
		//System.out.println(durationindec);

		return durationindec;

	}//end convertTicksToDuration

	public static List<ArrayList<FloatNote>> findMelody(Track track, float res){//Takes a track and returns a list of melody packages

		//File[] files =new File ("/Users/Albin/Desktop/songweknow/").listFiles(); 

		//for (File file : files){
		//String ext1 = FilenameUtils.getExtension(file.getName());
		//if(ext1.equals("mid")){


		long tick=1;
		//long spectick=1;
		ArrayList<Integer> note= new ArrayList<Integer>(0);
		ArrayList<Float> notelength= new ArrayList<Float>(0);
		//ArrayList halfbars= new ArrayList(0);
		//int counter1=0;
		//int counter2=0;
		//boolean foundNote = false;
		boolean startOfSong = true;



		//float halfbar = 0;
		//float notel = 0;

		for(int nEvent = 0; nEvent < track.size()-1; nEvent++){//loop through events
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();
			//System.out.println(message);
			MidiEvent event2 = track.get(nEvent+1);
			//if(message instanceof MetaMessage){
			//MetaMessage metaMessage=(MetaMessage) message;
			//System.out.println("type:   " + metaMessage.getType());
			//System.out.println("data:   " + metaMessage.getData());



			//}

			if(message instanceof ShortMessage){//every event contains a short message or a meta message.

				ShortMessage shortMessage = (ShortMessage) message;

				if(event.getTick()!=0 && startOfSong){

					notelength.add(convertTicksToNoteLength(0, event.getTick(), res));
					note.add(0);

					//dataarray[counter1][1]=convertTicksToNoteLength(0, event.getTick(), res);
					//dataarray[counter1][0]=0;

					//counter1+=1;
				}//End if

				if(shortMessage.getCommand() == ShortMessage.NOTE_ON){
					//System.out.println(shortMessage.getData1() +  "    " + shortMessage.getData2()  );
					note.add(shortMessage.getData1());
					//dataarray[counter1][0]=shortMessage.getData1();
					tick=event.getTick();
					//System.out.println("Detta �r en note on");
					//System.out.println(tick);
				}else if(shortMessage.getCommand() == ShortMessage.NOTE_OFF){
					//System.out.println("Detta �r en note off");
					notelength.add(convertTicksToNoteLength(tick, event.getTick(), res));
					//dataarray[counter1][1]=convertTicksToNoteLength(tick, event.getTick(), res);
					//counter1 +=1;  
					if(event.getTick()!=event2.getTick()&& nEvent!=track.size()-2){
						//halfbar=halfbar + convertTicksToNoteLength(event.getTick(), event2.getTick(), res);

						notelength.add(convertTicksToNoteLength(event.getTick(), event2.getTick(), res));
						note.add(0);

						//dataarray[counter1][1]=convertTicksToNoteLength(event.getTick(), event2.getTick(), res);
						//dataarray[counter1][0]=0;
						//counter1+=1;
					}//End if


				}//End if
				startOfSong=false;


			}//End if

		}//End for
		float lastDuration=0;
		ArrayList<FloatNote> melody = new ArrayList<FloatNote>();
		List<ArrayList<FloatNote>> melodyPack = new ArrayList<ArrayList<FloatNote>>();
		float combinedLength=0;
		int where=0;
		FloatNote floatNote;
		//int nmbrOfPacks = 0;
		for(int index = 0; index < notelength.size()-1; index++){
			combinedLength=combinedLength + (float)notelength.get(index);
			//System.out.println(combinedLength);
			if(combinedLength >= (float)0.5){
				if(lastDuration != (float)0){
					floatNote = new FloatNote(lastDuration, (float) note.get(where-1));
					melody.add(floatNote);
				}
				lastDuration = combinedLength - (float)0.5;
				if(lastDuration>(float)0.5){
					if(where-index==0){
						while(lastDuration>=(float)0.5){
							floatNote = new FloatNote((float)0.5, (float) note.get(index));
							melody.add(floatNote);
							melodyPack.add(melody);
							melody = new ArrayList<FloatNote>();
							lastDuration = lastDuration - (float)0.5;
						}//end while
						where = index+1;
						combinedLength=lastDuration;
					}else{
						for(int i = where; i < index; i++){
							floatNote = new FloatNote((float) notelength.get(i), (float) note.get(i));
							melody.add(floatNote);

						}//End for
						floatNote = new FloatNote( (float)notelength.get(index)-lastDuration, (float) note.get(index));
						melody.add(floatNote);
						melodyPack.add(melody);
						melody = new ArrayList<FloatNote>();
						//lastDuration = lastDuration - (float)0.5;
						while(lastDuration>=(float)0.5){
							floatNote = new FloatNote((float)0.5, (float) note.get(index));
							melody.add(floatNote);
							melodyPack.add(melody);
							melody = new ArrayList<FloatNote>();
							lastDuration = lastDuration - (float)0.5;

						}//end while
						where = index+1;
						combinedLength=lastDuration;
					}//end else
				}else{
					if(lastDuration != (float)0){
						for(int i = where; i < index; i++){
							floatNote = new FloatNote((float) notelength.get(i), (float) note.get(i));
							melody.add(floatNote);

						}//End for

						floatNote = new FloatNote( (float)notelength.get(index)-lastDuration, (float) note.get(index));
						melody.add(floatNote);

					}else{

						for(int i = where; i < index+1; i++){
							floatNote = new FloatNote((float) notelength.get(i), (float) note.get(i));
							melody.add(floatNote);

						}//End for


					}//end else
					//nmbrOfPacks++;
					melodyPack.add(melody);
					//System.out.println(melodyPack.get(nmbrOfPacks-1).size());
					melody = new ArrayList<FloatNote>();
					where = index+1;
					combinedLength=lastDuration;

					//System.out.println(melodyPack.size());
					//System.out.println(melodyPack.get(nmbrOfPacks-1).size());
				}//end else
			}//End if





		}//End for
		return melodyPack;

	}// End MelodyAnalyzer

	public static ArrayList<Chord> findChords(Track track, float res){

		ArrayList<Chord> chordList = createChordList(track, res);

		//Print chordList
		//				Iterator itr = chordList.iterator();
		//				while(itr.hasNext()) {
		//					Chord chord = (Chord) itr.next();
		//					System.out.println(chord.getLabel() + "   " + chord.getDuration());
		//				}

		ArrayList<Chord> halfBarList = createHalfBarList(chordList);

		//Print halfBarList
//		System.out.println(" ");
//		Iterator itr2 = halfBarList.iterator();
//		while(itr2.hasNext()) {
//			Chord chord = (Chord) itr2.next();
//			System.out.println(chord.getLabel() + "   " + chord.getDuration());
//		}

		return chordList;

	}

	public static boolean newTick(Track track, int nEvent){

		if(track.get(nEvent).getTick() - track.get(nEvent-1).getTick() == 0){
			return false;
		}else return true;
	}

	public static int[] extractChordNotes(MidiEvent event1, MidiEvent event2, MidiEvent event3, MidiEvent event4, MidiEvent event5, MidiEvent event6 ){

		if(event4 == null){
			MidiMessage message1 = event1.getMessage();
			MidiMessage message2 = event2.getMessage();
			MidiMessage message3 = event3.getMessage();

			ShortMessage shortMessage1 = (ShortMessage) message1;
			ShortMessage shortMessage2 = (ShortMessage) message2;
			ShortMessage shortMessage3 = (ShortMessage) message3;

			int[] commands = new int[3];
			commands[0] = shortMessage1.getCommand();
			commands[1] = shortMessage2.getCommand();
			commands[2] = shortMessage3.getCommand();

			int[] data = new int[6];
			data[0] = shortMessage1.getData1();
			data[1] = shortMessage2.getData1();
			data[2] = shortMessage3.getData1();

			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					count++;
				}

			}return notesInChord;

		}else{
			MidiMessage message1 = event1.getMessage();
			MidiMessage message2 = event2.getMessage();
			MidiMessage message3 = event3.getMessage();
			MidiMessage message4 = event4.getMessage();
			MidiMessage message5 = event5.getMessage();
			MidiMessage message6 = event6.getMessage();

			ShortMessage shortMessage1 = (ShortMessage) message1;
			ShortMessage shortMessage2 = (ShortMessage) message2;
			ShortMessage shortMessage3 = (ShortMessage) message3;
			ShortMessage shortMessage4 = (ShortMessage) message4;
			ShortMessage shortMessage5 = (ShortMessage) message5;
			ShortMessage shortMessage6 = (ShortMessage) message6;

			int[] commands = new int[6];
			commands[0] = shortMessage1.getCommand();
			commands[1] = shortMessage2.getCommand();
			commands[2] = shortMessage3.getCommand();
			commands[3] = shortMessage4.getCommand();
			commands[4] = shortMessage5.getCommand();
			commands[5] = shortMessage6.getCommand();



			int[] data = new int[6];
			data[0] = shortMessage1.getData1();
			data[1] = shortMessage2.getData1();
			data[2] = shortMessage3.getData1();
			data[3] = shortMessage4.getData1();
			data[4] = shortMessage5.getData1();
			data[5] = shortMessage6.getData1();

			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					count++;
				}

			}
			return notesInChord;

		}


	}

	public static ArrayList<Chord> createChordList(Track track, float res){
		//Loop through ticks
		ArrayList<Chord> chordList=new ArrayList<Chord>();
		int chordCount = 0;
		int tickCount = 0;
		int eventCount = 0;
		int[] breakPoints = new int[(int) track.ticks()];
		breakPoints[0] = 1;
		MidiEvent [] eventAfter = new MidiEvent[track.size()];
		MidiEvent [] eventBefore = new MidiEvent[track.size()];

		//Count metaMessages in the end
		int metaMessageCount = 0;
		boolean isMeta = true;

		while(isMeta){
			metaMessageCount++;
			MidiEvent event = track.get(track.size()-metaMessageCount);
			MidiMessage message = event.getMessage();
			isMeta = message instanceof MetaMessage;

		}
		metaMessageCount--;


		for(int nEvent = 0; nEvent < track.size(); nEvent++){
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();
			//System.out.println("Tick:  " + event.getTick());

			//Skip MetaMessages in beginning
			if(message instanceof ShortMessage){

				//If PAUS in beginning
				if(eventCount == 0 && track.get(nEvent).getTick() != 0){
					Chord paus = new Chord(0, 0, 0, convertTicksToDuration(track.get(nEvent).getTick(), 0,res)); 
					chordList.add(paus);

				}

				eventCount++;

				//Extract last chord
				if(nEvent == track.size()-metaMessageCount-1){ 
					int nbrOfEvents = eventCount+1 - breakPoints[tickCount];
					if(nbrOfEvents == 3){

						MidiEvent event1 = track.get(nEvent-2);
						MidiEvent event2 = track.get(nEvent-1);
						MidiEvent event3 = track.get(nEvent);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0], notesInChord[1], notesInChord[2], 
									convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res)); 
							chordList.add(chord);
							chordCount++;

						}

					}
				}



				//Extract all other chords
				if(newTick(track, nEvent)){
					eventAfter[tickCount] = track.get(nEvent);
					eventBefore[tickCount] = track.get(nEvent-1);
					tickCount++;
					breakPoints[tickCount] = eventCount;
					int nbrOfEvents = eventCount - breakPoints[tickCount-1];

					if(nbrOfEvents == 3){ //Chord ON or PAUS in tick

						MidiEvent event1 = track.get(nEvent-3);
						MidiEvent event2 = track.get(nEvent-2);
						MidiEvent event3 = track.get(nEvent-1);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);


							Chord paus = new Chord(0, 0, 0, convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res)); 
							chordList.add(paus);
							chordCount++;

						}

					}else if(nbrOfEvents == 6){ //CHORD ON and OFF in same tick
						MidiEvent event1 = track.get(nEvent-6);
						MidiEvent event2 = track.get(nEvent-5);
						MidiEvent event3 = track.get(nEvent-4);
						MidiEvent event4 = track.get(nEvent-3);
						MidiEvent event5 = track.get(nEvent-2);
						MidiEvent event6 = track.get(nEvent-1);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, event4, event5, event6);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);
							chordCount++;

						}
					}
				}
			}
		}
		//System.out.println(chordCount);
		return chordList;
	}

	public static ArrayList<Chord> createHalfBarList(ArrayList<Chord> chordList){
		ArrayList<Chord> halfBarList = new ArrayList<Chord>();
		ArrayList<Chord> chordsInBar = new ArrayList<Chord>();

		float durationCount = 0;
		int barCount = 0; 
		for(int i = 0; i < chordList.size(); i++){

			Chord chord = chordList.get(i);
			durationCount = durationCount + chord.getDuration();
			chordsInBar.add(chord);


			//If last bar doesnt equals 1;
			if(i == chordList.size()-1 && durationCount != 1){
				Chord chord1 = chordsInBar.get(0);
				if(durationCount <= 0.5){
					halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
				}else if(durationCount < 1){
					halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
					halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
				}
			}

			if(durationCount == 1){//One bar
				barCount++;
				Chord chord1 = chordsInBar.get(0);
				if(barCount == 1 && chord1.getLabel() == null && chordsInBar.size() <= 4){
					if(chord1.getLabel() == null){//Paus in beginning
						if(chordsInBar.size() == 1){ 
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord("C", 0.5f));//??
						}else if(chordsInBar.size() == 2){
							Chord chord2 = chordsInBar.get(1);
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
						}else if(chordsInBar.size() == 3){
							Chord chord2 = chordsInBar.get(1);
							Chord chord3 = chordsInBar.get(2);
							halfBarList.add(new Chord("C", 0.5f));//??
							if(chord1.getDuration() == 0.5f){
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}
						}else{
							Chord chord3 = chordsInBar.get(2);
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
						}
					}
				}else{
					if(chordsInBar.size() == 1){ 
						if(chord.getLabel() != null){//CHORD
							halfBarList.add(new Chord(chord.getLabel(), 0.5f));
							halfBarList.add(new Chord(chord.getLabel(), 0.5f));
						}else{//PAUS (Fill with previous chord)
							halfBarList.add(halfBarList.get(halfBarList.size()-1));
							halfBarList.add(halfBarList.get(halfBarList.size()-1));
						}
					}else if(chordsInBar.size() == 2){ //
						Chord chord2 = chordsInBar.get(1);
						if(chord1.getLabel() != null && chord2.getLabel() != null){//CHORDS
							if(chord1.getDuration() == 0.75f){//Fill with first chord twice
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							}else{//First and second chord
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}
						}else{//PAUS 
							if(chord1.getLabel() == null){ 
								halfBarList.add(halfBarList.get(halfBarList.size()-1));//(fill with previous chord)
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}else{ //Fill first chord twice
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							}
						}
					}else if(chordsInBar.size() == 3){ 
						Chord chord2 = chordsInBar.get(1);
						Chord chord3 = chordsInBar.get(2);
						if(chord1.getLabel() != null && chord2.getLabel() != null && chord3.getLabel() != null){//CHORDS
							if(chord1.getDuration() == 0.5f){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));

							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}
						}else{//PAUS
							if(chord1.getLabel() == null){
								halfBarList.add(halfBarList.get(halfBarList.size()-1));//(fill with previous chord)
								if(chord1.getDuration() == 0.5f){
									halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
								}else{
									halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
								}
							}else if(chord2.getLabel() == null){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}
						}
					}else if(chordsInBar.size() == 4){ 
						Chord chord2 = chordsInBar.get(1);
						Chord chord3 = chordsInBar.get(2);
						Chord chord4 = chordsInBar.get(3);

						if(chord1.getLabel() != null && chord2.getLabel() != null && chord3.getLabel() != null && chord4.getLabel() != null){//CHORDS
							halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
						}else{//PAUS
							if(chord1.getLabel() == null){
								halfBarList.add(halfBarList.get(halfBarList.size()-1));//(fill with previous chord)
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}else if(chord3.getLabel() == null){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord4.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}

						}
					}else if(chordsInBar.size() == 5){




						int maxCount = 0;
						Iterator itr = chordsInBar.iterator();
						while(itr.hasNext()) {
							Chord c = (Chord) itr.next();
							if(c.getLabel() != null && maxCount == 0){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
						ListIterator itrRev = chordsInBar.listIterator();
						while(itrRev.hasPrevious()) {
							Chord c = (Chord) itrRev.previous();
							if(c.getLabel() != null && maxCount == 1){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
						
					}else if(chordsInBar.size() == 6){
						int maxCount = 0;
						Iterator itr = chordsInBar.iterator();
						while(itr.hasNext()) {
							Chord c = (Chord) itr.next();
							if(c.getLabel() != null && maxCount == 0){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
						ListIterator itrRev = chordsInBar.listIterator();
						while(itrRev.hasPrevious()) {
							Chord c = (Chord) itrRev.previous();
							if(c.getLabel() != null && maxCount == 1){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
					}else if(chordsInBar.size() == 7){
						int maxCount = 0;
						Iterator itr = chordsInBar.iterator();
						while(itr.hasNext()) {
							Chord c = (Chord) itr.next();
							if(c.getLabel() != null && maxCount == 0){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
						ListIterator itrRev = chordsInBar.listIterator();
						while(itrRev.hasPrevious()) {
							Chord c = (Chord) itrRev.previous();
							if(c.getLabel() != null && maxCount == 1){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}
					}else if(chordsInBar.size() == 8){//Ner till 8-delar
						int maxCount = 0;
						ListIterator<Chord> itr = chordsInBar.listIterator();
						while(itr.hasNext()) {
							Chord c = (Chord) itr.next();
							if(c.getLabel() != null && maxCount == 0){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}

						while(itr.hasPrevious()) {
							Chord c = (Chord) itr.previous();
							if(c.getLabel() != null && maxCount == 1){
								halfBarList.add(new Chord(c.getLabel(), 0.5f));
								maxCount++;
							}
						}




					}
				}
				chordsInBar.clear();
				durationCount = 0;
			}else if(durationCount == 2){//Chord lies over 2 bars
				halfBarList.add(new Chord(chord.getLabel(), 0.5f));
				halfBarList.add(new Chord(chord.getLabel(), 0.5f));
				halfBarList.add(new Chord(chord.getLabel(), 0.5f));
				halfBarList.add(new Chord(chord.getLabel(), 0.5f));
				chordsInBar.clear();
				durationCount = 0;

			}
			
			


		}
		//Clear halfBarList if it contains any chords with null-label
		ListIterator<Chord> itr = halfBarList.listIterator();
		while(itr.hasNext() && !halfBarList.isEmpty()) {
			Chord c = (Chord) itr.next();
			if(c.getLabel() == null){
				halfBarList.clear();




			}



		}
		return halfBarList;
	}






}// End MidiAnalyzer

