import java.util.*;
import java.io.*;

public class RealEmissionsDistributions {

   private Map<String,Map<String, Double>> WordDistsPOS;
   
   public RealEmissionsDistributions() throws FileNotFoundException {
      WordDistsPOS = WordDistsPOSGene();  
   }
   
   public Map<String, Map<String,Double>> get() throws FileNotFoundException {
      return WordDistsPOS;
   }
   
   public static Map<String, Map<String,Double>> WordDistsPOSGene() throws FileNotFoundException {
      
      int TRAIN = 350;
       
      Map<String,Integer> wordPOSCounter = new HashMap<String,Integer>(); //my word/pos counter
      Map<String, Map<String,Double>> distributionsMap = new HashMap<String, Map<String,Double>>(); //the wordMap
      Set<String> posLibrary = new TreeSet<String>(); //my pos library
      
      Scanner scannerIn = new Scanner(System.in); //debug tool
      
      for(int i = 1; i <= TRAIN; i++) {
         File readIn = new File("src\\train_data\\childhood ("+i+").txt");
         Scanner words = new Scanner(readIn);
//         System.out.println("Begin reading file: " + i); //debug tool
         
         
         while(words.hasNext()){
         
            String newWord = words.next();
            newWord = newWord.toLowerCase();
                    
            //build pos librarycc
            int firstIn = newWord.lastIndexOf("/");
            String tag = newWord.substring(firstIn);
            String word = newWord.substring(0,firstIn);
            posLibrary.add(tag);
            
//            System.out.println("The tag is: " + tag); //debug tool
//            System.out.println("The word is: " + word); //debug tool
            
            //Store count of each word/pos
            if(!wordPOSCounter.containsKey(newWord)) {
               wordPOSCounter.put(newWord, 1);
            } else {
               int value = wordPOSCounter.get(newWord);
               value = value + 1;
               wordPOSCounter.put(newWord,value);
            }
         
            //build the distributionsMap
            Map<String,Double> distributionOverWords;
            if(!distributionsMap.containsKey(tag)) {
//               System.out.println("The distributions map does not contain a map for this tag, creating one"); //debug tool
               distributionOverWords = new HashMap<String, Double>();
               distributionOverWords.put("count",1.0);
            } else {
//               System.out.println("The distributions map contains a map for this tag"); //debug tool 
               distributionOverWords = distributionsMap.get(tag);         
               double numb = distributionOverWords.get("count");
               numb = numb + 1;
               distributionOverWords.put("count", numb);
            } 
                       
            if(distributionOverWords.containsKey(word)){
               double numb = distributionOverWords.get(word);
               numb = numb + 1;
               distributionOverWords.put(word, numb);
            } else {
               distributionOverWords.put(word,1.0);
            }
            
//            System.out.println(); //debug tool
//            System.out.println("Examine a single distribution."); // debug tool
//            Set<String> distributionOverWordsKeySet = distributionOverWords.keySet(); //debug tool
//            for(String wordInDist : distributionOverWordsKeySet) { //debug tool
//               double totalAppearances = distributionOverWords.get(wordInDist); //debug tool
//               System.out.println(wordInDist + ": " + totalAppearances);//debug tool
//            }//debug tool
            
            distributionsMap.put(tag, distributionOverWords);
            
//            System.out.println(); //debug tool
//            System.out.println("Now examine the distributions map");//debug tool
//            Set<String> distributionsMapKeySet = distributionsMap.keySet(); //debug tool
//            for(String pos : distributionsMapKeySet) { //debug tool
//               double totalAppearances = distributionsMap.get(pos).get("count"); //debug tool
//               System.out.println("The total number of appearances for: " + pos + " part of speech is: " + totalAppearances);//debug tool
//            } //debug tool
                 
//            System.out.println("Advance?"); //debug tool
//            String discard = scannerIn.nextLine(); //debug tool        
         }
//         System.out.println("File Read is Completed"); //debug tool
//         System.out.println("Advance?"); //debug tool
//         String discard = scannerIn.nextLine(); //debug tool
      }
      
      //now build all the posDistribution within the word map. 
      Set<String> keyset = distributionsMap.keySet();
      
      for(String posp : keyset ) {
         Map<String, Double> distributionOverWords = distributionsMap.get(posp);
//         System.out.println("We have acquired the map for: " + posp);//debug tool
         double totalOccur = distributionOverWords.get("count"); 
//         System.out.println("The total number occurrances for this map is: " + totalOccur); //debug tool 
         Set<String> distributionOverWordsKeySet = distributionOverWords.keySet();
          
         for(String werd : distributionOverWordsKeySet ) {
            if(!werd.equals("count")) {
               double tally = distributionOverWords.get(werd);
               double prob = tally/totalOccur;
               //System.out.println(" The word " + werd + " occurs " + tally + " times.");
               //System.out.println(" The word " + werd + " occurs with probability " + prob);
               distributionOverWords.put(werd,prob);  
               //System.out.println(" Advance to Next word"); //debug tool
               //String discard = scannerIn.nextLine(); //debug tool 
            }
         }
         
//         System.out.println("Advance to Next Distribution?"); //debug tool
//         String discard = scannerIn.nextLine(); //debug tool         
         
         distributionsMap.put(posp,distributionOverWords);              
      }  
      return distributionsMap;
   }
}