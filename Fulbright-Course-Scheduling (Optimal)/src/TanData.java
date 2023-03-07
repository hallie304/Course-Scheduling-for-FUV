import java.util.*;

public class TanData {
    private String courseId;
    private String courseName;
    private int capacity;
    private String instructor;
    private boolean isOnline;
    private String classroom;
    private int clrCapacity;
    private String classTime;

    public TanData(String courseId, String courseName, String instructor, int capacity, boolean isOnline, String classroom, int clrCapacity, String classTime) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
        this.capacity = capacity;
        this.isOnline = isOnline;
        this.classroom = classroom;
        this.clrCapacity = clrCapacity;
        this.classTime = classTime;
    }

    public String toString() {
        String ret = "";
        ret += "COURSE ID " + this.courseId + " * ";
        ret += "COURSE NAME " + this.courseName + " * ";
        ret += "INSTRUCTOR " + this.instructor + " * ";
        ret += "CAPACITY " + this.capacity + " * ";
        ret += "ONLINE " + this.isOnline + " * ";
        ret += "CLASSROOM " + this.classroom + " * ";
        ret += "CLASSROOM CAP " + this.clrCapacity + " * ";
        ret += "TIME " + this.classTime + " * ";
        return ret;
    }
}
