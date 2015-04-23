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
		
		for(Motive motive : motives) {
			if (motive.isNew) {
				newMotives.put(motive.index, tmn.generateSong(doubleChord(chords.get(i)), prevFrame));
				for(Frame m : newMotives.get(motive.index)) {
					song.add(m.clone());
				}
			} else { //not new
				List<Frame> newMotive = new ArrayList<Frame>();
				switch (motive.getSimilarity()) {
				case HUNDRED:
					newMotive = newMotives.get(motive.index);
					break;
				case EIGHTY:
					newMotive = mutateMotive(newMotives.get(motive.index), Percentage.EIGHTY, tmn);
					break;
				case SIXTY:
					newMotive = mutateMotive(newMotives.get(motive.index), Percentage.SIXTY, tmn);
				}
				for(Frame m : newMotive) {
					song.add(m);
				}
			}
			
			i++;
			i = i%CHORDPROGLENGTH;
			prevFrame = song.peekLast();
		}
		song = fixChords(song, chords);
		return song;
	}
	//Assumes the motive List is 4 frames long
	private List<Frame> mutateMotive(List<Frame> motive, Percentage similarity, MelodyFrameGenerator tmn) {
		if(motive.size() != 2) {
			throw new IllegalArgumentException("cannot mutate a motive that isn't exactly 2 frames long");
		}
		List<Frame> newMotive = new ArrayList<Frame>();
		List<Integer> chords = new ArrayList<Integer>();
		switch (similarity) {
		case EIGHTY:
			newMotive = tmn.eightyMutation(motive);
			break;
		case SIXTY: // mutates half the frames
			newMotive.add(motive.get(0).clone());
			chords.add(motive.get(1).getChord());
			newMotive.addAll(tmn.generateSong(chords, motive.get(0)));
			break;
		default:
			throw new IllegalArgumentException();
		}
		return newMotive;
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
