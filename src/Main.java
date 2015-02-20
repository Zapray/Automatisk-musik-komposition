import java.util.List;


public class Main {

	private static int pMax, dMax, order = 2 , length = 20;
	public static void main(String[] args){
		
		MidiManager mm = new MidiManager(System.getProperty("user.dir")+"/databas.txt");
		List<? extends List<Note>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		NMarkov markov = new NMarkov(order, pMax, dMax); //TODO give conversion table
		markov.train(l);
		
		List<Note> song = markov.generateSong(length);
		try {
			mm.createMidi(song);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
