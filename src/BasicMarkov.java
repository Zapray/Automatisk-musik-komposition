
import java.util.List;

import org.ejml.*;
import org.ejml.simple.SimpleMatrix;
public class BasicMarkov {
	SimpleMatrix transitionMatrix;
	
	public BasicMarkov(List<Note> data) {
		transitionMatrix = new SimpleMatrix(224,224);
	}
	
	public static void main(String[] args) {
		
	}
	
}
