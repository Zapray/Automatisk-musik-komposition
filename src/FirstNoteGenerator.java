import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FirstNoteGenerator extends MelodyNotesGenerator{
	private double[] notes;
	public FirstNoteGenerator(int pMax, int dMax) {
		super(pMax,dMax);
		notes = new double[dMax*pMax];
	}
	
	@Override
	public Note generateNote(List<Note> prevs, Random rand) {
		//prevs deliberately not used, since it doesnt depend on the prevs
		double roll = rand.nextDouble();
		int i = 0;
		double accum = 0;
		
		for(; i < notes.length; i++) {
			accum+=notes[i];
			if(accum >= roll) {
				break;
			}
		}
		
		
//		while(accum <= roll) {
//			accum+=notes[i];
//			i++;
//		}
		return getNote(i);
				
//		for(; i < transitionMatrix.numCols()-1; i++) {
//			accum+=transitionMatrix.get(getRowPos(prevs), i);
//			if(accum >= roll) {
//				break;
//			}
//		}
//		
//		return getNote(i);
		

	}

	public void train(List<? extends List<Note>> data) {
		for(List<Note> song : data) {
			notes[song.get(0).getNumberRepresentation(pMax)]++;
		}
		for(int i = 0; i < notes.length; i++) {
			notes[i] /= data.size();
		}
	}
	@Override
	public String toString(){
		return notes.toString();
	}

	@Override
	public List<Note> generateSong(double length, List<Float> conversionTable) {
		try {
			throw new Exception("TODO bad infrastructure, (USE generateNote() instead!)");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		FirstNoteGenerator n = new FirstNoteGenerator(3, 1);
		ArrayList<ArrayList<Note>> data = new ArrayList<ArrayList<Note>>();
		ArrayList<Note> list = new ArrayList<Note>();
		list.add(new Note(1,2));
		list.add(new Note(1,3));
		list.add(new Note(1,3));
		list.add(new Note(1,2));
		list.add(new Note(1,2));
		data.add(list);
		
		ArrayList<Note> list2 = new ArrayList<Note>();
		list2.add(new Note(1,2));
		list2.add(new Note(1,3));
		list2.add(new Note(1,3));
		list2.add(new Note(1,2));
		list2.add(new Note(1,2));
		data.add(list);
		
		n.train(data);
		System.out.println(n.generateNote(null, new Random()));
		}
}
