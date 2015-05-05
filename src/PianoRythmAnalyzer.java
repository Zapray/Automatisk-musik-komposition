package src;


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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.*;

import javax.sound.midi.*;


public class PianoRythmAnalyzer {

	
	private static ArrayList<Integer> velocitynbrs = new ArrayList<Integer>();
	
	public static void main(String args[]) throws Exception{
		File[] files =new File (System.getProperty("user.dir")+"/database/Chorus/").listFiles(); 
		Arrays.sort(files);
		
		int count = 0;
		int equalCount = 0;
		for(File file : files){
			velocitynbrs = new ArrayList<Integer>();
			String ext1 = FilenameUtils.getExtension(file.getName());
			System.out.println(file);
			if(ext1.equals("mid")){
				
				Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
				sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
				InputStream is = new BufferedInputStream(new FileInputStream( new File(System.getProperty("user.dir")+"/database/Chorus/" + file.getName())));
				Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
				float res = sequence.getResolution();
				Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
				int track0 = 0;
				int track1 = 1;
				Track   melodytrack = tracks[track0];
				Track   chordtrack = tracks[track1];
				ArrayList<Chord> chordList = findChords(chordtrack, res);
				
				//check how long the totalchordlist is.
				
				float durationen = 0;
				boolean fyrtakt= false;
				for(int i=0;i<chordList.size();i++){
					durationen=durationen +chordList.get(i).getDuration();
					if(durationen == 4){
						fyrtakt = true;
					}
				}
						
				//System.out.println(durationen);
				
				//Print the first 4 bars to sheet
				
				System.out.println(velocitynbrs.size() + chordList.size());
				
				
				if(durationen > 4 && fyrtakt){
				File filen = new File(System.getProperty("user.dir")+"/pianorythm_chorus.txt");
				

				if (!filen.exists()) {
					filen.createNewFile();
				}


				PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/pianorythm_chorus.txt", true));

				float durationcounter = 0;
				int indexcounter = 0;
				
				
				while(durationcounter<4){
					outFile.println(chordList.get(indexcounter).getDuration() + "," + chordList.get(indexcounter).getLabel() + "," + velocitynbrs.get(indexcounter));	
					durationcounter = durationcounter + chordList.get(indexcounter).getDuration();
					indexcounter++;
					
				}
				if(durationcounter>4){
				System.out.println(durationcounter);
				}
				outFile.println("-");

				outFile.close();
				
				sequencer.close();
			}
		}
		}
		//System.out.println((double)equalCount/count);
	}//end main

	private static ArrayList<Integer> findVelocity(Track chordtrack, float res) {
		// TODO Auto-generated method stub
		return null;
	}

	public static float convertTicksToNoteLength(long tick1, long tick2, float res){
		res=res*4;

		return (tick2-tick1)/res;

	}//end convertTicksToNoteLength

	public static float convertTicksToDuration(long tick1, long tick2, float res){
		res=res*4;
		Integer tickone = (int) (long) tick1;
		Integer ticktwo = (int) (long) tick2;
		float durationindec = (tick1-tick2)/res;
		//float durationindec = (tickone-ticktwo)/res;
		//System.out.println(durationindec);

		return durationindec;

	}//end convertTicksToDuration

	
	public static ArrayList<Chord> findChords(Track track, float res){

		ArrayList<Chord> chordList = createChordList(track, res);
		
		return chordList;

	}

	public static boolean newTick(Track track, int nEvent){

		if(track.get(nEvent).getTick() - track.get(nEvent-1).getTick() == 0){
			return false;
		}else return true;
	}

	public static int[] extractChordNotes(MidiEvent event1, MidiEvent event2, MidiEvent event3, MidiEvent event4, MidiEvent event5, MidiEvent event6 ){
		boolean  nofind = true;
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
			int[] data2 = new int[6];
			data2[0] = shortMessage1.getData2();
			data2[1] = shortMessage2.getData2();
			data2[2] = shortMessage3.getData2();

			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					count++;
					
				}else if(nofind){
					velocitynbrs.add(data2[i]);	
					nofind = false;
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
			int[] data2 = new int[6];
			data2[0] = shortMessage1.getData2();
			data2[1] = shortMessage2.getData2();
			data2[2] = shortMessage3.getData2();
			data2[3] = shortMessage4.getData2();
			data2[4] = shortMessage5.getData2();
			data2[5] = shortMessage6.getData2();
			
			
			
			
			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					
					count++;
				}else if(nofind){
					System.out.println(data2[i] + "      " + i );
					velocitynbrs.add(data2[i]);	
					nofind = false;
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
					velocitynbrs.add(0);
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
							ShortMessage messet = (ShortMessage) message;
							Chord chord = new Chord(notesInChord[0], notesInChord[1], notesInChord[2], 
									convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res),messet.getData2()); 
							
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
						MidiEvent eventett = track.get(nEvent);
						
						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);
						if(!Arrays.equals(notesInChord, new int[3])){

							ShortMessage messagen = (ShortMessage) event1.getMessage();
							
							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res),messagen.getData2()); 
							chordList.add(chord);
							


							Chord paus = new Chord(0, 0, 0, convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res)); 
							chordList.add(paus);
							velocitynbrs.add(0);
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

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res),0); 
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

	




}// End MidiAnalyzer

