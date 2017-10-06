import java.util.*;
import java.io.*;

public class SimpleProduct {
   private TransitionDistributions transDistsOb;
   private AltEmissionsDistributions emisDistsOb;
   private Map<String,Map<String,Double>> transMaps;
   private Map<String,Map<String,Double>> emisMaps;
   private ArrayList<String> allTags;
   
   public SimpleProduct() throws FileNotFoundException {
      TransitionDistributions transDistsOb = new TransitionDistributions();
      AltEmissionsDistributions emisDistsOb = new AltEmissionsDistributions();
              
      transMaps = transDistsOb.get();
      emisMaps = emisDistsOb.get();
      
      allTags = new ArrayList<String>();
      for (String tag : transMaps.keySet()){
         allTags.add(tag);
      }
      
      allTags.add("UNK");
   }
   
   public ArrayList<String> tag(ArrayList<String> sentence) {
   
      /* the possibleTags object is supposed to be 1 element too large */
      ArrayList<Integer> possibleTags = new ArrayList<Integer>();
      int numberOfTags = sentence.size();
      for(int i = 0; i <= numberOfTags; i++) {
         possibleTags.add(1);   
      }
      
      double maxProb = 0;
      ArrayList<Integer> mostLikely = possibleTags;
      while(possibleTags.get(numberOfTags) == 1) {
         double prob;
         double emisProb = calculateEmisProb(possibleTags, sentence);
         double tranProb = calculateTransProb(possibleTags, sentence);
         prob = emisProb*tranProb;
         if(prob > maxProb) {
            maxProb = prob;
            mostLikely = new ArrayList<Integer>(possibleTags);      
         }
         updatePossibleTags(possibleTags, sentence);
      }
      return convert(mostLikely,sentence,allTags);
   }
   
   public void updatePossibleTags(ArrayList<Integer> possibleTags, ArrayList<String> sentence){
      int zerothLabel = possibleTags.get(0);
      zerothLabel = zerothLabel + 1;
      possibleTags.set(0, zerothLabel);
      int numberOfTags = sentence.size();
      int tagKeySetSize = possibleTags.size();
      for(int i = 0; i <= numberOfTags - 1; i++){
         if(possibleTags.get(i) > tagKeySetSize) {
            int temp = possibleTags.get(i+1);
            temp = temp + 1;
            possibleTags.set(i+1,temp);
            possibleTags.set(i,1);
         }
      }
   }
   
   public double calculateEmisProb(ArrayList<Integer> possibleTags, ArrayList<String> sentence) {
   /* The convention is: no punctuation in (sentence handed to language model.)  */
   /*Sensitive issue: Should a period be added the start and end of the sentence? no */
   /*Sensitive issue: unknown words get the uniform distribution  */
      double emisProb = 1; //how does it handle unking
      int numberOfWords = sentence.size();
      
      for(int i = 0; i < numberOfWords; i++) {
         String candidatePOS = allTags.get(possibleTags.get(i)); 
         String word = sentence.get(i);
         double newEmisProb;
         if(emisMaps.get(word) == null) {//unk word
            double sizeOfKeySet = (double) allTags.size();
            newEmisProb = 1/sizeOfKeySet;
         } else {// known word
            if(emisMaps.get(word).get(candidatePOS) != null) {
               newEmisProb = emisMaps.get(word).get(candidatePOS);
            } else { //word and pos don't co-occur. 
               newEmisProb = .00001;
            }
         }
         
         emisProb = newEmisProb*emisProb;
         
      }
      
      return emisProb;
   }
   
   public double calculateTransProb(ArrayList<Integer> possibleTags, ArrayList<String> sentence) {
   /* Sensitive issue: Should transistion to and from end and start of sentence be included: yes ?*/    
      
      //handle possibleTags(0)
      String candidatePOS = allTags.get(possibleTags.get(0));
      double transProb = 1;
      if(transMaps.get("/.").get(candidatePOS) != null) {
         transProb = transMaps.get("/.").get(candidatePOS);
      } else { //this transition just don't happen
         transProb = .00001;
      }
      
      //handle possibleTags(i)
      int numberOfWords = sentence.size();
      for(int i = 1; i < numberOfWords; i++) {
         String candidatePOSi = allTags.get(possibleTags.get(i));
         String precedingCandidatePOS = allTags.get(possibleTags.get(i - 1));
         if(transMaps.get(precedingCandidatePOS).get(candidatePOSi) != null) {
            transProb = transProb*transMaps.get(precedingCandidatePOS).get(candidatePOSi); 
         } else {//this transition just don't happen
            transProb = transProb*.00001;
         } 
      } 
      
      //handle possibleTags(last word)
      String finalCandidatePOS = allTags.get(possibleTags.get(numberOfWords-1));
      if(transMaps.get(finalCandidatePOS).get("./") != null) {
         transProb = transProb*transMaps.get(finalCandidatePOS).get("/.");
      } else {//this transition just don't happen
         transProb = transProb*.00001;
      }
      
      return transProb;
   }
   
   public ArrayList<String> convert(ArrayList<Integer> tags, ArrayList<String> sentence, ArrayList<String> allTags) {
      
      ArrayList<String> finalResult = new ArrayList<String>();
      finalResult.add("/.");
      
      for(int i = 0; i < sentence.size(); i++) {
         finalResult.add(allTags.get(tags.get(i)));
      }
      
      finalResult.add("/.");
      return finalResult;      
   }  
}