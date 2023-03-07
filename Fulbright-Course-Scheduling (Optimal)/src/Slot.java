import java.util.*;

public class Slot implements Comparable<Slot> {
    private ClassTime time;
    private Room room;

    public Slot(ClassTime time, Room room) {
        this.time = time;
        this.room = room;
    }

    public ClassTime getTime() {
        return time;
    }

    public void setTime(ClassTime time) {
        this.time = time;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int compareTo(Slot other) {
        return this.room.getCapacity().compareTo(other.getRoom().getCapacity());
    }

    @Override
    public String toString() {
        String ret = "";
        ret += time + " ";
        ret += room + " ";
        return ret;
    }
}
