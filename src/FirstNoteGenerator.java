import java.util.List;
import java.util.Random;


public class FirstNoteGenerator extends MelodyGenerator{
	private double[] notes;
	private Random randgen;
	
	public FirstNoteGenerator(int pMax, int dMax) {
		super(pMax,dMax);
		notes = new double[dMax*pMax];
		randgen = new Random();

	}
	public Note generateNote() {
		double roll = randgen.nextDouble();
		int i = 0;
		int accum = 0;
		while(accum <= roll) {
			accum+=notes[i];
			i++;
		}
		return getNote(i);

	}

	public void train(List<List<Note>> data) {
		for(List<Note> song : data) {
			notes[song.get(0).getNumberRepresentation(pMax)]++;
		}
		for(int i = 0; i < notes.length; i++) {
			notes[i] /= data.size();
		}
	}
	@Override
	public List<Note> generateSong(int length) {
		throw new Exception("TODO bad infrastructure, (USE generateNote() instead!)");
	}
	
}
