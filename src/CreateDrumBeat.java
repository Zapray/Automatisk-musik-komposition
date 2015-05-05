package src;

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

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class CreateDrumBeat {

	public static long convertNoteLengthToTicks(float noteLength,int res) throws Exception{
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks
	public static void main(String[] args) throws Exception{
		
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();
		int resolution = 192;
		Sequence sequence= new Sequence(Sequence.PPQ,resolution);;
		
		Track track = sequence.createTrack();
		int perc = 36;
		long tick = convertNoteLengthToTicks((float)0.25,resolution);
		
		
		ShortMessage sm = new ShortMessage( );
		sm.setMessage(ShortMessage.PROGRAM_CHANGE, 9, 35, 0); //9 ==> is the channel 10.
		track.add(new MidiEvent(sm, 0));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_ON,perc,114);
		track.add(new MidiEvent(sm, 0));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_OFF,perc,114);
		track.add(new MidiEvent(sm,tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_ON,perc,114);
		track.add(new MidiEvent(sm, tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_OFF,perc,114);
		track.add(new MidiEvent(sm,2*tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_ON,perc,114);
		track.add(new MidiEvent(sm, 2*tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_OFF,perc,114);
		track.add(new MidiEvent(sm,3*tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_ON,perc,114);
		track.add(new MidiEvent(sm, 3*tick));
		
		sm = new ShortMessage( );
		sm.setMessage(ShortMessage.NOTE_OFF,perc,114);
		track.add(new MidiEvent(sm,4*tick));
		
		
		File outputFile = new File(System.getProperty("user.dir")+"/songs/TestSong.mid");
		MidiSystem.write(sequence, 1, outputFile);
        System.out.println("Song is created");
        sequencer.close();
	}
	
	
	
	
}
