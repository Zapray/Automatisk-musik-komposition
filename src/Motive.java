

public class Motive {
	public MotiveVariation variation;
	public int index;
	public int bars;
	
	public Motive(MotiveVariation variation, int index, int bars) {
		this.variation = variation;
		this.index = index;
		this.bars = bars;
	}
	
	public static Motive exampleMot() { //SICK EXAMPLE YO
		return new Motive(MotiveVariation.NEW, 1, 4);
	}
}
