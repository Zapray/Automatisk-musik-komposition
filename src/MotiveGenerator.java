import java.util.ArrayList;
import java.util.HashMap;
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
		ArrayList<Frame> song = new ArrayList<Frame>(); 

		for(Section section : sections) {
			if(!section.isNew) {
				song.addAll(sectionMap.get(section.sectionID));
			}else {
				HashMap<Integer, List<Frame>> newMotives = new HashMap<Integer, List<Frame>>();
				ArrayList<Frame> songSection = new ArrayList<Frame>();
				List<Integer> typeIndexes = new ArrayList<Integer>();
				for(Motive motive : section.getMotives()) {
					switch (motive.variation) {
						case NEW:
							List<Integer> subChords = new ArrayList<Integer>();
							for(int i = 0; i < motive.bars; i++) {
								subChords.add(chords.remove(i));
							}
							//TODO double the amount of chords?
							newMotives.put(motive.index, tmn.generateSong(subChords));
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
