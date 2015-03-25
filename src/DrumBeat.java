import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class DrumBeat {
	private List<MidiEvent> drumList;
	private List<Long> tickList;
	
	public DrumBeat(int beat,int bars) throws Exception{
		String filePath = System.getProperty("user.dir")+"/BasicPop1.mid";
		
		createDrumTrack(beat,bars,filePath);
			
	}
	
	public List<MidiEvent> getDrumTrack(){
		return drumList;
	}
	public List<Long> getTickList(){
		return tickList;
	}
	
	private void createDrumTrack(int beat,int bars,String filePath) throws Exception{
		File file = new File(System.getProperty("user.dir")+"/Trummor" + filePath);
		
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();
		Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		float res = sequence.getResolution();
		Track[] tracks = sequence.getTracks();
		Track drum = tracks[0];
		
		for(int nEvent = 0; nEvent < drum.size()-1; nEvent++){
			MidiEvent event = drum.get(nEvent);
			MidiMessage message = event.getMessage();
			if(message instanceof ShortMessage){
				drumList.add(event);
				tickList.add(event.getTick());
			}
			
			
					
		}
		
		
	
	
		
	}	
		
	

	public static long convertNoteLengthToTicks(float noteLength,int res) throws Exception{
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks
}
