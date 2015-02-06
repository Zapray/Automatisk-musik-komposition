
public class Note {
	private int pitch;
	private int duration;
	public static int PITCHMAX = 14;
	public static int DURATIONMAX = 15;
	
	public Note(int duration,int pitch) {
		this.pitch = pitch;
		this.duration = duration;
	}
	public float getPitch() {
		return pitch;
	}
	public float getDuration() {
		return duration;
	}
	public boolean isNote() {
		return pitch <= PITCHMAX && duration <= DURATIONMAX; 
	} //magic numbers!
	public int getNumberRepresentation() {
		return pitch+1+(duration-1)*PITCHMAX;
	}
	/**
	 * @param numberRepresentation of the note
	 * @return note the note corresponding to the given number
	 */
	public static Note getNote(int matrixNumber) {
		return new Note((matrixNumber+PITCHMAX)/DURATIONMAX, matrixNumber%DURATIONMAX-1);
	}
}
