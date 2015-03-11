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
	private String keys[] = {"C","F","G","Am","Dm","Em","Cm","D","E","G#","Gm","Fm","F#","A","B","Bm","C#","D#","A#","Cm#","Dm#","Fm#","Gm#","Am#","Cdim","Ddim","Edim","Fdim","Gdim","Adim","Bdim","C#dim","D#dim","F#dim","G#dim","A#dim"};

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
		chordAaug.add("A"); chordAaug.add("A#"); chordAaug.add("F");
		
		
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

	}





}