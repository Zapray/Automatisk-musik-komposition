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

	public static int convertTicksToLength(long tick1, long tick2, float res){
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
	}

	public static float convertTicksToDuration(long tick1, long tick2, float res){
		res=res*4;
		//System.out.println(tick1);
		//System.out.println(tick2);
		Integer tickone = (int) (long) tick1;
		Integer ticktwo = (int) (long) tick2;
		float durationindec = (tick1-tick2)/res;
		//float durationindec = (tickone-ticktwo)/res;
		//System.out.println(durationindec);

		return durationindec;

	}//end convertTicksToNoteLength

	public static boolean newTick(Track track, int nEvent){

		if(track.get(nEvent).getTick() - track.get(nEvent-1).getTick() == 0){
			return false;
		}else return true;
	}

	public static int[] extractChordNotes(MidiEvent event1, MidiEvent event2, MidiEvent event3, MidiEvent event4, MidiEvent event5, MidiEvent event6 ){

		if(event4 == null){
			MidiMessage message1 = event1.getMessage();
			MidiMessage message2 = event2.getMessage();
			MidiMessage message3 = event3.getMessage();

			ShortMessage shortMessage1 = (ShortMessage) message1;
			ShortMessage shortMessage2 = (ShortMessage) message2;
			ShortMessage shortMessage3 = (ShortMessage) message3;

			int[] commands = new int[3];
			commands[0] = shortMessage1.getCommand();
			commands[1] = shortMessage2.getCommand();
			commands[2] = shortMessage3.getCommand();

			int[] data = new int[6];
			data[0] = shortMessage1.getData1();
			data[1] = shortMessage2.getData1();
			data[2] = shortMessage3.getData1();

			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					count++;
				}

			}return notesInChord;

		}else{
			MidiMessage message1 = event1.getMessage();
			MidiMessage message2 = event2.getMessage();
			MidiMessage message3 = event3.getMessage();
			MidiMessage message4 = event4.getMessage();
			MidiMessage message5 = event5.getMessage();
			MidiMessage message6 = event6.getMessage();

			ShortMessage shortMessage1 = (ShortMessage) message1;
			ShortMessage shortMessage2 = (ShortMessage) message2;
			ShortMessage shortMessage3 = (ShortMessage) message3;
			ShortMessage shortMessage4 = (ShortMessage) message4;
			ShortMessage shortMessage5 = (ShortMessage) message5;
			ShortMessage shortMessage6 = (ShortMessage) message6;

			int[] commands = new int[6];
			commands[0] = shortMessage1.getCommand();
			commands[1] = shortMessage2.getCommand();
			commands[2] = shortMessage3.getCommand();
			commands[3] = shortMessage4.getCommand();
			commands[4] = shortMessage5.getCommand();
			commands[5] = shortMessage6.getCommand();

			
			
			int[] data = new int[6];
			data[0] = shortMessage1.getData1();
			data[1] = shortMessage2.getData1();
			data[2] = shortMessage3.getData1();
			data[3] = shortMessage4.getData1();
			data[4] = shortMessage5.getData1();
			data[5] = shortMessage6.getData1();

			System.out.println(commands[0] + " " + commands[1] + " " + commands[2] + " " +  commands[3] + " " + commands[4] + " " + commands[5]);
			
			int[] notesInChord = new int[3];

			int count = 0;
			for(int i = 0; i < commands.length; i++){

				if(commands[i] == ShortMessage.NOTE_OFF){
					notesInChord[count] = data[i];
					count++;
				}

			}
			return notesInChord;
			
		}


	}

	public static ArrayList<Chord> createChordList(Track track, int res){
		//Loop through ticks
		ArrayList<Chord> chordList=new ArrayList<Chord>();
		int chordCount = 0;
		int tickCount = 0;
		int eventCount = 0;
		int[] breakPoints = new int[(int) track.ticks()];
		breakPoints[0] = 1;
		MidiEvent [] eventAfter = new MidiEvent[track.size()];
		MidiEvent [] eventBefore = new MidiEvent[track.size()];

		//Count metaMessages in the end
		int metaMessageCount = 0;
		boolean isMeta = true;

		while(isMeta){
			metaMessageCount++;
			MidiEvent event = track.get(track.size()-metaMessageCount);
			MidiMessage message = event.getMessage();
			isMeta = message instanceof MetaMessage;
			
		}

		for(int nEvent = 0; nEvent < track.size(); nEvent++){
			MidiEvent event = track.get(nEvent);
			MidiMessage message = event.getMessage();
			//System.out.println("Tick:  " + event.getTick());

			//Skip MetaMessages in beginning
			if(message instanceof ShortMessage){

				//If PAUS in beginning
				if(eventCount == 0 && track.get(nEvent).getTick() != 0){
					Chord paus = new Chord(0, 0, 0, convertTicksToDuration(track.get(nEvent).getTick(), 0,res)); 
					chordList.add(paus);

				}
				//Extract last chord
				
				
				
				if(nEvent == track.size()-metaMessageCount-1){ //There is a MetaMessage in the end (Is it always??)
					int nbrOfEvents = eventCount - breakPoints[tickCount-1]-1;
					if(nbrOfEvents == 3){

						MidiEvent event1 = track.get(nEvent-2);
						MidiEvent event2 = track.get(nEvent-1);
						MidiEvent event3 = track.get(nEvent);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);
							Chord paus = new Chord(0, 0, 0, convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res)); 
							chordList.add(paus);
							chordCount++;

						}

					}else if(nbrOfEvents == 6){

						MidiEvent event1 = track.get(nEvent-5);
						MidiEvent event2 = track.get(nEvent-4);
						MidiEvent event3 = track.get(nEvent-3);
						MidiEvent event4 = track.get(nEvent-2);
						MidiEvent event5 = track.get(nEvent-1);
						MidiEvent event6 = track.get(nEvent);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, event4, event5, event6);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);
							chordCount++;

						}
					}
				}

				eventCount++;

				//Extract all other chords
				if(newTick(track, nEvent)){
					eventAfter[tickCount] = track.get(nEvent);
					eventBefore[tickCount] = track.get(nEvent-1);
					tickCount++;
					breakPoints[tickCount] = eventCount;
					int nbrOfEvents = eventCount - breakPoints[tickCount-1];

					if(nbrOfEvents == 3){ //Chord ON or PAUS in tick

						MidiEvent event1 = track.get(nEvent-3);
						MidiEvent event2 = track.get(nEvent-2);
						MidiEvent event3 = track.get(nEvent-1);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, null, null, null);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);


							Chord paus = new Chord(0, 0, 0, convertTicksToDuration(eventAfter[tickCount-1].getTick(), eventBefore[tickCount-1].getTick(),res)); 
							chordList.add(paus);
							chordCount++;

						}

					}else if(nbrOfEvents == 6){ //CHORD ON and OFF in same tick
						MidiEvent event1 = track.get(nEvent-6);
						MidiEvent event2 = track.get(nEvent-5);
						MidiEvent event3 = track.get(nEvent-4);
						MidiEvent event4 = track.get(nEvent-3);
						MidiEvent event5 = track.get(nEvent-2);
						MidiEvent event6 = track.get(nEvent-1);

						int[] notesInChord = new int[3];
						notesInChord = extractChordNotes(event1, event2, event3, event4, event5, event6);
						if(!Arrays.equals(notesInChord, new int[3])){

							Chord chord = new Chord(notesInChord[0],notesInChord[1],notesInChord[2], convertTicksToDuration(eventAfter[tickCount-2].getTick(), eventBefore[tickCount-2].getTick(),res)); 
							chordList.add(chord);
							chordCount++;

						}
					}
				}
			}
		}

		return chordList;
	}

	public static ArrayList<Chord> createHalfBarList(ArrayList<Chord> chordList){
		ArrayList<Chord> halfBarList = new ArrayList<Chord>();
		ArrayList<Chord> chordsInBar = new ArrayList<Chord>();

		float durationCount = 0;
		int barCount = 0; 
		for(int i = 0; i < chordList.size(); i++){

			Chord chord = chordList.get(i);
			durationCount = durationCount + chord.getDuration();
			chordsInBar.add(chord);

			if(durationCount == 1){//One bar
				barCount++;
				Chord chord1 = chordsInBar.get(0);
				if(barCount == 1 && chord1.getLabel() == null){
					if(chord1.getLabel() == null){//Paus in beginning
						if(chordsInBar.size() == 1){ 
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord("C", 0.5f));//??
						}else if(chordsInBar.size() == 2){
							Chord chord2 = chordsInBar.get(1);
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
						}else if(chordsInBar.size() == 3){
							Chord chord2 = chordsInBar.get(1);
							Chord chord3 = chordsInBar.get(2);
							halfBarList.add(new Chord("C", 0.5f));//??
							if(chord1.getDuration() == 0.5f){
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}
						}else{
							Chord chord3 = chordsInBar.get(2);
							halfBarList.add(new Chord("C", 0.5f));//??
							halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
						}
					}
				}else{
					if(chordsInBar.size() == 1){ 
						if(chord.getLabel() != null){//CHORD
							halfBarList.add(new Chord(chord.getLabel(), 0.5f));
							halfBarList.add(new Chord(chord.getLabel(), 0.5f));
						}else{//PAUS (Fill with previous chord)
							halfBarList.add(halfBarList.get(halfBarList.size()));
							halfBarList.add(halfBarList.get(halfBarList.size()));
						}
					}else if(chordsInBar.size() == 2){ //
						Chord chord2 = chordsInBar.get(1);
						if(chord1.getLabel() != null && chord2.getLabel() != null){//CHORDS
							if(chord1.getDuration() == 0.75f){//Fill with first chord twice
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							}else{//First and second chord
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}
						}else{//PAUS 
							if(chord1.getLabel() == null){ 
								halfBarList.add(halfBarList.get(halfBarList.size()));//(fill with previous chord)
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}else{ //Fill first chord twice
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							}
						}
					}else if(chordsInBar.size() == 3){ 
						Chord chord2 = chordsInBar.get(1);
						Chord chord3 = chordsInBar.get(2);
						if(chord1.getLabel() != null && chord2.getLabel() != null && chord3.getLabel() != null){//CHORDS
							if(chord1.getDuration() == 0.5f){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));

							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}
						}else{//PAUS
							if(chord1.getLabel() == null){
								halfBarList.add(halfBarList.get(halfBarList.size()));//(fill with previous chord)
								if(chord1.getDuration() == 0.5f){
									halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
								}else{
									halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
								}
							}else if(chord2.getLabel() == null){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord2.getLabel(), 0.5f));
							}
						}
					}else{ 
						Chord chord2 = chordsInBar.get(1);
						Chord chord3 = chordsInBar.get(2);
						Chord chord4 = chordsInBar.get(3);

						if(chord1.getLabel() != null && chord2.getLabel() != null && chord3.getLabel() != null && chord4.getLabel() != null){//CHORDS
							halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
							halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
						}else{//PAUS
							if(chord1.getLabel() == null){
								halfBarList.add(halfBarList.get(halfBarList.size()));//(fill with previous chord)
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}else if(chord3.getLabel() == null){
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord4.getLabel(), 0.5f));
							}else{
								halfBarList.add(new Chord(chord1.getLabel(), 0.5f));
								halfBarList.add(new Chord(chord3.getLabel(), 0.5f));
							}

						}
					}
				}
				chordsInBar.clear();
				durationCount = 0;
			}
		}
		return halfBarList;
	}

	public static void main(String[] args) throws Exception{

		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
		sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\Hooktheory-2015-02-04-01-25-10.mid")));
		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Dropbox/Chalmers/Kandidatarbete/Hooktheory_data/Chorus/Hooktheory-2015-02-18-03-40-19.mid")));
		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Dropbox/Chalmers/Kandidatarbete/Hooktheory_data/Intro/Hooktheory-2015-02-18-03-54-00.mid")));//Paus in beginning!!
		InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/KarinBrotjefors/Dropbox/Chalmers/Kandidatarbete/Hooktheory_data/Chorus/Hooktheory-2015-02-18-03-51-40.mid")));//Snabb lŒt, kompilerar ej , nŒt fel pŒ slutet...
		//InputStream is = new BufferedInputStream(new FileInputStream( new File("/Users/Albin/Desktop/Hooktheory-2015-02-18-01-46-41.mid")));
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/music.mid")));
		Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		int res = sequence.getResolution();
		//System.out.println(res);
		Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.

		int chordtrack = 1; //CHORDS
		Track track = tracks[chordtrack];

		ArrayList<Chord> chordList = createChordList(track, res);

		//Print chordList
		Iterator itr = chordList.iterator();
		while(itr.hasNext()) {
			Chord chord = (Chord) itr.next();
			System.out.println(chord.getLabel() + "   " + chord.getDuration());
		}

		ArrayList<Chord> halfBarList = createHalfBarList(chordList);

		//Print halfBarList
		System.out.println(" ");
		Iterator itr2 = halfBarList.iterator();
		while(itr2.hasNext()) {
			Chord chord = (Chord) itr2.next();
			System.out.println(chord.getLabel() + "   " + chord.getDuration());
		}

		sequencer.close();
	}

}
