import java.util.*;
import java.io.*;

public class RealHmm {
   private static RealMatrixGenerator emptyHmmMatrix;
   private ArrayList<String> allTags;

   public RealHmm() throws FileNotFoundException {
   
      TransitionDistributions DistsPOSPOS = new TransitionDistributions();
            //read DistsPOSPOS as "Distributions over POS given a POS"
      RealEmissionsDistributions DistsWordPOS = new RealEmissionsDistributions();
            //read DistsWordPOS as "Distributions over Words given a POS"  **added 9.28.2017** I think this is not right.  
   
      Map<String, Map<String,Double>> tranMaps = DistsPOSPOS.get();
      Map<String, Map<String,Double>> emisMaps = DistsWordPOS.get();
      
      emptyHmmMatrix = new RealMatrixGenerator(tranMaps, emisMaps);
      
      allTags = new ArrayList<String>();
      for (String tag : tranMaps.keySet()){
         allTags.add(tag);
      }
      
      allTags.add("UNK");
      
   }
   

   // Returns all POS tags. Important for confusion matrix interpretation.
   public ArrayList<String> enumerateAllTags(){
      
      return allTags;
      
   }
   
   public static ArrayList<String> tag(ArrayList<String> sentence) throws Exception {
      
      //the tag method in MatrixGenerator object is non-static
      //but, hte tag method in hmm is not static. It is called from main. But that is ok. 
      //so all methods of the objects must be static
      ArrayList<String> tags = emptyHmmMatrix.tag(sentence); 
      return tags;
      
   }
   
}