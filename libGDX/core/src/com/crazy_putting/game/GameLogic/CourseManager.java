package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.Parser.Parser;

import java.util.List;

public class CourseManager {
    private static  int _amoutCourse = 1;
    private static List<Course> _courseList;
    private static Course _activeCourse;
    private static  String _cacheFileName;

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
        }
        else
            System.out.println("No courses in the file");

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
    public static void setActiveCourseWithID(int pID)
    {
        setActiveCourse(getCourseWithID(pID));
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

}
