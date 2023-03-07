import java.util.*;

public class ClassTime {
    private List<String> date;
    private String startTime;
    private String endTime;

    public ClassTime(List<String> date, String startTime, String endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += date + " ";
        ret += startTime + " ";
        ret += endTime + " ";
        return ret;
    }
}
