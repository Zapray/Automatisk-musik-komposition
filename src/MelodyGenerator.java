package src;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public abstract class MelodyGenerator {
	protected final int pMax;
	protected final int dMax;
	
	public MelodyGenerator(int pMax, int dMax) {
			this.pMax = pMax;
			this.dMax = dMax;
	}
	
	protected SimpleMatrix addOneToEmptyRows(SimpleMatrix m) {
		Random rand = new Random(); //TODO matrixSize -> rows
		for(int i = 0; i < m.numRows(); i++) {
			double rowLength = 0;
			for(int j = 0; j < m.numCols(); j++) {
				rowLength+=m.get(i, j);
			}
			if (rowLength == 0) {
				m.set(i,(int)(rand.nextDouble()*m.numCols()), 1);
			}
		}
		return m;
	}
}