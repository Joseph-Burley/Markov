import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;
public class Markov
{
   private static class Bucket
   {
      String word;
      int mult=1;
      
      Bucket(String s)
      {
         word = s;
      }
   }
   
   public static void main(String[] args)
   {
   
      
      ArrayList<Bucket>[] chain = new ArrayList[1000];
      for(int i=0; i<chain.length; i++)
      {
         chain[i] = new ArrayList<Bucket>();
      }
      //FileInputStream f = new FileInputStream("constitution.txt");
      Scanner Con = null;
      try
      {
         Con = new Scanner(new File("constitution.txt"));
      }
      catch(Exception e){
         System.out.println(e);
      }
      String s = "";
      int code = 0;
      
      //build chain
      while(Con.hasNext())
      {
         s = Con.next().trim().toLowerCase(); //remove leading and trailing whitespace
         if(s.charAt(s.length()-1) == ',' || s.charAt(s.length()-1) == '.')
            s = s.substring(0, s.length()-1);
         code = Math.abs(s.hashCode());
         try
         {
            
            int i = code % chain.length;
            int index = -1;
            if(!s.equals("")) 
               for(int j=0; j< chain[i].size(); j++)
                  if(chain[i].get(j).word.equals(s))
                     index = j;
            if(index > -1)
               chain[i].get(index).mult++;
            else
               chain[i].add(new Bucket(s));
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
      }
      
      //show chain
      System.out.println("-------Chain data-------");
      Bucket b=chain[0].get(0);
      int c = 0;
      for(int i=0; i<chain.length; i++)
      {
         if(chain[i].size() > 0)
         {
            System.out.println("Bucket " + i+"------");
            for(int j=0; j<chain[i].size(); j++)
            {
               System.out.print(chain[i].get(j).word + " (X"+ chain[i].get(j).mult + "), ");
               
               if(b.mult < chain[i].get(j).mult)
               {
                  b = chain[i].get(j);
                  c = i;
               }
            }
            System.out.println();
         }
         
      }
      
      System.out.println("The most used word is: " + b.word + " (" + b.mult + " bucket: " + c + ")");
      
      
   }
}