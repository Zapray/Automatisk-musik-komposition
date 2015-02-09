
public class Main {

	private static int pMax, dMax;
	
	public static void main(String[] args){
		
		MidiMananger mm = new MidiManager(System.getProperty("user.dir")+"songData.txt");
		List<List<Notes>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		NMarkov markov = new NMarkov(pMax, dMax);
		markov.train();
		List<Note> = markov.generateSong();
		mm.createMidi(song);
	
	}
}
