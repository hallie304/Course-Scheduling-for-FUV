import java.io. * ;
import java.util.Scanner;
import java.util.*;

public class ReadData {
    public static List<String> readLine(String pathFile) throws Exception {
        Scanner sc = new Scanner(new File(pathFile));
        List<String> lineList = new ArrayList<String>();

        int t = 0;
        while(sc.hasNextLine()) {
            t++;
            String currentLine = sc.nextLine();
            if(t == 1)
                continue;

            lineList.add(currentLine);
        }

        return lineList;
    }

    public static List<Course> readCourse() throws Exception {
        List<String> lineList = readLine("C:\\Users\\Thuan\\Documents\\GitHub\\Course Scheduling\\CourseOffering.csv");
        List<Course> courseList = new ArrayList<Course>();
        for(int T = 0; T < lineList.size(); T++) {
            String currentLine = lineList.get(T);
            // System.out.println(currentLine);
            String[] lineSplit = currentLine.split(",");
            // System.out.println(Arrays.toString(lineSplit));
            //System.out.println(lineSplit[0]);
            List<String> categoryList = new ArrayList<String>();
            String category = lineSplit[4];
            String[] categorySplit = category.split(" ");

            for(int i = 0; i < categorySplit.length; i++)
                categoryList.add(categorySplit[i]);
            boolean isOnline = true;
            if(lineSplit[5].equals("On-campus")) isOnline = true;
            else isOnline = false;
            Course course = new Course(lineSplit[1], lineSplit[0], lineSplit[3], categoryList, Integer.parseInt(lineSplit[2]), isOnline);
            courseList.add(course);
        }

        return courseList;
    }


    public static List<Room> readRoom() throws Exception {
        List<String> lineList = readLine("C:\\Users\\Thuan\\Documents\\GitHub\\Course Scheduling\\Room.csv");
        List<Room> roomList = new ArrayList<Room>();
        for(int i = 0; i < lineList.size(); i++) {
            String currentLine = lineList.get(i);
            String[] lineSplit = currentLine.split(",");
            Room room = new Room(lineSplit[0], Integer.parseInt(lineSplit[1]));
            roomList.add(room);
        }

        return roomList;
    }

    public static List<ClassTime> readClassTime() throws Exception {
        List<String> lineList = readLine("C:\\Users\\Thuan\\Documents\\GitHub\\Course Scheduling\\Time.csv");
        List<ClassTime> classTimeList = new ArrayList<ClassTime>();

        for(int i = 0; i < lineList.size(); i++) {
            String currentLine = lineList.get(i);
            String[] lineSplit = currentLine.split(" ");
            int lenSplit = lineSplit.length;
            List<String> dateList = new ArrayList<String>();
            for(int j = 0; j < lenSplit - 2; j++)
                dateList.add(lineSplit[j]);
            ClassTime classTime = new ClassTime(dateList, lineSplit[lenSplit - 2], lineSplit[lenSplit - 1]);
            classTimeList.add(classTime);
        }

        return classTimeList;
    }

//    public static void main(String[] args) throws Exception {
//        List<Course> courseList = readCourse();
//        System.out.println(courseList);
//        List<Room> roomList = readRoom();
//        System.out.println(roomList);
//        List<ClassTime> classTimeList = readClassTime();
//        System.out.println(classTimeList);
//    }
}


