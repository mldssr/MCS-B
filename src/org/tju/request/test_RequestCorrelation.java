package org.tju.request;

public class test_RequestCorrelation {
	public static void main(String[] args) {
//		RequestCorrelation requestCorrelation = new RequestCorrelation(5);
//		RequestCorrelation.init(5);
		RequestCorrelation.putIntoCorr(5541, 6521);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(5541, 6521);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(5541, 5541);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(5541, 6521);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 1);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 0);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 2);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 3);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 3);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 4);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 5);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 6);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 7);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 8);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(1, 1);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(1, 2);
		RequestCorrelation.display();
		
		RequestCorrelation.putIntoCorr(0, 6);
		RequestCorrelation.display();
	}

}
