import java.util.*;

public class Data {
    private List<Course> courseList;
    private List<Room> roomList;
    private List<ClassTime> classTime;

    public Data(List<Course> courseList, List<Room> roomList, List<ClassTime> classTime) {
        this.courseList = courseList;
        this.roomList = roomList;
        this.classTime = classTime;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<ClassTime> getClassTime() {
        return classTime;
    }

    public void setClassTime(List<ClassTime> classTime) {
        this.classTime = classTime;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += courseList + " ";
        ret += roomList + " ";
        ret += classTime + " ";
        return ret;
    }
}
