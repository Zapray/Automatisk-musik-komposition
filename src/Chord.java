import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Måste se över denna, spottar ut fel ackord ibland :( Tex så blir (62, 67, 59) - (D G B) ett D när det ska vara ett G. 

public class Chord {

	private int note1;
	private int note2;
	private int note3;
	private int duration;
	private String label= "other";
	private String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	ArrayList<String> chord = new ArrayList<String>(0);
	Map<String, List<String>> chordTable= new HashMap<String, List<String>>();
	private String keys[] = {"C","Cm","D","Dm","E","Em","F","Fm","G","Gm","A","Am","B","Bm"};
	
	public static void main(String[] args){
		Chord theChord = new Chord(76, 72, 79, 0);
		System.out.println(theChord.getLabel());
	}
	
	public Chord(int note1, int note2, int note3, int duration){
		
		this.note1 = note1;
		this.note2 = note2;
		this.note3 = note3;
		this.duration = duration;
		this.chord.add(note2letter(note1));
		this.chord.add(note2letter(note2));
		this.chord.add(note2letter(note3));
		createChordTable();
		
		for(String key : keys){
			
			if(chordTable.get(key).contains(note2letter(note1)) && chordTable.get(key).contains(note2letter(note2)) && chordTable.get(key).contains(note2letter(note3))){
				label = key;
			}
			
		}
		
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String note2letter(int note){
		
		return notes[note%12];
	}
	
	public void createChordTable(){
		List<String> chordC = new ArrayList<String>();
		//List<String> chordCsharp = new ArrayList<String>();
		List<String> chordD = new ArrayList<String>();
		List<String> chordE = new ArrayList<String>();
		List<String> chordF = new ArrayList<String>();
		List<String> chordG = new ArrayList<String>();
		List<String> chordA = new ArrayList<String>();
		List<String> chordB = new ArrayList<String>();
		List<String> chordCm = new ArrayList<String>();
		List<String> chordDm = new ArrayList<String>();
		List<String> chordEm = new ArrayList<String>();
		List<String> chordFm = new ArrayList<String>();
		List<String> chordGm = new ArrayList<String>();
		List<String> chordAm = new ArrayList<String>();
		List<String> chordBm = new ArrayList<String>();
		
		
		chordC.add("C"); chordC.add("E"); chordC.add("G");
		//chordCsharp.add("C#"); chordCsharp.add("E#"); chordCsharp.add("G#");
		chordD.add("D"); chordD.add("F#"); chordD.add("A");
		chordE.add("E"); chordE.add("G#"); chordE.add("B");
		chordF.add("F"); chordF.add("A"); chordF.add("C");
		chordG.add("G"); chordG.add("B"); chordG.add("D");
		chordA.add("A"); chordA.add("C#"); chordA.add("E");
		chordB.add("B"); chordB.add("D#"); chordB.add("F#");
		chordCm.add("C"); chordCm.add("D#"); chordCm.add("G");
		chordDm.add("D"); chordDm.add("F"); chordDm.add("A");
		chordEm.add("E"); chordEm.add("G"); chordEm.add("B");
		chordFm.add("F"); chordFm.add("G#"); chordFm.add("C");
		chordGm.add("G"); chordGm.add("A#"); chordGm.add("D");
		chordAm.add("A"); chordAm.add("C"); chordAm.add("E");
		chordBm.add("B"); chordBm.add("D"); chordBm.add("F#");
		
		chordTable.put("C", chordC);
		chordTable.put("D", chordD);
		chordTable.put("E", chordE);
		chordTable.put("F", chordF);
		chordTable.put("G", chordG);
		chordTable.put("A", chordA);
		chordTable.put("B", chordB);
		chordTable.put("Cm", chordCm);
		chordTable.put("Dm", chordDm);
		chordTable.put("Em", chordEm);
		chordTable.put("Fm", chordFm);
		chordTable.put("Gm", chordGm);
		chordTable.put("Am", chordAm);
		chordTable.put("Bm", chordBm);
		
	}
	
	
	
	
	
}
