package com.crazy_putting.game.Parser;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//import java.util.stream.Stream;

public class Parser {
    public static Object readCourse(String pFileName) throws  IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(pFileName));

        return null;
    }
    public static void writeCourse(String pFileName, Object pCourse)
    {
        String out = "";
        try{
            writeToTextFile(pFileName, out);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public static void writeToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
    }


}
