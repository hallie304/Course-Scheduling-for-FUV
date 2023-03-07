import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Do {
    public static int hourToMinute(String time) {
        String[] hourMinSplit = time.split(":");
        int hour = Integer.parseInt(hourMinSplit[0]);
        int min = Integer.parseInt(hourMinSplit[1]);
        return hour * 60 + min;
    }

    public static boolean checkConflictRoom(Class classA, Class classB) {
        Room roomA = classA.getRoom();
        Room roomB = classB.getRoom();

        // Duyệt xuôi
        if(roomA.equals("Classroom 6789") && roomB.equals("Classroom 67"))
            return true;
        if(roomA.equals("Classroom 6789") && roomB.equals("Classroom 89"))
            return true;
        if(roomA.equals("Classroom 45") && roomB.equals("Classroom 4"))
            return true;
        if(roomA.equals("Classroom 45") && roomB.equals("Classroom 5"))
            return true;

        // Duyệt ngược
        if(roomB.equals("Classroom 6789") && roomA.equals("Classroom 67"))
            return true;
        if(roomB.equals("Classroom 6789") && roomA.equals("Classroom 89"))
            return true;
        if(roomB.equals("Classroom 45") && roomA.equals("Classroom 4"))
            return true;
        if(roomB.equals("Classroom 45") && roomA.equals("Classroom 5"))
            return true;

        // Check for equality
        return classA.getRoom().getClassroom().equals(classB.getRoom().getClassroom());
    }

    public static boolean checkCapacityConflict(Course course, Room room) {
        return course.getCapacity() > room.getCapacity();
    }

    public static boolean checkConflictCourse(Course courseA, Course courseB) {
        int trace = 0;
        
        String[] split = courseA.getFacultyName().split("&");
        String[] split2 = courseB.getFacultyName().split("&");
        for(int i = 0; i < split.length; i++){
            for(int j = 0; j < split2.length; j++){
                if(Arrays.asList(split).get(i).equals(Arrays.asList(split2).get(j))){
                    trace++;
                }
            }
        }
        
        if(trace==0){return false;}
        else{return true;}
            
    }

        

    public static boolean checkConflictTime(ClassTime timeA, ClassTime timeB) {
        // Kiểm tra giờ trước. Nếu khác giờ thì thôi. Nếu cùng giờ hoặc overlap thì kiểm coi có cùng ngày hay không
        // Tạm thời đúng với conflict time. Hãy chờ xem!

        // Kiểm tra overlap giờ
        List<String> dateA = timeA.getDate();
        String startA = timeA.getStartTime();
        String endA = timeA.getEndTime();

        List<String> dateB = timeB.getDate();
        String startB = timeB.getStartTime();
        String endB = timeB.getEndTime();


        if((hourToMinute(endA) < hourToMinute(startB)) || (hourToMinute(endB) < hourToMinute(startA)))
            return false;


        // Kiểm ngày
        for(int i = 0; i < dateA.size(); i++) {
            if(dateB.contains(dateA.get(i))) {
                return true;
            }
        }

        
        return false;
    }

    public static boolean checkConflict(Class classA, Class classB) {
        // TH1: Trùng giờ khác phòng
        // TH2: Trùng phòng khác giờ
        // TH3: Trùng gvien khác lớp
        // TH khác: return không trùng

        // TH1: Trùng giờ khác phòng
        if (checkConflictTime(classA.getClassTime(), classB.getClassTime()) && !checkConflictRoom(classA, classB) && !checkConflictCourse(classA.getCourse(), classB.getCourse())) {
            return false;
        }
        // TH2: Trùng phòng khác giờ
        if(!checkConflictTime(classA.getClassTime(), classB.getClassTime()) && checkConflictRoom(classA, classB)) {
            // System.out.println("lmao");
            return false;
        }
        // TH3: Trùng gvien khác giờ

        if(checkConflictCourse(classA.getCourse(), classB.getCourse()) && !checkConflictTime(classA.getClassTime(), classB.getClassTime())) {
            
            return false;
        }
        return true;
    }

    public static int computeConflict(List<Class> classList) {
        int numConflict = 0;
       
        for(int i = 0; i < classList.size(); i++) {

            if(checkCapacityConflict(classList.get(i).getCourse(), classList.get(i).getRoom())) {
                
                numConflict++;
            }
            for(int j = i + 1; j < classList.size(); j++) {
                Class classA = classList.get(i);
                Class classB = classList.get(j);
                if(checkConflictRoom(classA, classB)) {
                    
                    numConflict++;
                }
                if(checkConflictTime(classA.getClassTime(), classB.getClassTime())) {
                    numConflict++;
                }
                if(checkConflictCourse(classA.getCourse(), classB.getCourse())) {
                    numConflict++;
                }
                if(!checkConflict(classA, classB)) {
                    
                    numConflict--;
                }
            }
        }

        
        return numConflict;
    }

    public double calculateFitness() {


        return 0;
    }
    
    public static List<List<Integer>> generatePermute(int[] source) {
        List<List<Integer>> results = new ArrayList<List<Integer>>();
        if (source == null || source.length == 0) {
            return results;
        }
        List<Integer> NewResult = new ArrayList<>();
        dfs(source, results, NewResult);
        return results;
    }

    public static void dfs(int[] source, List<List<Integer>> results, List<Integer> NewResult) {
        if (source.length == NewResult.size()) {
            List<Integer> temp = new ArrayList<>(NewResult);
            results.add(temp);
        }        
        for (int i=0; i<source.length; i++) {
            if (!NewResult.contains(source[i])) {
                NewResult.add(source[i]);
                dfs(source, results, NewResult);
                NewResult.remove(NewResult.size() - 1);
            }
        }
    }

    public static ArrayList<ArrayList<Mutate>> generateSubset(ArrayList<Mutate> allCombination, int n, ArrayList<ArrayList<Mutate>> p) {
        int length = allCombination.size();
 
        for (int i = 0; i < (1<<length); i++){
            ArrayList<Mutate> q = new ArrayList<Mutate>();
            for (int j = 0; j < length; j++){
                if ((i & (1 << j)) > 0){
                    q.add(allCombination.get(j));
                }
            }
            
            if(q.size()==n){
                p.add(q);
            }
        }
        return p;
    }


    public static void main(String[] args) throws Exception {
        List<Course> courseList = ReadData.readCourse();
        List<Room> roomList = ReadData.readRoom();
        List<ClassTime> classTimeList = ReadData.readClassTime();
        
        ArrayList<Mutate> allCombination = new ArrayList<Mutate>();
        int count = 0;
        for(int i = 0; i < roomList.size(); i++){
            for(int j =0; j<classTimeList.size();j++){
                Mutate k = new Mutate(classTimeList.get(j), roomList.get(i),count);
                allCombination.add(k);
                count++;

            }
        }

        
        ArrayList<ArrayList<Mutate>> allPossible = new ArrayList<ArrayList<Mutate>>();
        ArrayList<ArrayList<Mutate>> finalPossible = generateSubset(allCombination,courseList.size(),allPossible);
        
        int[] CourseIndex = new int[courseList.size()];
        Arrays.setAll(CourseIndex, i -> i + 1);
        
        
        List<List<Integer>> allPermute = generatePermute(CourseIndex);

        List<List<Class>> toStore = new ArrayList<List<Class>>(); 

        for(int i = 0; i < finalPossible.size(); i++){
            ArrayList<Mutate> TRtoCheck = finalPossible.get(i);
            for(int j =0; j<allPermute.size();j++){
                List<Integer> temp = allPermute.get(j);
                List<Class> toCheck = new ArrayList<Class>();
                for(int k =0; k<temp.size();k++){
                    int store = Integer.valueOf(temp.get(k))-1;
                    ClassTime timeTo = TRtoCheck.get(store).getTime();
                    Room roomTo = TRtoCheck.get(store).getRoom();
                    Class toAdd = new Class(courseList.get(store),timeTo,roomTo);
                    toCheck.add(toAdd);
                }
                toStore.add(toCheck);
            }
        }

        int note = 0;
        for(int i = 0; i < toStore.size(); i++){
            if(computeConflict(toStore.get(i))==0){
                System.out.println(toStore.get(i));
                note++;
                break;
            }
        }
        if(note==0){
            System.out.println("No Solution");
        }
    }
}
