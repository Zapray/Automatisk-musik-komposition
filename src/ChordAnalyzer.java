import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.sound.midi.*;

public class ChordAnalyzer {



	public static int convertTicksToDuration(long tick1, long tick2, float res){
		res=res*4;
		//System.out.println(tick1);
		//System.out.println(tick2);
		Integer tickone = (int) (long) tick1;
		Integer ticktwo = (int) (long) tick2;
		float durationindec = (tick1-tick2)/res;
		//float durationindec = (tickone-ticktwo)/res;
		//System.out.println(durationindec);
		int duration=0;

		if(durationindec <= 0.25){
			duration = 1;
		}else if(durationindec<= 0.5 && durationindec>0.25){
			duration = 2;
		}else if(durationindec<= 2 && durationindec>0.5){
			duration = 4;
		}else{
			duration = 8;
		}

		return duration;

	}//end convertTicksToNoteLength

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

	public static Chord addChord(int [] notesInChord, long tickAfter, long tickBefore, int res, ArrayList<Chord> chordList){
		if(!Arrays.equals(notesInChord, new int[3])){
			Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(tickAfter, tickBefore,res)); 
			chordList.add(chord);

			return chord;
		}else return null;

	}
	
	
	public static void main(String[] args) throws Exception{
		ArrayList<Chord> chordList=new ArrayList<Chord>(0);
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\Hooktheory-2015-02-04-01-25-10.mid")));
		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Desktop/Hooktheory_data/Chorus/Hooktheory-2015-02-18-03-40-19.mid")));
		InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Desktop/Hooktheory_data/Intro/Hooktheory-2015-02-18-03-54-00.mid")));//Paus in beginning!!

		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/Albin/Desktop/Hooktheory-2015-02-18-01-46-41.mid")));
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/music.mid")));
		Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		int res = sequence.getResolution();
		//System.out.println(res);
		Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.

		int chordtrack = 1; //CHORDS
		Track track = tracks[chordtrack];


		//Loop through ticks
		int chordCount = 0;
		int tickCount = 0;
		int eventCount = 0;
		int[] breakPoints = new int[(int) track.ticks()];
		breakPoints[0] = 1;
		MidiEvent [] eventAfter = new MidiEvent[track.size()];
		MidiEvent [] eventBefore = new MidiEvent[track.size()];


		for(int nEvent = 0; nEvent < track.size(); nEvent++){
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();
			//System.out.println("Tick:  " + event.getTick());

			if(message instanceof ShortMessage){

				//If PAUS in beginning
				if(eventCount == 0 && track.get(nEvent).getTick() != 0){
					System.out.print("PAUS    ");
					System.out.println(convertTicksToDuration(track.get(nEvent).getTick(), 0,res));
				}
				//END chord
				if(nEvent == track.size()-2){ //There is a MetaMessage in the end (Is it always??)
					int nbrOfEvents = eventCount - breakPoints[tickCount-1]-1;
					if(nbrOfEvents == 3){

						MidiEvent event1 = track.get(nEvent-2);
						MidiEvent event2 = track.get(nEvent-1);
						MidiEvent event3 = track.get(nEvent);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);

						Chord chord = addChord(notesInChord, eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(), res, chordList);
						
						if(chord!=null){
							System.out.println("------------------------ACCORD---------------------");
							System.out.println(chord.getLabel() + "     " + chord.getDuration());
							System.out.print("PAUS    ");
							System.out.println(convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res));
							chordCount++;
						}
					}else if(nbrOfEvents == 6){

						MidiEvent event1 = track.get(nEvent-5);
						MidiEvent event2 = track.get(nEvent-4);
						MidiEvent event3 = track.get(nEvent-3);
						MidiEvent event4 = track.get(nEvent-2);
						MidiEvent event5 = track.get(nEvent-1);
						MidiEvent event6 = track.get(nEvent);
						
						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, event4, event5, event6);

						Chord chord = addChord(notesInChord, eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(), res, chordList);
						
						if(chord!=null){

							System.out.println("------------------------ACCORD---------------------");
							System.out.println(chord.getLabel() + "     " + chord.getDuration());				
							chordCount++;
						}
					}
				}

				eventCount++;

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
						Chord chord = addChord(notesInChord, eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(), res, chordList);

						if(chord!=null){
							System.out.println("------------------------ACCORD---------------------");
							System.out.println(chord.getLabel() + "     " + chord.getDuration());
							System.out.print("PAUS    ");
							System.out.println(convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res));
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
						Chord chord = addChord(notesInChord, eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(), res, chordList);

						if(chord!=null){

							System.out.println("------------------------ACCORD---------------------");
							System.out.println(chord.getLabel() + "     " + chord.getDuration());
							chordCount++;
						}

					}
				}
			}
		}
		System.out.println("Antal ackord: " + chordCount);

		sequencer.close();
	}

}
