import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public abstract class MelodyNotesGenerator extends MelodyGenerator{
	
	public MelodyNotesGenerator(int pMax, int dMax) {
		super(pMax, dMax);
	}
	public abstract void train(List<? extends List<Note>> data);
	/**
	 * 
	 * @param numberRepr a number representation of a note
	 * @return the corresponding note
	 */
	public Note getNote(int numberRepr) {
		return Note.getNote(numberRepr, pMax, dMax);
	}
	public abstract Note generateNote(List<Note> prevs, Random rand);
	
	public abstract List<Note> generateSong(double length, List<Float> conversionTable);
	
}
