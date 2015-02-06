import java.io.*;
import java.util.ArrayList;

import javax.sound.midi.*;

public class MidiPlayer {
      
      public static void main(String args[]){
         ArrayList theSong = new ArrayList(0);
         BufferedReader in=null;
         
         try{
        	 String line;
        	 float notePitch=0;
	         float noteLength=0;
	         
	         in =new BufferedReader(new FileReader("D:\\filename.txt"));
	         line=in.readLine();
	         System.out.println(line);
	         
	         
	         while(line !=null){
	        	 for(int i=0; i<line.length(); i++){
	            	 if(line.charAt(i)==','){
	            		 System.out.println(i);
	            		 System.out.println(line);
	            		 notePitch=Float.parseFloat(line.substring(0,i));
	            		 noteLength=Float.parseFloat(line.substring(i+1,line.length()));
	            	 }
	             }
	        	 line=in.readLine();
	        	 Note note = new Note(noteLength,notePitch); 
	        	 theSong.add(note);
	            
	         
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
        
        for (int i = 0; i<100;i++){
        	System.out.print((theSong.get(i)).getPitch() + "   ");
        	System.out.println((theSong.get(i)).getDuration());
        }  
         
         
      
      
      
      
      
      
      
      
      
      }








}
/**
public class BufferedReaderExample {
	 
	public static void main(String[] args) {
 
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("C:\\testing.txt"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	}
}*/