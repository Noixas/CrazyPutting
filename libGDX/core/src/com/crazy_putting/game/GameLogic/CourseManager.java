package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.FormulaParser.*;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.Parser.Parser;

import java.util.List;

public class CourseManager {
    private static  int _amoutCourse = 1;
    private static List<Course> _courseList;
    private static Course _activeCourse;
    private static  String _cacheFileName;
    private static int _indexActive = 0;

//TODO call again the expressionNode if another course is selected
    private static FormulaParser parser = new FormulaParser();
    private static ExpressionNode expr = null;

    public static int getCourseAmount()
    {
        _amoutCourse = _courseList.size();
        return  _amoutCourse;
    }
    public static void loadFile(String pFileName)
    {
        _cacheFileName = pFileName;
        _courseList = Parser.getCourses(_cacheFileName);
        if(_courseList != null) {
            _activeCourse = _courseList.get(0);
            expr = parser.parse(_activeCourse.getHeight());
        }
        else {
            System.out.println("No courses in the file");
            return;
        }

    }
    public static List<Course> getCourseList() {
        return _courseList;
    }
    public static Course getCourseWithID(int pID)
    {
        for(int i = 0; i < getCourseAmount(); i++)
        {
            if(_courseList.get(i).getID() == pID)
                return _courseList.get(i);
        }
        return null;
    }
    public  static Course getCourseWithIndex(int pIndex)
    {
        return _courseList.get(pIndex);
    }
    public static void setActiveCourseWithID(int pID)
    {
        setActiveCourse(getCourseWithID(pID));
    }
    public static void setActiveCourseWithIndex(int pIndex)
    {
        setActiveCourse(_courseList.get(pIndex));
    }
    private static void setActiveCourse(Course pCourse)
    {
        _activeCourse = pCourse;
    }
    public static Course getActiveCourse(){
        return _activeCourse;
    }
    public static void addCourseToList(Course pCourse)
    {
        _courseList.add(pCourse);
        reWriteCourse();
    }
    public static void reWriteCourse()
    {
        if(_cacheFileName == null) return; //If we havent cache a filename then we should not proceed
        Parser.writeCourseList(_cacheFileName ,_courseList);
    }
    public  static Vector2 getStartPosition()
    {
        return _activeCourse.getStartBall();
    }
    public static int getIndexActive()
    {
        return _indexActive;
    }
    public static void reParseHeightFormula(int pNewIndex)
    {

        expr = parser.parse(_activeCourse.getHeight());
        _indexActive = pNewIndex;
    }
    public static float calculateHeight(float x, float y){
    if(_activeCourse == null)
    {
        System.out.println("No course have being defined from a file, load a file first");
        return -1;
    }
        try {
            if (expr == null) {
                expr = parser.parse(_activeCourse.getHeight());
            }
            expr.accept(new SetVariable("x", x));
            expr.accept(new SetVariable("y", y));

            float result = (float) expr.getValue();
            return result;

        }
        catch (ParserException e)
        {
            System.out.println(e.getMessage());
        }
        catch (EvaluationException e)
        {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public static Vector2 getGoalStartPosition()
    {
        return  _activeCourse.getGoalPosition();
    }
    public static float getMaxSpeed()
    {
        return _activeCourse.getMaxSpeed();
    }
}
