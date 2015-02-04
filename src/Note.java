
public class Note {
	private int pitch;
	private int duration;
	public static int PITCHMAX = 14;
	public static int DURATIONMAX = 16;
	
	public Note(int pitch, int duration) {
		this.pitch = pitch;
		this.duration = duration;
	}
	public int getPitch() {
		return pitch;
	}
	public int getDuration() {
		return duration;
	}
	public boolean isNote() {
		return pitch <= PITCHMAX && duration <= DURATIONMAX; 
	} //magic numbers!
	public int getmatrixPosition() {
		return pitch-1 + (duration-1)*PITCHMAX;
	}
}
