import java.util.List;
import java.util.Random;


public abstract class MelodyFrameGenerator extends MelodyGenerator {

	public MelodyFrameGenerator(int pMax, int dMax) {
		super(pMax, dMax);
	}
	public abstract void train(List<? extends List<Frame>> data);
	
	public abstract List<Frame> generateSong(int length, List<List<Frame>> data);

}
