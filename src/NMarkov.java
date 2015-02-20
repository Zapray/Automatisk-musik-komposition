import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public class NMarkov extends MelodyGenerator{

	private SimpleMatrix transitionMatrix;
	private MelodyGenerator nMinusOneMarkov;
	
	private final int n; //order n
	private final int matrixSize;
	
	/**
	 * @param pMax The number of pitches represented in the notes
	 * @param dMax The number of durations represented in the notes
	 */
	public NMarkov(int n, int pMax, int dMax) {
		super(pMax, dMax);
		this.n = n;
		matrixSize = dMax*pMax;
		transitionMatrix = new SimpleMatrix((int)Math.pow(matrixSize, n),matrixSize);
		if(n > 1) {
			nMinusOneMarkov = new NMarkov(n-1, pMax, dMax);
		} else {
			nMinusOneMarkov = new FirstNoteGenerator(pMax,dMax);
		}
		
	}
	public MelodyGenerator getNMinusOneMarkov() {
		return nMinusOneMarkov;
	}
	
	/**
	 * @return an integer representing what number the note combination represents
	 * @throws IllegalArgumentException
	 */
	private int getRowPos(List<Note> notes) throws IllegalArgumentException{
		
		//testloop
		for(Note n : notes) {
			if (n.getPitch() > pMax || n.getDuration() > dMax) {
				throw new IllegalArgumentException("p > pMax | d > dMax");
			}
		}
		
		
		if(notes.size() != n) {
			throw new IllegalArgumentException("error in getRowPos");
		}
		int sum1 = 0;
		for(int i = 1; i < n; i++) {
			sum1+= ((int)Math.pow(pMax, i)*(int)Math.pow(dMax, i-1))*(notes.get(i).getPitch()-1);
		}
		int sum2 = 0;
		for(int i = 2; i < n; i++) {
			sum2+= (int)Math.pow(pMax, i-1)*(int)Math.pow(dMax, i-1)*(notes.get(i).getDuration()-1);
		}
		
		
		
		return notes.get(0).getPitch()+sum1+sum2-1;
	}
	
	public void train(List<? extends List<Note>> data) {
		nMinusOneMarkov.train(data);
		int[] counter = new int[transitionMatrix.numRows()];
		int row = 0;
		int col = 0;
		LinkedList<Note> prevs = new LinkedList<Note>();
		
		for( List<Note> song : data) {
			for(int i = 0; i < n; i++) {
				 prevs.addFirst(song.get(i));
			}
			
			for(int i = n; i < song.size(); i++) {
				row = getRowPos(prevs);
				col = song.get(i).getNumberRepresentation(pMax);
				transitionMatrix.set(row, col, transitionMatrix.get(row, col)+1);
				counter[row]++;
				prevs.removeLast();
				prevs.addFirst(song.get(i));
			}
			prevs.clear();
		}
		for(int i = 0; i < transitionMatrix.numRows(); i++) {
			for(int j = 0; j < transitionMatrix.numCols(); j++) {
				if(counter[i] == 0) {
					counter[i] = 1;
				}
				transitionMatrix.set(i, j, transitionMatrix.get(i, j)/counter[i]);
			}
		}
		transitionMatrix = this.addOneToEmptyRows(transitionMatrix);
	}
	public List<MelodyGenerator> getGenerators() {
		ArrayList<MelodyGenerator> gens = new ArrayList<MelodyGenerator>();
		
		if(nMinusOneMarkov instanceof FirstNoteGenerator) {
			gens.add(nMinusOneMarkov);
			return gens;
		} else {
			NMarkov subMarkov = ((NMarkov)(nMinusOneMarkov));
			gens.addAll(subMarkov.getGenerators());
			gens.add(subMarkov);
		}
		
		return gens;
	}
	
	/**
	 * 
	 * @param length how many bars of melody is to be generated
	 * @return a new song
	 */
	public List<Note> generateSong(int length) { //assuming four four
		
		ArrayList<Note> newSong = new ArrayList<Note>();
		Random rand = new Random();
		
		List<MelodyGenerator> generators = getGenerators();
		LinkedList<Note> prevs = new LinkedList<Note>();
		//TODO correct order?
		for(int i = 0; i < n; i++) {
			prevs.add(generators.get(i).generateNote(prevs, rand));
		}
		
		//TODO int length needs to work as intended
		
		
		double tot = 0;
		
		while(tot < length) {
			Note newNote = generateNote(prevs, rand);
			newSong.add(newNote);
			prevs.addFirst(newNote);
			prevs.removeLast();
			
			tot += 1/((double)newNote.getDuration());
		}
		
		return newSong;
	}
	public Note generateNote(List<Note> prevs, Random rand) {
		double roll = rand.nextDouble();
		int i = 0;
		double accum = 0;
		
		for(; i < transitionMatrix.numCols()-1; i++) {
			accum+=transitionMatrix.get(getRowPos(prevs), i);
			if(accum >= roll) {
				break;
			}
		}
		
		return getNote(i);
	}
	
	
	public static void main(String[] args) {
		//NMarkov m = new NMarkov(2,6,8);
		NMarkov m = new NMarkov(2,2,1);
		ArrayList<ArrayList<Note>> data = new ArrayList<ArrayList<Note>>(); 
		ArrayList<Note> list = new ArrayList<Note>();
		list.add(new Note(1,1));
		list.add(new Note(1,2));
		list.add(new Note(1,2));
		list.add(new Note(1,1));
		
//		ArrayList<Note> list2 = new ArrayList<Note>();
		list.add(new Note(1,2));
		list.add(new Note(1,2));
		data.add(list);
		m.train(data);
		System.out.println(m.generateSong(2));
		System.out.println(Integer.MAX_VALUE);
	}
	@Override
	public String toString() {
		return transitionMatrix.toString();
		
	}
	
	
}
