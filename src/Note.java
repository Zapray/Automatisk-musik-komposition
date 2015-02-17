
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
		return pitch+(duration-1)*pitchMax-1;
	}
	/**
	 * @param numberRepresentation of the note
	 * @return note the note corresponding to the given number
	 */
	public static Note getNote(int matrixNumber, int pitchMax, int durationMax) {
		return new Note((int) Math.floor(matrixNumber/pitchMax)+1, (matrixNumber%pitchMax)+1); //TODO pitch �r feel!
	}
}
