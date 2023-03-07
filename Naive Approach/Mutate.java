import java.util.*;
public class Mutate {
    private int MutateId;
    private ClassTime time;
    private Room room;
    public Mutate(ClassTime time, Room room, int MutateId){
        this.time =time;
        this.room = room;
        this.MutateId = MutateId;
    }
    public ClassTime getTime(){
        return time;
    }
    public Room getRoom(){
        return room;
    }
    public void setTime(Classtime time){
        this.time = time;
    }
    public void setRoom(Room room){
        this.room = room;
    }
    public int getMutateId(){
        return MutateId;
    }
    public void setMutateId(Int MutateId){
        this.MutateId = MutateId;
    }
    public String toString() {
        String ret = "";
        ret += MutateId + " ";
        ret += time + " ";
        ret += room + " ";
        return ret;
    }

}
