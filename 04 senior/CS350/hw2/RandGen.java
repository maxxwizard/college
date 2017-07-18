import java.util.*;

/**
 * @author Matthew Huynh
 * @description This program has 2 methods which can generate
 * random values from the standard normal distribution and
 * from a normal distribution with a parameterized mean and
 * standard deviation.
 */
public class RandGen {

 public static void main(String[] args) {
   System.out.println("random value from standard normal dist: " + Zrand(100));
   System.out.println("random value from norm dist, mean=0, stdev=1: " + Grand(0, 2));
 }
 
 /**
  * returns a random value that is distributed according to
  * a standard normal distribution
  */
 static double Zrand(int N)
 {
   Random uniRand = new Random();
   double[] samples = new double[N];
   for (int i = 0; i < samples.length; i++)
   {
     samples[i] = 4*uniRand.nextDouble()-2; // -2 to 2
   }
   
   double mean = mean(samples);
     double sd = stdev(samples, mean);
     System.out.println("mean: " + mean + ", stdev: " + sd);
   
     // return a random sample from this sample
   return samples[uniRand.nextInt(N)];
 }
 
 /**
  * returns a random value that is distributed according to
  * a normal distribution with mean U and standard deviation S
  */
 static double Grand(double U, double S)
 {
   Random uniRand = new Random();
   double shift = 0.5 - U; // if shift is negative, add it to random value, else subtract
   double variance = S*S;
   double expansion = variance;
   //System.out.println("shift: " + shift + ", variance: " + variance + ", expansion: " + expansion);
   
   double[] samples = new double[100];
   for (int i = 0; i < samples.length; i++)
   {
     double rv = uniRand.nextDouble();
     if (shift < 0)
     {
       rv += shift;
     }
     else
     {
       rv -= shift;
     }
     rv *= expansion;
     samples[i] = rv;
   }
   
   double mean = mean(samples);
     double sd = stdev(samples, mean);
     System.out.println("mean: " + mean + ", stdev: " + sd);
   
   // return a random sample from this sample
   return samples[uniRand.nextInt(samples.length)];
 }

static double mean(double[] samples)
{
  double sum = 0;
  for (double s : samples)
  {
    sum += s;
  }
  return sum/samples.length;
}

static double stdev(double[] samples, double mean)
{
  double variance = 0;
  for (double s : samples)
  {
    variance += Math.pow(s, 2);
  }
  variance /= samples.length;
  return Math.sqrt(variance);
}

}