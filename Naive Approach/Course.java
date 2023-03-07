import java.util.*;

public class Course {
    private String courseId;
    private String courseName;
    private String facultyName;
    private List<String> category;
    private int capacity;
    private boolean isOnline;

    public Course(String courseId, String courseName, String facultyName, List<String> category, int capacity, boolean isOnline) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.facultyName = facultyName;
        this.category = category;
        this.capacity = capacity;
        this.isOnline = isOnline;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(List<String> facultyName) {
        this.facultyName = facultyName;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += courseId + " ";
        ret += courseName + " ";
        ret += facultyName + " ";
        ret += category + " ";
        ret += capacity + " ";
        ret += isOnline + " ";
        return ret;
    }
}
