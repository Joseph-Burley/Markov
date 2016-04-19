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
   
   
   public static ArrayList<Word>[] chain = new ArrayList[1000];
   public static void main(String[] args)
   {
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
      Word current = null;
      Word previous = null;
      int code = 0;
      
      //build chain
      while(Con.hasNext())
      {
         s = Con.next();
         s=s.replaceAll(";","");
         s=s.replaceAll("\\.",""); //uses regex. full stop must be escaped
         s=s.replaceAll(",","");
         code = computeHash(s);
         try
         {
            current = null; //index stores position in bucket. -1 indicates not found
            if(!s.equals("")) 
               current = find(s);
            if(current != null) //if word exists
            {
               current.mult++; 
            }
            else
               chain[code].add(current = new Word(s));
               
            if(previous != null)
               previous.addLink(current.data); //problem. p is a string, not a word
            previous = current; //previous string is assigned the current string
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
   
   public static Word find(String c)
   {
      Word r = null;
      int hash = computeHash(c);
      for(int i=0; i<chain[hash].size(); i++)
      {
         if(chain[hash].get(i).data.equals(c)) //c is never stripped of whitespace, commas, or periods. This may evaluate incorrectly
         {
            r = chain[hash].get(i);
         }
      }
      return r;
   }
   
   public static int computeHash(String s)
   {
      int code;
      s = s.trim().toLowerCase(); //remove leading and trailing whitespace
      //if(s.charAt(s.length()-1) == ',' || s.charAt(s.length()-1) == '.')
         //s = s.substring(0, s.length()-1);
      code = Math.abs(s.hashCode()) % chain.length;
      return code;
   }
}