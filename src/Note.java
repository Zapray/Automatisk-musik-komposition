
public class Note {
	private int pitch;
	private int duration;
	
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
		return pitch <= 14 && duration <= 16; 
	}
}
