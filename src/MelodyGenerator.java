import java.util.List;


public abstract class MelodyGenerator {
	public abstract void train(List<? extends List<Note>> data);
	protected final int pMax;
	protected final int dMax;
	
	public MelodyGenerator(int pMax, int dMax) {
			this.pMax = pMax;
			this.dMax = dMax;
	}
	/**
	 * 
	 * @param numberRepr a number representation of a note
	 * @return the corresponding note
	 */
	public Note getNote(int numberRepr) {
		return Note.getNote(numberRepr, pMax, dMax);
	}
	public abstract List<Note> generateSong(int length);
	
}
