import java.io.*;
import java.util.*;

public class ReadData {
    public static List<List<String>> readLineCSV(String pathFile) throws Exception {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } 

        return records;
    }

    public static List<Course> readCourse(String path) throws Exception {
        List<List<String>> lineList = readLineCSV(path); // Get all data by rows
        List<Course> courseList = new ArrayList<>(); // Convert each observation in the type of Course

        for(int i = 0; i < lineList.size(); i++) {
            List<String> currentLine = lineList.get(i);
            String courseName = currentLine.get(0);
            String courseId = currentLine.get(1);
            int courseCapacity = Integer.parseInt(currentLine.get(2));

            String faculties = currentLine.get(3);
            String[] facultySplit = faculties.split("&");
            if(facultySplit.length > 1) {
                facultySplit[0] = facultySplit[0].substring(0, facultySplit[0].length() - 1);
            
                // Giữa
                for(int k = 1; k < facultySplit.length - 1; k++)
                    facultySplit[k] = facultySplit[k].substring(1, facultySplit[k].length() - 1);
                
                // Cuối
                int lastId = facultySplit.length - 1;
                facultySplit[lastId] = facultySplit[lastId].substring(1);
            }
            
            List<String> facultyList = new ArrayList<String>();
            for(int k = 0; k < facultySplit.length; k++)
                facultyList.add(facultySplit[k]);

            boolean isOnline = true;
            if(currentLine.get(4).equals("On-campus"))
                isOnline = false;
            if(currentLine.get(4).equals("Hybrid"))
                isOnline = false;

            Course course = new Course(courseId, courseName,facultyList, courseCapacity, isOnline);
            courseList.add(course);
        }

        return courseList; // Return the list of course after being extracted
    }


    public static List<Room> readRoom(String path) throws Exception {
        List<List<String>> lineList = readLineCSV(path); // Get all data by rows
        List<Room> roomList = new ArrayList<Room>();
        for (int i = 0; i < lineList.size(); i++) {
            List<String> currentLine = lineList.get(i);
            String classroom = currentLine.get(0);
            int roomCap = Integer.parseInt(currentLine.get(1));
            Room room = new Room(classroom, roomCap);
            roomList.add(room);
        }

        return roomList;
    }
//
    public static List<ClassTime> readClassTime(String path) throws Exception {
        List<List<String>> lineList = readLineCSV(path); // Get all data by rows
        List<ClassTime> classTimeList = new ArrayList<ClassTime>();

        for (int i = 0; i < lineList.size(); i++) {
            List<String> currentLine = lineList.get(i);
            String classTime = currentLine.get(0);
            String classTimeSplit[] = classTime.split(" ");
            int lenSplit = classTimeSplit.length;
            List<String> dateList = new ArrayList<String>();
            for (int j = 0; j < lenSplit - 2; j++)
                dateList.add(classTimeSplit[j]);
            ClassTime newClassTime = new ClassTime(dateList, classTimeSplit[lenSplit - 2], classTimeSplit[lenSplit - 1]);
            classTimeList.add(newClassTime);
        }

        return classTimeList;
    }
}



