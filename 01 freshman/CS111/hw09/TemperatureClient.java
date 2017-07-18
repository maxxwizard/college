/*
 * TemperatureClient.java
 *
 * A sample client program for the Temperature class
 */
public class TemperatureClient {
    public static void main(String[] args) {
	Temperature freezing = new Temperature(0, 'C');
	
	System.out.println("The freezing point in Celcius is " + freezing);
	System.out.println("The freezing point in Fahrenheit is " + 
	  freezing.valueIn('F') + " F");
	System.out.println("The freezing point in Kelvin is " + 
	  freezing.valueIn('K') + " K");

	// freezing should still be 0 C
	System.out.println("The object freezing = " + freezing);
	System.out.println();

	// Now convert it to Fahrenheit.
	System.out.println("Converting freezing to Fahrenheit...");
	freezing.convertTo('F');
	System.out.println("The object freezing = " + freezing);
	System.out.println();

	// Now convert it to Kelvin.
	System.out.println("Converting freezing to Kelvin...");
	freezing.convertTo('K');
	System.out.println("The object freezing = " + freezing);
	System.out.println();

	// Compare two temperatures.
	Temperature freezing2 = new Temperature(32, 'F');
	System.out.println("The object freezing2 = " + freezing2);
	System.out.println("freezing.equals(freezing2) = " +
	  freezing.equals(freezing2));
	Temperature body = new Temperature(98.6, 'F');
	System.out.println("The object body = " + body.getValue() + " " +
	  body.getScale());
	System.out.println("freezing.equals(body) = " +
	  freezing.equals(body));
	System.out.println();

	// Test for temperature being below freezing.
	Temperature windChill = new Temperature(-10, 'F');
	System.out.println("The object windChill = " + windChill);
	System.out.println("windChill.isBelowFreezing() = " +
	  windChill.isBelowFreezing());
	System.out.println("body.isBelowFreezing() = " +
	  body.isBelowFreezing());
	System.out.println();
	
	// Try to make an invalid change.
	Temperature temp = new Temperature();
	System.out.println("The object temp = " + temp);	
	System.out.println("Trying to change temp to -10 K...");
	try {
	    temp.changeTo(-10, 'K');
	} catch(IllegalArgumentException e) {
	    System.out.println("Caught an IllegalArgumentException...");
	}
	System.out.println("The object temp = " + temp);
    }
}
