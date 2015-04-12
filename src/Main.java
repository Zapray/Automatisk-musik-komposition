import java.util.ArrayList;
import java.util.List;


public class Main {
	private static int cMax, pMax, dMax, order = 2, frames = 8;
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

		List<Section> sections = new ArrayList<Section>(); // = StructueGenerator.GenerateStructure(frames)
		List<Motive> mvl= new ArrayList<Motive>();
		mvl.add(Motive.exampleMot()); // temporary structure
		mvl.add(new Motive(MotiveVariation.REPEAT, 1, 4));
		mvl.add(new Motive(MotiveVariation.NEW, 2, 1));
		mvl.add(new Motive(MotiveVariation.NEW, 3, 1));
		mvl.add(new Motive(MotiveVariation.REPEAT, 2, 1));
		List<Motive> mvl2= new ArrayList<Motive>();
		mvl2.add(Motive.exampleMot()); // temporary structure
		mvl2.add(new Motive(MotiveVariation.REPEAT, 1, 4));
		mvl2.add(new Motive(MotiveVariation.NEW, 2, 1));
		mvl2.add(new Motive(MotiveVariation.NEW, 3, 1));
		mvl2.add(new Motive(MotiveVariation.REPEAT, 2, 1));
		
		sections.add(new Section(mvl, 1, true));
		sections.add(new Section(mvl, 1, false));
		sections.add(new Section(mvl2, 2, true));
		
		MotiveGenerator mg = new MotiveGenerator();
		ChordMarkov markov = new ChordMarkov(CHORDORDER, cMax, l);
		int nrOfChords = 0;
		for(Section section : sections) {
			nrOfChords = section.getLength()*2;
		}
				
		List<Frame> song = mg.generateSong(tmn, sections, markov.generateChordProg(nrOfChords));
		
		System.out.println(mm.analyzeSong(song));
		
		try {
			mm.createMidi(song, frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
