import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public class NMarkov {

	SimpleMatrix transitionMatrix;
	private int n; //order n
	
	private static int matrixSize = Note.DURATIONMAX*Note.PITCHMAX;
	
	public NMarkov(int n) {
		this.n = n;
		transitionMatrix = new SimpleMatrix((int)Math.pow(matrixSize, n),matrixSize);
		
	}
	public void train(List<List<Note>> data) {
		int[] counter = new int[transitionMatrix.numRows()];
		
		int a = 0;
		int b = 0;
		for( List<Note> song : data) {
			Note prev = song.get(0);
			for(int i = 1; i < song.size(); i++) {
				a = prev.getNumberRepresentation();
				b = song.get(i).getNumberRepresentation();
				transitionMatrix.set(b, a, transitionMatrix.get(b, a));
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
	}
	/**
	 * 
	 * @param length how many bars of melody is to be generated
	 * @return a new song
	 */
	public List<Note> generateSong(int length) { //assuming four four
		ArrayList<Note> newSong = new ArrayList<Note>();
		Random rand = new Random();
		int first = (int)(rand.nextDouble()*matrixSize);
		double tot = 0;
		double accum = 0;
		newSong.add(Note.getNote(first));
		while(tot < length) {
			double roll = rand.nextDouble();
			int i = 0;
			while(accum <= roll) {
				accum+=transitionMatrix.get(first, i);
				i++;
			}
			Note newNote = Note.getNote(i);
			tot += 1/((double)newNote.getDuration());
			newSong.add(newNote);
			i = 0;
			accum = 0;
		}
		
		return newSong;
	}
	
}
