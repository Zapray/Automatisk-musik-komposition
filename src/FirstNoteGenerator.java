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
		//prevs deliberatley not used, since it doesnt depend on the prevs
		double roll = rand.nextDouble();
		int i = 0;
		double accum = 0;
		while(accum <= roll) {
			accum+=notes[i];
			i++;
		}
		return getNote(i);

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
	public List<Note> generateSong(double length) {
		try {
			throw new Exception("TODO bad infrastructure, (USE generateNote() instead!)");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String toString(){
		return notes.toString();
	}
}
