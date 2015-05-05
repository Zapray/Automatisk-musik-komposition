package src;

import java.io.*;
import java.util.ArrayList;

import javax.sound.midi.*;

public class MidiPlayer {
      
	
	
	public static long convertNoteLengthToTicks(float noteLength,int res){
        res=res*4;
        
        return (long) (res*noteLength);
  
	}//end convertNoteLengthToTicks
	
	
	
	
	
	
	
      public static void main(String args[]) throws Exception{
         ArrayList<Note> theSong = new ArrayList<Note>(0);
         BufferedReader in=null;
         File outputFile = new File("/Users/Albin/Desktop/callme.mid");
         
         
         
         
         
         try{
        	 String line;
        	 float notePitch=0;
	         float noteLength=0;
	         
	         //in =new BufferedReader(new FileReader("D:\\filename.txt"));
	         in =new BufferedReader(new FileReader("/Users/Albin/Desktop/filename.txt"));
	         line=in.readLine();
	         //System.out.println(line);
	         
	         
	         while(line !=null){
	        	 for(int i=0; i<line.length(); i++){
	            	 if(line.charAt(i)==','){
	            		 //System.out.println(i);
	            		 //System.out.println(line);
	            		 notePitch=Float.parseFloat(line.substring(0,i));
	            		 noteLength=Float.parseFloat(line.substring(i+1,line.length()));
	            	 }
	             }
	        	 line=in.readLine();
	        	 //Note note = new Note(noteLength,notePitch); 
	        	 //theSong.add(note);
	        	 //TODO fix this Albin ?
	            
	         
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
        
        for (int i = 0; i<theSong.size();i++){
        	System.out.print((theSong.get(i)).getPitch() + "   ");
        	System.out.println((theSong.get(i)).getDuration());
        }  
         
         
         //Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
         //sequencer.open(); 
         Sequence sequence;
         sequence=new Sequence(Sequence.PPQ,192);
         
         
         Track track = sequence.createTrack();
         //ShortMessage shortMessage = new ShortMessage();
         MetaMessage metaMessage = new MetaMessage();
         long tick =0;
         long tickMeter=0;
         MidiEvent NoteOn;
         MidiEvent NoteOff;
         
         
         //V�ldigt ful kod
         //metaMessage.setMessage(4,[B@851052d],8);
         //metaMessage.setMessage(81,[B@54281d4b,8);
         //metaMessage.setMessage(89,[B@82a6f16,8);
         //metaMessage.setMessage(88,[B@a94884d,8);
         
         
         
         
         
         for(int i=0;i<theSong.size();i+=1){
        	 
        	 tick=convertNoteLengthToTicks((theSong.get(i)).getDuration(),192);
        	 System.out.println(tickMeter);
        	 if((theSong.get(i)).getPitch()==0){
        		tickMeter=tickMeter + tick;
        		 
        	 }
        	 
        	 //System.out.println((theSong.get(i)).getPitch());
        	 if((theSong.get(i)).getPitch()!=0){
        		
        		//System.out.println("Command1=   " +ShortMessage.NOTE_ON);
        		//System.out.println("Command2=   " + ShortMessage.NOTE_OFF);
        		ShortMessage	shortMessage1 = new ShortMessage();
        		shortMessage1.setMessage(ShortMessage.NOTE_ON,0,(int)(theSong.get(i)).getPitch(), 114 );
        		NoteOn=new MidiEvent(shortMessage1,tickMeter);
        		//System.out.println("commad:" +shortMessage.getCommand() + "     " + shortMessage.getData1() + "     " + shortMessage.getData2());
        		track.add(NoteOn);
        		ShortMessage	shortMessage2 = new ShortMessage();
        		tickMeter=tickMeter + tick;	
        		shortMessage2.setMessage(ShortMessage.NOTE_OFF,0,(int)(theSong.get(i)).getPitch(), 0 );
        		//System.out.println("commad:" +shortMessage.getCommand() + "     " + shortMessage.getData1() + "     " + shortMessage.getData2());
        		NoteOff=new MidiEvent(shortMessage2,tickMeter);
        		
        		track.add(NoteOff);
        			
        	 }
        		 
        	 
        	 
        	 
        	 
        	 
         }
        //int[  ] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        MidiSystem.write(sequence,0,outputFile);
 			MidiSystem.write(sequence, 0, outputFile);
         for(int nEvent = 0; nEvent < track.size()-1; nEvent++){	 
    		 MidiEvent event = track.get(nEvent);
    		 MidiMessage message = event.getMessage();
         	System.out.println(message);
    	 }
 	

        
        
        //sequencer.setSequence(sequence);
        //sequencer.start();
      
      
      
      
      
      
      
      //sequencer.close();
      }// end main








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