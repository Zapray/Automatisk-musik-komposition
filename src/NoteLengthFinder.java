import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class NoteLengthFinder {

	public static void main(String args[])throws Exception{
		ArrayList<float> listOfNoteLengths = new ArrayList<float>();
        BufferedReader in=null;
		
		 try{
        	 String line;
        	 float notePitch=0;
	         float noteLength=0;
	         boolean existInArray;
	         //in =new BufferedReader(new FileReader("D:\\filename.txt"));
	         in =new BufferedReader(new FileReader("/Users/Albin/Desktop/filename.txt"));
	         line=in.readLine();
	         //System.out.println(line);
	         
	         
	         
	         while(line !=null){
	        	 existInArray=false;
	        	 for(int i=0; i<line.length(); i++){
	            	 if(line.charAt(i)==','){
	            		 noteLength=Float.parseFloat(line.substring(i+1,line.length()));
	            	 }
	             }
	        	 
	        	 for(int j=0; j < listOfNoteLengths.size();j++){
	        		 if(listOfNoteLengths(i)==noteLength){ 
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
	
	
}
