import java.util.ArrayList;
import java.util.List;


public class ChordMarkov {
	NMarkov markovChain;
	public ChordMarkov(int order, int nrOfChords, List<? extends List<Frame>> data) {
		markovChain = new NMarkov(order, nrOfChords, 1);
		markovChain.train(filterOneChordPerBar(data));
	}
	private List<? extends List<Note>> filterOneChordPerBar(
			List<? extends List<Frame>> data) {
		
		ArrayList<ArrayList<Note>> songList = new ArrayList<ArrayList<Note>>();
		
		
		ArrayList<Note> noteList;
		for(int i = 0; i < data.size(); i++) {
			noteList = new ArrayList<Note>();
			for(int j = 0; j < data.get(i).size(); j+=2) {
				if( j+1 >= data.get(i).size()) {
					noteList.add(new Note(1, data.get(i).get(j).getChord()));
				}else if (data.get(i).get(j).getChord() == data.get(i).get(j+1).getChord()) {
					noteList.add(new Note(1, data.get(i).get(j).getChord()));
				}else {
					noteList.add(new Note(1, data.get(i).get(j).getChord()));
					noteList.add(new Note(1, data.get(i).get(j+1).getChord()));
				}
			}
			songList.add(noteList);
		}
		return songList;
	}
	public List<Integer> generateChordProg (int bars) {
		ArrayList<Float> conversionTable = new ArrayList<Float>();
		//creating a silly conversionTable so that I can reuse the NMarkov code
		for(int i = 0; i < markovChain.pMax; i++) {
			conversionTable.add(new Float(1));
		}
		
		List<Note> NoteList = markovChain.generateSong(bars, conversionTable);
		List<Integer> chordList = new ArrayList<Integer>();
		for(Note note : NoteList) {
			chordList.add(new Integer(note.getPitch()));
		}
		return chordList;
		
	}
	public static void main(String[] args) {
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/Databases_parts/verse.txt");
		List<? extends List<Frame>> data = mm.getData();
		int cMax = mm.getCMax();
		ChordMarkov m = new ChordMarkov(2, cMax, data);

		System.out.println("chordMarkov:");

		List<Integer> chords = m.generateChordProg(4);
		List<String>  realChords = new ArrayList<String>();
		for(int i = 0; i<chords.size();i++){
			realChords.add(mm.getConvertTableChords().get(chords.get(i)-1));
		} 
		System.out.println(realChords);
	}
}
