import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;


public class AnalyzeTuringTest {

	private void load( String fileName ) throws FileNotFoundException {
		// Read the dictionary into a suitable container.
		// The file is a simple text file. One word per line.

		String key = "";
		BufferedReader in = new BufferedReader(new FileReader(fileName));
//		try{
//			String line=in.readLine();
//			while(line !=null){	
//				if(line.length() > wordLength){
//					wordLength = line.length();
//				}
//				if(line.matches(".+'s")){
//					dictionary.put(key, wordList);
//					wordList = new ArrayList();
//					key = String.valueOf(line.charAt(0));
//				}else{
//					wordList.add(line);
//				}
//				line=in.readLine();
//			}
//			dictionary.put(key, wordList);
//			System.out.println();
//		}catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (in != null)in.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//	

	
	
	}
	
}
