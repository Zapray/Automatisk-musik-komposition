


import java.util.ArrayList;
import java.util.List;


public class Frame {
	List<Note> melodyPackage; //TODO queue Instead? Maybe?
	private int chord;
	
	public Frame(List<Note> melodyPackage, int chord) {
		this.chord = chord;
		this.melodyPackage = melodyPackage;
	}
	public List<Note> getMelodyPackage(){
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
		return melodyPackage.get(0);
	}
	public Note getLastNote() {
		return melodyPackage.get(melodyPackage.size()-1);

	}
	public void setChord(Integer integer) {
		// TODO Auto-generated method stub
		chord = integer;
	}
	@Override
	public String toString() {
		return "frame wtih chord = " + chord; 
	}
	public Frame clone() {
		return new Frame(melodyPackage, chord);
	}
	@Override
	public boolean equals(Object o) {
		if ( o instanceof Frame) {
			return (chord == ((Frame)o).getChord()) && melodyPackage.equals(((Frame)o).getMelodyPackage());
		}else {
			return false;
		}
		
	}
}