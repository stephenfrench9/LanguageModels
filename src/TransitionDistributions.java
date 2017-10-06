//This object holds many distributions.
//The distributions are over pos.
//The distributions are conditioned on a previous pos.
//POSDistsPOS is a map containing maps. The key values for the outer map are 
//the first member of POS bigrams. The key values for the inner maps are the 
//second members of POS bigrams, as well as a count of the total number of occurrences
//of the first member. 

import java.util.*;
import java.io.*;

public class TransitionDistributions {

   private Map<String, Map<String,Double>> POSDistsPOS;//read as "POS distributions given a POS"
   
   //constructor
   public TransitionDistributions() throws FileNotFoundException {
      POSDistsPOS = buildPrecedingPOSMap();
   }
   
   //getter. Returns a collection of distributions over pos. 
   public Map<String,Map<String,Double>> get() throws FileNotFoundException {   
      return POSDistsPOS;       
   }
   
   //'prevTag' and 'Tag' form a part-of-speech bigram. 'firstTagInBigramMap' has the first tag
   //of bigrams as its keys, and a map as its values. An inner map is often referred to as 
   //'secondTagsMap'. The inner map has as its keys the second part of speech tag in the bigram,
   //as well as a running count of the total number of times the first part of the
   //bigram has occurred. 
   //Post: firstTagInBigramMap has keys for the first partner of all bigrams, and its values
   //are maps, each of which contains all possible second partners.   
   public Map<String,Map<String,Double>> buildPrecedingPOSMap() throws FileNotFoundException {
      int TRAIN = 350;
      
      Map<String, Map<String,Double>> firstTagInBigramMap = 
                                       new HashMap<String, Map<String,Double>>();
      Map<String, Double> secondTags = new HashMap<String, Double>();
      secondTags.put("firstTagCount", 1.0);
      firstTagInBigramMap.put("/." , secondTags );
      
      for(int i = 1; i <= TRAIN; i++) {
         
         File readIn = new File("src\\train_data\\childhood ("+i+").txt");
         Scanner words = new Scanner(readIn);
         String prevTag = "/.";
         
         while(words.hasNext()) {
            
            String newWord = words.next();
            int firstIn = newWord.lastIndexOf("/");
            String tag = newWord.substring(firstIn);
            
            if( !firstTagInBigramMap.containsKey(prevTag) ) {         
               createNewSecondTagsMap(firstTagInBigramMap, prevTag);
            } else {
               incrementUnigramCount(firstTagInBigramMap, prevTag);      
            }
            
            incrementBigramCount(firstTagInBigramMap, prevTag, tag);
            prevTag = tag; //update previous tag to tag
            
         }//word
         words.close();
      }//file
      convertCountToProbability(firstTagInBigramMap);                      
      return firstTagInBigramMap; 
   }
   
   //There is counter tells you the number of times a bigram starting with 
   //a particular tag has occurred. This counter is referred to as 'firstTagCount' and 
   //is stored in a 'secondTags' map. 
   //Post: The counter inside an inner map of firstTagInBigramMap is incremented.  
   private void incrementUnigramCount(Map<String, Map<String,Double>> firstTagInBigramMap,
                                          String prevTag) {
      Map<String, Double> secondTags = firstTagInBigramMap.get(prevTag);
      double firstTagCount = secondTags.get("firstTagCount");
      firstTagCount = firstTagCount + 1;
      secondTags.put("firstTagCount", firstTagCount);
      firstTagInBigramMap.put(prevTag, secondTags);// I don't think you need this line                                          
   }
   
   //This method is called when a tag is encountered for the first time playing the role of
   //first tag in a pos bigram. A new inner map to hold all the possible second tags that will
   //form bigrams with the first tag is created. The first tag is used as a key and the new 
   //map is used as the value, and they are stored in the firstTagInBigramMap. 
   //Post: firstTagInBigramMap has been modified to contain a new set of bigrams. 
   private void createNewSecondTagsMap(Map<String, Map<String,Double>> firstTagInBigramMap,
                                          String prevTag) {
      Map<String, Double> secondTags = new HashMap<String, Double>();
      double firstTagCount = 1;
      secondTags.put("firstTagCount", firstTagCount);
      firstTagInBigramMap.put(prevTag,secondTags);                                     
   }
   
   //increment a bigram count for a bigram consisting of 'prevTag' and 'tag'
   //Post: firstTagInBigramMap has been modified so that one of its bigrams has a 
   //count that is one larger. 
   private void incrementBigramCount(Map<String, Map<String,Double>> firstTagInBigramMap,
                                       String prevTag, String tag) { 
      Map<String, Double> secondTags = firstTagInBigramMap.get(prevTag);
      if( secondTags.containsKey(tag) ) { //tags is the second tag in the bigram here
         double occurrancesBigram = secondTags.get(tag);
         occurrancesBigram = occurrancesBigram + 1;
         secondTags.put(tag, occurrancesBigram);
      } else {
         secondTags.put(tag, 1.0);
      }
      firstTagInBigramMap.put(prevTag, secondTags);
   }
   
   //Modify all maps inside firstTagInBigramMap. Convert count values in the
   //inner maps to probabilities.
   //Post: firstTagInBigramMap has been modified so that all of its bigrams now have a count 
   //associated with them rather than a probability. 
   private void convertCountToProbability(Map<String, Map<String,Double>> firstTagInBigramMap) {  
      Set<String> firstTagLibrary = firstTagInBigramMap.keySet();
      for(String firstTag : firstTagLibrary ) {
         Map<String, Double> secondTags = firstTagInBigramMap.get(firstTag);
         Set<String> secondTagLibrary = secondTags.keySet();
         double unigramCount = secondTags.get("firstTagCount");
         secondTags.remove("firstTagCount");
         for(String secondTag : secondTagLibrary ) { 
            double prob = secondTags.get(secondTag)/unigramCount;
            secondTags.put(secondTag,prob);
         }
      }  
   }
}