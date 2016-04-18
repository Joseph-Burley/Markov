import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;
public class Markov
{
   private static class Word
   {
      String data;
      int mult=1;
      
      private ArrayList links = new ArrayList<String>();
      private ArrayList<Integer> weights = new ArrayList<Integer>();
      private ArrayList<Integer> CDF = new ArrayList<Integer>();
      private boolean CDFValid;
      
      Word(String s)
      {
         data = s;
      }
      
      public void addLink(String s)
      {
         int index = links.indexOf(s);
         if(index > -1)
         {
            weights.set(index, Integer.valueOf((weights.get(index))+1));
         }
         else
         {
            links.add(s);
            weights.add(1);
         }
         
         CDFValid = false;
      }
      
      public ArrayList<Integer> getCDF()
      {
         ArrayList<Integer> ret = new ArrayList<Integer>();
         for(int i=0; i<CDF.size(); i++)
         {
            ret.add(CDF.get(i));
         }
         return ret;
      }
      public void generateCDF()
      {
         CDF.clear();
         int current = 0;
         for(int i=0; i<weights.size(); i++)
         {
            current += weights.get(i);
            CDF.add(current);
         }
         
         CDFValid = true;
      }
      
      public void printLinks()
      {
         System.out.println("Links for \"" + data +"\"");
         for(int i=0; i<links.size(); i++)
         {
            System.out.println(links.get(i) + ": " + weights.get(i));
         }
      }
   }
   
   
   
   public static void main(String[] args)
   {
   
      
      ArrayList<Word>[] chain = new ArrayList[1000];
      for(int i=0; i<chain.length; i++)
      {
         chain[i] = new ArrayList<Word>();
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
      String p = "";
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
            
            int i = code % chain.length; //i is between 0 and 999
            Word index = null; //index stores position in bucket. -1 indicates not found
            if(!s.equals("")) 
               for(int j=0; j< chain[i].size(); j++) //search for the entry in the bucket
                  if(chain[i].get(j).data.equals(s))
                     index = chain[i].get(j);
            if(index != null) //if word exists
            {
               index.mult++;
               
            }
            else
               chain[i].add(new Word(s));
               
            if(p != null)
               p.addLink(index); //problem. p is a string, not a word
            p = s; //previous string is assigned the current string
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
      }
      
      //show chain
      System.out.println("-------Chain data-------");
      Word b=chain[0].get(0);
      int c = 0;
      for(int i=0; i<chain.length; i++)
      {
         if(chain[i].size() > 0)
         {
            System.out.println("Bucket " + i+"------");
            for(int j=0; j<chain[i].size(); j++)
            {
               System.out.print(chain[i].get(j).data + " (X"+ chain[i].get(j).mult + "), ");
               
               if(b.mult < chain[i].get(j).mult)
               {
                  b = chain[i].get(j);
                  c = i;
               }
            }
            System.out.println();
         }
         
      }
      
      System.out.println("The most used data is: \"" + b.data + "\" (" + b.mult + " times in bucket: " + c + ")");
      
      b.printLinks();
      
      
   }
}