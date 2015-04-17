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

public class BassLine {
	private ArrayList<String> listOfTonics = new ArrayList<String>();
	private String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	private int resolution =192;
	private ArrayList<MidiEvent> bassLine = new ArrayList<MidiEvent>();
	private ArrayList<Float> listOfDurations = new ArrayList<Float>();
	
	public BassLine(ArrayList<Chord> chords) throws Exception{
		ArrayList<String> listOfChords = new ArrayList<String>(); 
		
		for(int i=0;i<chords.size();i++){
			listOfChords.add(chords.get(i).getLabel());	
			listOfDurations.add(chords.get(i).getDuration());
		}
		
		createListOfTonics(listOfChords);
		//createEightNoteBaseLine();
		//createQuarterNoteBassLine();
		//createHalfNoteBassLine();
		//createQuarterEightNoteBassLine();
		//createBerlinNightBassLine();
		createPianorythmBassLine();
	}
	public ArrayList<MidiEvent> getBassLine(){
		
		return bassLine;
	}
		
	private int convertLabelToNote(String note) {
		int a=0;
		for(String not : notes){
			
			if(not.equals(note)){
				return(36+a);
			}
			a++;
		}
		return 0;
	}

	private void createEightNoteBaseLine() throws Exception {
		int tick =resolution/2;
		int tickMeter=0;
		for(int i=0;i<listOfTonics.size();i++){
			for(int j=0;j<4;j++){
				ShortMessage	shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);
			}
		}
		
	}
	private void createQuarterNoteBassLine() throws Exception{
		int tick =resolution;
		int tickMeter=0;
		for(int i=0;i<listOfTonics.size();i++){
			for(int j=0;j<2;j++){
				ShortMessage	shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);
			}
		}
		
		
		
	}
	private void createHalfNoteBassLine() throws Exception{
		int tick =2*resolution;
		int tickMeter=0;
		for(int i=0;i<listOfTonics.size();i++){
			for(int j=0;j<1;j++){
				ShortMessage	shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);
			}
		}
		
		
		
	}
	private void createQuarterEightNoteBassLine() throws Exception{
		int tick1= 288;
		int tick2 = 96;
		int tickMeter=0;
		for(int i=0;i<listOfTonics.size();i++){
			for(int j=0;j<1;j++){
				ShortMessage	shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick1;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);
				shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick2;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);
				
			}
		}	
	}
	
	private void createBerlinNightBassLine() throws Exception{
		int tick1= resolution/2;
		int tickMeter=0;
		for(int i=0;i<listOfTonics.size();i=i+2){
			for(int j=0;j<4;j++){
				ShortMessage shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick1;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);	
			}
			for(int j=0;j<2;j++){
				ShortMessage shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i))+5, 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick1;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i))+5, 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);	
			
			}
			for(int j=0;j<2;j++){
				ShortMessage shortMessage1 = new ShortMessage();
				shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i))+7, 114 );
				MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
				bassLine.add(NoteOn);
				ShortMessage	shortMessage2 = new ShortMessage();
				tickMeter=tickMeter + tick1;	
				shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i))+7, 0 );
				MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
				bassLine.add(NoteOff);	
			
			}
		}	
		
		
	}
	private void createPianorythmBassLine() throws Exception{
		int tick = 0;
		int tickMeter = 0;
		for(int i = 0; i<listOfTonics.size();i++){
			ShortMessage shortMessage1 = new ShortMessage();
			shortMessage1.setMessage(ShortMessage.NOTE_ON,0,convertLabelToNote(listOfTonics.get(i)), 114 );
			MidiEvent NoteOn=new MidiEvent(shortMessage1,tickMeter);
			bassLine.add(NoteOn);
			ShortMessage	shortMessage2 = new ShortMessage();
			tickMeter=(int) (tickMeter + convertNoteLengthToTicks(listOfDurations.get(i), resolution));	
			shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,convertLabelToNote(listOfTonics.get(i)), 0 );
			MidiEvent NoteOff=new MidiEvent(shortMessage2,tickMeter);
			bassLine.add(NoteOff);	
			
			
		}
		
		
		
	}
	
	
	
	
	
	public void printBassLine(){
		for(int i=0;i<bassLine.size();i++){
			System.out.println(bassLine.get(i));
		}
		
		
		
	}
	public void createListOfTonics(ArrayList<String> listOfChords){
		
		for(int i = 0;i<listOfChords.size();i++){
			for(String note : notes){
				
				if(listOfChords.get(i).length()>1){
					if(listOfChords.get(i).substring(0,1).equals(note)){
						listOfTonics.add(listOfChords.get(i).substring(0,1));
					}else if(listOfChords.get(i).substring(0, 0).equals(note)){
						listOfTonics.add(listOfChords.get(i).substring(0, 0));
					}
					
				}else if(listOfChords.get(i).equals(note)){
					listOfTonics.add(listOfChords.get(i));	
				}
			}
			
			
			
			
		}
		
		
		
	}
	public void printListOfTonics(){
		for(int i=0; i<listOfTonics.size();i++){
			System.out.println(listOfTonics.get(i));
		}

	}
	public static long convertNoteLengthToTicks(float noteLength,int res) throws Exception{
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks
	
}
