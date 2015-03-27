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
	
	public BassLine(ArrayList<String> listOfChords){
		createListOfTonics(listOfChords);
		
		
		
	}
	
	public void createListOfTonics(ArrayList<String> listOfChords){
		
		for(int i = 0;i<listOfChords.size();i++){
			for(String note : notes){
				if(listOfChords.get(i).length()>1){
					if(listOfChords.get(i).substring(0,2) == note){
						listOfTonics.add(listOfChords.get(i).substring(0,2));
					}else if(listOfChords.get(i).substring(0, 1) == note){
						listOfTonics.add(listOfChords.get(i).substring(0, 1));
					}
					
				}else{
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
