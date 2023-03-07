public class Class {
    private Course course;
    private Slot slot;

    public Class(Course course, Slot slot) {
        this.course = course;
        this.slot = slot;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public String toStringTan() {
        String ret = "";
        ret += "COURSEID* ";
        ret += course.getCourseId() + " *";
        ret += "COURSENAME* ";
        ret += course.getCourseName() + " *";
        ret += "INSTRUCTOR* ";
        ret += course.getFacultyName() + " *";
        ret += "CAPACITY* ";
        ret += course.getCapacity() + " *";
        ret += "ONLINE* ";
        ret += course.isOnline() + " *";
        ret += "CLASSROOM* ";
        ret += slot.getRoom() + " *";
        ret += "TIME* ";
        ret += slot.getTime() + " *";
        ret += slot + " ";
        return ret;
    }

    public String toString() {
        String ret = "";
        ret += this.course + " ";
        ret += this.slot + " ";
        return ret;
    }
}
