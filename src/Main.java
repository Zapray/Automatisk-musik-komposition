import java.util.List;


public class Main {

	private static int pMax, dMax, order = 3 , length = 20;
	
	public static void main(String[] args){
		
		MidiManager mm = new MidiManager(System.getProperty("user.dir")+"songData.txt");
		List<List<Note>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		NMarkov markov = new NMarkov(order,pMax, dMax);
		markov.train(l);
		List<Note> song = markov.generateSong(length);
		mm.createMidi(song);
	
	}
}
