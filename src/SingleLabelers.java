
public class SingleLabelers {

   public NounLabeler nounLabeler = new NounLabeler();
   public VerbLabeler verbLabeler = new VerbLabeler();
   public AdjectiveLabeler adjectiveLabeler = new AdjectiveLabeler();
   
   public SingleLabelers() {
   
   }
   
   public class NounLabeler extends binaryLabeler {
      public NounLabeler(){
         tp = 0;
         fp = 0;
         tn = 0;
         fn = 0;
         label = "/nn";
      }
   }

   public class VerbLabeler extends binaryLabeler {
      public VerbLabeler(){
         tp = 0;
         fp = 0;
         tn = 0;
         fn = 0;
         label = "/vb";
      }
   }
   
   public class AdjectiveLabeler extends binaryLabeler {
      public AdjectiveLabeler(){
         tp = 0;
         fp = 0;
         tn = 0;
         fn = 0;
         label = "/jj";
      }
   }
   
   private class binaryLabeler {
      public String label;
      
      public int tp;
      public int fp;
      public int tn;
      public int fn;
      
      public void update(String tag, String goldTag, String cleanTag, String cleanGoldTag) {
         boolean troo = false;
         boolean positive = false;
         
         //System.out.println("lets label " + label);
         
         if(cleanTag.equals(label)){ // I said its a label
            positive = true;
            //System.out.println("Diagnosis: Positive");
            if(cleanGoldTag.equals(label)){ // I was right! it is a label
               troo = true;
               //System.out.println("True");
            }
         } else { // I said it is not a label
            positive = false;
            //System.out.println("Diagnosis: Negative");
            if(!cleanGoldTag.equals(label)){ // I was right! it is not a label.
               troo = true;
               //System.out.println("True");
            }
         }
         
         if(positive && troo) {
            //System.out.println("True Positive Incremented");
            tp = tp + 1;
         } else if (positive && !troo) {
            
            fp = fp + 1;
         } else if ( !positive && troo ) {
            
            tn = tn + 1;
         } else {
            //System.out.println("False Negative Incremented");
            fn = fn + 1;
 
         }
      }
   
      public void report(){
         double recall = (double)tp/(double)(tp+fn);
         double precision = (double)tp/(double)(tp+fn);
         
         System.out.println("PERFORMANCE FOR " + label);
         System.out.println("The number of true positives is: " + tp);
         System.out.println("The number of false positives is: " + fp);
         System.out.println("The number of true negatives is: " + tn);
         System.out.println("The number of false negatives: " + fn);
         System.out.println("Recall: " + recall);
         System.out.println("Precision: " + precision);
         System.out.println();
      }   
   }
}