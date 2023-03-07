public class Class {
    private Course course;
    private ClassTime classTime;
    private Room room;

    public Class(Course course, ClassTime classTime, Room room) {
        this.course = course;
        this.classTime = classTime;
        this.room = room;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ClassTime getClassTime() {
        return classTime;
    }

    public void setClassTime(ClassTime classTime) {
        this.classTime = classTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String toString() {
        String ret = "";
        ret += course + " ";
        ret += classTime + " ";
        ret += room + " ";
        return ret;
    }
}
