package com.crazy_putting.game.Parser;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadAndAnalyse
{
  // instance variables
  private static String fileName;
  private static float[][] result;

  private static int n;
  // constructor


  // insrance methods
  public static void calculate(String pFileName)
  {
    fileName = pFileName;
    Scanner in = null;
    { try
        {
          FileReader reader = new FileReader(fileName);
          in = new Scanner(reader);
          String l = in.nextLine();
          String[] words = l.split("\\s+");
          n = Integer.parseInt(words[words.length - 1]);
          result = new float[n][2];
          in.nextLine();
          int i = 0;
          while (in.hasNextLine())
          {
            l = in.nextLine();
            words = l.split("\\s+");
            int j = 0;
            for (int x=0; x<words.length; x++)
            {
                if (isNumeric(words[x]))
                {
                  result[i][j] = Float.parseFloat(words[x]);
                  j++;
                }
            }
            i++;
          }
          reader.close();
        }
      catch(FileNotFoundException e) { System.out.println("Bad file name");}
      catch(IOException e) { System.out.println("Corrupted file");}
    }
  }
  public static int getN()
  {
    return n;
  }
  public static boolean isNumeric(String str)
  {
      return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
  }

  public static float[][] getResult()
  {
      return result;
  }

  public static void print(float[][] l) {
    for (int i=0; i<l.length; i++)
      for (int j=0; j<l[i].length; j++)
        System.out.print(l[i][j]);
    System.out.println();
  }

  /*// the main method
	public static void main(String[] args)
	{
      // String x = "myFile.txt";
      // new ReadAndAnalyse(x);
      Scanner in = new Scanner(System.in);
      //System.out.println("Please enter a file name");
      //String fileName = in.next();
      String fileName = "myFile.txt";
      float[][] data = new ReadAndAnalyse(fileName).getResult();
      print(data);
  }
*/
}
