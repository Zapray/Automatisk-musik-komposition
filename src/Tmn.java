import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;


/**
 * A song generator that generates songs using three seperate markov chains and frames.
 * @author Zap
 *
 */
public class Tmn extends MelodyFrameGenerator{
	
	private int cMax;//The maximum amount of allowed chords
	private List<Float> conversionTable;
	
	//transitionMatrix for determining the next tone of the next melodyPackage
	private SimpleMatrix nextToneMatrix;
	private NMarkov chordsMarkov;
	private double lastFrameLengthError = 0;
	private static final int chordsOrder = 3;
	private static final int MELODYORDER = 2;
	private static final double FRAMELENGTH = 0.5;
	/**
	 * 
	 * @param pMax the maximum allowed number of pitches
	 * @param dMax the maximum allowed number of duration
	 * @param cMax the maximum allowed number of chords
	 * @param conversionTable
	 */
	public Tmn(int pMax, int dMax, int cMax, List<Float> conversionTable) {
		super(pMax, dMax);
		this.cMax = cMax;
		this.conversionTable = conversionTable;
		chordsMarkov = new NMarkov(chordsOrder, cMax, 1);
		nextToneMatrix = new SimpleMatrix(pMax*cMax, pMax);
	}

	@Override
	public void train(List<? extends List<Frame>> data) {
		chordsMarkov.train(getChordData(data));
		Frame prevFrame;
		//TODO train with longer notes in mind.
		int[] counter = new int[nextToneMatrix.numRows()];
		for(List<Frame> song : data) {
			prevFrame = song.get(0);
			for(int i = 1; i < song.size(); i++) {
				int col = song.get(i).getFirstNote().getPitch()-1; //+1?
				int lastNote = prevFrame.getLastNote().getPitch();
				int lastChord = prevFrame.getChord();
				int row = getRowPos(lastNote, lastChord);
				nextToneMatrix.set(row, col, nextToneMatrix.get(row, col)+1);
				counter[row]++;
				prevFrame = song.get(i);
			}
		}

		for(int i = 0; i < nextToneMatrix.numRows(); i++) {
			for(int j = 0; j < nextToneMatrix.numCols(); j++) {
				if(counter[i] == 0) {
					counter[i] = 1;
				}
				nextToneMatrix.set(i, j, nextToneMatrix.get(i, j)/counter[i]);
			}
		}
		nextToneMatrix = this.addOneToEmptyRows(nextToneMatrix);
	}
	/**
	 * 
	 * @param data A list of of all songs, where a song is a list of frames
	 * @return a list of notes with duration 1 and pitch equal to the chord in the corresponding frame
	 */
	private List<? extends List<Note>> getChordData(
			List<? extends List<Frame>> data) {
		List<ArrayList<Note>> chordData = new ArrayList<ArrayList<Note>>();
		for(List<Frame> song : data) {
			ArrayList<Note> newList = new ArrayList<Note>();
			for(Frame frame : song) {
				newList.add(new Note(1, frame.getChord()));
			}
			chordData.add(newList);
		}
		return chordData;
	}

	public Frame generateFrame(Frame prev, List<? extends List<Frame>> data, Random rand) {
		ArrayList<Note> prevs = new ArrayList<Note>();
		prevs.add(new Note(1, prev.getChord()));
		int chord = chordsMarkov.generateNote(prevs, rand).getPitch();
		
		int pitch;
		if (lastFrameLengthError == 0) {
			pitch = generatePitch(prev.getLastNote().getPitch(), chord ,rand);
		} else {
			pitch = prev.getLastNote().getPitch();
		}
		
		
		NMarkov melodyGen = new NMarkov(MELODYORDER, pMax, dMax);
		melodyGen.train(filterData(chord, data));
		List<Note> melodyPackage = melodyGen.generateSong(FRAMELENGTH-lastFrameLengthError, pitch, conversionTable); //halvtakter
		this.lastFrameLengthError = melodyGen.lastSongLengthError();
		return new Frame(melodyPackage, chord);
	}
	
	/**
	 * @return an integer representing what number the note combination represents
	 * @throws IllegalArgumentException
	 */
	private int getRowPos(int pitch, int chord) throws IllegalArgumentException{
		List<Note> notes = new ArrayList<Note>();
		notes.add(new Note(chord, pitch));
		int sum1 = 0;
		for(int i = 1; i < 1; i++) {
			sum1+= ((int)Math.pow(pMax, i)*(int)Math.pow(dMax, i-1))*(notes.get(i).getPitch()-1);
		}
		int sum2 = 0;
		for(int i = 2; i < 1; i++) {
			sum2+= (int)Math.pow(pMax, i-1)*(int)Math.pow(dMax, i-1)*(notes.get(i).getDuration()-1);
		}	
		return notes.get(0).getPitch()+sum1+sum2-1;
	}
	public int getFirstPitch(List<List<Note>> data, Random rand) {
		FirstNoteGenerator fng = new FirstNoteGenerator(dMax, pMax);
		fng.train(data);
		return fng.generateNote(null, rand).getPitch();
	}
	
	public int generatePitch(int prevP, int chord, Random rand) {
		double roll = rand.nextDouble();
		int i = 0;
		double accum = 0;
		
		for(; i < nextToneMatrix.numCols()-1; i++) {
			accum+=nextToneMatrix.get(getRowPos(prevP, chord), i);
			if(accum >= roll) {
				break;
			}
		}
		
		return i+1; //TODO +1-1 ?
	}

	@Override
	public List<Frame> generateSong(int frames, List<? extends List<Frame>> data) {
		//TODO let frames be longer than intended ? + paus
		List<Frame> song = new ArrayList<Frame>();
		Random rand = new Random();
		int prevChord = chordsMarkov.generateNote(null, rand).getPitch();
		
		FirstNoteGenerator fng = new FirstNoteGenerator(pMax, dMax);
		fng.train(filterData(prevChord, data));
		Note firstNote = fng.generateNote(null, rand);
		
		ArrayList<Note> firstNoteList = new ArrayList<Note>();
		firstNoteList.add(firstNote);
		Frame prevFrame = new Frame(firstNoteList, prevChord);
		
		for(int i = 0; i < frames; i++) {
			Frame newFrame = generateFrame(prevFrame, data, rand);
			prevFrame = newFrame;
			song.add(newFrame);
			System.gc();
		}
		lastFrameLengthError = 0;
		return song;
	}

	private List<List<Note>> filterData(int firstChord, List<? extends List<Frame>> data) {
		ArrayList<List<Note>> newData = new ArrayList<List<Note>>();
		for(List<Frame> song : data) {
			for(Frame frame: song) {
				if(frame.getChord() == firstChord) {
					newData.add(frame.getMelodyPackage());
				}
			}
		}
		return newData;
	}
}
