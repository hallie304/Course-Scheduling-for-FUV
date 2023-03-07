public class Room {
    private String classroom; // Nếu lớp học online thì classroom = Online
    private int capacity;

    public Room(String classroom, int capacity) {
        this.classroom = classroom;
        this.capacity = capacity;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String toString() {
        String ret = "";
        ret += classroom + " ";
        ret += capacity + " ";
        return ret;
    }
}
