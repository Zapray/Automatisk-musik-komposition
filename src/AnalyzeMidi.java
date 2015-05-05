package src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.*;

public class AnalyzeMidi {

	public static void main(String[] args) throws Exception{
		Sequencer sequencer = MidiSystem.getSequencer();//Creates a sequencer
        sequencer.open();// have to open the sequencer to be able to use sequences. Don't know why, it works without the first two lines.
		//InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\taylor_swift-shake_it_off.mid")));//Creates an inputstream of the midi file you want to use.
        //InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\john_legend-all_of_me.mid")));
        InputStream is = new BufferedInputStream(new FileInputStream(new File("/Users/Albin/Desktop/Hooktheory-2015-02-04-01-25-10.mid")));
        Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
        
        System.out.println(sequence.getTickLength());
        //float bpm=60;
        //sequencer.setTempoInBPM(bpm);
        System.out.println(sequencer.getTempoInBPM());
        //System.out.println(sequence.getTickPosition());
        
        int ppqn=sequence.getResolution();
        System.out.println("PPQN:" + sequence.getResolution());
        
        
        
        
        Sequence output = new Sequence(sequence.getDivisionType(), sequence.getResolution(), 0);
        output.createTrack();
        Track[] tracken=output.getTracks();
        Track tracken2=tracken[0];
        ShortMessage kortmed=new ShortMessage();
        kortmed.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        
        
        MidiEvent eventet = new MidiEvent(kortmed,10);
        tracken2.add(eventet);
        
        /** Test
        	Synthesizer synthesizer = MidiSystem.getSynthesizer();//Creates a synthesizer. Contains information about instruments.
        synthesizer.open();
        Instrument[] orchestra = synthesizer.getAvailableInstruments();//An array to compare numbers to instruments.
        String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        //System.out.println(orchestra.length);
       
        
        System.out.println(tracken2.size());
        for(int nEvent = 0; nEvent < 1; nEvent++)//loop through events
        	{
        		//System.out.print("Event"+nEvent+":");
        		MidiEvent event = tracken2.get(nEvent);
        		MidiMessage message = event.getMessage();
            System.out.println(event.getMessage());
        		if(message instanceof ShortMessage)//every event contains a short message or a meta message.
        		{
        			ShortMessage message2 = (ShortMessage) message;
        			if(message2.getCommand() == ShortMessage.PROGRAM_CHANGE)// can write more if:s with ex NOTE_ON instead of PROGRAM_CHANGE. NOTE_ON: finds the start of a note, PROGRAM_CHANGE: instruments
        			{
        				System.out.println(orchestra[message2.getData1()].toString());//gives the instruments
        			}
        			if(message2.getCommand() == ShortMessage.NOTE_ON)
        			{
                  System.out.println(event.getTick() + "  " + "Note:"+notes[message2.getData1()%12]+message2.getData1()/12+" "+message2.getData1()+", Velocity:"+message2.getData2());
        			}
        			if(message2.getCommand() == ShortMessage.NOTE_OFF )
        			{
        				System.out.println(event.getTick()  + " " + "Noteoff:"+message2.getData1());
        			}
        		}
        		else if(message instanceof MetaMessage)
        		{
        			MetaMessage message2 = (MetaMessage)message;
        			
        			if(message2.getType() == 0x03) // gives tracks
        			{
        				String str = new String(message2.getData());
        	            System.out.println("Name : "+str);
        				
        			}
        		}
        	}//end for

        
         
         
         
         
       */       
        
        
		Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
		Synthesizer synthesizer = MidiSystem.getSynthesizer();//Creates a synthesizer. Contains information about instruments.
        synthesizer.open();
        Instrument[] orchestra = synthesizer.getAvailableInstruments();//An array to compare numbers to instruments.
        String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        //System.out.println(orchestra.length);
       
		System.out.println(tracks.length);
		
        //for(int nTrack = 0; nTrack < tracks.length; nTrack++)//loop through tracks.
        //{
        	int nTrack = 0;
        	Track   track = tracks[nTrack];
         System.out.println(track.ticks());
        	System.out.println("Track"+nTrack+":");
        	for(int nEvent = 0; nEvent < 20; nEvent++)//loop through events
        	{
        		//System.out.print("Event"+nEvent+":");
        		MidiEvent event = track.get(nEvent);
        		MidiMessage message = event.getMessage();
            System.out.println(event.getMessage());
        		if(message instanceof ShortMessage)//every event contains a short message or a meta message.
        		{
        			ShortMessage message2 = (ShortMessage) message;
        			if(message2.getCommand() == ShortMessage.PROGRAM_CHANGE)// can write more if:s with ex NOTE_ON instead of PROGRAM_CHANGE. NOTE_ON: finds the start of a note, PROGRAM_CHANGE: instruments
        			{      
                  System.out.println(orchestra[message2.getData1()].toString());//gives the instruments
        			}
        			if(message2.getCommand() == ShortMessage.NOTE_ON)
        			{
                  System.out.println(event.getTick() + "  " + "Note:"+notes[message2.getData1()%12]+message2.getData1()/12+" "+message2.getData1()+", Velocity:"+message2.getData2());
        			}
        			if(message2.getCommand() == ShortMessage.NOTE_OFF )
        			{
        				System.out.println(event.getTick()  + " " + "Noteoff:"+message2.getData1());
        			}
        		}
        		else if(message instanceof MetaMessage)
        		{
        			MetaMessage message2 = (MetaMessage)message;
        			
        			if(message2.getType() == 0x03) // gives tracks
        			{
        				String str = new String(message2.getData());
        	            System.out.println("Name : "+str);
        				
        			}
        		}
        	}//end for
         
        //}
	sequencer.close();
   }

}
