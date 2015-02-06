
public class Note {
	private float pitch;
	private float duration;
	public static int PITCHMAX = 14;
	public static int DURATIONMAX = 15;
	
	public Note(float duration,float pitch) {
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
	//public int getmatrixPosition() {
		//return pitch+1 + (duration-1)*PITCHMAX;
	//}
}
