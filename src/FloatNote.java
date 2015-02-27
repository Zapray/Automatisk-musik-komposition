
public class FloatNote {
	private float pitch;
	private float duration;

	public FloatNote(float duration,float pitch) {
		this.pitch = pitch;
		this.duration = duration;
	}
	public float getPitch() {
		return pitch;
	}
	public float getDuration() {
		return duration;
	}
	
	public String toString() {
		return duration + ", " + pitch; 
	}
}
