
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

	int sectionID, identicalSectionID;
	int [] phraseLengths; 
	
	/////////////////
	// Konstruktor //
	/////////////////
	public Section(int sectionID, int identicalSectionID, 
				   int [] phraseLengths)
	{
		this.sectionID = sectionID;
		this.identicalSectionID = identicalSectionID;
		this.phraseLengths = phraseLengths;
	}
	
}
