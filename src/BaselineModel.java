import java.util.*;
import java.io.*;

public class BaselineModel {

   AltEmissionsDistributions emisDistOb;
   Map<String, Map<String, Double>> allMaps;


   public BaselineModel() throws FileNotFoundException{
      emisDistOb = new AltEmissionsDistributions();
      allMaps = emisDistOb.get();
   }
   
   //provides part of speech labels for a sentence.
   //Input: sentence includes no punctuation
   //Output: the models best guess for parts of speech is returned. Punctuation is included.
   public ArrayList<String> tag(ArrayList<String> sentence) {   
      ArrayList<String> tags = new ArrayList<String>();
      tags.add("/.");
      for(int z = 0; z < sentence.size(); z++) {
         String tag; //model's tag
         String word = sentence.get(z).toLowerCase();//word to be tagged
         if(allMaps.containsKey(word)){//known word
            Map<String, Double> posDistribution = allMaps.get(word);
            Set<String> posDistKeySet = posDistribution.keySet();
            double maxProb = 0;
            String maxLabel = "/.";
            for(String label : posDistKeySet) {//find most likely label
               if(label != "count") {
                  double prob = posDistribution.get(label);
                  if(prob > maxProb){
                     maxProb = prob;
                     maxLabel = label;
                  }
               }
            }
            tag = maxLabel;
         } else {//unk word
            tag = "/nn";
         }
         tags.add(tag);
      } //sentence    
      tags.add("/.");
      return tags;
   }
}