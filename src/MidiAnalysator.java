import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*;

public class MidiAnalysator {

   
   public static float convertTicksToNoteLength(long tick1, long tick2, float res){
         res=res*4;
         
         return (tick2-tick1)/res;
   
   }//end convertTicksToNoteLength
   

   

   public static void main(String args[])throws Exception{
      long tick=1;
      long spectick=1;
      ArrayList note= new ArrayList(0);
      ArrayList notelength= new ArrayList(0);
      int counter1=0;
      int counter2=0;
      boolean foundNote = false;
      boolean startOfSong = true;
      Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
      sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
      InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\Hooktheory-2015-02-04-01-25-10.mid")));
      Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
      float res = sequence.getResolution();
      Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
      
      int melodytrack = 0;
      Track   track = tracks[melodytrack];
      for(int nEvent = 0; nEvent < track.size()-1; nEvent++){//loop through events
         MidiEvent event = track.get(nEvent);
        	MidiMessage message = event.getMessage();
         MidiEvent event2 = track.get(nEvent+1);
   
         
         if(message instanceof ShortMessage)//every event contains a short message or a meta message.
        		{
               ShortMessage shortMessage = (ShortMessage) message;
               
               if(event.getTick()!=0 && startOfSong){
               
                  notelength.add(convertTicksToNoteLength(0, event.getTick(), res));
                  note.add(0);
             
                  //dataarray[counter1][1]=convertTicksToNoteLength(0, event.getTick(), res);
                  //dataarray[counter1][0]=0;
                  
                  counter1+=1;
               }
               
               if(shortMessage.getCommand() == ShortMessage.NOTE_ON)
        			{
                  
                  note.add(shortMessage.getData1());
                //dataarray[counter1][0]=shortMessage.getData1();
                tick=event.getTick();
              
        			}else if(shortMessage.getCommand() == ShortMessage.NOTE_OFF){
                  
                  notelength.add(convertTicksToNoteLength(tick, event.getTick(), res));
                  //dataarray[counter1][1]=convertTicksToNoteLength(tick, event.getTick(), res);
                  counter1 +=1;  
                  if(event.getTick()!=event2.getTick()&& nEvent!=track.size()-2){
                     notelength.add(convertTicksToNoteLength(event.getTick(), event2.getTick(), res));
                     note.add(0);
                     
                     //dataarray[counter1][1]=convertTicksToNoteLength(event.getTick(), event2.getTick(), res);
                     //dataarray[counter1][0]=0;
                     counter1+=1;
                  }
                  
                   
               }

               startOfSong=false;
         
         
            }
         
            
            
         
         
         
         }
    
       String content = "";
       for (int i=0;i<counter1;i++){
         content = content + note.get(i) + "," + notelength.get(i) + "\r\n";
       }
    
 
			File file = new File("D:\\filename.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");   
       
         
    //for (int i = 0; i<100;i++){
         //System.out.print(dataarray[i][0] + "   ");
         //System.out.println(dataarray[i][1]);
    //}  
      
  
      
      
      
   
   
   
   
   
   
   sequencer.close();
   }//end main



}