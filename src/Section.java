import java.util.List;


/////////////////////////////////////////////////////////////////////
// Denna klass representerar en section 
// En section består av en fras/ett antal fraser samt har två ID-"handlingar"
//
// Den första ID-handlingen sectionID identifierar vilken typ av frasföljd
// som den specifika section:en är uppbyggd med
//
// Den andra ID-handlingen identicalSectionID är specifik för en viss låt
// och talar förekommer flera gånger endast om två extion inom en låt är
// identiska
//
// phraseLengths talar om hur långa fraserna som kommer efter varandra
// är där 1 är kort 2 är medel 3 är lång och 4 täcker en hel section
//////////////////////////////////////////////////////////////////////
public class Section {

	int sectionID;
	Boolean isNew;
	private List<Motive> motives;
	int identicalMotives;
	/**
	 * 
	 * @param motives the list of motives making up the phrase
	 * @param sectionID A unique ID for a particular section
	 * @param isNew whether this section has occured before '
	 * (in which case it will share the same sectionID as the idential one)
	 */
	public Section(List<Motive> motives, int sectionID, Boolean isNew, int identicalMotives)
	{
		this.motives = motives;
		this.sectionID = sectionID;
		this.isNew = isNew;
		this.identicalMotives = identicalMotives;
	}
	
	public List<Motive> getMotives() {
		return motives;
	}
}
