import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
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
	private String[][] patternMatrix;
	private int countEquals = 0;
	private int countSection = 0;
	private String textFile = "/Databases_parts/chorus.txt";
	private String outputTextFile = "/Crazy.txt";
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

		//printMatrix(patternMatrix);

		//		testMethods(1);
		//		testMethods(2);
		//		testMethods(3);
		//		testMethods(4);
		//		testMethods(5);

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
					if(countBar==barsInSection){
						section.add(pitchList);
						section.add(durationList);
						data.add(section);

						countSection++;
						System.out.println("Section:"+countSection);

					}

					//Temporary solution to cases where a sections duration doesn't equal 4.
					boolean isDurationFour = true;
					for(int i = 0; i < countSection; i++){
						if(calculateSectionDuration(data.get(i)) != 4.0f){
							isDurationFour = false;
						}
					}

					if(data.size() > 1 && isDurationFour){
						analyzeMotifs();
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
					printMatrix(patternMatrix);
					System.out.println("***********************NEW SONG******************************");
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
			System.out.print("Done");

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

	private void otherTextFileThingy(String [][] matrix){

		int count = 1;
		for(int row = 0; row < matrix.length; row++){
			System.out.println();
			for(int col = 0; col < matrix[row].length-1; col++){

				if(patternMatrix[row][col].charAt(0) != patternMatrix[row][col+1].charAt(0)){
					count++;
					if(col == 3 ){
						System.out.print(count + "  "); count = 1;
					}
				}else{
					System.out.print(count + "  "); count = 1;
				}

			}

		}
	}

	private void analyzeMotifs() throws Exception{


		File filen = new File(System.getProperty("user.dir")+outputTextFile);

		if(!filen.exists()) {
			filen.createNewFile();
		}
		PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+ outputTextFile, true));
		try{

			//**********************PLOT**************************//
			if(isPlottingOn){
				//BUG: Durations does not add up to 4 (for one section) for some songs!! FIX!
				double[] section1 = convertToDouble(createPitchVector(data.get(0)));
				double[] section2 = convertToDouble(createPitchVector(data.get(1)));

				if(countSection < 4){//2 sections
					proxy.feval("plotMelody", section1, section2);
				}else{
					double[] section3 = convertToDouble(createPitchVector(data.get(2)));
					double[] section4 = convertToDouble(createPitchVector(data.get(3)));
					if (countSection < 6){//4 sections
						proxy.feval("plotMelody", section1, section2, section3, section4);
					}else{
						double[] section5 = convertToDouble(createPitchVector(data.get(4)));
						double[] section6 = convertToDouble(createPitchVector(data.get(5)));
						if (countSection < 8){//6 sections
							proxy.feval("plotMelody", section1, section2, section3, section4, section5, section6);
						}else{//more than 6 sections...
							double[] section7 = convertToDouble(createPitchVector(data.get(6)));
							double[] section8 = convertToDouble(createPitchVector(data.get(7)));
							proxy.feval("plotMelody", section1, section2, section3, section4, section5, section6, section7, section8);
						}
					}
				}
			}

			//**********************COMPARE*************************//

			sections.add(createPitchVector(data.get(0)));
			sections.add(createPitchVector(data.get(1)));
			if(countSection < 4){ //2 sections
			}else{
				sections.add(createPitchVector(data.get(2)));
				sections.add(createPitchVector(data.get(3)));
				if (countSection < 6){ //4 sections
				}else{
					sections.add(createPitchVector(data.get(4)));
					sections.add(createPitchVector(data.get(5)));
					if(countSection < 8){ //6sections
					}else{//more than 6 sections...
						sections.add(createPitchVector(data.get(6)));
						sections.add(createPitchVector(data.get(7)));
					}
				}
			}
			patternMatrix = new String[sections.size()][4];

			for(int first=0; first < sections.size(); first++){
				for(int second=first+1; second < sections.size(); second++){
					//*******************SECTIONS********************//
					int firstSection = first+1;
					int secondSection = second+1;
					//String hej = patternMatrix[0][0];
					compareBars(data.get(first), data.get(second), firstSection, secondSection);

				}
			}
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
		} finally{
			if(outFile != null){
				outFile.close();
			}
		}

	}

	private void compareBars(ArrayList<ArrayList<Float>> section1, ArrayList<ArrayList<Float>> section2, int firstSection, int secondSection){
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
				String motive = getMotive(oneBars.get(first), oneBars.get(second));
				if(motive != "0"){

					int firstBar = first+1;
					int secondBar = second+1;
					//System.out.print("Sections: " + firstSection + " and "+ secondSection + "       ");
					//System.out.println("Equal bars: " + firstBar + " and " + secondBar);

					if(firstBar <=4 && secondBar<=4){
						//Check to see if matrix entry is empty or not (initialize or copy value)
						if(patternMatrix[firstSection-1][first] == null){
							countEquals++;
							patternMatrix[firstSection-1][first] = Integer.toString(countEquals) + "0";
							if(patternMatrix[firstSection-1][first].charAt(1) < motive.charAt(0) && patternMatrix[firstSection-1][second] != null){

							}else{
								patternMatrix[firstSection-1][second] = patternMatrix[firstSection-1][first].charAt(0) + motive;
							}
						}else{
							patternMatrix[firstSection-1][second] = patternMatrix[firstSection-1][first].charAt(0) + motive;
						}
					}else if(firstBar <= 4 && secondBar > 4){
						//Check to see if matrix entry is empty or not (initialize or copy value)
						if(patternMatrix[firstSection-1][first] == null){
							countEquals++;
							patternMatrix[firstSection-1][first] = Integer.toString(countEquals) + "0";
							if(patternMatrix[firstSection-1][first].charAt(1) < motive.charAt(0) && patternMatrix[secondSection-1][second-4] != null){

							}else{
								patternMatrix[secondSection-1][second-4] = patternMatrix[firstSection-1][first].charAt(0) + motive;
							}
							
						}else {
							patternMatrix[secondSection-1][second-4] = patternMatrix[firstSection-1][first].charAt(0) + motive;
						}
					}else{
						//Check to see if matrix entry is empty or not (initialize or copy value)
						if(patternMatrix[secondSection-1][first-4] == null){
							countEquals++;
							patternMatrix[secondSection-1][first-4] = Integer.toString(countEquals) + "0";
							if(patternMatrix[secondSection-1][first-4].charAt(1) < motive.charAt(0) && patternMatrix[secondSection-1][second-4] != null){

							}else{
								patternMatrix[secondSection-1][second-4] = patternMatrix[secondSection-1][first-4].charAt(0) + motive;
							}
						}else {
							patternMatrix[secondSection-1][second-4] = patternMatrix[secondSection-1][first-4].charAt(0) + motive;
						}
					}

				}
			}
		}
		//		countEquals++;
		//		for(int row = 0; row < patternMatrix.length; row++){
		//			for(int col = 0; col < patternMatrix[row].length; col++){
		//				if(patternMatrix[row][col]==null){
		//					patternMatrix[row][col]=countEquals+"0";
		//					countEquals++;
		//				}
		//				
		//			}
		//		}
		oneBars=new ArrayList<float[]>();
	}

	private String getMotive(float[] phrase1, float[] phrase2){

		if(similarNotes(phrase1, phrase2,(float)0.62)){
			return "1";
		}
		else if(similarNotes(phrase1, phrase2,(float)0.6)){
			return "2";
		}
		//		else if(similarRelativePitch(phrase1, phrase2)){ //FUNKAR EJ
		//			return "3";
		//		}
		//		else if(similarDuration(phrase1, phrase2)){
		//			return "4";
		//		}
		else{
			return "0";
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
		int yes=0;
		int no=0;
		for(int i=0; i<vector1.length-1; i++){
			if(Math.abs((vector1[i]-vector1[i+1])-(vector2[i]-vector2[i+1]))<=1 && (((vector1[i]-vector1[i+1])<0 && (vector2[i]-vector2[i+1]<0)) || ((vector1[i]-vector1[i+1])>0 && (vector2[i]-vector2[i+1]>0))) ){
				yes++;
			}else{
				no++;
			}
		}
		float ratio = (float)yes/(yes+no);
		//System.out.println(ratio);
		if(ratio>=0.5){
			return true;
		}else{
			return false;
		}
	}	
	private boolean reflectedPitch(float[] vector1, float[] vector2){
		float[] vectorExtra = vector1;
		for(int i=0; i<vector1.length; i++){
			for(int j=vector1.length-1; j>=0; j--){
				vector1[i]=vectorExtra[j];
			}
		}
		return similarNotes(vector1, vector2, (float)0.8);
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
			System.out.println();

		}else if(motive == 4){ // MOTIV 4
			p2 = p1.clone();
			for (int i = 0; i < p2.length / 2; i++) {
				float tmp = p2[i];
				p2[i] = p2[p2.length - 1 - i];
				p2[p2.length - 1 - i] = tmp;
			}
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