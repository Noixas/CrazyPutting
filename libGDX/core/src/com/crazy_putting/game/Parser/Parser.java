package com.crazy_putting.game.Parser;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameObjects.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static String _courseSeparator = "\nCOURSE:";
    private static String _fileNameCached = "";
    private static List<Course> _cacheFile = null;
    private static String _endCourse = "Course End";
    private static int _obstaclesAmount;
    public static void readCourse(String pFileName) throws  IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(pFileName));
       _cacheFile = generateCourses(lines);
       _fileNameCached = pFileName;

    }
    public static void writeCourseList(String pFileName, List<Course> pCourseList)
    {
        String out = "";
        for (int i = 0; i < pCourseList.size(); i++)
        out += generateStringFile(pFileName, pCourseList.get(i), i);

        try{
            writeToTextFile(pFileName, out);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public static String generateStringFile(String pFileName, Course pCourse, int pIndex)
    {
    if(checkForCache() == false) cacheFile(pFileName);
       List<String> out = new ArrayList<String>();//= getCacheFile();
        out.add(_courseSeparator + "");
        out.add("\nID: " +  pIndex);//Set the next course ID
        out.add("\nName: " + pCourse.getName());
        out.add("\nHeight: " + pCourse.getHeight());
        out.add("\nFriction: " +pCourse.getFriction());
        out.add("\nGoal Pos: "+ pCourse.getGoalPosition().x + " " + pCourse.getGoalPosition().y);
        out.add("\nGoal Radius: " + pCourse.getGoalRadius());
        out.add("\nBall Start Pos: " + pCourse.getStartBall().x + " " + pCourse.getStartBall().y);
        out.add("\nMax Speed: " + pCourse.getMaxSpeed());
        out.add("\nSpline Points: " + pCourse.toStringSplinePoints());
        List<String> obstacles = pCourse.getObstaclesStringList();
        out.addAll(obstacles);
        String finalFile = "";
        for(int i = 0; i < out.size(); i++) {
            finalFile += out.get(i);
        }
        finalFile+= "\n" +_endCourse;
        return finalFile;
    }

    private static void writeToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes());
    }
    private static int getAmountCourses()
    {
        return 0;
    }
    private static void cacheFile(String pFileName)
    {
        try{
            readCourse(pFileName);
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public static void updateCache(String pFileName)
    {
        cacheFile(pFileName);
    }
    private static List<Course> getCacheFile()
    {
        return _cacheFile;
    }
    private static  boolean checkForCache()
    {
        if(getCacheFile() == null)
         return false;
        else return  true;
    }
    private static List<Course> generateCourses(List<String> pLines)
    {

    List<Course> courses = new ArrayList<Course>();
    int lineCount = 0;
    boolean readingCourse = false;
    Course newCourse = null;
    int propertiesAmount = 8;
        for(int i = 0; i < pLines.size(); i++) {
            String line = pLines.get(i);
            line.trim();
            if (line.compareTo(_endCourse) == 0) {
             //   setCourseProperty(newCourse, line, lineCount);
                courses.add(newCourse);
                newCourse = null;
                readingCourse = false;
                lineCount = 0;
            }
            if (readingCourse) {
                setCourseProperty(newCourse, line, lineCount);
                lineCount++;
            }
            if (line.startsWith("COURSE")) {

                readingCourse = true;
                newCourse = new Course();
            }
        }
        return courses;
    }
    private static void setCourseProperty(Course pCourse, String pProperty, int pLine)
        {
            switch (pLine)
            {
                case 0:
                    pProperty = pProperty.replace("ID: ","");
                    pCourse.setID(Integer.parseInt(pProperty));
                    break;
                case 1:
                    pProperty = pProperty.replace("Name: ","");
                    pCourse.setName(pProperty);
                    break;
                case 2:
                    pProperty = pProperty.replace("Height: ","");
                    pCourse.setHeight(pProperty);
                    break;
                case 3:
                    pProperty = pProperty.replace("Friction: ","");
                    pCourse.setFriction(Float.parseFloat(pProperty));
                    break;
                case 4:
                    pProperty = pProperty.replace("Goal Pos: ","");
                    String[] GoalPos = pProperty.trim().split("\\s+");
                    pCourse.setGoalPosition(new Vector3(Float.parseFloat(GoalPos[0]), Float.parseFloat(GoalPos[1]),0));
                    break;
                case 5:
                    pProperty = pProperty.replace("Goal Radius: ","");
                    pCourse.setGoalRadius(Float.parseFloat(pProperty));
                    break;
                case 6:
                    pProperty = pProperty.replace("Ball Start Pos: ","");
                    String[] ballStartPos = pProperty.trim().split("\\s+");
                    pCourse.setBallStartPos(new Vector3(Float.parseFloat(ballStartPos[0]), Float.parseFloat(ballStartPos[1]),0));
                    break;
                case 7:
                    pProperty = pProperty.replace("Max Speed: ","");
                    pCourse.setMaxSpeed(Float.parseFloat(pProperty));
                    break;
                case 8:
                    pProperty = pProperty.replace("Spline Points: ", "");
                    pCourse.setSplinePoints(generatePoints(pProperty));
                    break;
                case 9:
                    pProperty = pProperty.replace("Obstacles: ", "");
                    _obstaclesAmount = Integer.parseInt(pProperty);
                    break;
                    default:
                    pCourse.addObstacle((pLine-9 + 2)%3,pProperty);
                        break;
            }
        }
    private static float[][] generatePoints(String line){
        String[] num = line.trim().split("\\s+");
        int length = Integer.parseInt(num[0]);
        int length0 = Integer.parseInt(num[1]);
        float[][] points = new float[length][length0];
        int count = 2;
        for(int i = 0; i<length; i++){
            for(int j = 0; j<length0; j++){
                points[i][j] = Float.parseFloat(num[count]);
                count++;
            }
        }
            return points;
    }
    public static List<Course> getCourses(String pFileName)
    {
        try {
            if (checkForCache() == false ||_fileNameCached.equals(pFileName) == false)
                readCourse(pFileName);
        }catch(Exception e){
            System.out.println("Exception reading file \n"+e);}
        return _cacheFile;

    }


}
