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
	
	
	public static void main(String[] args) throws Exception{
		 ArrayList chords = new ArrayList(ArrayList);
		 Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
	      sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
	      //InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\Hooktheory-2015-02-04-01-25-10.mid")));
	      InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Desktop/Hooktheory_data/Chorus/Hooktheory-2015-02-18-03-40-19.mid")));
	      //InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/music.mid")));
	      Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
	      float res = sequence.getResolution();
	      Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
	      
	      int chordtrack = 1;
	      Track   track = tracks[chordtrack];
	      for(int nEvent = 0; nEvent < track.size(); nEvent++){
	    	  
	    	  MidiEvent event1 = track.get(nEvent);
	    	  MidiMessage message1 = event1.getMessage();
	    	  
	    	  MidiEvent event2 = track.get(nEvent+1);
    		  MidiMessage message2 = event2.getMessage();
    		  MidiEvent event3 = track.get(nEvent+2);
    		  MidiMessage message3 = event3.getMessage();
	    	  
	    	  if(message1 instanceof ShortMessage && message2 instanceof ShortMessage && message3 instanceof ShortMessage){
	    		  ShortMessage shortMessage1 = (ShortMessage) message1;
	    		  ShortMessage shortMessage2 = (ShortMessage) message2;
	    		  ShortMessage shortMessage3 = (ShortMessage) message3;
	    		  
	    		  
	    		  if(shortMessage1.getCommand() == ShortMessage.NOTE_ON && shortMessage2.getCommand() == ShortMessage.NOTE_ON && shortMessage3.getCommand() == ShortMessage.NOTE_ON){
	    			  
	    			  note.add(shortMessage1.getData1());
	    			  note.add(shortMessage2.getData1());
	    			  note.add(shortMessage3.getData1());
	    			  
	    			  
	    		  }
	    		  
	    	  }
	    	  
	      }
	}

}
