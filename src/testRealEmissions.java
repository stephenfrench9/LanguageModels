import java.util.*;
import java.io.*;

//This class runs a script. The script tests the emissions distributions.
//Each word has its own emission distribution. 
// 1) Each emission distribution is shown to be normalized by this script. 

// 2) Unking: When an unknown word is fed to the emission distribution, A pointer-to-null is returned. 

// Note: many emissions distributions contain only a few parts of speech in their keyset.
// Note: There is no emission distribution for "unk".
public class testRealEmissions {
   
   public static void main(String[] args) throws FileNotFoundException {
   
      RealEmissionsDistributions mapsOb = new RealEmissionsDistributions();
      
      Map<String,Map<String,Double>> allMaps = mapsOb.get();      
   
      System.out.println("There are this many POS: " + allMaps.keySet().size());
            
      
      for(String pos : allMaps.keySet()) {
         
         Map<String, Double> mapForPOS = allMaps.get(pos);
         double totalProb = 0;
         
         for( String word : mapForPOS.keySet() ) {
            if( !word.equals("count") )
               totalProb = mapForPOS.get(word) + totalProb;   
         }
         
/*1*/    System.out.println("For the " + pos + " map, the total probability is: " + totalProb);
                    
      }
      
      //How does the emissionsMaps object handle unkown words? 
      Map<String,Double> unkMap = allMaps.get("thrunagood");
      
      if( unkMap == null )
/*2)*/   System.out.println("upon recieving a key for which there is no value, the maps object returns null");
           
      
   }
}