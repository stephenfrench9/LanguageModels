import java.io.*;
import java.util.*;

//This class runs a script which tests the MatrixGenerator Object. 


public class TestMatrixGenerator {

   public static void main(String[] args) throws Exception {
      
      //make a matrix generator object
      TransitionDistributions lee = new TransitionDistributions();
      AltEmissionsDistributions tommy = new AltEmissionsDistributions();
      Map<String,Map<String,Double>> EDists = tommy.POSDistsWordGene();
      Map<String,Map<String,Double>> TDists = lee.buildPrecedingPOSMap();
      MatrixGenerator emptyMatrix = new MatrixGenerator(TDists,EDists);
      
      //check the total number of pos. 
      ArrayList<String> indexArray = emptyMatrix.getIndexArray();
      System.out.println("There are " + indexArray.size() + " pos in the empty matrix.");
      
      //test on an antecdote
      ArrayList<String> test = new ArrayList<String>();
      test.add("You");
      test.add("will");
      test.add("permit");
      test.add("it");
      //test.add("parking");
      //test.add("permit");
      //test.add("see");
      //test.add("you"); //Note: Main feeds untagged words with no punctuation into the hmm.
       
      

      
      //IDEA: modify matrix generator to return intermediate forms of the posScoreMatrix and the backTraceMatrix 
      
      //double[][] posScoreMatrix = emptyMatrix.tag(test);
      
                  /* ---- OR ----- */
                  
      ArrayList<String> tags = emptyMatrix.tag(test);

      

      
      ArrayList<String> realFinalAnswer = new ArrayList<String>();
      for(int podium = 0; podium < test.size(); podium++) {
         String element = test.get(podium) + tags.get(podium);
         realFinalAnswer.add(element);   
      }
      
      for(int j = 0; j < tags.size(); j++) {
         System.out.println(realFinalAnswer.get(j));
      }
      
       /*---------------OR-------------------*/           
      /*
      System.out.println("The number of rows is: " + posScoreMatrix.length);
      System.out.println("The length of the first row is: " + posScoreMatrix[1].length);
      
      for(int row = 0; row < posScoreMatrix.length; row++) {
         System.out.println();
         for(int col = 0; col < posScoreMatrix[1].length; col++) {
            System.out.printf("%14.8f" ,posScoreMatrix[row][col]);      
         }
      }
      */
      /* ----- OR ------- */
      /*
      System.out.println("The number of rows is: " + backTraceMatrix.length);
      System.out.println("The length of the first row is: " + backTraceMatrix[1].length);
      
      for(int row = 0; row < backTraceMatrix.length; row++) {
         System.out.println();
         for(int col = 0; col < backTraceMatrix[1].length; col++) {
            System.out.printf("%5d", backTraceMatrix[row][col]);      
         }
      }
      */    
   }   
}