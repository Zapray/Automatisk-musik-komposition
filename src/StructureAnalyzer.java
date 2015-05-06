

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;
import java.lang.*;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;


public class StructureAnalyzer {

	private boolean isPlottingOn = false; //Set to true if you wish to plot the section while debugging
	private ArrayList<ArrayList<ArrayList<Float>>> data = new ArrayList<ArrayList<ArrayList<Float>>>();
	private ArrayList<float[]> oneBars = new ArrayList<float[]>();
	private ArrayList<float[]> sections = new ArrayList<float[]>();
	private ArrayList<Float> pitchList = new ArrayList<Float>();
	private ArrayList<Float> durationList = new ArrayList<Float>();
	private ArrayList<ArrayList<Float>> lastBars = new ArrayList<ArrayList<Float>>();
	private String[][] patternMatrix;
	private String[] patternArray;
	private int countEquals = 0;
	private int countSection = 0;
	private int countSong = 0;
	private int countSectionType;
	private String textFile = "/Databases_parts/chorus.txt";
	private String outputTextFile2 = "/Structure_parts/Sections/yolo.txt";
	private String outputTextFile = "/Structure_parts/Motifs/yolo.txt";


	//Analyze the result
	private int countMotive1 = 0;
	private int countMotive2 = 0;
	private int countMotive3 = 0;
	private int countMotive = 0;
	private int countTotalBarComb = 0;
	private int countSimilarSections = 0;
	private int countTotalSectionComb = 0;
	private int tmp;
	private int tmp1;
	private int tmp2;
	private int tmp3;
	private int countAnalyzedSongs = 0;
	private Map<ArrayList<Integer>, Integer> countCombinations = new HashMap<ArrayList<Integer>, Integer>();
	private Map<ArrayList<Integer>, Integer> twoCombinations = new HashMap<ArrayList<Integer>, Integer>();
	private Map<ArrayList<Integer>, Integer> sortedCombinations;
	private int[][] combinationMatrix;

	private MatlabProxy proxy;

	public static void main(String[] args) throws Exception{

		new StructureAnalyzer();

		//Temporary solution for bug (empty first section in some songs)
		//float[] p1 =createPitchVector((!section1.get(0).isEmpty() ? section1:section2));
		//float[] p2 =createPitchVector(!section1.get(0).isEmpty() ? section2:data.get(2));
	}

	public StructureAnalyzer() throws Exception{
		if(isPlottingOn){
			MatlabProxyFactoryOptions options =
					new MatlabProxyFactoryOptions.Builder()
			.setUsePreviouslyControlledSession(true)
			.build();
			MatlabProxyFactory factory = new MatlabProxyFactory(options);
			proxy = factory.getProxy();
			String path = System.getProperty("user.dir") + "/Matlab";
			proxy.feval("addpath", path);
		}

		parseData();

		if(isPlottingOn){
			proxy.disconnect();
		}
	}



	private void parseData() throws Exception{
		Float pitch;
		Float duration;
		int countBar = 0;
		//int song = 8;
		int barsInSection = 8;
		ArrayList<ArrayList<Float>> section = new ArrayList<ArrayList<Float>>();
		//ArrayList<float[]> vectors = new ArrayList<float[]>(); 
		BufferedReader in =new BufferedReader(new FileReader(System.getProperty("user.dir")+ textFile));

		try{
			String line=in.readLine();
			while(line !=null){	
				if(line.charAt(0) == '?'){
					countBar++;
					//System.out.println("Bar:"+countBar);
				}else if(line.charAt(0) == '-'){
					countSong++;
					if(countBar==barsInSection){
						section.add(pitchList);
						section.add(durationList);
						data.add(section);
						countSection++;
						//System.out.println("Section:"+countSection);

					}else{
						lastBars.add(pitchList);
						lastBars.add(durationList);

					}

					//Temporary solution to cases where a sections duration doesn't equal 4.
					boolean isDurationFour = true;
					for(int i = 0; i < countSection; i++){
						if(calculateSectionDuration(data.get(i)) != 4.0f){
							isDurationFour = false;
						}
					}
					System.out.println("***********************SONG: " + countSong + "******************************");
					if(data.size() > 1 && isDurationFour){
						countAnalyzedSongs++;
						analyzeMotifs();
						printMatrix(patternMatrix);
						System.out.println();
						printArray(patternArray);
						System.out.println();
						//otherTextFileThingy(patternMatrix);
					}
					countBar=0;
					countSection=0;
					oneBars=new ArrayList<float[]>();
					sections = new ArrayList<float[]>();
					countEquals = 0;
					data=new ArrayList<ArrayList<ArrayList<Float>>>();
					pitchList = new ArrayList<Float>();
					durationList = new ArrayList<Float>();
					section = new ArrayList<ArrayList<Float>>();


				}else{
					for(int i=0; i<line.length(); i++){

						if(line.charAt(i)==','){
							pitch= Float.parseFloat(line.substring(0,i));
							duration =Float.parseFloat(line.substring(i+1,line.length()));
							pitchList.add(pitch);
							durationList.add(duration);

						}	
					} 
				}
				if(countBar==barsInSection+1){
					section.add(pitchList);
					section.add(durationList);
					data.add(section);
					pitchList = new ArrayList<Float>();
					durationList = new ArrayList<Float>();
					section = new ArrayList<ArrayList<Float>>();
					countBar=1;
					countSection++;
					//System.out.println("Section:"+countSection);
					//System.out.println("Bar:"+countBar);
				}
				line=in.readLine();

			}
			System.out.println("Done");
			System.out.println("How often motive 1 occurs at least once in a song: " + (float)countMotive1/countAnalyzedSongs);
			System.out.println("How often motive 2 occurs at least once in a song: " + (float)countMotive2/countAnalyzedSongs);
			System.out.println("How often motive 3 occurs at least once in a song: " + (float)countMotive3/countAnalyzedSongs);
			System.out.println("How often a motive occurs at least once in a song: " + (float)countMotive/countAnalyzedSongs);
			System.out.println("Amount of similar sections: " + (float)countSimilarSections/countAnalyzedSongs);
			//sort
			//sortedCombinations = new TreeMap<ArrayList<Integer>, Integer>(countCombinations);
			
			combinationsMatrixForMATLAB();
			printFile();
			System.out.println();
			
		}catch (IOException e) {
			e.printStackTrace();
		} finally {         
			try {
				if (in != null){
					in.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void printFile() throws IOException{
		
		PrintWriter outFile3 = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/countedCombinations.txt", true));
		PrintWriter outFile4 = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/twoCombinations.txt", true));
		PrintWriter outFile5 = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/combinationMatrix.txt", true));

		
		//FrequencySorted
		for (Map.Entry<ArrayList<Integer>, Integer> entry : countCombinations.entrySet()) {
			outFile3.print(entry.getValue() + ": ");
			for(int i = 0; i < entry.getKey().size(); i++){
				 outFile3.print("   " + entry.getKey().get(i));
				 
			}
			outFile3.println();
		}
		
		for (Map.Entry<ArrayList<Integer>, Integer> entry : twoCombinations.entrySet()) {
			outFile4.print(entry.getValue() + ": ");
			for(int i = 0; i < entry.getKey().size(); i++){
				 outFile4.print("  " + entry.getKey().get(i));
				 
			}
			outFile4.println();
		}
		
		//Matrix for MATLAB
		outFile5.print("[");
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				outFile5.print(combinationMatrix[row][col]);
				if((col == 7 && row == 7) || col == 7){
					//do nothing
				}else{
					outFile5.print(", ");
				}
			}
			if(row != 7){
				outFile5.println(";");
			}
			
		}
		outFile5.print("];");
		
		outFile3.close();
		outFile4.close();
		outFile5.close();

	}
	
	public void combinationsMatrixForMATLAB(){
		combinationMatrix = new int[8][8];
		for (Map.Entry<ArrayList<Integer>, Integer> entry : twoCombinations.entrySet()) {
			int row = entry.getKey().get(0)-1;
			int col = entry.getKey().get(1)-1;
			int row2 = entry.getKey().get(1)-1;
			int col2 = entry.getKey().get(0)-1;
			if(row < 8 && col < 8){
				combinationMatrix[row][col] = entry.getValue();
				combinationMatrix[row2][col2] = entry.getValue();
			}
		}
	}


	private void analyzeMotifs() throws Exception{


		File filen = new File(System.getProperty("user.dir")+outputTextFile);
		File filen2 = new File(System.getProperty("user.dir")+outputTextFile2);

		if(!filen.exists()) {
			filen.createNewFile();
		}
		if(!filen2.exists()) {
			filen2.createNewFile();
		}
		PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+ outputTextFile, true));
		PrintWriter outFile2 = new PrintWriter(new FileWriter(System.getProperty("user.dir")+ outputTextFile2, true));
		try{

			//**********************PLOT**************************//
			if(isPlottingOn){
				//BUG: Durations does not add up to 4 (for one section) for some songs!! FIX!
				double[] section1 = convertToDouble(createPitchVector(data.get(0)));
				double[] section2 = convertToDouble(createPitchVector(data.get(1)));

				if(countSection < 3){//2 sections
					proxy.feval("plotMelody", section1, section2);
				}else{
					double[] section3 = convertToDouble(createPitchVector(data.get(2)));
					if (countSection < 4){//4 sections
						proxy.feval("plotMelody", section1, section2, section3);
					}else{
						double[] section4 = convertToDouble(createPitchVector(data.get(3)));
						if (countSection < 5){//6 sections
							proxy.feval("plotMelody", section1, section2, section3, section4);
						}else{//more than 6 sections...
							double[] section5 = convertToDouble(createPitchVector(data.get(4)));
							if(countSection < 6){
								proxy.feval("plotMelody", section1, section2, section3, section4, section5);
							}else{
								double[] section6 = convertToDouble(createPitchVector(data.get(5)));
								if(countSection < 7){
									proxy.feval("plotMelody", section1, section2, section3, section4, section5, section6);
								}else{
									double[] section7 = convertToDouble(createPitchVector(data.get(6)));
									if(countSection < 8){
										proxy.feval("plotMelody", section1, section2, section3, section4, section5, section6, section7);
									}else{
										double[] section8 = convertToDouble(createPitchVector(data.get(7)));
										proxy.feval("plotMelody", section1, section2, section3, section4, section5, section6, section7, section8);
									}

								}
							}
						}
					}
				}
			}

			//**********************COMPARE*************************//
			sections.add(createPitchVector(data.get(0)));
			sections.add(createPitchVector(data.get(1)));
			if(countSection < 3){ //2 sections
			}else{
				sections.add(createPitchVector(data.get(2)));
				if (countSection < 4){ //3 sections
				}else{
					sections.add(createPitchVector(data.get(3)));
					if(countSection < 5){ //4 sections
					}else{
						sections.add(createPitchVector(data.get(4)));
						if(countSection < 6){//5 sections
						}else{
							sections.add(createPitchVector(data.get(5)));
							if(countSection < 7){//6 sections
							}else{
								sections.add(createPitchVector(data.get(6)));
								if(countSection < 8){//7 sections
								}else{
									sections.add(createPitchVector(data.get(7)));
								}
							}
						}


					}
				}
			}
			patternMatrix = new String[sections.size()][4];
			tmp1 = 0;
			tmp2 = 0;
			tmp3 = 0;
			tmp = 0;
			compareBars();
			for(int row = 0; row < patternMatrix.length; row++){
				for(int col = 0; col < patternMatrix[row].length; col++){
					if(patternMatrix[row][col]==null){
						patternMatrix[row][col]="00";
					}
				}
			}
			compareSections();
			for(int i = 0; i < patternArray.length; i++){
				if(patternArray[i]==null){
					countSectionType++;
					patternArray[i] = countSectionType + "N";
				}
			}
			
			//Analyze result
			analyzeResult(patternMatrix);
			
			//Print to textfiles:
			for(int row = 0; row < patternMatrix.length; row++){
				for(int col = 0; col < patternMatrix[row].length; col++){
					outFile.print(patternMatrix[row][col]);
					if(col<3){
						outFile.print(',');
					}
				}
				outFile.println();
			}
			outFile.println('-');
			
			for(int i = 0; i < patternArray.length; i++){
				outFile2.println(patternArray[i]);
			}
			outFile2.println('-');
		} finally{
			if(outFile != null){
				outFile.close();
			}
			if(outFile2 != null){
				outFile2.close();
			}
		}

	}

	private void compareSections(){
		patternArray = new String[sections.size()];
		countSectionType = 0;
		int tmpS = 0;
		for(int first=0; first < sections.size(); first++){
			for(int second=first+1; second < sections.size(); second++){
				countTotalSectionComb++;
				//COMPARE SECTIONS (patternMatrix done)
				String status = "NEW";
				String similarBars = "";
				for(int bar = 0; bar < 4; bar++){
					if((patternMatrix[first][bar].charAt(0) == patternMatrix[second][bar].charAt(0) 
							&& patternMatrix[first][bar].charAt(0) != '0' 
							&& bar < 4)){
						similarBars = similarBars + Integer.toString(bar+1);
						status = "REP";
					}
				}

				if(status == "REP"){
					//*******Save result**************
					if(similarBars.equals("1234") && tmpS == 0){
						countSimilarSections++;
						tmpS++;
					}

					if(patternArray[first] == null){
						countSectionType++;
						if(patternArray[second] == null){
							patternArray[first] = Integer.toString(countSectionType) + "N";
							patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
						}else{
							if(patternArray[second].length()-2 < similarBars.length()){
								patternArray[first] = Integer.toString(countSectionType) + "N";
								patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
							}
						}
					}else{
						if(patternArray[first].charAt(1) == 'R'){
							countSectionType++;
							if(patternArray[second]==null){
								patternArray[first] = Integer.toString(countSectionType) + "N";
								patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
							}else{
								if(patternArray[second].length()-2 < similarBars.length()){
									patternArray[first] = Integer.toString(countSectionType) + "N";
									patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
								}
							}
						}else{
							if(patternArray[second] == null){
								patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
							}else{
								if(patternArray[second].length()-2 < similarBars.length()){
									patternArray[second] = patternArray[first].charAt(0) + "R" + similarBars;
								}
							}
						}
					}
				}
			}
		}
	}

	private void compareBars(){

		int motiveCount = 0;
		while(motiveCount < 3){
			motiveCount++;
			for(int firstS=0; firstS < sections.size(); firstS++){
				for(int secondS=firstS+1; secondS < sections.size(); secondS++){
					ArrayList<ArrayList<Float>> section1 = data.get(firstS);
					ArrayList<ArrayList<Float>> section2 = data.get(secondS);
					int firstSection = firstS+1;
					int secondSection = secondS+1;


					float[] p1 =new float[16];
					float[] p2 =new float[16];
					float[] p3 =new float[16];
					float[] p4 =new float[16];
					float[] p5 =new float[16];
					float[] p6 =new float[16];
					float[] p7 =new float[16];
					float[] p8 =new float[16];

					for(int j=0; j<16;j++){
						//Bars in sections
						if(firstSection == 1 && secondSection == 3 ){
							System.out.println();
						}
						p1[j]=createPitchVector(section1)[j];
						p2[j]=createPitchVector(section1)[j+16];
						p3[j]=createPitchVector(section1)[j+32];
						p4[j]=createPitchVector(section1)[j+48];
						p5[j]=createPitchVector(section2)[j];
						p6[j]=createPitchVector(section2)[j+16];
						p7[j]=createPitchVector(section2)[j+32];
						p8[j]=createPitchVector(section2)[j+48];
					}
					oneBars.add(p1);
					oneBars.add(p2);
					oneBars.add(p3);
					oneBars.add(p4);
					oneBars.add(p5);
					oneBars.add(p6);
					oneBars.add(p7);
					oneBars.add(p8);
					p1 =new float[16];
					p2 =new float[16];
					p3 =new float[16];
					p4 =new float[16];
					p5 =new float[16];
					p6 =new float[16];
					p7 =new float[16];
					p8 =new float[16];


					for(int first=0; first<8; first++){
						for(int second=first+1; second<8; second++){
							countTotalBarComb++;
							if(countSong==10){
								System.out.print("");
							}
							if(countSong == 3 && firstSection == 1 && secondSection == 3 && first == 0 && second == 6){
								System.out.print("");
							}

							String motive = getMotive(oneBars.get(first), oneBars.get(second));
							if(motive.equals(Integer.toString(motiveCount))){

								//**********Save result************
								if(tmp == 0){
									countMotive++;
									tmp++;
								}
								if(motive == "1" && tmp1 == 0){
									countMotive1++;
									tmp1++;
								}else if(motive == "2"&& tmp2 == 0){
									countMotive2++;
									tmp2++;
								}else if(motive == "3"&& tmp3 == 0){
									countMotive3++;
									tmp3++;
								}
								//*********************************

								int firstBar = first+1;
								int secondBar = second+1;
								//System.out.println(firstSection + "  " + secondSection + "  " +  firstBar + "  " + secondBar);
								//System.out.print("Sections: " + firstSection + " and "+ secondSection + "       ");
								//System.out.println("Equal bars: " + firstBar + " and " + secondBar);

								if(firstBar <=4 && secondBar<=4){
									addPattern(firstSection-1, firstSection-1, first, second, motive);
								}else if(firstBar <= 4 && secondBar > 4){
									addPattern(firstSection-1, secondSection-1, first, second-4, motive);
								}else{
									addPattern(secondSection-1, secondSection-1, first-4, second-4, motive);
								}
							}
						}
					}

					//countEquals++;

					oneBars=new ArrayList<float[]>();
				}
			}
			
		}
	}

	private String getMotive(float[] phrase1, float[] phrase2){


		if(similarNotes(phrase1, phrase2,(float)1)){
			return "1";
		}
		else if(similarNotes(phrase1, phrase2,(float)0.8)){
			return "2";
		}
		else if(similarNotes(phrase1, phrase2,(float)0.6)){
			return "3";
		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.7)){
		//			return "7";
		//		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.6)){
		//			return "6";
		//		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.5)){
		//			return "5";
		//		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.4)){
		//			return "4";
		//		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.3)){
		//			return "3";
		//		}
		//		else if(similarNotes(phrase1, phrase2,(float)0.2)){
		//			return "2";
		//		}
		//		else if(similarRelativePitch(phrase1, phrase2)){ //FUNKAR EJ
		//			return "3";
		//		}
		//		else if(similarDuration(phrase1, phrase2)){
		//			return "4";
		//		}
		//		else if(reflectedPitch(phrase1, phrase2)){
		//			return "5";
		//		}
		//		else if(reflectedVertically(phrase1, phrase2)){
		//			return "6";
		//		}
		else{
			return "0";
		}

	}


	private void addPattern(int firstSection, int secondSection, int firstBar, int secondBar, String motive){


		if(patternMatrix[firstSection][firstBar] == null && patternMatrix[secondSection][secondBar] == null ){
			countEquals++;
			patternMatrix[firstSection][firstBar] = Integer.toString(countEquals) + "0";
			patternMatrix[secondSection][secondBar] = patternMatrix[firstSection][firstBar].charAt(0) + motive;
		}else if((patternMatrix[firstSection][firstBar] != null && patternMatrix[firstSection][firstBar].charAt(1) == '0') 
				&& patternMatrix[secondSection][secondBar] == null){
			patternMatrix[secondSection][secondBar] = patternMatrix[firstSection][firstBar].charAt(0) + motive;
		}else if((patternMatrix[secondSection][secondBar] != null && patternMatrix[secondSection][secondBar].charAt(1) == '0') 
				&& patternMatrix[firstSection][firstBar] == null){
			patternMatrix[firstSection][firstBar] = patternMatrix[secondSection][secondBar].charAt(0) + motive;
		}

	}


	private void hejsvej(int firstSection, int secondSection, int firstBar, int secondBar, String motive){

		if(patternMatrix[firstSection][firstBar] == null && patternMatrix[secondSection][secondBar] == null){//BOTH EMPTY
			countEquals++;
			patternMatrix[firstSection][firstBar] = Integer.toString(countEquals) + "0";
			patternMatrix[secondSection][secondBar] = patternMatrix[firstSection][firstBar].charAt(0) + motive;

		}else if(patternMatrix[firstSection][firstBar] == null && patternMatrix[secondSection][secondBar] != null){ //FIRST EMPTY

			//Find original pattern and replace with new
			if(patternMatrix[secondSection][secondBar].charAt(1) != '0'){
				for(int row = 0; row < patternMatrix.length; row++){
					for(int col = 0; col < patternMatrix[row].length; col++){
						if(patternMatrix[row][col] != null && patternMatrix[row][col].equals(patternMatrix[secondSection][secondBar].charAt(0) + "0")){
							patternMatrix[row][col] = Integer.toString(countEquals) + patternMatrix[secondSection][secondBar].charAt(1);
						}
					}
				}
				//New pattern in second
				patternMatrix[secondSection][secondBar] = Integer.toString(countEquals) + "0";
			}
			patternMatrix[firstSection][firstBar] = patternMatrix[secondSection][secondBar].charAt(0) + motive;

		}else if(patternMatrix[firstSection][firstBar] != null && patternMatrix[secondSection][secondBar] == null){ //SECOND EMPTY


			//Send motive in first to the original phrase
			if(patternMatrix[firstSection][firstBar].charAt(1) != '0'){
				for(int row = 0; row < patternMatrix.length; row++){
					for(int col = 0; col < patternMatrix[row].length; col++){
						if(patternMatrix[row][col] != null && patternMatrix[row][col].equals(patternMatrix[firstSection][firstBar].charAt(0) + "0")){
							patternMatrix[row][col] = Integer.toString(countEquals) + patternMatrix[firstSection][firstBar].charAt(1);
						}
					}
				}
				//New pattern in first
				patternMatrix[firstSection][firstBar] = Integer.toString(countEquals) + "0";
			}
			patternMatrix[secondSection][secondBar] = patternMatrix[firstSection][firstBar].charAt(0) + motive;

		}else{//BOTH FULL
			if(patternMatrix[firstSection][firstBar].charAt(1) > motive.charAt(0) && patternMatrix[secondSection][secondBar].charAt(1) > motive.charAt(0)){

				//Send motive in first to the original phrase
				for(int row = 0; row < patternMatrix.length; row++){
					for(int col = 0; col < patternMatrix[row].length; col++){
						if(patternMatrix[row][col] != null && patternMatrix[row][col].equals(patternMatrix[firstSection][firstBar].charAt(0) + "0")){
							patternMatrix[row][col] = Integer.toString(countEquals) + patternMatrix[firstSection][firstBar].charAt(1);
						}
					}
				}
				patternMatrix[firstSection][firstBar] = Integer.toString(countEquals) + "0";
				patternMatrix[secondSection][secondBar] = patternMatrix[firstSection][firstBar].charAt(0) + motive;
			}
		}
	}


	private void printMatrix(String [][] matrix){
		for(int row = 0; row < matrix.length; row++){
			for(int col = 0; col < matrix[row].length; col++){
				System.out.print(matrix[row][col] + "   ");
			}
			System.out.println("");
		}

	}

	private void printArray(String [] array){
		for(int i = 0; i < array.length; i++){
			System.out.println(array[i]);
		}
	}

	private void analyzeResult(String [][] matrix){

		//Detect patterns in song
		Map<Integer, ArrayList<Integer>> detectedPatterns = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for(int row = 0; row < matrix.length; row++){
			for(int col = 0; col < matrix[row].length; col++){
				if(matrix[row][col].charAt(0) != '0'){
					int pattern = Character.getNumericValue(matrix[row][col].charAt(0));
					int bar = 4*row + col + 1;
	
				    if(detectedPatterns.get(pattern) == null){
				    	positions = new ArrayList<Integer>();
				    }else{
				    	positions = detectedPatterns.get(pattern);
				    }
					positions.add(bar);
					detectedPatterns.put(pattern, positions); 
				}
			}
		}

		//Add patterns in total map and count!
		for (Entry<Integer, ArrayList<Integer>> entry : detectedPatterns.entrySet()) {
			ArrayList<Integer> pattern = entry.getValue();
			Integer count = countCombinations.get(pattern);
			countCombinations.put(pattern, (count == null) ? 1:count+1);	
		}
		
		//Add patterns in twoCombinations and count
		for (Entry<Integer, ArrayList<Integer>> entry : detectedPatterns.entrySet()) {
			ArrayList<Integer> pattern = entry.getValue();
			ArrayList<Integer> tmp;
			//Extract patterns of two and add and count
			for(int i = 0; i < pattern.size(); i++){
				for(int j = i+1; j < pattern.size(); j++){
					int p1 = pattern.get(i);
					int p2 = pattern.get(j);
					tmp = new ArrayList<Integer>();
					tmp.add(p1);
					tmp.add(p2);
					Integer count = twoCombinations.get(tmp);
					twoCombinations.put(tmp, (count == null) ? 1:count+1);	
				}
			}
		}

	}

	private double[] convertToDouble(float[] floatArray){
		double[] doubleArray = new double[floatArray.length];
		for (int index = 0 ; index < floatArray.length; index++)
		{
			doubleArray[index] = (double) floatArray[index];
		}
		return doubleArray;
	}

	//Temporary method to fix bug in database
	private float calculateSectionDuration(ArrayList<ArrayList<Float>> section){
		float l = 0;
		ArrayList<Float> durationList = section.get(1);
		for(int i = 0; i < durationList.size(); i++){
			l = (float) (l + durationList.get(i));
		}
		return l;
	}

	private float[] createPitchVector(ArrayList<ArrayList<Float>> phrase){

		ArrayList<Float> pitchList = phrase.get(0);
		ArrayList<Float> durationList = phrase.get(1);
		float l = 0;
		for(int i = 0; i < durationList.size(); i++){
			l = (float) (l + durationList.get(i));
		}
		int L = Math.round(l*16);

		float [] p = new float[L];
		int numberOfPoints;
		int countPoints = 0;
		for(int i = 0; i < pitchList.size(); i++){

			float pitch = pitchList.get(i);
			float time = durationList.get(i);

			numberOfPoints = Math.round(time*16);
			for(int j = countPoints; j < countPoints + numberOfPoints; j++){
				p[j] = pitch;
			}
			countPoints = countPoints + numberOfPoints;
		}

		return p;
	}

	private boolean similarNotes(float[] vector1, float[] vector2, float n){ //Compares two vectors if they have a percent of similarity in notes above a percentage n
		int yes=0;
		int no=0;
		for(int i=0; i<vector1.length; i++){
			if(vector1[i] == vector2[i]){
				yes++;
			}else{
				no++;
			}

		}
		float ratio = (float) yes/(yes+no);
		//System.out.println(ratio);

		if(ratio>=n){
			return true;
		}else{
			return false;
		}
	}

	private boolean similarDuration(float[] vector1, float[] vector2){ //Compares two vectors if they have a percent of similarity in duration above 50%
		int yes=0;
		int no=0;
		int length1=0;
		int length2=0;
		float pastPitch1=-1;
		float pastPitch2=-1;
		for(int i=0; i<vector1.length; i++){
			if(vector1[i]!=pastPitch1 && vector2[i]!=pastPitch2){
				pastPitch1=vector1[i];
				pastPitch2=vector2[i];
				length1++;
				length2++;
				i++;
				if(i==vector1.length){
					break;
				}
				while(vector1[i]==pastPitch1){
					length1++;
					if(vector2[i]==pastPitch2){
						length2++;
					}
					i++;
					if(i==vector1.length){
						break;
					}

				}
				if(length1 == length2){
					yes++;
				}else{
					no++;
				}
				i--;
			}
			pastPitch1=vector1[i];
			pastPitch2=vector2[i];
		}
		float ratio = (float) yes/(yes+no);
		//System.out.println(ratio);
		if(ratio>=0.8){
			return true;
		}else{
			return false;
		}
	}

	private boolean similarRelativePitch(float[] vector1, float[] vector2){ // Compares two vectors if they have the same relative pitch

		if(countSong==8){
			System.out.print("");
		}

		int yes=0;
		int no=0;
		for(int i=0; i<vector1.length-1; i++){
			boolean isNegative = ((vector1[i]-vector1[i+1])<0 && (vector2[i]-vector2[i+1]<0));
			boolean isPositive = ((vector1[i]-vector1[i+1])>0 && (vector2[i]-vector2[i+1]>0));
			boolean isZero = ((vector1[i]-vector1[i+1])==0 && (vector2[i]-vector2[i+1]==0));
			if(Math.abs((vector1[i]-vector1[i+1])-(vector2[i]-vector2[i+1]))<=1 && isPositive){
				yes++;
			}else if(Math.abs((vector1[i]-vector1[i+1])-(vector2[i]-vector2[i+1]))<=1 && isNegative){
				yes++;
			}else if(isZero){
				yes++;
			}else{
				no++;
			}
		}
		float ratio = (float)yes/(yes+no);

		//System.out.println(ratio);



		if(ratio>=0.8){

			return true;
		}else{
			return false;
		}
	}	
	private boolean reflectedPitch(float[] vector1, float[] vector2){

		float [] reflVector = new float[vector1.length];
		reflVector = vector1.clone();
		for (int i = 0; i < reflVector.length / 2; i++) {
			float tmp = reflVector[i];
			reflVector[i] = reflVector[reflVector.length - 1 - i];
			reflVector[reflVector.length - 1 - i] = tmp;
		}

		return similarNotes(reflVector, vector2, (float)0.8);
	}

	private boolean reflectedVertically(float[] vector1, float[] vector2){
		float [] reflVector = new float[vector1.length];
		reflVector = vector1.clone();
		float maxValue = vector1[0]; 
		for(int i=1;i < vector1.length;i++){ 
			if(vector1[i] > maxValue){ 
				maxValue = vector1[i]; 
			} 
		}
		for(int i = 0; i < vector1.length; i++){
			reflVector[i] = maxValue - vector1[i];
		}

		return(similarRelativePitch(reflVector, vector2));
	}

	private boolean scaledDuration(float[] tiny, float[] bigger){ //Compares one vector with a longer vector if they have the same pitches but a different scale of duration
		int yes=0;
		int no=0;
		int count1=0;
		int count2=0;
		float[] tinyBigger = new float[bigger.length];
		for(int i=0; i<tiny.length; i++){
			count1++;
			if(i == tiny.length-1){ //Last note
				for(int j=count2; j<count2+(count1*2);j++){
					tinyBigger[j] = tiny[i];
				}
			}else if(tiny[i]!=tiny[i+1]){
				for(int j=count2; j<count2+(count1*2);j++){
					tinyBigger[j] = tiny[i];
				}
				count2=count1*2+count2;
				count1=0;
			}


		} 
		return similarNotes(tinyBigger, bigger,(float)0.8);
	}

	private void testMethods(int motive){

		double [] tmp1 = {52.0, 52.0, 52.0, 52.0, 52.0, 52.0, 52.0, 52.0, 54.0, 54.0, 54.0, 54.0, 54.0, 54.0, 54.0, 54.0, 65.0, 65.0, 65.0, 65.0, 65.0, 65.0, 65.0, 65.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0};
		float[] p1 = new float[tmp1.length];
		//Convert to float
		for (int i = 0 ; i < tmp1.length; i++)
		{
			p1[i] = (float) tmp1[i];
		}
		float [] p2 = new float[p1.length];

		//MOTIVES
		if(motive == 1){	// MOTIV 1
			p2 = p1.clone();
			System.out.println("similarNotes: ");
			similarNotes(p1, p2,(float)0.8);
			System.out.println("similarDuration: ");
			similarDuration(p1, p2);
		}else if(motive == 2){ // MOTIV 2
			for(int i = 0; i < p1.length; i++){
				p2[i] = p1[i] + 8;
			}
			System.out.println("similarRelativePitch: ");
			similarRelativePitch(p1, p2);
		}else if(motive == 3){ // MOTIV 3

			float maxValue = p1[0]; 
			for(int i=1;i < p1.length;i++){ 
				if(p1[i] > maxValue){ 
					maxValue = p1[i]; 
				} 
			}
			for(int i = 0; i < p1.length; i++){
				p2[i] = maxValue - p1[i];
			}
			System.out.println("reflectedVertically: ");
			reflectedVertically(p1, p2);

		}else if(motive == 4){ // MOTIV 4
			p2 = p1.clone();
			for (int i = 0; i < p2.length / 2; i++) {
				float tmp = p2[i];
				p2[i] = p2[p2.length - 1 - i];
				p2[p2.length - 1 - i] = tmp;
			}
			System.out.println("reflectedPitch: ");
			reflectedPitch(p1,p2);

		}else{ // MOTIV 5
			double [] tiny = {52.0, 52.0, 52.0,	52.0, 54.0, 54.0, 54.0, 54.0, 65.0, 65.0, 65.0, 65.0, 69.0, 69.0, 69.0, 69.0, 72.0, 72.0, 72.0, 72.0, 74.0, 74.0, 74.0, 74.0, 76.0, 76.0, 76.0, 76.0, 78.0, 78.0, 78.0, 78.0};
			float[] tmp2 = new float[tiny.length];
			for (int i = 0 ; i < tmp2.length; i++)
			{
				tmp2[i] = (float) tiny[i];
			}
			p2 = tmp2;
			System.out.println("scaledDuration: ");
			scaledDuration(p2, p1);
		}



	}
}