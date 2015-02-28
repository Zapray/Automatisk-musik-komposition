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
import java.util.List;

import org.apache.commons.io.*;

import javax.sound.midi.*;

public class MidiAnalyzer {
	
	public static void main(String args[]) throws Exception{
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
		InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\Latarfranhook\\Verse\\Hooktheory-2015-02-21-01-37-31.mid")));
		//System.out.println("/Users/Albin/Desktop/Filerfranhook/Chorus/" + file.getName());
		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/Albin/Desktop/songweknow/" + file.getName())));
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/music.mid")));
		Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		float res = sequence.getResolution();
		//System.out.println(res);
		//System.out.println(sequence.getDivisionType());
		Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
		int melodytrack = 0;
		Track   track = tracks[melodytrack];
		List<ArrayList<FloatNote>> test = MelodyAnalyzer(track, res);
		System.out.println(test.size());
		System.out.println(test.get(22).size());
		for(int i=0; i<test.size(); i++ ){
			for(int j=0; j<test.get(i).size(); j++){
				System.out.println(test.get(i).size());
				System.out.println(test.get(i).get(j));
			}//end for
		}//end for
		sequencer.close();
	}//end main

	public static float convertTicksToNoteLength(long tick1, long tick2, float res){
		res=res*4;

		return (tick2-tick1)/res;

	}//end convertTicksToNoteLength
	public static List<ArrayList<FloatNote>> MelodyAnalyzer(Track track, float res){//Takes a track and returns a list of melody packages
		
		//File[] files =new File ("/Users/Albin/Desktop/songweknow/").listFiles(); 

		//for (File file : files){
			//String ext1 = FilenameUtils.getExtension(file.getName());
			//if(ext1.equals("mid")){


				long tick=1;
				long spectick=1;
				ArrayList<Integer> note= new ArrayList<Integer>(0);
				ArrayList<Float> notelength= new ArrayList<Float>(0);
				//ArrayList halfbars= new ArrayList(0);
				//int counter1=0;
				//int counter2=0;
				boolean foundNote = false;
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
				for(int index = 0; index < notelength.size()-1; index++){
					combinedLength=combinedLength + (float)notelength.get(index);
					if(combinedLength >= 0.5){
						if(lastDuration != 0){
							floatNote = new FloatNote(lastDuration, (float) note.get(where));
							melody.add(floatNote);
						}
						lastDuration = combinedLength - (float)0.5;
						
						floatNote = new FloatNote( (float)notelength.get(index)-lastDuration, (float) note.get(index));
						melody.add(floatNote);

						for(int i = index; i > where; i--){
							floatNote = new FloatNote((float) notelength.get(i), (float) note.get(i));
							melody.add(floatNote);
							
						}//End for
						melodyPack.add(melody);
						melody.clear();
						where = index;
						combinedLength=lastDuration;
						
					}//End if
					
					
					
					
					
				}//End for
				return melodyPack;
	
	}// End MelodyAnalyzer
}// End MidiAnalyzer
