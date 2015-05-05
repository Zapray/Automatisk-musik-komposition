package src;



public class Motive {
	public Boolean isNew;
	public int index;
	private Percentage similarity;
	public static int FRAMESIZE = 2;
	
	public Motive(int index, Boolean isNew, Percentage similarity) {
		this.isNew = isNew;
		this.index = index;
		this.similarity = similarity;
	}
	/**
	 * Creates a new Motive
	 * @param index
	 */
	public Motive(int index) {
		this.isNew = true;
		this.index = index;
		this.similarity = null;
	}
	public Percentage getSimilarity() {
		if(similarity == null) {
			throw new IllegalArgumentException();
		}
		return similarity;
	}
	// Allt nedanför ska bort
	public int getInex()
	{
		return index;
	}
	
	public Boolean isNew()
	{
		return isNew;
	}
	
	public Percentage getSim()
	{
		if(similarity == null) {
			throw new IllegalArgumentException();
		}
		return similarity;
	}
	@Override
	public String toString() {
		return getInex() + " and sim " + getSim();
	}
}

