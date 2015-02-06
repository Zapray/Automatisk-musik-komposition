import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FirstNoteGenerator{
	private double[] notes;
	private Random randgen;
	public FirstNoteGenerator() {
		notes = new double[Note.DURATIONMAX*Note.PITCHMAX];
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
		return Note.getNote(i);

	}

	public void train(List<List<Note>> data) {
		for(List<Note> song : data) {
			notes[song.get(0).getNumberRepresentation()]++;
		}
		for(int i = 0; i < notes.length; i++) {
			notes[i] /= data.size();
		}
	}
	
}
