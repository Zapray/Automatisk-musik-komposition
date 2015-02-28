import java.util.List;


public class Frame {
	private Note[] melodyPackage; //TODO queue Instead? Maybe?
	private int chord;
	
	public Frame(List<Note> melodyPackage, int chord) {
		this.chord = chord;
		this.melodyPackage = (Note[]) melodyPackage.toArray();
	}
	public Note[] getMelodyPackage(){
		return melodyPackage;
	}
	/**
	 * 
	 * @return The number representation of the chord
	 */
	public int getChord() {
		return chord;
	}
	public Note getFirstNote() {
		return melodyPackage[0];
	}
	public Note getLastNote() {
		return melodyPackage[melodyPackage.length-1];
	}
}
