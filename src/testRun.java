import java.util.*;
import java.io.*;

public class testRun {

   public static void main(String[] args) throws FileNotFoundException, Exception {
   
      String u = "Begin Test Script";
      System.out.println(u);
   
      AltEmissionsDistributions edistsOb = new AltEmissionsDistributions();
      Map<String, Map<String,Double>> allMaps = edistsOb.get();

      RealHmm languageModel = new RealHmm();
//      ArrayList<String> horses = languageModel.enumerateAllTags();
//      System.out.println(horses.toString());
//      System.out.println(horses.toString().replace("-hl","").replace("-nc","").replace("fw-","").replace("-tl",""));
     
      ArrayList<String> sentenceExa = new ArrayList<String>();
      
      sentenceExa.add("The");
      sentenceExa.add("man");
      sentenceExa.add("quickly");
      sentenceExa.add("drinks");
      sentenceExa.add("my");
      sentenceExa.add("beer");
      
      
      ArrayList<String> tags = languageModel.tag(sentenceExa);
      
      System.out.println(sentenceExa.toString());
      System.out.println(tags.toString());
      
      String v = "Conclude Test Script";
      System.out.println(v);
   }
   
   public static void printKeySetComplexMap( Map<String,Map<String,Double>> anyOld ) {
   
      Set<String> keySet = anyOld.keySet();
   
      for(String key : keySet) {
         System.out.println(key);
      }
   
   }
   
   public static void printKeySetSimpleMap( Map<String,Double> anyOld ) {
   
      if( anyOld == null ){
         System.out.println("There is no map for this word.");
         
      }
   
      Set<String> keySet = anyOld.keySet();
   
      for(String pos : keySet) {
         System.out.print(pos + ": ");
         System.out.println(anyOld.get(pos));
      }  
   }   
}