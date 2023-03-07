import java.util.*;

public class Schedule implements Comparable<Schedule> {
    private Integer numberConflicts;
    private List<Class> scheduleList;

    public Schedule(int numberConflicts, List<Class> scheduleList) {
        this.numberConflicts = numberConflicts;
        this.scheduleList = scheduleList;
    }

    public Schedule() {
        this.numberConflicts = null;
        this.scheduleList = null;
    }

    public int getNumberConflicts() {
        return numberConflicts;
    }

    public void setNumberConflicts(int numberConflicts) {
        this.numberConflicts = numberConflicts;
    }

    public List<Class> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Class> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String toString() {
        String ret = "";
        ret += numberConflicts + " ";
        ret += scheduleList + " ";
        return ret;
    }

    public int compareTo(Schedule other) {
        return this.numberConflicts.compareTo(other.numberConflicts);
    }
}
