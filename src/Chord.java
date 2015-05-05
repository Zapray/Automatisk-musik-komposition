package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//M�ste se �ver denna, spottar ut fel ackord ibland :( Tex s� blir (62, 67, 59) - (D G B) ett D n�r det ska vara ett G. 

public class Chord {

	private int note1;
	private int note2;
	private int note3;
	private int vel;
	private float duration;
	private String label= "other";
	private String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	ArrayList<String> chord = new ArrayList<String>(0);
	Map<String, List<String>> chordTable= new HashMap<String, List<String>>();
	private String keys[] = {"C","F","G","Am","Dm","Em","Cm","D","E","G#","Gm","Fm","F#","A","B","Bm","C#","D#","A#","Cm#","Dm#","Fm#","Gm#","Am#","Cdim","Ddim","Edim","Fdim","Gdim","Adim","Bdim","C#dim","D#dim","F#dim","G#dim","A#dim","Caug","Daug","Eaug","Faug","Gaug","Aaug","Baug","C#aug","D#aug","F#aug","G#aug","A#aug","C7","D7","E7","F7","G7","A7","B7","C#7","D#7","F#7","G#7","A#7","Cm7","Dm7","Em7","Fm7","Gm7","Am7","Bm7","Cm#7","Dm#7","Fm#7","Gm#7","Am#7","Csus","Dsus","Esus","Fsus","Gsus","Asus","Bsus","C#sus","D#sus","F#sus","G#sus","A#sus"};

	public static void main(String[] args){
		Chord theChord = new Chord("");
		System.out.println(theChord.getNote1() + " " + theChord.getNote2()+ " "+ theChord.getNote3());
	}
	public Chord(String ackordet){
		createChordTable();
		for(String key : keys){
			if(key.equals(ackordet)){
				List<String> allnotes = chordTable.get(key);
				note1 = letter2note(allnotes.get(0));
				note2 = letter2note(allnotes.get(1));
				note3 = letter2note(allnotes.get(2));
				
				
			}
			
		}
		
		
		
		
	}
	public int getVel(){
		return vel;
	}
	public Chord(int note1, int note2, int note3, float duration,int vel){

		this.note1 = note1;
		this.note2 = note2;
		this.note3 = note3;
		this.duration = duration;
		this.vel = vel;
		int [] notesInChord = new int[3];
		notesInChord[0] = note1;
		notesInChord[1] = note2;
		notesInChord[2] = note3;

		if(Arrays.equals(notesInChord, new int[3])){
			label = null;
		}else{
			this.chord.add(note2letter(note1));
			this.chord.add(note2letter(note2));
			this.chord.add(note2letter(note3));
			createChordTable();

			for(String key : keys){

				if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
					label = key;
					break;
				}

			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note3))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1))){
						label = key;
						break;
					}
				}
			}
			
		}

	}
	
	public Chord(int note1, int note2, int note3, float duration){

		this.note1 = note1;
		this.note2 = note2;
		this.note3 = note3;
		this.duration = duration;

		int [] notesInChord = new int[3];
		notesInChord[0] = note1;
		notesInChord[1] = note2;
		notesInChord[2] = note3;

		if(Arrays.equals(notesInChord, new int[3])){
			label = null;
		}else{
			this.chord.add(note2letter(note1));
			this.chord.add(note2letter(note2));
			this.chord.add(note2letter(note3));
			createChordTable();

			for(String key : keys){

				if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
					label = key;
					break;
				}

			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note3))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
						label = key;
						break;
					}
				}
			}
			if(label=="other"){
				for(String key : keys){
					if(chordTable.get(key).contains(note2letter(note1))){
						label = key;
						break;
					}
				}
			}
			
		}

	}
	
	
	
	
	
	
	public Chord(String label, float duration){
		
		this.label = label;
		this.duration = duration;
		createChordTable();
		for(String key : keys){
			if(key.equals(label)){
				List<String> allnotes = chordTable.get(key);
				note1 = letter2note(allnotes.get(0));
				note2 = letter2note(allnotes.get(1));
				note3 = letter2note(allnotes.get(2));
				
				
			}
			
		}
		
	}
	
	public Chord(String label, float duration,int vel){
		this.vel = vel;
		this.label = label;
		this.duration = duration;
		createChordTable();
		for(String key : keys){
			if(key.equals(label)){
				List<String> allnotes = chordTable.get(key);
				note1 = letter2note(allnotes.get(0));
				note2 = letter2note(allnotes.get(1));
				note3 = letter2note(allnotes.get(2));
				
				
			}
			
		}
	}
	
	public float getDuration(){
		return this.duration;
	}

	public String getLabel(){
		return this.label;
	}

	public String note2letter(int note){

		return notes[note%12];
	}
	public int getNote1(){

		return note1;
	}
	public int getNote2(){

		return note2;
	}

	public int getNote3(){
		return note3;
		
	}
	public void setDuration(float durr){
		this.duration=durr;
	}
	
	
	public int letter2note(String note){
		for(int i = 0 ; i < notes.length;i++ ){
			if(note.equals(notes[i])){
				return 60+i;
			}
			
		}
		return 0;
		
		
	}
	
	
	
	public void createChordTable(){
		List<String> chordC = new ArrayList<String>();
		List<String> chordCsharp = new ArrayList<String>();
		List<String> chordD = new ArrayList<String>();
		List<String> chordDsharp = new ArrayList<String>();
		List<String> chordE = new ArrayList<String>();
		List<String> chordF = new ArrayList<String>();
		List<String> chordFsharp = new ArrayList<String>();
		List<String> chordG = new ArrayList<String>();
		List<String> chordGsharp = new ArrayList<String>();
		List<String> chordA = new ArrayList<String>();
		List<String> chordAsharp = new ArrayList<String>();
		List<String> chordB = new ArrayList<String>();
		List<String> chordCm = new ArrayList<String>();
		List<String> chordCmsharp = new ArrayList<String>();
		List<String> chordDm = new ArrayList<String>();
		List<String> chordDmsharp = new ArrayList<String>();
		List<String> chordEm = new ArrayList<String>();
		List<String> chordFm = new ArrayList<String>();
		List<String> chordFmsharp = new ArrayList<String>();
		List<String> chordGm = new ArrayList<String>();
		List<String> chordGmsharp = new ArrayList<String>();
		List<String> chordAm = new ArrayList<String>();
		List<String> chordAmsharp = new ArrayList<String>();
		List<String> chordBm = new ArrayList<String>();
		List<String> chordCdim = new ArrayList<String>();
		List<String> chordCsharpdim = new ArrayList<String>();
		List<String> chordDdim = new ArrayList<String>();
		List<String> chordDsharpdim = new ArrayList<String>();
		List<String> chordEdim = new ArrayList<String>();
		List<String> chordFdim = new ArrayList<String>();
		List<String> chordFsharpdim = new ArrayList<String>();
		List<String> chordGdim = new ArrayList<String>();
		List<String> chordGsharpdim = new ArrayList<String>();
		List<String> chordAdim = new ArrayList<String>();
		List<String> chordAsharpdim = new ArrayList<String>();
		List<String> chordBdim = new ArrayList<String>();
		List<String> chordCaug = new ArrayList<String>();
		List<String> chordDaug = new ArrayList<String>();
		List<String> chordEaug = new ArrayList<String>();
		List<String> chordFaug = new ArrayList<String>();
		List<String> chordGaug = new ArrayList<String>();
		List<String> chordAaug = new ArrayList<String>();
		List<String> chordBaug = new ArrayList<String>();
		List<String> chordCsharpaug = new ArrayList<String>();
		List<String> chordDsharpaug = new ArrayList<String>();
		List<String> chordFsharpaug = new ArrayList<String>();
		List<String> chordGsharpaug = new ArrayList<String>();
		List<String> chordAsharpaug = new ArrayList<String>();
		List<String> chordC7 = new ArrayList<String>();
		List<String> chordD7 = new ArrayList<String>();
		List<String> chordE7 = new ArrayList<String>();
		List<String> chordF7 = new ArrayList<String>();
		List<String> chordG7 = new ArrayList<String>();
		List<String> chordA7 = new ArrayList<String>();
		List<String> chordB7 = new ArrayList<String>();
		List<String> chordCsharp7 = new ArrayList<String>();
		List<String> chordDsharp7 = new ArrayList<String>();
		List<String> chordFsharp7 = new ArrayList<String>();
		List<String> chordGsharp7 = new ArrayList<String>();
		List<String> chordAsharp7 = new ArrayList<String>();
		List<String> chordCm7 = new ArrayList<String>();
		List<String> chordCmsharp7 = new ArrayList<String>();
		List<String> chordDm7 = new ArrayList<String>();
		List<String> chordDmsharp7 = new ArrayList<String>();
		List<String> chordEm7 = new ArrayList<String>();
		List<String> chordFm7 = new ArrayList<String>();
		List<String> chordFmsharp7 = new ArrayList<String>();
		List<String> chordGm7 = new ArrayList<String>();
		List<String> chordGmsharp7 = new ArrayList<String>();
		List<String> chordAm7 = new ArrayList<String>();
		List<String> chordAmsharp7 = new ArrayList<String>();
		List<String> chordBm7 = new ArrayList<String>();
		List<String> chordCsus = new ArrayList<String>();
		List<String> chordCsharpsus = new ArrayList<String>();
		List<String> chordDsus = new ArrayList<String>();
		List<String> chordDsharpsus = new ArrayList<String>();
		List<String> chordEsus = new ArrayList<String>();
		List<String> chordFsus = new ArrayList<String>();
		List<String> chordFsharpsus = new ArrayList<String>();
		List<String> chordGsus = new ArrayList<String>();
		List<String> chordGsharpsus = new ArrayList<String>();
		List<String> chordAsus = new ArrayList<String>();
		List<String> chordAsharpsus = new ArrayList<String>();
		List<String> chordBsus = new ArrayList<String>();


		chordC.add("C"); chordC.add("E"); chordC.add("G");
		chordCsharp.add("C#"); chordCsharp.add("E#"); chordCsharp.add("G#");
		chordD.add("D"); chordD.add("F#"); chordD.add("A");
		chordDsharp.add("D#"); chordDsharp.add("G"); chordDsharp.add("A#");
		chordE.add("E"); chordE.add("G#"); chordE.add("B");
		chordF.add("F"); chordF.add("A"); chordF.add("C");
		chordFsharp.add("F#"); chordFsharp.add("A#"); chordFsharp.add("C#");
		chordG.add("G"); chordG.add("B"); chordG.add("D");
		chordGsharp.add("G#"); chordGsharp.add("C"); chordGsharp.add("D#");
		chordA.add("A"); chordA.add("C#"); chordA.add("E");
		chordAsharp.add("A#"); chordAsharp.add("D"); chordAsharp.add("F");
		chordB.add("B"); chordB.add("D#"); chordB.add("F#");
		chordCm.add("C"); chordCm.add("D#"); chordCm.add("G");
		chordCmsharp.add("C#"); chordCmsharp.add("E"); chordCmsharp.add("G#");
		chordDm.add("D"); chordDm.add("F"); chordDm.add("A");
		chordDmsharp.add("D#"); chordDmsharp.add("F#"); chordDmsharp.add("A#");
		chordEm.add("E"); chordEm.add("G"); chordEm.add("B");
		chordFm.add("F"); chordFm.add("G#"); chordFm.add("C");
		chordFmsharp.add("F#"); chordFmsharp.add("A"); chordFmsharp.add("C#");
		chordGm.add("G"); chordGm.add("A#"); chordGm.add("D");
		chordGmsharp.add("G#"); chordGmsharp.add("B"); chordGmsharp.add("D#");
		chordAm.add("A"); chordAm.add("C"); chordAm.add("E");
		chordAmsharp.add("A#"); chordAmsharp.add("C#"); chordAmsharp.add("F");
		chordBm.add("B"); chordBm.add("D"); chordBm.add("F#");
		chordCdim.add("C"); chordCdim.add("D#"); chordCdim.add("F#");
		chordDdim.add("D"); chordDdim.add("F"); chordDdim.add("G#");
		chordEdim.add("E"); chordEdim.add("G"); chordEdim.add("A#");
		chordFdim.add("F"); chordFdim.add("G#"); chordFdim.add("B");
		chordGdim.add("G"); chordGdim.add("A#"); chordGdim.add("C#");
		chordAdim.add("A"); chordAdim.add("C"); chordAdim.add("D#");
		chordBdim.add("B"); chordBdim.add("D"); chordBdim.add("F");
		chordCsharpdim.add("C#"); chordCsharpdim.add("E"); chordCsharpdim.add("G");
		chordDsharpdim.add("D#"); chordDsharpdim.add("F#"); chordDsharpdim.add("A");
		chordFsharpdim.add("F#"); chordFsharpdim.add("A"); chordFsharpdim.add("C");
		chordGsharpdim.add("G#"); chordGsharpdim.add("B"); chordGsharpdim.add("D");
		chordAsharpdim.add("A#"); chordAsharpdim.add("C#"); chordAsharpdim.add("E");
		chordCaug.add("C"); chordCaug.add("E"); chordCaug.add("G#");
		chordDaug.add("D"); chordDaug.add("F#"); chordDaug.add("A#");
		chordEaug.add("E"); chordEaug.add("G#"); chordEaug.add("C");
		chordFaug.add("F"); chordFaug.add("A"); chordFaug.add("C#");
		chordGaug.add("G"); chordGaug.add("B"); chordGaug.add("D#");
		chordAaug.add("A"); chordAaug.add("C#"); chordAaug.add("F");
		chordBaug.add("B"); chordBaug.add("D#"); chordBaug.add("G");
		chordCsharpaug.add("C#"); chordCsharpaug.add("F"); chordCsharpaug.add("A");
		chordDsharpaug.add("D#"); chordDsharpaug.add("G"); chordDsharpaug.add("B");
		chordFsharpaug.add("F#"); chordFsharpaug.add("A#"); chordFsharpaug.add("D");
		chordGsharpaug.add("G#"); chordGsharpaug.add("C"); chordGsharpaug.add("E");
		chordAsharpaug.add("A#"); chordAsharpaug.add("D"); chordAsharpaug.add("F#");
		chordC7.add("C"); chordC7.add("E"); chordC7.add("B");
		chordD7.add("D"); chordD7.add("F#"); chordD7.add("C#");
		chordE7.add("E"); chordE7.add("G#"); chordE7.add("D#");
		chordF7.add("F"); chordF7.add("A"); chordF7.add("E");
		chordG7.add("G"); chordG7.add("B"); chordG7.add("F#");
		chordA7.add("A"); chordA7.add("C#"); chordA7.add("G#");
		chordB7.add("B"); chordB7.add("D#"); chordB7.add("A#");
		chordCsharp7.add("C#"); chordCsharp7.add("F"); chordCsharp7.add("C");
		chordDsharp7.add("D#"); chordDsharp7.add("G"); chordDsharp7.add("D");
		chordFsharp7.add("F#"); chordFsharp7.add("A#"); chordFsharp7.add("F");
		chordGsharp7.add("G#"); chordGsharp7.add("C"); chordGsharp7.add("G");
		chordAsharp7.add("A#"); chordAsharp7.add("D"); chordAsharp7.add("A");
		chordCm7.add("C"); chordCm7.add("D#"); chordCm7.add("A#");
		chordCmsharp7.add("C#"); chordCmsharp7.add("E"); chordCmsharp7.add("B");
		chordDm7.add("D"); chordDm7.add("F"); chordDm7.add("C");
		chordDmsharp7.add("D#"); chordDmsharp7.add("F#"); chordDmsharp7.add("C#");
		chordEm7.add("E"); chordEm7.add("G"); chordEm7.add("D");
		chordFm7.add("F"); chordFm7.add("G#"); chordFm7.add("D#");
		chordFmsharp7.add("F#"); chordFmsharp7.add("A"); chordFmsharp7.add("E");
		chordGm7.add("G"); chordGm7.add("A#"); chordGm7.add("F");
		chordGmsharp7.add("G#"); chordGmsharp7.add("B"); chordGmsharp7.add("F#");
		chordAm7.add("A"); chordAm7.add("C"); chordAm7.add("G");
		chordAmsharp7.add("A#"); chordAmsharp7.add("C#"); chordAmsharp7.add("G#");
		chordBm7.add("B"); chordBm7.add("D"); chordBm7.add("A#");
		chordCsus.add("C"); chordCsus.add("F"); chordCsus.add("G");
		chordCsharpsus.add("C#"); chordCsharpsus.add("F#"); chordCsharpsus.add("G#");
		chordDsus.add("D"); chordDsus.add("G"); chordDsus.add("A");
		chordDsharpsus.add("D#"); chordDsharpsus.add("G#"); chordDsharpsus.add("A#");
		chordEsus.add("E"); chordEsus.add("A"); chordEsus.add("B");
		chordFsus.add("F"); chordFsus.add("A#"); chordFsus.add("C");
		chordFsharpsus.add("F#"); chordFsharpsus.add("B"); chordFsharpsus.add("C#");
		chordGsus.add("G"); chordGsus.add("C"); chordGsus.add("D");
		chordGsharpsus.add("G#"); chordGsharpsus.add("C#"); chordGsharpsus.add("D#");
		chordAsus.add("A"); chordAsus.add("D"); chordAsus.add("E");
		chordAsharpsus.add("A#"); chordAsharpsus.add("D#"); chordAsharpsus.add("F");
		chordBsus.add("B"); chordBsus.add("E"); chordBsus.add("F#");
		
		chordTable.put("C", chordC);
		chordTable.put("C#", chordCsharp);
		chordTable.put("D", chordD);
		chordTable.put("D#", chordDsharp);
		chordTable.put("E", chordE);
		chordTable.put("F", chordF);
		chordTable.put("F#", chordFsharp);
		chordTable.put("G", chordG);
		chordTable.put("G#", chordGsharp);
		chordTable.put("A", chordA);
		chordTable.put("A#", chordAsharp);
		chordTable.put("B", chordB);
		chordTable.put("Cm", chordCm);
		chordTable.put("Cm#", chordCmsharp);
		chordTable.put("Dm", chordDm);
		chordTable.put("Dm#", chordDmsharp);
		chordTable.put("Em", chordEm);
		chordTable.put("Fm", chordFm);
		chordTable.put("Fm#", chordFmsharp);
		chordTable.put("Gm", chordGm);
		chordTable.put("Gm#", chordGmsharp);
		chordTable.put("Am", chordAm);
		chordTable.put("Am#", chordAmsharp);
		chordTable.put("Bm", chordBm);
		chordTable.put("Cdim", chordCdim);
		chordTable.put("Ddim", chordDdim);
		chordTable.put("Edim", chordEdim);
		chordTable.put("Fdim", chordFdim);
		chordTable.put("Gdim", chordGdim);
		chordTable.put("Adim", chordAdim);
		chordTable.put("Bdim", chordBdim);
		chordTable.put("C#dim", chordCsharpdim);
		chordTable.put("D#dim", chordDsharpdim);
		chordTable.put("F#dim", chordFsharpdim);
		chordTable.put("G#dim", chordGsharpdim);
		chordTable.put("A#dim", chordAsharpdim);
		chordTable.put("Caug", chordCaug);
		chordTable.put("Daug", chordDaug);
		chordTable.put("Eaug", chordEaug);
		chordTable.put("Faug", chordFaug);
		chordTable.put("Gaug", chordGaug);
		chordTable.put("Aaug", chordAaug);
		chordTable.put("Baug", chordBaug);
		chordTable.put("C#aug", chordCsharpaug);
		chordTable.put("D#aug", chordDsharpaug);
		chordTable.put("F#aug", chordFsharpaug);
		chordTable.put("G#aug", chordGsharpaug);
		chordTable.put("A#aug", chordAsharpaug);
		chordTable.put("C7", chordC7);
		chordTable.put("C#7", chordCsharp7);
		chordTable.put("D7", chordD7);
		chordTable.put("D#7", chordDsharp7);
		chordTable.put("E7", chordE7);
		chordTable.put("F7", chordF7);
		chordTable.put("F#7", chordFsharp7);
		chordTable.put("G7", chordG7);
		chordTable.put("G#7", chordGsharp7);
		chordTable.put("A7", chordA7);
		chordTable.put("A#7", chordAsharp7);
		chordTable.put("B7", chordB7);
		chordTable.put("Cm7", chordCm7);
		chordTable.put("Cm#7", chordCmsharp7);
		chordTable.put("Dm7", chordDm7);
		chordTable.put("Dm#7", chordDmsharp7);
		chordTable.put("Em7", chordEm7);
		chordTable.put("Fm7", chordFm7);
		chordTable.put("Fm#7", chordFmsharp7);
		chordTable.put("Gm7", chordGm7);
		chordTable.put("Gm#7", chordGmsharp7);
		chordTable.put("Am7", chordAm7);
		chordTable.put("Am#7", chordAmsharp7);
		chordTable.put("Bm7", chordBm7);
		chordTable.put("Csus", chordCsus);
		chordTable.put("C#sus", chordCsharpsus);
		chordTable.put("Dsus", chordDsus);
		chordTable.put("D#sus", chordDsharpsus);
		chordTable.put("Esus", chordEsus);
		chordTable.put("Fsus", chordFsus);
		chordTable.put("F#sus", chordFsharpsus);
		chordTable.put("Gsus", chordGsus);
		chordTable.put("G#sus", chordGsharpsus);
		chordTable.put("Asus", chordAsus);
		chordTable.put("A#sus", chordAsharpsus);
		chordTable.put("Bsus", chordBsus);


	}





}