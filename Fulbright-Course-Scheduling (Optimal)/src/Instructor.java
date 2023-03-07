import java.util.*;

public class Instructor implements Comparable<Instructor> {
    private String name;
    private List<Course> teachingList;

    public Instructor(String name, List<Course> teachingList) {
        this.name = name;
        this.teachingList = teachingList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getTeachingList() {
        return teachingList;
    }

    public void setTeachingList(List<Course> teachingList) {
        this.teachingList = teachingList;
    }

    public String toString() {
        String ret = "";
        ret += name + " ";
        ret += teachingList + " ";
        return ret;
    }

    public int compareTo(Instructor other) {
        Integer thisTeachingSize = this.teachingList.size();
        Integer otherTeachingSize = other.getTeachingList().size();
        return otherTeachingSize.compareTo(thisTeachingSize);
    }
}
