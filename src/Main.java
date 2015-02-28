import java.util.List;


public class Main {
	private static int pMax, dMax, order = 2;
	private static double length = 0.5;
	public static void main(String[] args) {
		//You need to allow more memory in eclipse / java to run!
		
		MidiManager mm = new MidiManager(System.getProperty("user.dir")+"/databas.txt");
		List<? extends List<Note>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		NMarkov markov = new NMarkov(order, pMax, dMax);
		markov.train(l);
		
		List<Note> song = markov.generateSong(length, mm.getConversionTable());
		try {
			mm.createMidi(song);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
