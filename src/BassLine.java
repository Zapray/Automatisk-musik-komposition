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
	
	public BassLine(ArrayList<String> listOfChords) throws Exception{
		
		createListOfTonics(listOfChords);
		createQuarterNoteBaseLine();
		
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

	private void createQuarterNoteBaseLine() throws Exception {
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
	
	
}
