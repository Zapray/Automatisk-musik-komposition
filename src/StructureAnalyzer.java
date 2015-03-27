import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class StructureAnalyzer {
	//All the songs unconverted from textfile, only the pitch
	private static ArrayList<Float> unconPitchList = new ArrayList<Float>();
	//All the songs unconverted from textfile, only the duration
	private static ArrayList<Float> unconDurationList = new ArrayList<Float>();

	private static ArrayList<Float> pitchList = new ArrayList<Float>();
	private static ArrayList<Float> durationList = new ArrayList<Float>();

	private static ArrayList<Float> sectionA_pitch = new ArrayList<Float>();
	private static ArrayList<Float> sectionA_duration = new ArrayList<Float>();
	private static ArrayList<Float> sectionB_pitch = new ArrayList<Float>();
	private static ArrayList<Float> sectionB_duration = new ArrayList<Float>();



	public static void main(String[] args){
		try{
			
			BufferedReader in =new BufferedReader(new FileReader(System.getProperty("user.dir")+"/Databases_parts/chorus.txt"));
			//createUnconvertedArrays(in);
			ArrayList<ArrayList<ArrayList<Float>>> data = parseTextFile(in);
			float[] p =createPitchVector(16*4, data);
			System.out.println("");


		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<ArrayList<ArrayList<Float>>> parseTextFile(BufferedReader  in){
		Float pitch;
		Float duration;
		int countBar = 0;
		int countSong = 0;
		int song = 6;

		ArrayList<ArrayList<ArrayList<Float>>> data = new ArrayList();
		ArrayList<ArrayList<Float>> section = new ArrayList();

		try{
			String line=in.readLine();
			while(line !=null){	

				if(line.charAt(0) == '?'){
					countBar++;
				}else if(line.charAt(0) == '-'){
					countSong++;
					//System.out.println(countSong + ": " + countBar);
					countBar = 0;
					if(countSong == song){
						break;
					}
				}else if(countSong == song-1){
					for(int i=0; i<line.length(); i++){

						if(line.charAt(i)==','){
							pitch= Float.parseFloat(line.substring(0,i));
							duration =Float.parseFloat(line.substring(i+1,line.length()));
							pitchList.add(pitch);
							durationList.add(duration);

						}	
					}
					if(countBar == 8){
						countBar = 0;
						section.add(pitchList);
						section.add(durationList);
						data.add(section);
						pitchList = new ArrayList();
						durationList = new ArrayList();
						section = new ArrayList();


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

	private static float[] createTimeVector(int L){
		float [] t = new float[L];

		for(int i = 0; i < t.length; i++){
			t[i+1] = (float) (t[i] + 1.0/16);
		}
		return t;
	}

	private static float[] createPitchVector(int L, ArrayList<ArrayList<ArrayList<Float>>> data){

		float [] p = new float[L];
		int numberOfPoints;
		int countPoints = 0;
		for(int i = 0; i < data.get(0).get(0).size(); i++){
			
			float pitch = data.get(0).get(0).get(i);
			float time = data.get(0).get(1).get(i);
			
			numberOfPoints = Math.round(time*16);
			for(int j = countPoints; j < countPoints + numberOfPoints; j++){
				p[j] = pitch;
			}
			countPoints = countPoints + numberOfPoints;
		}
	
		return p;
	}

	private static void createUnconvertedArrays(BufferedReader  in){
		int count = 0;

		Float unconvertedNotePitch;
		Float unconvertedNoteDuration;
		try{
			String line=in.readLine();
			while(line !=null){	

				if(line.charAt(0) == '?'){
					count++;
				}else if(line.charAt(0) == '-'){
					boolean result = compareSections(sectionA_pitch, sectionA_duration, sectionB_pitch, sectionB_duration);
					System.out.println(result);
					break;
				}else{
					for(int i=0; i<line.length(); i++){

						if(line.charAt(i)==','){
							unconvertedNotePitch=Float.parseFloat(line.substring(0,i));
							unconvertedNoteDuration=Float.parseFloat(line.substring(i+1,line.length()));
							unconPitchList.add(unconvertedNotePitch);
							unconDurationList.add(unconvertedNoteDuration);

						}	
					}
					if(count == 8){
						count = 0;
						if(sectionA_pitch.isEmpty()){
							sectionA_pitch = unconPitchList;
							sectionA_duration = unconDurationList;
						}else{
							sectionB_pitch = unconPitchList;
							sectionB_duration = unconDurationList;
						}
						unconPitchList.clear();
						unconDurationList.clear();

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
		if(yes/(no + yes) > 0.8){
			return true;
		}else return false;	
	}
	private static boolean similarNotes(float[] vector1, float[] vector2){
			int yes=0;
			int no=0;
			for(int i=0; i<vector1.length; i++){
				if(vector1[i] == vector2[i]){
					yes++;
				}else{
					no++;
				}
			}
			if(yes/(yes+no)>0.8){
				return true;
			}else{
				return false;
			}
		}
		private static boolean similarDuration(float[] vector1, float[] vector2){
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
					while(vector1[i]==pastPitch1){
						pastPitch1=vector1[i];
						pastPitch2=vector2[i];
						length1++;
						if(vector2[i]==pastPitch2){
							length2++;
						}
						i++;
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
			if(yes/(yes+no)>0.8){
				return true;
			}else{
				return false;
			}
		}

	private static boolean similarRelativePitch(ArrayList<Note> list1, ArrayList<Note> list2){
		int yes=0;
		int no=0;
		for(int i=0; i<list1.size()-1; i++){
			if(list1.get(i).getPitch()-list1.get(i+1).getPitch() == list2.get(i).getDuration()-list2.get(i+1).getDuration()){
				yes++;
			}else{
				no++;
			}
		}
		if(yes/(yes+no)>0.8){
			return true;
		}else{
			return false;
		}
	}
}
