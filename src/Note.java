
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
		return new Note((int) Math.floor(matrixNumber/pitchMax)+1, (matrixNumber%pitchMax)+1);
	}
	@Override
	public String toString() {
		return duration + ", " + pitch; 
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + duration;
		result = prime * result + pitch;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (duration != other.duration)
			return false;
		if (pitch != other.pitch)
			return false;
		return true;
	}
}
