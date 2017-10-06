import java.util.*;
import java.io.*;

public class AltEmissionsDistributions {

   private Map<String,Map<String, Double>> POSDistsWord;
   
   public AltEmissionsDistributions() throws FileNotFoundException {
      POSDistsWord = POSDistsWordGene();  
   }
   
   public Map<String, Map<String,Double>> get() throws FileNotFoundException {
      return POSDistsWord;
   }
   
   public static Map<String, Map<String,Double>> POSDistsWordGene() throws FileNotFoundException { 
      int TRAIN = 350;
       
      Map<String,Integer> wordPOSCounter = new HashMap<String,Integer>(); 
      Map<String, Map<String,Double>> wordMap = new HashMap<String, Map<String,Double>>(); 
      Set<String> posLibrary = new TreeSet<String>(); //my pos library
      
      for(int i = 1; i <= TRAIN; i++) {
         File readIn = new File("src\\train_data\\childhood ("+i+").txt");
         Scanner words = new Scanner(readIn);
         
         while(words.hasNext()){
            String newWord = words.next();
            newWord = newWord.toLowerCase();
                 
            //build pos librarycc
            int firstIn = newWord.lastIndexOf("/");
            String tag = newWord.substring(firstIn);
            String word = newWord.substring(0,firstIn);
            posLibrary.add(tag);

            //Store count of each word/pos
            if(!wordPOSCounter.containsKey(newWord)) {
               wordPOSCounter.put(newWord, 1);
            } else {
               int value = wordPOSCounter.get(newWord);
               value = value + 1;
               wordPOSCounter.put(newWord,value);
            }
         
            //build the wordMap
            if( !wordMap.containsKey(word) ) {
               Map<String,Double> posDistribution = new HashMap<String, Double>();
               posDistribution.put("count",1.0);
               wordMap.put(word, posDistribution);
            } else {
               Map<String, Double> posDistribution = wordMap.get(word);//get posDistribution 
            
               Double numb = posDistribution.get("count");// modify posDistribution
               numb = numb + 1;// modify posDistribution
               posDistribution.put("count", numb);// modify posDistribution
            
               wordMap.put(word, posDistribution);// replace posDistribution
            }     
         }
         words.close();
      }
      
      //now build all the posDistribution within the word map. 
      Set<String> keyset = wordMap.keySet();
      for(String werd : keyset ) {   
         Map<String, Double> posDistribution = wordMap.get(werd);
         double totalOccur = posDistribution.get("count");  
         for(String ending : posLibrary ) {
            String candidate = werd + ending;
            if( wordPOSCounter.containsKey(candidate) ) {
               int wordPOSCount = wordPOSCounter.get(candidate);
               double wordPOSProb = wordPOSCount/totalOccur;
               posDistribution.put(ending,wordPOSProb);  //build the posDistribution
            }
         }  
         wordMap.put(werd,posDistribution); //store the posDistribution in the word map             
      }    
      return wordMap;
   }
}