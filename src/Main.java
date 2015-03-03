import java.util.List;


public class Main {
	private static int pMax, dMax, order = 2, frames = 1;
	private static double length = 0.5;
	public static void main(String[] args) {
		//You need to allow more memory in eclipse / java to run!
		runTmn();
	}
	public static void runNMarkov() {

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
	public static void runTmn() {
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/nydatabas.txt");
		List<? extends List<Frame>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
//		Tmn tmn = new Tmn(order, pMax, dMax, mm.getDurationConversionTable());
//		tmn.train(l);
//		
		//List<Frame> song = tmn.generateSong(frames, l);
		try {
			mm.createMidi(l.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
