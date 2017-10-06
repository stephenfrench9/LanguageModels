import java.util.*;
import java.io.*;

public class HmmDeriv {
   private static MatrixGenerator emptyHmmMatrix;
   private ArrayList<String> allTags;

   public HmmDeriv() throws FileNotFoundException {
      TransitionDistributions DistsPOSPOS = new TransitionDistributions();
      AltEmissionsDistributions DistsWordPOS = new AltEmissionsDistributions(); 
      Map<String, Map<String,Double>> tranMaps = DistsPOSPOS.get();
      Map<String, Map<String,Double>> emisMaps = DistsWordPOS.get();
      
      emptyHmmMatrix = new MatrixGenerator(tranMaps, emisMaps);  
      allTags = new ArrayList<String>();
      for (String tag : tranMaps.keySet()){
         allTags.add(tag);
      }
      allTags.add("UNK");
   }
   
    // Returns all POS tags.
   public ArrayList<String> enumerateAllTags(){
      return allTags;
   }
   
   public ArrayList<String> tag(ArrayList<String> sentence) throws Exception {
      ArrayList<String> tags = emptyHmmMatrix.tag(sentence); 
      return tags;
   }
}