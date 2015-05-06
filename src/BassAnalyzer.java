

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



public class BassAnalyzer{
	
	public static void main(String[] args)throws Exception{
		
	InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Documents/Chalmers/Kandidatarbete/Midifiler/Backstreet Boys - Larger Than Life.mid")));
	//InputStream is = new BufferedInputStream(new FileInputStream(new File(System.getProperty("user.dir")+"/songs/TestSong.mid")));
	Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
	sequencer.open();
	Sequence sequence = MidiSystem.getSequence(is);
	float res = sequence.getResolution();
	System.out.println(res);
	Track[] tracks = sequence.getTracks();
	
	Track basstrack = findBassTrack(tracks);
	if(basstrack == null){
		System.out.println("Didn't find bass track");
		System.exit(0);
	}
	
	Boolean veloroff = findTrackFormat(basstrack);
	
	
	
	if(veloroff == null){
		System.out.println("This file is fucked up");
		System.exit(0);
	}
	

	
	
	
	
	ArrayList<Integer> note= new ArrayList<Integer>(0);
	ArrayList<Float> notelength= new ArrayList<Float>(0);
	boolean startOfSong = true;
	int counter1=0;
	long tick=1;
	
	if(!veloroff){
		for(int nEvent = 0; nEvent < basstrack.size()-1; nEvent++){//loop through events
			MidiEvent event = basstrack.get(nEvent);
			MidiMessage message = event.getMessage();
			MidiEvent event2 = basstrack.get(nEvent+1);
			if(message instanceof MetaMessage){
				MetaMessage metaMessage=(MetaMessage) message;
				System.out.println("MEEETA");
			}

			if(message instanceof ShortMessage)//every event contains a short message or a meta message.
			{
				ShortMessage shortMessage = (ShortMessage) message;
				System.out.println(shortMessage.getCommand());
				if(shortMessage.getCommand() == ShortMessage.NOTE_ON || shortMessage.getCommand() == ShortMessage.NOTE_OFF){
					if(event.getTick()!=0 && startOfSong){

						notelength.add(convertTicksToNoteLength(0, event.getTick(), res));
						note.add(0);


						counter1+=1;
					}

					if(shortMessage.getCommand() == ShortMessage.NOTE_ON)
					{
						note.add(shortMessage.getData1());
						tick=event.getTick();

					}else if(shortMessage.getCommand() == ShortMessage.NOTE_OFF){
						notelength.add(convertTicksToNoteLength(tick, event.getTick(), res));
						counter1 +=1;  
						
						if(event.getTick()!=event2.getTick() && convertTicksToNoteLength(event.getTick(), event2.getTick(), res) > convertTicksToNoteLength(tick, event.getTick(), res)&& nEvent!=basstrack.size()-2 ){
							notelength.remove(notelength.size()-1);
							notelength.add(convertTicksToNoteLength(tick,event2.getTick(),res));
							
						}else if(event.getTick()!=event2.getTick()&& nEvent!=basstrack.size()-2){
							notelength.add(convertTicksToNoteLength(event.getTick(), event2.getTick(), res));
							note.add(0);

							counter1+=1;
						}


					}

					startOfSong=false;

				}
			}

		}
	}else{
		for(int nEvent = 0; nEvent < basstrack.size()-1; nEvent++){//loop through events
			MidiEvent event = basstrack.get(nEvent);
			MidiMessage message = event.getMessage();
			MidiEvent event2 = basstrack.get(nEvent+1);
			if(message instanceof MetaMessage){
				MetaMessage metaMessage=(MetaMessage) message;

			}

			if(message instanceof ShortMessage)//every event contains a short message or a meta message.
			{
				ShortMessage shortMessage = (ShortMessage) message;

				if(event.getTick()!=0 && startOfSong){

					notelength.add(convertTicksToNoteLength(0, event.getTick(), res));
					note.add(0);


					counter1+=1;
				}
				System.out.println(shortMessage.getCommand());
				if(shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2()!=0)
				{
					note.add(shortMessage.getData1());
					tick=event.getTick();
				}else if(shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2()==0){
					notelength.add(convertTicksToNoteLength(tick, event.getTick(), res));
					counter1 +=1;  
					
					if(event.getTick()!=event2.getTick() && convertTicksToNoteLength(event.getTick(), event2.getTick(), res) > convertTicksToNoteLength(tick, event.getTick(), res)&& nEvent!=basstrack.size()-2 ){
						notelength.remove(notelength.size()-1);
						notelength.add(convertTicksToNoteLength(tick,event2.getTick(),res));
					}else if(event.getTick()!=event2.getTick()&& nEvent!=basstrack.size()-2){
						notelength.add(convertTicksToNoteLength(event.getTick(), event2.getTick(), res));
						note.add(0);

						counter1+=1;
					}


				}

				startOfSong=false;


			}

		}
		
		
	}
	
	
	
	
	
	File filen = new File(System.getProperty("user.dir")+"/bass.txt");
	// if file doesnt exists, then create it
	if (!filen.exists()) {
		filen.createNewFile();
	}


	PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/bass.txt", true));


	for (int i=0;i<counter1;i++){

		outFile.println(note.get(i) + "," + notelength.get(i));

	}

	outFile.close();
	
	
	
	
	
	
	
	
	sequencer.close();
	

	
	
	}

	private static Boolean findTrackFormat(Track track) {
		int noteoffs = 0;
		int noteons = 0;
		
		for(int nEvent = 0; nEvent < track.size(); nEvent++){//loop through events
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();

			if(message instanceof ShortMessage){//every event contains a short message or a meta message.
				ShortMessage shortMessage = (ShortMessage) message;
				if(shortMessage.getCommand() == ShortMessage.NOTE_ON){
					noteons++;
				}
				else if(shortMessage.getCommand() ==ShortMessage.NOTE_OFF){
					noteoffs++;
				}
			}
			
		}
		if(noteoffs==0){
			return true;
		}else if(noteons > noteoffs){
			return null;
		}else if(noteons == noteoffs){
			return false;
		}
		return null;
	}

	private static Track findBassTrack(Track[] tracks){
		Track nullTrack = null;
		for(int i=0;i<tracks.length;i++){
			
			for(int j=0; j<tracks[i].size();j++){
				MidiEvent event = tracks[i].get(j);
				MidiMessage message = event.getMessage();
				if(message instanceof ShortMessage){
					ShortMessage sMess = (ShortMessage) message;
					int command = sMess.getCommand();
					if (command == 192){
						int instNbr = sMess.getData1();
						
						if (instNbr==33 ||instNbr==34 ||instNbr==35 ||instNbr==36 ||instNbr==37 ||instNbr==38 ||instNbr==39|| instNbr ==40){
							System.out.println(instNbr);
							return tracks[i];
						}
					}
						
				}
				
			}
			
		}
		return 	nullTrack;
		
		
	}
	

	public static float convertTicksToNoteLength(long tick1, long tick2, float res){
		res=res*4;
		
		//System.out.println("TickNoteOn: " + tick1 + "  "+ "TickNoteOff: " + tick2 + "  " +"Duration:  " + (tick2-tick1)/res);
		return (tick2-tick1)/res;

	}//end convertTicksToNoteLength


}
