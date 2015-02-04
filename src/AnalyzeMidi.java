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
        InputStream is = new BufferedInputStream(new FileInputStream(new File("D:\\MidiMusic\\ed_sheeran-thinking_out_loud.mid")));
        Sequence sequence = MidiSystem.getSequence(is);//Creates a sequence which you can analyze.
		Track[] tracks = sequence.getTracks();//Creates an array to be able to separate tracks.
		Synthesizer synthesizer = MidiSystem.getSynthesizer();//Creates a synthesizer. Contains information about instruments.
        synthesizer.open();
        Instrument[] orchestra = synthesizer.getAvailableInstruments();//An array to compare numbers to instruments.
        String notes[] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        //System.out.println(orchestra.length);
       
		//System.out.println(tracks.length);
		//System.out.println(track.size());
        //for(int nTrack = 0; nTrack < tracks.length; nTrack++)//loop through tracks.
        //{
        	int nTrack = 0;
        	Track   track = tracks[nTrack];
        	System.out.println("Track"+nTrack+":");
        	for(int nEvent = 0; nEvent < track.size(); nEvent++)//loop through events
        	{
        		//System.out.print("Event"+nEvent+":");
        		MidiEvent event = track.get(nEvent);
        		MidiMessage message = event.getMessage();
        
        		if(message instanceof ShortMessage)//every event contains a short message or a meta message.
        		{
        			ShortMessage message2 = (ShortMessage) message;
        			if(message2.getCommand() == ShortMessage.PROGRAM_CHANGE)// can write more if:s with ex NOTE_ON instead of PROGRAM_CHANGE. NOTE_ON: finds the start of a note, PROGRAM_CHANGE: instruments
        			{
        				System.out.println(orchestra[message2.getData1()].toString());//gives the instruments
        			}
        			if(message2.getCommand() == ShortMessage.NOTE_ON)
        			{
        				System.out.println("Note:"+notes[message2.getData1()%12]+message2.getData1()/12+" "+message2.getData1()+", Velocity:"+message2.getData2());
        			}
        			//if(message2.getCommand() == ShortMessage.NOTE_OFF )
        			//{
        				//System.out.println("Noteoff:"+message2.getData1());
        			//}
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
        	}
        //}
	}

}
