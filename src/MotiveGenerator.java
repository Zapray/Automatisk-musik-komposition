import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class in charge of putting a song together, given a structure and a generator.
 * Deals with MotiveGeneration
 * @author Zapray
 *
 */
public class MotiveGenerator {
	public static final int CHORDPROGLENGTH = 4;
	public MotiveGenerator() {
		
	}
	
	/**
	 * @param tmn
	 * @param sections
	 * @param chordMarkov
	 * @return
	 */
	public List<Frame> generateSong(MelodyFrameGenerator tmn, List<Motive> motives, ChordMarkov chordMarkov) {
		LinkedList<Frame> song = new LinkedList<Frame>();
		Frame prevFrame = null;	
		List<Integer> chords = chordMarkov.generateChordProg(CHORDPROGLENGTH);
		HashMap<Integer, List<Frame>> newMotives = new HashMap<Integer, List<Frame>>();
		int i = 0;
		
		System.out.println(chords);
		
		for(Motive motive : motives) {
			if (motive.isNew) {
				newMotives.put(motive.index, tmn.generateSong(doubleChord(chords.get(i)), prevFrame));
			}
			for(Frame m : newMotives.get(motive.index)) {
				song.add(m.clone());
			}
			System.out.println(chords.get(i));
			i++;
			i = i%CHORDPROGLENGTH;
			prevFrame = song.peekLast();
		}
		song = fixChords(song, chords);
		System.out.println(song);
		return song;
	}
	
	private static LinkedList<Frame> fixChords(LinkedList<Frame> song, List<Integer> chordProg) {
		int j = 0;
		for(int i = 0; i < song.size(); i+=2) {
			song.get(i).setChord(chordProg.get(j));
			song.get(i+1).setChord(chordProg.get(j));
			j++;
			j = j%CHORDPROGLENGTH;
		}
		return song;
	}

	private static List<Integer> doubleChord(Integer i) {
		List<Integer> post = new ArrayList<Integer>();
		post.add(new Integer(i));
		post.add(new Integer(i));
		return post;
		
	}
	public static void main(String[] args) {
		List<Integer> chords = new ArrayList<Integer>();
		chords.add(1);
		chords.add(2);
		chords.add(3);
		LinkedList<Frame> song = new LinkedList<Frame>();
		song.add(new Frame(null, 4));
		song.add(new Frame(null, 4));
		song.add(new Frame(null, 4));
		song.add(new Frame(null, 4));
		song.add(new Frame(null, 4));
		song.add(new Frame(null, 4));
		System.out.println(fixChords(song,chords));
	}
}
