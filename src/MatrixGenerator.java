import java.util.*;

public class MatrixGenerator{
   private Map<String, Map<String, Double>> tranMaps;
   private Map<String, Map<String, Double>> emisMaps;
   private ArrayList<String> indexArray; //maps POS to an index

   //Constructor: initialize tranMaps and emisMapss as well as the indexArray
   public MatrixGenerator(Map<String, Map<String, Double>> transitions,
                 Map<String, Map<String, Double>> emissions){
      this.tranMaps = transitions;
      this.emisMaps = emissions;
      this.indexArray = new ArrayList<String>(); 
   
      for (String s : tranMaps.keySet()){ // Initializes the indexArray
         this.indexArray.add(s);
      }     
   }
   
   //build the matrix. produce the tags. 
   public ArrayList<String> tag(ArrayList<String> sentence) throws Exception {
      sentence.add(0, "."); //now there is a single period at the end.    
      sentence.add(".");
      double[][] posScoreMatrix = new double[indexArray.size()][sentence.size()];
      int[][] backTraceMatrix = new int[indexArray.size()][sentence.size()];
      
      fillTwoMatrices(sentence, posScoreMatrix, backTraceMatrix);
      //begin backTrace.
      double max = 0;
      int indexForMax = -1;
      for(int row = 0; row < indexArray.size(); row++) {
         if(max < posScoreMatrix[row][sentence.size() - 1]) {
            max = posScoreMatrix[row][sentence.size() - 1];
            indexForMax = row;
         } 
      }
      if(indexForMax == -1) {
         throw new Exception();
      }    
      
      int[] tags = new int[sentence.size()];
      int fillPosition = sentence.size() - 1;
      
      fillTagArray(fillPosition, indexForMax, backTraceMatrix, tags); //backTrace
      ArrayList<String> finalAnswer = new ArrayList<String>();//translate tags
      for(int j = 0; j < tags.length; j++) {
         finalAnswer.add(indexArray.get(tags[j]));
      }
      return finalAnswer;
   }
   
   public void fillTwoMatrices(ArrayList<String> sentence, 
            double[][] posScoreMatrix, int[][] backTraceMatrix) throws Exception {
      
      for (int col = 0; col < posScoreMatrix[0].length; col++){
         double maxInCol = 0;
         for (int row = 0; row < indexArray.size(); row++){
            if (col == 0){
               /*--get the emis prob--*/
               double emisProb;
               if(emisMaps.containsKey(sentence.get(col))) {
                  if( emisMaps.get(sentence.get(col)).containsKey(indexArray.get(row))){ 
                     //the word recognizes the given part of speech
                     emisProb = emisMaps.get(sentence.get(col)).get(indexArray.get(row)); 
                  } else { //the word doesn't recognize this part of speech
                     emisProb = .0000001;
                  }                
               } else { //the word is not recognized
                  emisProb = 1/362.0;
               }
               
               posScoreMatrix[row][col] = emisProb;
               backTraceMatrix[row][col] = 1;
               maxInCol = 1.0;
            } else if (col == 1006) { 
               /*--get the trans prob--*/               
               double transProb;
               if( tranMaps.get("/.").containsKey(indexArray.get(row))) {
                  transProb = tranMaps.get("/.").get(indexArray.get(row));
               } else { //there are a lot of things that /. just does not transition to
                  transProb = .0000001;
               }
               /*--get the emis prob--*/
               double emisProb;
               if(emisMaps.containsKey(sentence.get(col))) {
                  if( emisMaps.get(sentence.get(col)).containsKey(indexArray.get(row))){ 
                     //the word recognizes the given part of speech
                     emisProb = emisMaps.get(sentence.get(col)).get(indexArray.get(row)); 
                  } else { //the word doesn't recognize this part of speech
                     emisProb = .0000001;
                  }                
               } else { //the word is not recognized
                  emisProb = 1/362.0;
               }
               /*---Now we have the transition and emission probs---*/
               posScoreMatrix[row][col] = emisProb*transProb;
               if( posScoreMatrix[row][col] > maxInCol )
                  maxInCol = posScoreMatrix[row][col];
               backTraceMatrix[row][col] = 1;
            } else {   
               /*Find the maxTransProb to [row][col]*/ //Note: "transProb" is cumulative.  
               double maxTransProb = 0;
               int rowOfMaxPrev = 0;
               for( int prevRow = 0; prevRow < indexArray.size(); prevRow++ ) { //select largest transProb
                  double transProb = 0;
                  if( tranMaps.containsKey(indexArray.get(prevRow))) {// get the map
                     Map<String,Double> tranMap = tranMaps.get(indexArray.get(prevRow));
                     if(tranMap.containsKey(indexArray.get(row))) {
                        transProb = tranMap.get(indexArray.get(row));               
                     } else { //Each map typically only contains a strict subset of the keys.
                        transProb = .0000001;
                     }                                       
                  } else { //we didn't even find a map for the previous pos. 
                     throw new Exception();
                  }
                  transProb = transProb*posScoreMatrix[prevRow][col-1];
                  if(maxTransProb < transProb){
                     maxTransProb = transProb; 
                     rowOfMaxPrev = prevRow;
                  }
               }
               /*--get the emis prob--*/
               double emisProb;
               if(emisMaps.containsKey(sentence.get(col))) {
                  if( emisMaps.get(sentence.get(col)).containsKey(indexArray.get(row))){ 
                     //the word recognizes the given part of speech
                     emisProb = emisMaps.get(sentence.get(col)).get(indexArray.get(row)); 
                  } else { //the word doesn't recognize this part of speech
                     emisProb = .0000001;
                  }                
               } else { //the word is not recognized
                  emisProb = 1/362.0;
               }
               /*Now we have the transition and emission probs*/
               posScoreMatrix[row][col] = maxTransProb*emisProb;
               if( posScoreMatrix[row][col] > maxInCol )
                  maxInCol = posScoreMatrix[row][col];
               backTraceMatrix[row][col] = rowOfMaxPrev; 
            }   
         }
         for(int row = 0; row < indexArray.size(); row++) {
            posScoreMatrix[row][col] = posScoreMatrix[row][col]/maxInCol;   
         }     
      }
   }
   
   public static void fillTagArray(int fillPosition, int posIndex, //read as "part of speech index"
                                   int[][] backTraceMatrix ,int[] tags) {
      if(fillPosition > -1) {
    	 //posIndex is the part of speech for the word indicated by fillPosition.
    	 tags[fillPosition] = posIndex; 
         int prevPos = backTraceMatrix[posIndex][fillPosition];
         fillTagArray(fillPosition-1, prevPos, backTraceMatrix, tags);
      }
   }
   
   public ArrayList<String> getIndexArray() {
      return indexArray;
   }
}