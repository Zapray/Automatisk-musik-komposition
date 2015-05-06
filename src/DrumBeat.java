

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
	private ArrayList<MidiEvent> drumList = new ArrayList<MidiEvent>();
	private List<Long> tickList;
	
	public DrumBeat(int beat,int bars) throws Exception{
		
		File[] files =new File (System.getProperty("user.dir")+"/Trummor/").listFiles(); 
		
		String filePath = files[beat].getName();	
		createDrumTrack(beat,bars,filePath);		
	}
	
	public List<MidiEvent> getDrumList(){
		return drumList;
	}
	
	public List<Long> getTickList(){
		return tickList;
	}
	
	private void createDrumTrack(int beat,int bars,String filePath) throws Exception{
		File file = new File(System.getProperty("user.dir")+"/Trummor/" + filePath);
		
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();
		Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		float res = sequence.getResolution();
		//System.out.println(res);
		Track[] tracks = sequence.getTracks();
		//System.out.println(tracks.length);
		Track drum = tracks[0];
		
		//System.out.println(drum.size() + "   " + drumList.size());
		//MidiEvent ehej = drum.get(40);
		//MidiMessage mes = ehej.getMessage();
		//System.out.println(mes);
		int nbrOfItter=bars/4;
		float ticksPerFourBars=res*16;
		
		
		for(int j=0; j<nbrOfItter;j++){
			//System.out.println(drum.size() + "   " + drumList.size());
			for(int nEvent = 0; nEvent < drum.size()-1; nEvent++){
				MidiEvent event = drum.get(nEvent);
				MidiMessage message = event.getMessage();
				//System.out.println(event);
				if(message instanceof ShortMessage && event.getMessage()!=null && event!=null){
					//System.out.println(j);
					//System.out.println(event.getTick());
					MidiEvent eventj = new MidiEvent(event.getMessage(),event.getTick());
					eventj.setTick((long) (j*ticksPerFourBars + event.getTick()));	
					//System.out.println(event.getTick());
					//System.out.println();
					//System.out.println(event.getTick());
					//ShortMessage shortMessage = (ShortMessage) message;
					//System.out.println(event.getMessage() + "    " + shortMessage.getCommand() + "    " +shortMessage.getData1() + "    " + shortMessage.getData2());
					//System.out.println("Loopen k�rs");
					drumList.add(eventj);
				}



			}
		}
		//System.out.println(drum.size() + "   " + drumList.size());
		sequencer.close();
		
	
	
		
	}	
		
	

	public static long convertNoteLengthToTicks(float noteLength,int res) throws Exception{
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks
}
