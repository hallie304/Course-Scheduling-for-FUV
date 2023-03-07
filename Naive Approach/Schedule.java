import java.util.*;

public class Schedule {
    private double fitnessScore;
    private int numberConflicts;
    private List<Class> scheduleList;

    public Schedule(double fitnessScore, int numberConflicts, List<Class> scheduleList) {
        this.fitnessScore = fitnessScore;
        this.numberConflicts = numberConflicts;
        this.scheduleList = scheduleList;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
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
        ret += fitnessScore + " ";
        ret += numberConflicts + " ";
        ret += scheduleList + " ";
        return ret;
    }
}
