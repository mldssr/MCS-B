package org.tju.request;

public class test_RequestCorrelation {
	public static void main(String[] args) {
//		RequestCorrelation requestCorrelation = new RequestCorrelation(5);
//		RequestCorrelation.init(5);
		RequestCorrelation.put(0, 1);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 0);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 2);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 3);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 4);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 5);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 6);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 7);
		RequestCorrelation.display();
		
		RequestCorrelation.put(0, 8);
		RequestCorrelation.display();
		
		RequestCorrelation.put(1, 1);
		RequestCorrelation.display();
		
		RequestCorrelation.put(1, 2);
		RequestCorrelation.display();
	}

}
