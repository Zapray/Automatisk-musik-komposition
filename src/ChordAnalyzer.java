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
		Track   track = tracks[chordtrack];


		//Loop through ticks
		int tmpcount = 0;
		int tickCount = 0;
		int eventCount = 0;
		int[] breakPoints = new int[(int) track.ticks()];
		breakPoints[0] = 1;
		MidiEvent [] eventAfter = new MidiEvent[track.size()];
		MidiEvent [] eventBefore = new MidiEvent[track.size()];


		//System.out.println(track.size());

		for(int nEvent = 0; nEvent < track.size()-1; nEvent++){
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();
			//System.out.println("Tick:  " + event.getTick());

			if(message instanceof ShortMessage){

				//If PAUS in begginning
				if(eventCount == 0 && track.get(nEvent).getTick() != 0){
					System.out.print("PAUS    ");
					System.out.println(convertTicksToDuration(track.get(nEvent).getTick(), 0,res));
				}
				
				eventCount++;
				


				if(newTick(track, nEvent)){
					eventAfter[tickCount] = track.get(nEvent);
					eventBefore[tickCount] = track.get(nEvent-1);
					tickCount++;
					breakPoints[tickCount] = eventCount;
					int nbrOfEvents = eventCount - breakPoints[tickCount-1];
					//System.out.println("Tick: " + tickCount);
					//System.out.println("Antal events: " + nbrOfEvents);



					if(nbrOfEvents == 3){


						
						MidiEvent event1 = track.get(nEvent-3);
						MidiMessage message1 = event1.getMessage();

						MidiEvent event2 = track.get(nEvent-2);
						MidiMessage message2 = event2.getMessage();

						MidiEvent event3 = track.get(nEvent-1);
						MidiMessage message3 = event3.getMessage();

						ShortMessage shortMessage1 = (ShortMessage) message1;
						ShortMessage shortMessage2 = (ShortMessage) message2;
						ShortMessage shortMessage3 = (ShortMessage) message3;


						int[] commands = new int[6];
						commands[0] = shortMessage1.getCommand();
						commands[1] = shortMessage2.getCommand();
						commands[2] = shortMessage3.getCommand();

						if(commands[0] == ShortMessage.NOTE_OFF){




							int[] notesInChord = new int[3];

							int[] data = new int[6];
							notesInChord[0] = shortMessage1.getData1();
							notesInChord[1] = shortMessage2.getData1();
							notesInChord[2] = shortMessage3.getData1();


							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);



							
							System.out.println("------------------------ACCORD---------------------");
							System.out.println(chord.getLabel() + "     " + chord.getDuration());
							System.out.print("PAUS    ");
							System.out.println(convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res));
							
							tmpcount++;
						}

					}else if(nbrOfEvents == 6){

						MidiEvent event1 = track.get(nEvent-6);
						MidiMessage message1 = event1.getMessage();

						MidiEvent event2 = track.get(nEvent-5);
						MidiMessage message2 = event2.getMessage();

						MidiEvent event3 = track.get(nEvent-4);
						MidiMessage message3 = event3.getMessage();

						MidiEvent event4 = track.get(nEvent-3);
						MidiMessage message4 = event4.getMessage();

						MidiEvent event5 = track.get(nEvent-2);
						MidiMessage message5 = event5.getMessage();

						MidiEvent event6 = track.get(nEvent-1);
						MidiMessage message6 = event6.getMessage();

						ShortMessage shortMessage1 = (ShortMessage) message1;
						ShortMessage shortMessage2 = (ShortMessage) message2;
						ShortMessage shortMessage3 = (ShortMessage) message3;
						ShortMessage shortMessage4 = (ShortMessage) message4;
						ShortMessage shortMessage5 = (ShortMessage) message5;
						ShortMessage shortMessage6 = (ShortMessage) message6;

						//						System.out.println("--------------------NOTE ON/OFF------------------");
						//						System.out.println(shortMessage1.getCommand());
						//						System.out.println(shortMessage2.getCommand());
						//						System.out.println(shortMessage3.getCommand());
						//						System.out.println(shortMessage4.getCommand()); 
						//						System.out.println(shortMessage5.getCommand());
						//						System.out.println(shortMessage6.getCommand());
						//						System.out.println("------------------------TICK---------------------");
						//						System.out.println(event1.getTick());
						//						System.out.println(event2.getTick());
						//						System.out.println(event3.getTick());
						//						System.out.println(event4.getTick());
						//						System.out.println(event5.getTick());
						//						System.out.println(event6.getTick());


						int[] commands = new int[6];
						commands[0] = shortMessage1.getCommand();
						commands[1] = shortMessage2.getCommand();
						commands[2] = shortMessage3.getCommand();
						commands[3] = shortMessage4.getCommand();
						commands[4] = shortMessage5.getCommand();
						commands[5] = shortMessage6.getCommand();

						int[] notesInChord = new int[3];

						int[] data = new int[6];
						data[0] = shortMessage1.getData1();
						data[1] = shortMessage2.getData1();
						data[2] = shortMessage3.getData1();
						data[3] = shortMessage4.getData1();
						data[4] = shortMessage5.getData1();
						data[5] = shortMessage6.getData1();

						int count = 0;
						for(int i = 0; i < commands.length; i++){

							if(commands[i] == ShortMessage.NOTE_OFF && count < 3){
								notesInChord[count] = data[i];
								count++;
							}

						}


						Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
						chordList.add(chord);
						//						System.out.println("------------------------NOTES---------------------");
						//						System.out.println(notesInChord[0]);
						//						System.out.println(notesInChord[1]);
						//						System.out.println(notesInChord[2]);

						System.out.println("------------------------ACCORD---------------------");
						System.out.println(chord.getLabel() + "     " + chord.getDuration());
						tmpcount++;


					}

				}

			}



			//	      for(int nEvent = 0; nEvent < track.size()-3; nEvent++){
			//	
			//	    	  MidiEvent event1 = track.get(nEvent);
			//	    	  MidiMessage message1 = event1.getMessage();
			//
			//	    	  MidiEvent event2 = track.get(nEvent+1);
			//    		  MidiMessage message2 = event2.getMessage();
			//    		  MidiEvent event3 = track.get(nEvent+2);
			//    		  MidiMessage message3 = event3.getMessage();
			//    		  
			//    		  MidiEvent event4=track.get(nEvent+3);
			//    		  MidiMessage message4 = event4.getMessage();
			//    		  
			//    		  
			//	    	  
			//	    	  if(message1 instanceof ShortMessage && message2 instanceof ShortMessage && message3 instanceof ShortMessage && message4 instanceof ShortMessage){
			//	    		  ShortMessage shortMessage1 = (ShortMessage) message1;
			//	    		  System.out.println(shortMessage1.getCommand());
			//	    		  ShortMessage shortMessage2 = (ShortMessage) message2;
			//	    		  System.out.println(shortMessage2.getCommand());
			//	  
			//	    		  ShortMessage shortMessage3 = (ShortMessage) message3;
			//	    		  System.out.println(shortMessage3.getCommand());
			//	    		  ShortMessage shortMessage4 = (ShortMessage) message4;
			//	    		  System.out.println(shortMessage4.getCommand());
			//	    		  System.out.println("");
			//	    		  System.out.println(event1.getTick());
			//	    		  System.out.println(event2.getTick());
			//	    		  System.out.println(event3.getTick());
			//    			  System.out.println(event4.getTick());
			//    			  System.out.println("");
			//	    		  
			//	    		  
			//	    		  if(shortMessage1.getCommand() == ShortMessage.NOTE_ON && shortMessage2.getCommand() == ShortMessage.NOTE_ON && shortMessage3.getCommand() == ShortMessage.NOTE_ON && shortMessage4.getCommand() == ShortMessage.NOTE_OFF){
			//	    			 
			//	    			  System.out.println("");
			//	    			  Chord chord = new Chord(shortMessage1.getData1(),shortMessage2.getData1(),shortMessage3.getData1(),convertTicksToDuration(event4.getTick(), event1.getTick(),res)); 
			//	    			  chordList.add(chord);
			//	    			  
			//	    		  }
			//	    		  
			//	    	  }
			//	    	  
			//	      }
			//	      for(int i=0; i<chordList.size();i++){
			//	    	  System.out.println(chordList.get(i).getLabel() + "," + chordList.get(i).getDuration());
			//	    	  
			//	      }
		}
		System.out.println("Antal ackord: " + tmpcount);

		sequencer.close();
	}

}
