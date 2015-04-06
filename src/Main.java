import java.util.ArrayList;
import java.util.List;


public class Main {
	private static int cMax, pMax, dMax, order = 2, frames = 120;
	private static double length = 0.5;
	public static void main(String[] args) {
		//You need to allow more memory in eclipse / java to run!
		runTmn();
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
		
		List<Frame> song = tmn.generateSong(frames);
		System.out.println(mm.analyzeSong(song));
		
		try {
			mm.createMidi(song,frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void GenerateSongPart() {
		MidiManager2 mm = new MidiManager2(System.getProperty("user.dir")+"/databases_parts/chorus.txt");
		List<? extends List<Frame>> l = mm.getData();
		pMax = mm.getPMax();
		dMax = mm.getDMax();
		cMax = mm.getCMax();
		Tmn tmn = new Tmn(pMax, dMax, cMax, mm.getDurationConversionTable());
		tmn.train(l);

		List<Section> sections = new ArrayList<Section>(); // = StructueGenerator.GenerateStructure(frames)
		
		SongGenerator sg = new SongGenerator();
		List<Frame> song = sg.generateSong(tmn, sections);
		
		System.out.println(mm.analyzeSong(song));
		
		try {
			mm.createMidi(song,frames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
