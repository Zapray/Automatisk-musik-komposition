import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Måste se över denna, spottar ut fel ackord ibland :( Tex så blir (62, 67, 59) - (D G B) ett D när det ska vara ett G. 

public class Chord {

	private int note1;
	private int note2;
	private int note3;
	private float duration;
	private String label= "other";
	private String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	ArrayList<String> chord = new ArrayList<String>(0);
	Map<String, List<String>> chordTable= new HashMap<String, List<String>>();
	private String keys[] = {"C","Cm","D","Dm","E","Em","F","Fm","G","Gm","A","Am","B","Bm"};

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
				}else if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2))){
					label = key;
				}else if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note3))){
					label = key;
				}else if(chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
					label = key;
				}else if(chordTable.get(key).contains(note2letter(note1))){
					label = key;
				}

			}
		}

	}
	
	public Chord(String label, float duration){
		
		this.label = label;
		this.duration = duration;
		
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

	}





}
