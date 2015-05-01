
public enum Percentage {
	SIXTY, EIGHTY, HUNDRED;
public double toDouble(){
	switch(this){
	case EIGHTY:
		return 0.8;
	case HUNDRED:
		return 1.0;
	case SIXTY:
		return 0.6;
	default:
		throw new IllegalArgumentException();
	}
		
	}
}

