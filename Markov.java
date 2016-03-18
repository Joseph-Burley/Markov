import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;
public class Markov
{
   private class Bucket
   {
      String s;
      int mult=1;
   }
   
   public static void main(String[] args)
   {
   
      
      ArrayList<String>[] chain = new ArrayList[1000];
      for(int i=0; i<chain.length; i++)
      {
         chain[i] = new ArrayList<String>();
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
         s = Con.next().trim();
         code = Math.abs(s.hashCode());
         try
         {
            
            int i = code % chain.length;
            if(!s.equals("") && !chain[i].contains(s))
               chain[i].add(s);
         }
         catch(Exception e)
         {
            System.out.println(e);
         }
      }
      
      //show chain
      System.out.println("-------Chain data-------");
      for(int i=0; i<chain.length; i++)
      {
         if(chain[i].size() > 0)
         {
            System.out.println("Bucket " + i+"------");
            for(int j=0; j<chain[i].size(); j++)
            {
               System.out.print(chain[i].get(j) + ", ");
            }
            System.out.println();
         }
         
      }
      
      
   }
}