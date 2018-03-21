package com.crazy_putting.game.GameLogic;

import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.Parser.Parser;

import java.util.List;

public class CourseManager {
    private static  int _amoutCourse = 1;
    private static List<Course> _courseList;

    public static int getCourseAmount()
    {
        _amoutCourse = _courseList.size();
        return  _amoutCourse;
    }
    public static void loadFile(String pFileName)
    {
        _courseList = Parser.getCourses(pFileName);
        System.out.println(_courseList.size());

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


}
