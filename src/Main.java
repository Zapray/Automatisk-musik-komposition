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
		String part = "chorus";
		Boolean drums = false;
		Boolean bass = false;
		Boolean pianorythm = false;

		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/databases_parts/" + part + ".txt");
		StructureGenerator sg = new StructureGenerator(System.getProperty("user.dir")+"/Structure_parts/Sections/" + part +".txt", System.getProperty("user.dir")+"/Structure_parts/Motifs/" +part+".txt");
		
		
		List<? extends List<Frame>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		cMax = mm.getCMax();
		Tmn tmn = new Tmn(pMax, dMax, cMax, mm.getDurationConversionTable());
		tmn.train(l);

		List<Motive> mvl  = sg.generateNewStructure();
		//List<Motive> mvl = sg.stealStructure();
//		List<Motive> mvl = new ArrayList<Motive>();
//		mvl.add(new Motive(1));
//		mvl.add(new Motive(2));
//		mvl.add(new Motive(2, false, Percentage.SIXTY));
//		mvl.add(new Motive(2, false, Percentage.SIXTY));
//		mvl.add(new Motive(3));
//		mvl.add(new Motive(2, false, Percentage.SIXTY));
//		mvl.add(new Motive(2, false, Percentage.EIGHTY));
		frames = mvl.size();//magic number!
		for(Motive m : mvl) {
			System.out.println(m.isNew + ", " +m.index);
		}
		
		
		frames = mvl.size();
		
		MotiveGenerator mg = new MotiveGenerator();
		ChordMarkov markov = new ChordMarkov(CHORDORDER, cMax, l);	
		List<Frame> song = mg.generateSong(tmn, mvl, markov);
		//System.out.println(mm.analyzeSong(song));
		
		
		try {
			mm.createMidi(song,2*frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
