

public class Motive {
	public MotiveVariation variation;
	public int index;
	public int bars;
	
	public Motive(MotiveVariation variation, int index, int bars) {
		this.variation = variation;
		this.index = index;
		this.bars = bars;
	}
	
	private Motive exampleMot() { //SICK EXAMPLE YO
		return new Motive(MotiveVariation.NEW, 2, 3);
	}
}
