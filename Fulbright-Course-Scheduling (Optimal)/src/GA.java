import javax.swing.plaf.synth.SynthCheckBoxUI;
import java.awt.*;
import java.sql.Array;
import java.text.CollationElementIterator;
import java.util.*;
import java.util.List;

public class GA {
    private static final Random random = new Random();
    private static final int MAX_POP_SIZE = 20;
    private static final int MAX_ITERATION = 200;
    private static final int MAX_CROSS_MUTATE = 1000;
    private static int NUM_ROOMS = 0;

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
        return courseA.getFacultyName().equals(courseB.getFacultyName());
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

        // Không bị trùng giờ thì chắc chắn không bị overlap
        // System.out.println(hourToMinute(startA));
        // System.out.println(hourToMinute(endB));
        // System.out.println();
        // System.out.println(hourToMinute(startB));
        // System.out.println(hourToMinute(endA));

        if((hourToMinute(endA) < hourToMinute(startB)) || (hourToMinute(endB) < hourToMinute(startA)))
            return false;


        // Kiểm ngày
        for(int i = 0; i < dateA.size(); i++) {
            if(dateB.contains(dateA.get(i))) {
                return true;
            }
        }

        //    if()
        //}
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
            // System.out.println("lmao");
            return false;
        }
        return true;
    }

    public static int computeConflict(List<Class> classList) {
        int numConflict = 0;
        // List<Class> scheduleList = schedule.getScheduleList();
        for(int i = 0; i < classList.size(); i++) {

            if(checkCapacityConflict(classList.get(i).getCourse(), classList.get(i).getRoom())) {
                // System.out.println("Class" + i);
                numConflict++;
            }
            for(int j = i + 1; j < classList.size(); j++) {
                Class classA = classList.get(i);
                Class classB = classList.get(j);
                if(checkConflictRoom(classA, classB)) {
                    // System.out.println("Class" + i + " Class " + j);
                    numConflict++;
                }
                if(checkConflictTime(classA.getClassTime(), classB.getClassTime())) {
                    numConflict++;
                }
                if(checkConflictCourse(classA.getCourse(), classB.getCourse())) {
                    numConflict++;
                }
                if(!checkConflict(classA, classB)) {
                    // System.out.println("Class" + i + " Class " + j);
                    numConflict--;
                }
            }
        }

        // schedule.setNumberConflicts(numConflict);
        return numConflict;
    }

    public static List<Schedule> generatePopulation(Data data, List<Schedule> currentSchedule) throws Exception {
        Random random = new Random();

        List<Course> courseList = ReadData.readCourse();
        List<Room> roomList = ReadData.readRoom();
        List<ClassTime> classTimeList = ReadData.readClassTime();

        List<Schedule> scheduleList = new ArrayList<Schedule>();
        for(int i = 0; i < currentSchedule.size(); i++)
            scheduleList.add(currentSchedule.get(i));

        for(int T = 0; T < MAX_POP_SIZE; T++) {
            List<Class> classList = new ArrayList<Class>();
            for(int i = 0; i < courseList.size(); i++) {
                int randomClassTime = random.nextInt(classTimeList.size());
                int randomRoom = random.nextInt(roomList.size());
                Course currentCourse = courseList.get(i);
                Class curClass = new Class(currentCourse, classTimeList.get(randomClassTime), roomList.get(randomRoom));
                classList.add(curClass);
            }

            int numConflict = computeConflict(classList);
            Schedule schedule = new Schedule(numConflict, classList);
            scheduleList.add(schedule);
        }

        Collections.sort(scheduleList);
        List<Schedule> goodSchedule = new ArrayList<Schedule>();
        for(int i = 0; i < MAX_POP_SIZE; i++)
            goodSchedule.add(scheduleList.get(i));

        // System.out.println(scheduleList.get(0).getScheduleList().size());
        return goodSchedule;
    }
    /*
    public static List<Schedule> crossover(List<Schedule> currentScheduleList, int times) throws Exception {
        Random random = new Random();

        // List<Schedule> population = generatePopulation();

        List<Schedule> bestScheduleList = new ArrayList<Schedule>();
        List<Schedule> crossoverSchedule = new ArrayList<Schedule>();

        // System.out.println("Before crossover");
        // System.out.println(population.get(0));

        // Schedule bestSchedule = population.get(0);
        for(int i = 0; i < times; i++)
            bestScheduleList.add(currentScheduleList.get(0));

        for(int T = 0; T < times; T += 2) {
            Schedule currentSchedule = bestScheduleList.get(T);
            Schedule nextSchedule = bestScheduleList.get(T + 1);
            List<Class> classSchedule1 = currentSchedule.getScheduleList();
            List<Class> classSchedule2 = nextSchedule.getScheduleList();

            int numClass = classSchedule1.size();
            List<Class> firstHalfSchedule1 = new ArrayList<Class>();
            List<Class> firstHalfSchedule2 = new ArrayList<Class>();
            List<Class> secondHalfSchedule1 = new ArrayList<Class>();
            List<Class> secondHalfSchedule2 = new ArrayList<Class>();

            // Chia thành 2 phần nhưng tỉ lệ mỗi phần bị chia là random
            int pivot = numClass / 2;
            for(int i = 0; i < pivot; i++) {
                firstHalfSchedule1.add(classSchedule1.get(i));
                firstHalfSchedule2.add(classSchedule2.get(i));
            }

            for(int i = pivot + 1; i < numClass; i++) {
                secondHalfSchedule1.add(classSchedule1.get(i));
                secondHalfSchedule2.add(classSchedule2.get(i));
            }

            List<Class> crossSchedule12 = new ArrayList<Class>();
            List<Class> crossSchedule21 = new ArrayList<Class>();
            for(int i = 0; i < firstHalfSchedule1.size(); i++) {
                crossSchedule12.add(firstHalfSchedule1.get(i));
                crossSchedule21.add(firstHalfSchedule2.get(i));
            }

            for(int i = 0; i < secondHalfSchedule2.size(); i++) {
                crossSchedule12.add(secondHalfSchedule2.get(i));
                crossSchedule21.add(secondHalfSchedule1.get(i));
            }

            int conflictSchedule12 = computeConflict(crossSchedule12);
            int conflictSchedule21 = computeConflict(crossSchedule21);
            Schedule schedule12 = new Schedule(conflictSchedule12, crossSchedule12);
            Schedule schedule21 = new Schedule(conflictSchedule21, crossSchedule21);
            crossoverSchedule.add(schedule12);
            crossoverSchedule.add(schedule21);
        }

        // System.out.println("After crossover");
        Collections.sort(crossoverSchedule);
        // System.out.println(crossoverSchedule.get(0));

        return crossoverSchedule;
    } */

//    public static List<Schedule> crossover(List<Schedule> currentScheduleList, int times) throws Exception {
//        Random random = new Random();
//        // int size = currentScheduleList.get(0).getScheduleList().size();
//        int bestConflict = currentScheduleList.get(0).getNumberConflicts();
//        List<Schedule> resultSchedule = new ArrayList<Schedule>(currentScheduleList);
//
//        // List<Schedule> population = generatePopulation();
//        int t = 0;
//        while(true) {
//            t++;
//            System.out.println(t);
//            List<Schedule> filterSchedule = new ArrayList<Schedule>(); // Sau khi loại tệ và thay bằng crossover schedule
//
//            if(bestConflict == 0) {
//                resultSchedule = new ArrayList<Schedule>(filterSchedule);
//                break;
//            }
//
//            // List<Schedule> crossoverSchedule = new ArrayList<Schedule>();
//            int N = currentScheduleList.size(); // N là số lượng schedule trong list schedule
//
//            for (int i = 0; i < N - (times / 2); i++)
//                filterSchedule.add(resultSchedule.get(i)); // Bỏ đi Times / 2 schedule tệ nhất
//
//            for (int T = 0; T < N; T += 2) {
//                Schedule currentSchedule = resultSchedule.get(T);
//                Schedule nextSchedule = resultSchedule.get(T + 1);
//                // List<Class> firstHalfSchedule1 = new ArrayList<Class>();
//                // List<Class> secondHalfSchedule2 = new ArrayList<Class>();
//                List<Class> newClassList = new ArrayList<Class>();
//                int numClass = currentSchedule.getScheduleList().size();
//                // System.out.println(numClass);
//                int pivot = random.nextInt(numClass);
//                for (int i = 0; i < pivot; i++)
//                    newClassList.add(currentSchedule.getScheduleList().get(i));
//                for (int i = pivot; i < numClass; i++)
//                    newClassList.add(nextSchedule.getScheduleList().get(i));
//                int newConflict = computeConflict(newClassList);
//                Schedule newSchedule = new Schedule(newConflict, newClassList);
//                filterSchedule.add(newSchedule);
//            }
//
//            Collections.sort(filterSchedule);
//            resultSchedule = new ArrayList<Schedule>(filterSchedule);
//            if(filterSchedule.get(0).getNumberConflicts() < bestConflict) {
//                bestConflict = filterSchedule.get(0).getNumberConflicts();
//            }
//        }
//
//        return resultSchedule;
//    }

    public static List<Schedule> crossover(List<Schedule> currentScheduleList, int times) throws Exception {
        Random random = new Random();
        List<Schedule> filterSchedule = new ArrayList<Schedule>(); // Sau khi loại tệ và thay bằng crossover schedule
        int N = currentScheduleList.size(); // N là số lượng schedule trong list schedule
        // System.out.println(N);
        for (int i = 0; i < N - (times / 2); i++)
            filterSchedule.add(currentScheduleList.get(i)); // Bỏ đi Times / 2 schedule tệ nhất

        for (int T = 0; T < N; T += 2) {
            Schedule currentSchedule = currentScheduleList.get(T);
            Schedule nextSchedule = currentScheduleList.get(T + 1);
            // List<Class> firstHalfSchedule1 = new ArrayList<Class>();
            // List<Class> secondHalfSchedule2 = new ArrayList<Class>();
            List<Class> newClassList = new ArrayList<Class>();
            int numClass = currentSchedule.getScheduleList().size();
            // System.out.println(numClass);
            int pivot = random.nextInt(numClass);
            for (int i = 0; i < pivot; i++)
                newClassList.add(currentSchedule.getScheduleList().get(i));
            for (int i = pivot; i < numClass; i++)
                newClassList.add(nextSchedule.getScheduleList().get(i));

            List<Class> revNewClassList = new ArrayList<Class>();
            for(int i = 0; i < pivot; i++)
                revNewClassList.add(nextSchedule.getScheduleList().get(i));
            for(int i = pivot; i < numClass; i++)
                revNewClassList.add(currentSchedule.getScheduleList().get(i));

            int newConflict = computeConflict(newClassList);
            int revConflict = computeConflict(revNewClassList);
            Schedule newSchedule = new Schedule(newConflict, newClassList);
            Schedule revNewSchedule = new Schedule(revConflict, revNewClassList);
            filterSchedule.add(newSchedule);
            filterSchedule.add(revNewSchedule);
        }

        Collections.sort(filterSchedule);
        List<Schedule> goodSchedule = new ArrayList<Schedule>();
        for(int i = 0; i < N; i++)
            goodSchedule.add(filterSchedule.get(i));
        // System.out.println(filterSchedule.get(0).getScheduleList().size());
        return goodSchedule;
    }

////    public static List<Schedule> mutation(List<Schedule> currentScheduleList, int times) throws Exception {
////        // List<Schedule> population = generatePopulation();
////        List<Schedule> mutationSchedule = new ArrayList<Schedule>();
////        int N = currentScheduleList.size(); // N là số lượng schedule trong list schedule
////        for(int i = 0; i < N; i++)
////            mutationSchedule.add(currentScheduleList.get(i));
////
////
////        for (int T = 0; T < N; T += 2) {
////            Schedule currentSchedule = currentScheduleList.get(T);
////            Schedule nextSchedule = currentScheduleList.get(T + 1);
////            List<Class> classSchedule1 = currentSchedule.getScheduleList();
////            List<Class> classSchedule2 = nextSchedule.getScheduleList();
////
////            int numClass = classSchedule1.size();
////            int mutationId = random.nextInt(numClass);
////            Class mutationSchedule1 = classSchedule1.get(mutationId);
////            Class mutationSchedule2 = classSchedule2.get(mutationId);
////            Class temp = mutationSchedule1;
////            mutationSchedule1 = mutationSchedule2;
////            mutationSchedule2 = temp;
////            classSchedule1.set(mutationId, mutationSchedule1);
////            classSchedule2.set(mutationId, mutationSchedule2);
////
////            int mutationSchedule1Conflict = computeConflict(classSchedule1);
////            int mutationSchedule2Conflict = computeConflict(classSchedule2);
////            Schedule afterMutation1 = new Schedule(mutationSchedule1Conflict, classSchedule1);
////            Schedule afterMutation2 = new Schedule(mutationSchedule2Conflict, classSchedule2);
////            mutationSchedule.add(afterMutation1);
////            mutationSchedule.add(afterMutation2);
////        }
//
////        System.out.println("Before mutation");
////        System.out.println(population.get(0));
//
//        Collections.sort(mutationSchedule);
//        List<Schedule> goodSchedule = new ArrayList<Schedule>();
//        for(int i = 0; i < N; i++)
//            goodSchedule.add(mutationSchedule.get(i));
////        System.out.println("After mutation");
////        System.out.println(mutationSchedule);
//
//        return goodSchedule;
//    }

    public static List<Schedule> mutation(List<Schedule> currentScheduleList, int times) throws Exception {
        List<Schedule> mutationSchedule = new ArrayList<Schedule>();
        int N = currentScheduleList.size(); // N là số lượng schedule trong list schedule
        for(int i = 0; i < N; i++)
            mutationSchedule.add(currentScheduleList.get(i));


        for(int i = 0; i < N; i++) {
            Schedule currentSchedule = currentScheduleList.get(i);
            List<Class> currentClassList = currentSchedule.getScheduleList();
            int chooseClassId = random.nextInt(currentClassList.size());
            List<Room> roomList = ReadData.readRoom();
            List<Room> satisfyRoom = new ArrayList<Room>();
            Class changeClass = currentClassList.get(chooseClassId);

            for(int j = 0; j < roomList.size(); j++) {
                if(roomList.get(j).getCapacity() >= changeClass.getRoom().getCapacity()) {
                    satisfyRoom.add(roomList.get(j));
                }
            }

            if(satisfyRoom.isEmpty())
                continue;

            changeClass.setRoom(satisfyRoom.get(0));
            currentClassList.set(chooseClassId, changeClass);
            int newConflict = computeConflict(currentClassList);
            Schedule updatedSchedule = new Schedule(newConflict, currentClassList);
            mutationSchedule.add(updatedSchedule);
        }

        Collections.sort(mutationSchedule);
        List<Schedule> goodSchedule = new ArrayList<Schedule>();
        for(int i = 0; i < N; i++)
            goodSchedule.add(mutationSchedule.get(i));

        return goodSchedule;

    }

    public static Schedule GeneticAlgorithm(Data data) throws Exception {
        Schedule officialSchedule = new Schedule();
        List<Schedule> currentScheduleList = new ArrayList<Schedule>();
        boolean notDone = true;

        int repeat = 0;
        while(notDone) {
            repeat++;
            currentScheduleList = generatePopulation(data, currentScheduleList);
            // System.out.println(currentScheduleList.get(0).getScheduleList().size());
            Schedule chosenSchedule = currentScheduleList.get(0);
            int conflict = chosenSchedule.getNumberConflicts();

            int t = 0;
            boolean haveSolution = false;
            while (true) {
                t++;
                if (conflict == 0) {
                    haveSolution = true;
                    break;
                }
                // Do crossover
//
//                if(t == MAX_CROSS_MUTATE) {
//                    break;
//                }

                if(t == MAX_POP_SIZE / 2 * (MAX_POP_SIZE / 2 - 1)) {
                    break;
                }

                List<Schedule> crossoverSchedule = crossover(currentScheduleList, MAX_POP_SIZE);
                if (crossoverSchedule.get(0).getNumberConflicts() < conflict) {
                    conflict = crossoverSchedule.get(0).getNumberConflicts();
                    chosenSchedule = crossoverSchedule.get(0);

                    // Do mutation if the crossover thỏa
                    List<Schedule> mutationSchedule = mutation(crossoverSchedule, MAX_POP_SIZE);
                    if (mutationSchedule.get(0).getNumberConflicts() < conflict) {
                        conflict = mutationSchedule.get(0).getNumberConflicts();
                        chosenSchedule = mutationSchedule.get(0);
                    }
                }

                // System.out.println(t);
                // System.out.println(officialSchedule.getNumberConflicts());
                // System.out.println();
            }

            if(haveSolution) {
                officialSchedule = chosenSchedule;
                System.out.println("Number of regenerations");
                System.out.println(repeat);
                System.out.println("Number of generations");
                System.out.println(t);
                notDone = true;
                break;
            }

            System.out.println("Stage " + repeat);
            System.out.println(conflict);
            System.out.println();
        }

//        // Do crossover
//        List<Schedule> crossoverSchedule = crossover(currentScheduleList, 6);
//        if(crossoverSchedule.get(0).getNumberConflicts() < conflict) {
//            conflict = crossoverSchedule.get(0).getNumberConflicts();
//            officialSchedule = crossoverSchedule.get(0);
//
//            // Do mutation if the crossover thỏa
//            List<Schedule> mutationSchedule = mutation(crossoverSchedule, 6);
//            if(mutationSchedule.get(0).getNumberConflicts() < conflict) {
//                conflict = mutationSchedule.get(0).getNumberConflicts();
//                officialSchedule = mutationSchedule.get(0);
//            }
//        }

        return officialSchedule;
    }

    public static void main(String[] args) throws Exception {
        List<Course> courseList = ReadData.readCourse();
        List<Room> roomList = ReadData.readRoom();
        NUM_ROOMS = roomList.size();
        List<ClassTime> classTimeList = ReadData.readClassTime();

        Data data = new Data(courseList, roomList, classTimeList);
        System.out.println(GeneticAlgorithm(data));
        // System.out.println(mutation());
        // List<Schedule> populationDeck = generatePopulation(data);
        // System.out.println(populationDeck.get(0).getScheduleList().size());
        // System.out.println(populationDeck);
        // System.out.println(crossover(populationDeck, 6));


    }
}
