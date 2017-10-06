import java.io.*;
import java.util.*;

public class mainScript {

   public static void main(String[] args) throws FileNotFoundException, Exception {
      int LAST = 403;
      int FIRST = 351;
      
      //implement a language model.
      /* Choose from RealHmm, HmmDeriv, BaselineModel, or SimpleProduct */
      
      
      //BaselineModel languageModel = new BaselineModel();
      RealHmm languageModel = new RealHmm();
      //HmmDeriv languageModel = new HmmDeriv();
      //SimpleProduct languageModel = new SimpleProduct();
      
      
      
      /* *************  */
      
      // track performance
      double correct=0;
      double questions = 0;
      double answers = 0;
      SingleLabelers indivLab = new SingleLabelers();
      
      //read and tag files, one at a time
      int filesProcessed = 0;
      for(int i = FIRST; i <= LAST; i++ ) {
         File inFile = new File("src\\test_data\\childhood (" + i + ").txt");
         Scanner read = new Scanner(inFile);
         
         boolean go = true;
         while(read.hasNext() && go) {
            //read and tag a sentence
            ArrayList<String> sentence = getSentence(read);
            ArrayList<String> goldTags = process(sentence); //remove tags from sentence.
            ArrayList<String> tags = languageModel.tag(sentence);
            
            go = true;
            goldTags.add("/.");
            goldTags.add(0,"/.");  
            //update aggregate performance statistics  
            questions = questions + goldTags.size()-2;
            answers = answers + tags.size()-2;
            for(int podium = 1; podium < sentence.size() - 1; podium++) {
               //look at a single tag
               String goldTag = goldTags.get(podium);
               String tag = tags.get(podium);
               String cleanGoldTag = goldTag.replace("-hl","").replace("-nc","")
                  .replace("fw-","").replace("-tl","");
               String cleanTag = tag.replace("-hl","").replace("-nc","")
                  .replace("fw-","").replace("-tl","");
               
               if(goldTag.equals(tag)) {// update aggregate performance statistics
                  correct = correct + 1;
               }
               //update performance statistics for individual labels
               indivLab.nounLabeler.update(tag, goldTag, cleanGoldTag, cleanTag);
               indivLab.verbLabeler.update(tag, goldTag, cleanGoldTag, cleanTag);
               indivLab.adjectiveLabeler.update(tag, goldTag, cleanGoldTag, cleanTag);
            }// word
         }//sentence
         filesProcessed = filesProcessed + 1;
         System.out.println(filesProcessed + " Files Processed.");
         reportAggregateAccuracy(correct,questions);
         indivLab.nounLabeler.report();
         indivLab.verbLabeler.report();
         indivLab.adjectiveLabeler.report();   
         System.out.println();
      }//file
   }

   public static ArrayList<String> getSentence(Scanner read) {   
      ArrayList<String> newSent = new ArrayList<String>();
      boolean continueLoop = true;
      //read in a sentence, one word at a time
      while (read.hasNext() && continueLoop){     
         String newStr = read.next();
         if(!newStr.equals("./.")){
            newSent.add(newStr);
         }else{
            continueLoop = false;
         }
      }
      return newSent;
   }
   
   public static ArrayList<String> process(ArrayList<String> sentence) {
      ArrayList<String> goldTags = new ArrayList<String>();
         for (int i = 0; i < sentence.size(); i++){
            int splitter = sentence.get(i).lastIndexOf("/");
            String tag = sentence.get(i).substring(splitter);
            String word = sentence.get(i).substring(0,splitter);
            sentence.set(i, word);  // Puts untagged word back into sentence
            goldTags.add(tag);  // Puts tag into goldTags
         }
      return goldTags;
   }
   
   public static void reportAggregateAccuracy(double correct, double questions){
      double aggregateA = correct/questions;
      System.out.println("Aggregate Accuracy: " + aggregateA);
      System.out.println();
   }   
}
