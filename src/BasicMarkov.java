
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Random;

import org.ejml.*;
import org.ejml.simple.SimpleMatrix;
public class BasicMarkov {
	SimpleMatrix transitionMatrix;
	private static int matrixSize = Note.DURATIONMAX*Note.PITCHMAX;
	
	public BasicMarkov() {
		transitionMatrix = new SimpleMatrix(matrixSize,matrixSize);
		
	}
	public void train(List<List<Note>> data) {
		int[] counter = new int[matrixSize];
		
		int a = 0;
		int b = 0;
		for( List<Note> song : data) {
			Note prev = song.get(0);
			for(int i = 1; i < song.size(); i++) {
				a = prev.getmatrixPosition();
				b = song.get(i).getmatrixPosition();
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
	}
	private Note getNote(int matrixNumber) {
		return new Note((matrixNumber-1)*15+1, ((int)Math.floor((matrixNumber-1)/15))+1);
	}
	
	public List<Note> generateSong(int length) { //assuming four four
		ArrayList<Note> newSong = new ArrayList<Note>();
		Random rand = new Random();
		int first = (int)(rand.nextDouble()*matrixSize);
		double tot = 0;
		double accum = 0;
		newSong.add(getNote(first));
		while(tot < length) {
			double roll = rand.nextDouble();
			int i = 0;
			while(accum <= roll) {
				accum+=transitionMatrix.get(first, i);
				i++;
			}
			Note newNote = getNote(i);
			tot += 1/((double)newNote.getDuration());
			newSong.add(newNote);
			i = 0;
			accum = 0;
			
		}
		Note lastNote = newSong.get(newSong.size()); //(last)
		//newSong.set(newSong.size(), new Note(tot-lastNote.getDuration(),lastNote.getPitch()));
		//TODO fix ending avrunding
		return null;
	}
	
	
	public static void main(String[] args) {
		
	}
	
}
