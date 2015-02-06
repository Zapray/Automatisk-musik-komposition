import java.util.List;


public interface MelodyGenerator {
	public abstract List<Note> generateSong(int length);
	public abstract void train(List<List<Note>> data);
}
