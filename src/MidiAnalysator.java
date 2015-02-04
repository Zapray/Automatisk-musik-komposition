import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.*;

public class MidiAnalysator {

   
   public static float convertTicksToNoteLength(long tick1, long tick2, float res){
         res=res*4;
         
         return (tick2-tick1)/res;
   
   }//end convertTicksToNoteLength
   

   

   public static void main(String args[])throws Exception{
      long tick=1;
      float[][] dataarray= new float[100][2];
      int counter1=0;
      int counter2=0;
      boolean foundNote = false;
      Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
      sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
      InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/Hooktheory-2015-02-04-01-25-10.mid")));
      Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
      float res = sequence.getResolution();
      Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
      
      int melodytrack = 0;
      Track   track = tracks[melodytrack];
      for(int nEvent = 0; nEvent < track.size(); nEvent++){//loop through events
         MidiEvent event = track.get(nEvent);
        	MidiMessage message = event.getMessage();
   
         if(message instanceof ShortMessage)//every event contains a short message or a meta message.
        		{
               ShortMessage shortMessage = (ShortMessage) message;
               
               if(shortMessage.getCommand() == ShortMessage.NOTE_ON)
        			{
                dataarray[counter1][0]=shortMessage.getData1();
                tick=event.getTick();
              
        			}else if(shortMessage.getCommand() == ShortMessage.NOTE_OFF){
                  dataarray[counter1][1]=convertTicksToNoteLength(tick, event.getTick(), res);
                  counter1 +=1;   
               }

         
         
         
         
         
            }
         
            
            
         
         
         
         }
         
         
    for (int i = 0; i<100;i++){
         System.out.print(dataarray[i][0] + "   ");
         System.out.println(dataarray[i][1]);
    }  
      
  
      
      
      
   
   
   
   
   
   
   sequencer.close();
   }//end main



}