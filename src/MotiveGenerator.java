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

	public MotiveGenerator() {
		
	}
	
	/**
	 * Assumes chords and sections are of the same total bar length
	 * @param tmn
	 * @param sections
	 * @param chords
	 * @return
	 */
	public List<Frame> generateSong(MelodyFrameGenerator tmn, List<Section> sections, List<Integer> chords) {
		
		chords = doubleChords(chords);
		HashMap<Integer, List<Frame>> sectionMap = new HashMap<Integer, List<Frame>>(); 
		LinkedList<Frame> song = new LinkedList<Frame>(); 
		Frame prevFrame = null;
		for(Section section : sections) {
			if(!section.isNew) {
				song.addAll(sectionMap.get(section.sectionID));
			}else {
				HashMap<Integer, List<Frame>> newMotives = new HashMap<Integer, List<Frame>>();
				LinkedList<Frame> songSection = new LinkedList<Frame>();
				List<Integer> typeIndexes = new ArrayList<Integer>();
				
				for(Motive motive : section.getMotives()) {
					switch (motive.variation) {
						case NEW:
							List<Integer> subChords = new ArrayList<Integer>();
							for(int i = 0; i < motive.bars; i++) {
								subChords.add(chords.remove(i));
							}
							newMotives.put(motive.index, tmn.generateSong(subChords, prevFrame));
							songSection.addAll(newMotives.get(motive.index));
							break;
						case REPEAT:
							songSection.addAll(newMotives.get(motive.index));
							break;
						//Add more cases for the new motives here
					}
				}
				song.addAll(songSection);
				sectionMap.put(section.sectionID, songSection);
			}
			prevFrame = song.peekLast();
			//
		}
		return song;
	}
	private static List<Integer> doubleChords(List<Integer> pre) {
		List<Integer> post = new ArrayList<Integer>();
		for(Integer i: pre) {
			post.add(new Integer(i));
			post.add(new Integer(i));
		}
		return post;
		
	}
}
