


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Random;

import org.ejml.*;
import org.ejml.simple.SimpleMatrix;
public class BasicMarkov extends MelodyNotesGenerator{
	private SimpleMatrix transitionMatrix;
	private FirstNoteGenerator firstNoteGen;
	private static int matrixSize;
	/**
	 * @param pMax The number of pitches represented in the notes
	 * @param dMax The number of durations represented in the notes
	 */
	public BasicMarkov(int pMax, int dMax) {
		super(pMax,dMax);
		firstNoteGen = new FirstNoteGenerator(pMax, dMax);
		matrixSize = pMax*dMax;
		transitionMatrix = new SimpleMatrix(matrixSize, matrixSize);
	}
	
	public BasicMarkov() {
		this(14, 15);
	}
	
	public void train(List<? extends List<Note>> data) {
		firstNoteGen.train(data);
		int[] counter = new int[matrixSize];
		
		int a = 0;
		int b = 0;
		for( List<Note> song : data) {
			Note prev = song.get(0);
			for(int i = 1; i < song.size(); i++) {
				a = prev.getNumberRepresentation(pMax);
				b = song.get(i).getNumberRepresentation(pMax);
				transitionMatrix.set(b, a, transitionMatrix.get(b, a) + 1);
				counter[a]++;
				prev = song.get(i);
			}
		}
		for(int i = 0; i < matrixSize; i++) {
			for(int j = 0; j < matrixSize; j++) {
				if(counter[i] == 0) {
					counter[i] = 1;
				}
				transitionMatrix.set(i, j, transitionMatrix.get(i, j)/counter[i]);
			}
		}
		transitionMatrix = this.addOneToEmptyRows(transitionMatrix);
	}
	
	
	/**
	 * 
	 * @param length how many bars of melody is to be generated
	 * @return a new song
	 */
	public List<Note> generateSong(double length,List<Float> conversionTable) { //assuming four four
		ArrayList<Note> newSong = new ArrayList<Note>();
		Random rand = new Random();
		Note first = firstNoteGen.generateNote(null, rand);
		double tot = 0;
		double accum = 0;
		
		newSong.add(first);
		while(tot < length) {
			double roll = rand.nextDouble();
			int i = 0;
			
			for(; i < transitionMatrix.numCols()-1; i++) {
				accum+=transitionMatrix.get(first.getNumberRepresentation(pMax), i);
				if(accum >= roll) {
					break;
				}
			}
			
			Note newNote = getNote(i);
			tot += 1/((double)newNote.getDuration());
			newSong.add(newNote);

			first = newNote;
			i = 0;
			accum = 0;
		}
		
		return newSong;
	}

	@Override
	public Note generateNote(List<Note> prevs, Random rand) {
		throw new UnsupportedOperationException("not implemented");
	}
}
