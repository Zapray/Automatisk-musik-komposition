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
	
	
	
	public static void main(String[] args) throws Exception{
		 ArrayList<Chord> chordList=new ArrayList<Chord>(0);
		 Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
	      sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
	      //InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\Hooktheory-2015-02-04-01-25-10.mid")));
	      //InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Desktop/Hooktheory_data/Chorus/Hooktheory-2015-02-18-03-40-19.mid")));
	      InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/Albin/Desktop/Hooktheory-2015-02-18-01-46-41.mid")));
	      //InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/music.mid")));
	      Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
	      int res = sequence.getResolution();
	      //System.out.println(res);
	      Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
	      
	      int chordtrack = 1;
	      Track   track = tracks[chordtrack];
	      for(int nEvent = 0; nEvent < track.size()-3; nEvent++){
	
	    	  MidiEvent event1 = track.get(nEvent);
	    	  MidiMessage message1 = event1.getMessage();

	    	  MidiEvent event2 = track.get(nEvent+1);
    		  MidiMessage message2 = event2.getMessage();
    		  MidiEvent event3 = track.get(nEvent+2);
    		  MidiMessage message3 = event3.getMessage();
    		  
    		  MidiEvent event4=track.get(nEvent+3);
    		  MidiMessage message4 = event4.getMessage();
    		  
    		  
	    	  
	    	  if(message1 instanceof ShortMessage && message2 instanceof ShortMessage && message3 instanceof ShortMessage && message4 instanceof ShortMessage){
	    		  ShortMessage shortMessage1 = (ShortMessage) message1;
	    		  System.out.println(shortMessage1.getCommand());
	    		  ShortMessage shortMessage2 = (ShortMessage) message2;
	    		  System.out.println(shortMessage2.getCommand());
	  
	    		  ShortMessage shortMessage3 = (ShortMessage) message3;
	    		  System.out.println(shortMessage3.getCommand());
	    		  ShortMessage shortMessage4 = (ShortMessage) message4;
	    		  System.out.println(shortMessage4.getCommand());
	    		  System.out.println("");
	    		  System.out.println(event1.getTick());
	    		  System.out.println(event2.getTick());
	    		  System.out.println(event3.getTick());
    			  System.out.println(event4.getTick());
    			  System.out.println("");
	    		  
	    		  
	    		  if(shortMessage1.getCommand() == ShortMessage.NOTE_ON && shortMessage2.getCommand() == ShortMessage.NOTE_ON && shortMessage3.getCommand() == ShortMessage.NOTE_ON && shortMessage4.getCommand() == ShortMessage.NOTE_OFF){
	    			 
	    			  System.out.println("");
	    			  Chord chord = new Chord(shortMessage1.getData1(),shortMessage2.getData1(),shortMessage3.getData1(),convertTicksToDuration(event4.getTick(), event1.getTick(),res)); 
	    			  chordList.add(chord);
	    			  
	    		  }
	    		  
	    	  }
	    	  
	      }
	      for(int i=0; i<chordList.size();i++){
	    	  System.out.println(chordList.get(i).getLabel() + "," + chordList.get(i).getDuration());
	    	  
	      }
	
	
	
	}

}
