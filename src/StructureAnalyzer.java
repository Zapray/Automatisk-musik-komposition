import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;


public class StructureAnalyzer {

	private static ArrayList<Float> pitchList = new ArrayList<Float>();
	private static ArrayList<Float> durationList = new ArrayList<Float>();

	public static void main(String[] args) throws Exception{
		try{

			BufferedReader in =new BufferedReader(new FileReader(System.getProperty("user.dir")+"/Databases_parts/chorus.txt"));
			//createUnconvertedArrays(in);


			//ArrayList<ArrayList<ArrayList<Float>>> data = parseTextFile(in);
			//ArrayList<ArrayList<Float>> section1 = data.get(0);
			//ArrayList<ArrayList<Float>> section2 = data.get(1);

			//testMethods(1);
			//testMethods(2);
			//testMethods(3);
			//testMethods(4);
			//testMethods(5);

			//float[] p1 =createPitchVector((!section1.get(0).isEmpty() ? section1:section2));
			//float[] p2 =createPitchVector(!section1.get(0).isEmpty() ? section2:data.get(2));


			//System.out.println(similarNotes(p1, p2));
			//System.out.println(similarDuration(p1, p2));

			analyzeMotifs(in);


		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void analyzeMotifs(BufferedReader in) throws Exception{
		Float pitch;
		Float duration;
		int countBar = 0;
		int countSection = 0;
		//int song = 8;
		float[] p1 =new float[16];
		float[] p2 =new float[16];
		float[] p3 =new float[16];
		float[] p4 =new float[16];
		
		int barsInSection = 8;
		ArrayList<ArrayList<ArrayList<Float>>> data = new ArrayList<ArrayList<ArrayList<Float>>>();
		ArrayList<ArrayList<Float>> section = new ArrayList<ArrayList<Float>>();
		//ArrayList<float[]> vectors = new ArrayList<float[]>(); 
		ArrayList<float[]> oneBars = new ArrayList<float[]>();
		File filen = new File(System.getProperty("user.dir")+"/Crazy.txt");

		if(!filen.exists()) {
			filen.createNewFile();
		}
		PrintWriter outFile = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/Crazy.txt", true));
		try{
			
			String line=in.readLine();
			while(line !=null){	

				if(line.charAt(0) == '?'){
					countBar++;
					System.out.println("Bar:"+countBar);
				}else if(line.charAt(0) == '-'){
					if(countBar==barsInSection+1){
						section.add(pitchList);
						section.add(durationList);
						data.add(section);
						pitchList = new ArrayList<Float>();
						durationList = new ArrayList<Float>();
						section = new ArrayList<ArrayList<Float>>();
						countSection++;
						System.out.println("Section:"+countSection);
					}
					for(int i=0; i<countSection; i++){
						for(int j=0; j<16;j++){
							p1[j]=createPitchVector(data.get(i))[j];
							p2[j]=createPitchVector(data.get(i))[j+16];
							p3[j]=createPitchVector(data.get(i))[j+32];
							p4[j]=createPitchVector(data.get(i))[j+48];
						}
						oneBars.add(p1);
						oneBars.add(p2);
						oneBars.add(p3);
						oneBars.add(p4);
						p1 =new float[16];
						p2 =new float[16];
						p3 =new float[16];
						p4 =new float[16];
					}
					for(int i=0; i<countSection*4; i++){
						for(int j=i+1; j<countSection*4; j++){
							if(similarNotes(oneBars.get(i), oneBars.get(j))){
								
								outFile.print("Rep:"+i+" "+j+',');
							}else if(similarRelativePitch(oneBars.get(i), oneBars.get(j))){
								outFile.print("Pit:"+i+" "+j+',');
							}else if(similarDuration(oneBars.get(i), oneBars.get(j))){
								outFile.print("Dur:"+i+" "+j+',');
							}
							
						}
					}
					outFile.println();
					outFile.println('-');
					countBar=0;
					countSection=0;
					oneBars=new ArrayList<float[]>();
					data=new ArrayList<ArrayList<ArrayList<Float>>>();
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
					System.out.println("Section:"+countSection);
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
				if(outFile != null){
					outFile.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
	}
	private static ArrayList<ArrayList<ArrayList<Float>>> parseTextFile(BufferedReader  in){
		Float pitch;
		Float duration;
		int countBar = 0;
		int countSong = 0;
		int song = 49;
		int barsInSection = 8;

		ArrayList<ArrayList<ArrayList<Float>>> data = new ArrayList();
		ArrayList<ArrayList<Float>> section = new ArrayList();

		try{
			String line=in.readLine();
			while(line !=null){	

				if(line.charAt(0) == '?'){
					countBar++;
				}else if(line.charAt(0) == '-'){
					countSong++;

					countBar++;
					if(countBar == barsInSection+1){
						countBar = 0;
						section.add(pitchList);
						section.add(durationList);
						data.add(section);
						pitchList = new ArrayList();
						durationList = new ArrayList();
						section = new ArrayList();
					}
					//System.out.println(countSong + ": " + countBar);
					countBar = 0;
					if(countSong == song){
						break;
					}
				}else if(countSong == song-1){

					if(countBar == barsInSection+1){
						countBar = 1;
						section.add(pitchList);
						section.add(durationList);
						data.add(section);
						pitchList = new ArrayList();
						durationList = new ArrayList();
						section = new ArrayList();
					}

					for(int i=0; i<line.length(); i++){

						if(line.charAt(i)==','){
							pitch= Float.parseFloat(line.substring(0,i));
							duration =Float.parseFloat(line.substring(i+1,line.length()));
							pitchList.add(pitch);
							durationList.add(duration);

						}	
					}

				}

				line=in.readLine();
			}


		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}


		return data;


	}

	//Unnecessary
	private static float[] createTimeVector(int L){
		float [] t = new float[L];

		for(int i = 0; i < t.length; i++){
			t[i+1] = (float) (t[i] + 1.0/16);
		}
		return t;
	}


	private static float[] createPitchVector(ArrayList<ArrayList<Float>> phrase){

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

	private static boolean compareSections(ArrayList<Float> sectionA_pitch, ArrayList<Float> sectionA_duration, ArrayList<Float> sectionB_pitch, ArrayList<Float> sectionB_duration){
		int yes = 0;
		int no = 0;
		if(sectionA_pitch.equals(sectionB_pitch) && sectionA_duration.equals(sectionB_duration)){
			return true;
		}else{
			for(int i = 0; i < sectionA_pitch.size(); i++){

				if(sectionA_pitch.get(i) == sectionB_pitch.get(i)){
					yes++;
				}else{
					no++;
				}
				if(sectionA_duration.get(i) == sectionB_duration.get(i)){
					yes++;
				}else{
					no++;
				}
			}
		}
		if((float)yes/(no + yes) > 0.5){
			return true;
		}else return false;	
	}
	private static boolean similarNotes(float[] vector1, float[] vector2){ //Compares two vectors if they have a percent of similarity in notes above 50%
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
		System.out.println(ratio);

		if(ratio>0.8){
			return true;
		}else{
			return false;
		}
	}

	private static boolean similarDuration(float[] vector1, float[] vector2){ //Compares two vectors if they have a percent of similarity in duration above 50%
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
					pastPitch1=vector1[i];
					length1++;
					if(vector2[i]==pastPitch2){
						pastPitch2=vector2[i];
						length2++;
					}
					i++;
					if(i==vector1.length){
						break;
					}
					
				}
			}
			if(length1 == length2){
				yes++;
			}else{
				no++;
			}
			i--;

		}
		if((float)yes/(yes+no)>0.5){
			return true;
		}else{
			return false;
		}
	}
	



	private static boolean similarRelativePitch(float[] vector1, float[] vector2){ // Compares two vectors if they have the same relative pitch
		int yes=0;
		int no=0;
		for(int i=0; i<vector1.length-1; i++){
			if(Math.abs((vector1[i]-vector1[i+1])-(vector2[i]-vector2[i+1]))<=1 ){
				yes++;
			}else{
				no++;
			}
		}
		float ratio = (float)yes/(yes+no);
		System.out.println(ratio);
		if(ratio>0.5){
			return true;
		}else{
			return false;
		}
	}	

	private static boolean scaledDuration(float[] tiny, float[] bigger){ //Compares one vector with a longer vector if they have the same pitches but a different scale of duration
		int yes=0;
		int no=0;
		int count1=0;
		int count2=0;
		float[] tinyBigger = new float[bigger.length];
		for(int i=0; i<tiny.length; i++){
			count1++;
			if(tiny[i]!=tiny[i+1]){
				for(int j=count2; j<count2+(count1*2);j++){
					tinyBigger[j] = tiny[i];
				}
				count2=count1*2+count2;
				count1=0;
			}

		} 
		return similarNotes(tinyBigger, bigger);
	}

	private static void testMethods(int motive){

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
			similarNotes(p1, p2);
		}else if(motive == 2){ // MOTIV 2
			for(int i = 0; i < p1.length; i++){
				p2[i] = p1[i] + 8;
			}
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
			double [] tiny = {52.0, 52.0, 52.0,	52.0, 52.0,	52.0, 54.0, 54.0, 54.0, 54.0, 54.0, 54.0, 65.0, 65.0, 65.0, 65.0, 65.0, 65.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0};
			float[] tmp2 = new float[tiny.length];
			for (int i = 0 ; i < tmp2.length; i++)
			{
				tmp2[i] = (float) tiny[i];
			}
			p2 = tmp2;
			scaledDuration(p2, p1);
		}



	}
}