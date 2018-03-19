package com.crazy_putting.game.Parser;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import java.lang.*;

public class Parser {

    private static String _courseSeparator = "\nCOURSE:";
    private static List<String> _cacheFile = null;
    public static List<String> readCourse(String pFileName) throws  IOException
    {
        //List<String> lines = Files.readAllLines(Paths.get(pFileName));
       List<String> lines = new ArrayList<String>();
        return lines;
    }
    public static void writeCourse(String pFileName, Object pCourse)
    {
    if( checkForCache() == false) cacheFile(pFileName);
       List<String> out = getCacheFile();
        out.add(_courseSeparator + "");
        out.add( "\nID: " +  (getAmountCourses() + 1));//Set the next course ID
        out.add("\nHeight: " );//+ pCourse.getHeight();
        out.add("\nFriction: "); // +pCourse.getFriction();
        out.add("\nGoal Pos: "); //+ pCourse.getGoalPos();
        out.add("\nGoal Radius: "); //+ pCourse.getGoalRadius();
        out.add( "\nBall Start Pos: "); //+ pCourse.getBallStartPos();
        out.add("\nMax Speed: "); //+ pCourse.getMaxSpeed()
       String finalFile = "";
        for(int i = 0; i < out.size(); i++) {
            finalFile += out.get(i);

        }
        try{
            writeToTextFile(pFileName, finalFile);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    private static void writeToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
    }
    private static int getAmountCourses()
    {
        return 0;
    }
    private static void cacheFile(String pFileName)
    {
        try{
        _cacheFile = readCourse(pFileName);}catch (IOException e){
            System.out.println(e);
        }
    }
    public static void updateCache(String pFileName)
    {
        cacheFile(pFileName);
    }
    private static List<String> getCacheFile()
    {
        return _cacheFile;
    }
    private static  boolean checkForCache()
    {
        if(getCacheFile() == null)
         return false;
        else return  true;
    }
    public static Object returnCourseObj()
    {
        if(checkForCache() == false) return null;
        List<String> lines =_cacheFile;
        for(int i = 0; i < lines.size(); i++)
    {
        //  char[] characters = lines.get(i).toCharArray();
        String[] splitStr = lines.get(i).trim().split("\\s+");
      //  Integer[] baseInfo = readLine(splitStr, i);
        //  System.out.println(baseInfo.length);

    }
        return null;
    }


}
