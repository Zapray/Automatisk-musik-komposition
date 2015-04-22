import java.util.ArrayList;
import java.util.List;


public class Main {
	private static int cMax, pMax, dMax, order = 2, frames = 24;
	private static double length = 0.5;
	private final static int CHORDORDER = 2;
	public static void main(String[] args) {
		//You need to allow more memory in eclipse / java to run!
		//runTmn();
		generateSongPart();
		//System.out.println(Runtime.getRuntime().totalMemory());
	}
	public static void runNMarkov() {

		MidiManager mm = new MidiManager(System.getProperty("user.dir")+"/PersDatabas/database_chorus.txt");
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
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/databases_parts/chorus.txt");
		List<? extends List<Frame>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		cMax = mm.getCMax();
		Tmn tmn = new Tmn(pMax, dMax, cMax, mm.getDurationConversionTable());
		tmn.train(l);

//		System.out.println("hej svej i main!!");
		
		//add your own chord progg! :D
//		ArrayList<Integer> chords = new ArrayList<Integer>();
//		chords.add(1);
//		chords.add(3);
//		chords.add(3);
//		chords.add(1);
//		
//		List<Frame> song = tmn.generateSong(chords);
		List<Frame> song = tmn.generateSong(frames);
		System.out.println(mm.analyzeSong(song));
		
		try {
			mm.createMidi(song,frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void generateSongPart() {
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/databases_parts/chorus.txt");
		List<? extends List<Frame>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		cMax = mm.getCMax();
		Tmn tmn = new Tmn(pMax, dMax, cMax, mm.getDurationConversionTable());
		tmn.train(l);

		
		List<Motive> mvl = new ArrayList<Motive>();  // = StructueGenerator.GenerateStructure(frames)
		mvl.add(new Motive(1));
		mvl.add(new Motive(2));
		mvl.add(new Motive(1, false, Percentage.HUNDRED));
		mvl.add(new Motive(2, false, Percentage.HUNDRED));
		mvl.add(new Motive(3));
		mvl.add(new Motive(4));
		mvl.add(new Motive(5));
		mvl.add(new Motive(6));
		frames = mvl.size();//magic number!
		MotiveGenerator mg = new MotiveGenerator();
		ChordMarkov markov = new ChordMarkov(CHORDORDER, cMax, l);
				
		List<Frame> song = mg.generateSong(tmn, mvl, markov);
		
		System.out.println(mm.analyzeSong(song));
		
		try {
			mm.createMidi(song, frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
