import java.io.*;
import java.util.*;

//These distributions answer the question: Given a preceding pos, what is the probability for a 
//particular following pos.

//Checks that each distribution over pos given a preceding pos is normalized. 
//Prints out the distributions that are not normalized. 
//Prints out the number of elements over which probobility is distributed (for each distribution). 
//Prints out the number of pos tags for which there are distributions. 

//If you uncomment a certain println command, ....
//Prints out each part of speech for which there is a transition distribution.
//Restated, prints out each part of speech which can play the role of the first part of speech in the bigram.
//The total probability is printed for each of THESE pos. (THESE, as in, the pos which are the first in a pos bigram.) 
 
public class testTransistionDistributions {

   public static void main(String[] args) throws FileNotFoundException {
      TransitionDistributions TObject = new TransitionDistributions();
      Map<String, Map<String, Double>> POSDistsPOS = TObject.get();
      
      for( String prevPos : POSDistsPOS.keySet() ) {
         
         Map<String,Double> givenAPos = POSDistsPOS.get(prevPos);
         
         System.out.println("There are: " + givenAPos.size() + " elements over which probability " +
                              "is distributed when given " + prevPos);
         
         double totalProb = 0;
         for( String pos : givenAPos.keySet() ) {
            
            totalProb = totalProb + givenAPos.get(pos);         
            
         }
         
         //System.out.println(prevPos + ": " + totalProb);
         
         if( totalProb < .995 ) {
            System.out.println("Your total prob for bigrams starting with: " + prevPos + " is less than 1");
            System.out.println("The total prob is " + totalProb);
         }

         if( totalProb > 1.005 ) {
            System.out.println("Your total prob for bigrams starting with: " + prevPos + " is more than 1");
            System.out.println("The total prob is " + totalProb);
         }         
         
                 
      }
      
      
      System.out.println("There are " + POSDistsPOS.size() + " tags.");  
   }   
}