/*
 * Temperature.java
 * 
 * name: Matthew Huynh
 * email: mhuynh@bu.edu
 * 
 * description: This class is a blueprint for objects that can be used to
 * represent temperature readings. Each Temperature object encapsulates
 * both a temperature's value and its scale (Fahrenheit, Celcius, or Kelvin).
 * The value is stored as a floating-point number (a double), and the scale
 * is stored as one of the following characters: 'F', 'C', or 'K'.
 */

public class Temperature {
    private double temp;
    private char scale;
    
    /*
     * default empty constructor makes a Temperature of 0 degrees C
     */
    public Temperature() {
        temp = 0;
        scale = 'C';
    }
    
    /*
     * constructor that takes in a value and a scale
     */
    public Temperature(double temp, char scale) {
         changeTo(temp, scale);
    }
    
    /*
     * returns the Temperature's value
     */
    public double getValue() {
        return temp;
    }
    
    /*
     * returns the Temperature's scale
     */
    public char getScale() {
        return scale;
    }
    
    /*
     * changes the Temperature's value and scale to the specified new ones
     */
    public void changeTo(double temp, char scale) {
        if (scale == 'F' && temp >= -459.67) {
            this.temp = temp; 
            this.scale = scale;
        }
        else if (scale == 'C' && temp >= -273.15) {
            this.temp = temp;
            this.scale = scale; 
        }
        else if (scale == 'K' && temp >= 0) {
            this.temp = temp;
            this.scale = scale;
        }
        else
            throw new IllegalArgumentException();
    }
    
    /*
     * returns the Temperature's corresponding value in a specified scale
     * (does not change internal fields)
     */
    public double valueIn(char scale) {
        double newTemp = 0;
        
        if (scale == 'F') {
            if (this.scale == 'F') {
                newTemp = temp; }
            if (this.scale == 'C') {
                newTemp = (temp*1.8 + 32.0); }
            if (this.scale == 'K') {
                newTemp = (temp*1.8 - 459.67); }
        }
        else if (scale == 'C') {
            if (this.scale == 'F') {
                newTemp = (temp-32.0)*1.8; }
            if (this.scale == 'C') {
                newTemp = temp; }
            if (this.scale == 'K') {
                newTemp = temp - 273.15; }
        }
        else if (scale == 'K') {
            if (this.scale == 'F')
                newTemp = (temp + 459.67)*1.8;
            if (this.scale == 'C')
                newTemp = temp + 273.15;
            if (this.scale == 'K')
                newTemp = temp;
        }
        else
            throw new IllegalArgumentException();
        
        return newTemp;
    }
    
    /*
     * converts the Temperature's value to its corresponding value in a specified scale
     * (changes internal fields)
     */
    public void convertTo(char scale) {
        // if converting to same scale, don't do anything
        if (this.scale == scale)
            return;
        
        if (scale == 'F') {
            if (this.scale == 'C') {
                changeTo((temp*1.8 + 32.0),'F'); }
            if (this.scale == 'K') {
                changeTo((temp*1.8 - 459.67),'F'); }
        }
        else if (scale == 'C') {
            if (this.scale == 'F') {
                changeTo((temp-32.0)*1.8,'C'); }
            if (this.scale == 'K') {
                changeTo(temp - 273.15,'C'); }
        }
        else if (scale == 'K') {
            if (this.scale == 'F') {
                changeTo((temp + 459.67)*(5.0/9.0),'K'); }
            if (this.scale == 'C') {
                changeTo(temp + 273.15,'K'); }
        }
        else
            throw new IllegalArgumentException();
    }
    
    /*
     * checks if a Temperature is below freezing or not
     */
    public boolean isBelowFreezing() {
        if (valueIn('C') < 0.0)
            return true;
        
        return false;
    }
    
    /*
     * checks if a Temperature is equalivalent to another specified Temperature
     */
    public boolean equals(Temperature t) {
        if ((Math.abs(valueIn('C') - t.valueIn('C')) < 0.0000001))
            return true;
        
        return false;
    }
    
    /*
     * returns a String in the format of "value scale"
     */
    public String toString() {
        return temp + " " + scale;
    }
}