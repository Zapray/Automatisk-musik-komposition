
public class Note {
	private int pitch;
	private int duration;

	public Note(int duration,int pitch) {
		this.pitch = pitch;
		this.duration = duration;
	}
	public int getPitch() {
		return pitch;
	}
	public int getDuration() {
		return duration;
	}
	public int getNumberRepresentation(int pitchMax) {
		return pitch+1+(duration-1)*pitchMax;
	}
	/**
	 * @param numberRepresentation of the note
	 * @return note the note corresponding to the given number
	 */
	public static Note getNote(int matrixNumber, int pitchMax, int durationMax) {
		return new Note((matrixNumber+pitchMax)/durationMax, matrixNumber%durationMax-1);
	}
}
