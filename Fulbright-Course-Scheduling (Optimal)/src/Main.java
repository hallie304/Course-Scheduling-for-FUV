import java.util.*;

/**
 * Main driver class of the project. Users using this class to run the program
 * of course scheduling
 * 
 * To produce a schedule course, this program will:
 * 1. Create all possible slots (including class time and room).
 * 2. Schedule for instructors' teaching courses with priority to.
 * 
 * @author Le Thi Hong Ha
 * @author Than Doan Thuan
 */
public class Main {
    private static Random random;

    // This three constants are used to store the three inputs paths
    private static final String COURSE_OFFERING_PATH = "C:\\Users\\Thuan\\Documents\\GitHub\\Fulbright-Course-Scheduling\\Fall2022_Full .csv";
    private static final String ROOM_PATH = "C:\\Users\\Thuan\\Documents\\GitHub\\Fulbright-Course-Scheduling\\Room.csv";
    private static final String CLASS_TIME_PATH = "C:\\Users\\Thuan\\Documents\\GitHub\\Fulbright-Course-Scheduling\\Time.csv";

    // These constants are used to store name
    private static final String MAKER_SPACE = "Maker Space";
    private static final String SCIENCE_LAB = "Science Lab";
    private static final String ONLINE = "Online";
    private static final String ENGINEER_CODE = "ENG";
    private static final String INTEGRATED_SCIENCE_CODE = "IS";
    private static final String DST_CODE = "CORE_105";
    private static final String SI_CODE = "CORE 104";

    // These constants use to store numerical value (ie: capacity)
    private static final int LARGE_CAP = 60;
    private static final int SMALL_CAP = 30;

    // These global variables are used to store the main data structure using
    // in this project (ie: optimal strategy heuristic)
    private static List<Room> roomList;
    private static List<ClassTime> classTimeList;
    private static List<Course> courseList;
    private static Map<Course, Boolean> checkStatus;
    private static List<PriorityQueue<Slot>> allSlot;
    private static List<Class> answerSpace;
    private static List<List<Slot>> allOnlineSlot;
    private static List<Instructor> instructorList;
    private static Queue<Slot> mergeAllSlot;

    /**
     * A helper method used to schedule online courses
     * @param course the current course to be scheduled
     * @param courseOrder the type of the slot needed to schedule (ie: Mon Wed)
     */
    public static void scheduleOnlineCourse(Course course, int courseOrder) {
        random = new Random(); // Initialize the random class to put random timeslot
        int slotId = random.nextInt(allOnlineSlot.get(courseOrder).size());
        // Create a class for the course, meaning assign the value of the online
        // slot for the online course
        Class newClass = new Class(course, allOnlineSlot.get(courseOrder).get(slotId));
        checkStatus.put(course, true); // Mark the course has been scheduled
        answerSpace.add(newClass); // Add the scheduled course to the answer set
    }

    /**
     * A helper method used to schedule Engineering courses. It is used to solve the soft
     * constraint, meaning that Engineering courses are preffered to learn in the Maker Space
     * @param course the current course to be scheduled
     * @param courseOrder the type of the slot needed to schedule (ie: Mon Wed)
     */
    public static void scheduleEngineer(Course course, int courseOrder) {
        List<Slot> uselessSlot = new ArrayList<Slot>();
        Slot bestCurrentSlot = null;

        // Repeatition until successfully finding the Maker Space to fit the
        // Engineering course, or when we are running out of slots
        while(!allSlot.get(courseOrder).isEmpty()) {
            if(allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(MAKER_SPACE)) {
                // Found the suitable room, store it and then remove from the slots
                bestCurrentSlot = allSlot.get(courseOrder).remove();
                break;
            } else { // Otherwise, add to the temp list for the other usage
                uselessSlot.add(allSlot.get(courseOrder).remove());
            }
        }

        // In case we can find out the slot for the course and the course has
        // not yet be scheduled before, we would create the new scheduled class
        // for that course, and then add it to the solution set.
        if(bestCurrentSlot != null && !checkStatus.get(course)) {
            checkStatus.put(course, true);
            Class newClass = new Class(course, bestCurrentSlot);
            answerSpace.add(newClass);
        }
        
        // Restore the slots that are not fitted for the use case for other use!
        for(int k = 0; k < uselessSlot.size(); k++)
            allSlot.get(courseOrder).add(uselessSlot.get(k));    
    }

    /**
     * A helper method used to schedule Science courses. It is used to solve the soft
     * constraint, meaning that Science courses are preffered to learn in the Science Lab
     * @param course the current course to be scheduled
     * @param courseOrder the type of the slot needed to schedule (ie: Mon Wed)
     */
    public static void scheduleScience(Course course, int courseOrder) {
        List<Slot> uselessSlot = new ArrayList<Slot>();
        Slot bestCurrentSlot = null;

        // Repetition until successfully finding the Science Lab room to fit the
        // Science course, or when we are running out of slots
        while(!allSlot.get(courseOrder).isEmpty()) {
            // Found the suitable room, store it and then remove from the slots
            if(allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(SCIENCE_LAB)) {
                bestCurrentSlot = allSlot.get(courseOrder).remove();
                break;
            } else { // Otherwise, add to the temp list for the other usage
                uselessSlot.add(allSlot.get(courseOrder).remove());
            }
        }

        // In case we can find out the slot for the course and the course has
        // not yet be scheduled before, we would create the new scheduled class
        // for that course, and then add it to the solution set.
        if(bestCurrentSlot != null && !checkStatus.get(course)) {
            checkStatus.put(course, true);
            Class newClass = new Class(course, bestCurrentSlot);
            answerSpace.add(newClass);
        }

        // Restore the slots that are not fitted for the use case for other use!
        for(int k = 0; k < uselessSlot.size(); k++)
            allSlot.get(courseOrder).add(uselessSlot.get(k));
    }

    /**
     * A helper method to schedule a normal class (ie: not using any special facilities)
     * @param course the current course to be scheduled
     * @param courseOrder the type of the slot needed to schedule (ie: Mon Wed)
     */
    public static void generalSchedule(Course course, int courseOrder) {
        // The course capacity is small. Thus, we should prioritize the large
        // class for the high capacity class by arranging small classes to the
        // small rooms.
        if (course.getCapacity() <= SMALL_CAP) { 
            List<Slot> uselessSlot = new ArrayList<Slot>();
            Slot bestCurrentSlot = null;

            // Greedy to find the small classrooms to fit the small capacity
            // courses by looking at the priority queue (max heap)
            while (!allSlot.get(courseOrder).isEmpty()) {
                if (allSlot.get(courseOrder).peek().getRoom().getCapacity() == SMALL_CAP && !allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(MAKER_SPACE) && !allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(SCIENCE_LAB)) { // Nếu tìm đợc -> Lưu lại để tí nữa xếp vào
                    bestCurrentSlot = allSlot.get(courseOrder).remove();
                    break;
                } else { // If the current classroom is big, put it somewhere
                         // for later use.
                    uselessSlot.add(allSlot.get(courseOrder).remove()); 
                }
            }
            
            // If there is no small classroom remaining, and the conditiojn of
            // big classroom is still fitted, we can fit the small class to
            // the big classroom without violating the constraints.
            if (bestCurrentSlot == null && !uselessSlot.isEmpty()) {
                bestCurrentSlot = uselessSlot.get(uselessSlot.size() - 1);
                uselessSlot.remove(uselessSlot.size() - 1);
            }

            // If the slot is found and the course has not yet been scheduled,
            // we create a class to schedule for that course and also add the
            // class to the solution set.
            if (bestCurrentSlot != null && !checkStatus.get(course)) {
                Class newClass = new Class(course, bestCurrentSlot);
                checkStatus.put(course, true);
                answerSpace.add(newClass);
            }

            // Restore the slots that are not fitted for the use case for other use!
            for (int k = 0; k < uselessSlot.size(); k++) {
                allSlot.get(courseOrder).add(uselessSlot.get(k));
            }
        }
        
        // Otherwise, for the big class, we should only just arrange to the big
        // classroom. It means that the slot at the top of the max heap is fine.
        else {
            List<Slot> uselessSlot = new ArrayList<Slot>();
            Slot bestCurrentSlot = null;
            
            // Find the room to fit. However, we also need to keep the functional
            // room like Maker Space and Science Lab for the special course.
            while (!allSlot.get(courseOrder).isEmpty()) {
                if (allSlot.get(courseOrder).peek().getRoom().getCapacity() == LARGE_CAP && !allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(MAKER_SPACE) && !allSlot.get(courseOrder).peek().getRoom().getClassroom().equals(SCIENCE_LAB)) { 
                    bestCurrentSlot = allSlot.get(courseOrder).remove();
                    break;
                } else { // If the room is functional, we can put it somewhere
                         // for reservation.
                    uselessSlot.add(allSlot.get(courseOrder).remove()); 
                }
            }
            
            // If the slot has been assigned and the class has not yet been
            // scheduled, put it somewhere for later restoration.
            if (bestCurrentSlot != null && !checkStatus.get(course)) {
                Class newClass = new Class(course, bestCurrentSlot);
                checkStatus.put(course, true);
                answerSpace.add(newClass);
            }

            // Restore the slots that are not fitted for the use case for other use!
            for (int k = 0; k < uselessSlot.size(); k++) {
                allSlot.get(courseOrder).add(uselessSlot.get(k));
            }
        }
    }

    /**
     * This method helps to generate all of the timeslot which is all of the
     * combinations in terms of classtime and room
     * @param roomList the list of rooms offered by Fulbright
     * @param classTimeList the list of class time commonly proposed by Fulrbight
     * @return the list of the heaps (ie: different types of slots) storing all
     * of the slots that is sorted by the classroom capacity for greedy
     */
    public static List<PriorityQueue<Slot>> generateAllTimeSlot(List<Room> roomList, List<ClassTime> classTimeList) {
        // Convection: Mon-Wed is index 0. Tue-Thu is index 1. Fri is index 2

        // Create a list of heaps storing all of the possible time slots which
        // are all combinations of time and room
        List<PriorityQueue<Slot>> allTimeSlot = new ArrayList<PriorityQueue<Slot>>();
        for(int i = 0; i < 3; i++) 
            allTimeSlot.add(new PriorityQueue<Slot>(Collections.reverseOrder()));
        
        // Create a nested list contain three lists of slots
        for(int i = 0; i < roomList.size(); i++) {
            for(int j = 0; j < classTimeList.size(); j++) {
                Room currentRoom = roomList.get(i);
                ClassTime currentClassTime = classTimeList.get(j);
                Slot newSlot = new Slot(currentClassTime, currentRoom);
                
                // Conditional divides the time into three types said above
                if(currentClassTime.getDate().get(0).equals("Mon") && currentClassTime.getDate().get(1).equals("Wed")) 
                    allTimeSlot.get(0).add(newSlot);
                else if(currentClassTime.getDate().get(0).equals("Tue") && currentClassTime.getDate().get(1).equals("Thu")) 
                    allTimeSlot.get(1).add(newSlot);
                else if(currentClassTime.getDate().get(0).equals("Fri")) 
                    allTimeSlot.get(2).add(newSlot);
            }
        }

        return allTimeSlot;
    }

    /**
     * This method helps to generate all of the timeslot which is all of the
     * combinations in terms of classtime with the room is default as online
     * venue (ie: Zoom or Teams) to make a space for online courses
     * @param classTimeList the list of class time commonly proposed by Fulrbight
     * @return a list of slot list for different types of slots
     */
    public static List<List<Slot>> generateAllOnlineSlot(List<ClassTime> classTimeList) {
        // Convection: Mon-Wed is index 0. Tue-Thu is index 1. Fri is index 2

        List<List<Slot>> allOnlineSlot = new ArrayList<List<Slot>>();
        for(int i = 0; i < 3; i++)
            allOnlineSlot.add(new ArrayList<Slot>());

        Room fixedVenue = new Room(ONLINE, LARGE_CAP); // Room is fixed to Online
        for(int i = 0; i < classTimeList.size(); i++) {
            ClassTime currentClassTime = classTimeList.get(i);
            Slot newSlot = new Slot(currentClassTime, fixedVenue);

            // Conditional divides the time into three types said above
            if(currentClassTime.getDate().get(0).equals("Mon") && currentClassTime.getDate().get(1).equals("Wed")) 
                allOnlineSlot.get(0).add(newSlot);
            else if(currentClassTime.getDate().get(0).equals("Tue") && currentClassTime.getDate().get(1).equals("Thu")) 
                allOnlineSlot.get(1).add(newSlot);
            else if(currentClassTime.getDate().get(0).equals("Fri")) 
                allOnlineSlot.get(2).add(newSlot);
        }

        return allOnlineSlot;
    }

    /**
     * From the course offering, mapping each intructor with his or her teaching
     * course list
     * @param courseList list of course offfering in the semester/term
     * @return the list of instructors with his or her teaching courses
     */
    public static List<Instructor> generateInstructorTeachingSet(List<Course> courseList) {
        List<Instructor> instructorList = new ArrayList<Instructor>();
        // Create a map to map the instructor with the list of courses
        Map<String, List<Course>> mappingTeacher = new HashMap<String, List<Course>>();
        for(int i = 0; i < courseList.size(); i++) {
            Course currentCourse = courseList.get(i); // Take out the current course
            // Go to the instructors list in each course to split all instructors
            // and process one by one.
            for(int j = 0; j < currentCourse.getFacultyName().size(); j++) {
                String currentFaculty = currentCourse.getFacultyName().get(j);
                // If the instructor has appeared already.
                if(mappingTeacher.containsKey(currentFaculty)) {
                    List<Course> mappingCourse = mappingTeacher.get(currentFaculty);
                    mappingCourse.add(currentCourse);
                    mappingTeacher.put(currentFaculty, mappingCourse);
                } else { // Otherwise, create a new value to map
                    List<Course> mappingCourse = new ArrayList<Course>();
                    mappingCourse.add(currentCourse);
                    mappingTeacher.put(currentFaculty, mappingCourse);
                }
            }
        }

        // Convert from the map to the list for easy iteration in the next step
        for(String instructor: mappingTeacher.keySet()) {
            Instructor newInstructor = new Instructor(instructor, mappingTeacher.get(instructor));
            instructorList.add(newInstructor);
        }

        return instructorList;
    }

    /**
     * A method to deal with instructors teaching 2 courses per semester
     */
    public static void solveTwoClassInstructor() {
        for(int i = 0; i < instructorList.size(); i++) {
            Instructor currentInstructor = instructorList.get(i);
            int numberTeachingCourse = currentInstructor.getTeachingList().size();
            if (numberTeachingCourse == 2) { // Prioritize to be on Mon-Wed and Tue-Thu for symmetry purpose of the design
                Course firstCourse = currentInstructor.getTeachingList().get(0);
                Course secondCourse = currentInstructor.getTeachingList().get(1);

                // If the course is online, we can assign the class to the online venue so that
                // we can reduce slot for other courses
                if(firstCourse.isOnline() && !checkStatus.get(firstCourse)) {
                    scheduleOnlineCourse(firstCourse, 0);
                }

                // Process the Engineering course with priority (ie: functional room Maker Space)
                if(firstCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || firstCourse.getCourseId().indexOf(DST_CODE) != -1) {
                    scheduleEngineer(firstCourse, 0);
                }

                // Process the Science course with priority (ie: functional room Science Lab)
                if(firstCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || firstCourse.getCourseId().indexOf(SI_CODE) != -1) {
                    scheduleScience(firstCourse, 0);
                }

                // Schedule for the normal courses
                generalSchedule(firstCourse, 0); 

                // If the course is online, we can assign the class to the online venue so that
                // we can reduce slot for other courses
                if(secondCourse.isOnline() && !checkStatus.get(secondCourse)) {
                    scheduleOnlineCourse(secondCourse, 1);
                }

                // Process the Engineering course with priority (ie: functional room Maker Space)
                if(secondCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || secondCourse.getCourseId().indexOf(DST_CODE) != -1) {
                    scheduleEngineer(secondCourse, 1);
                }

                // Process the Science course with priority (ie: functional room Science Lab)
                if(secondCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || secondCourse.getCourseId().indexOf(SI_CODE) != -1) {
                scheduleScience(secondCourse, 1);
                }

                // Schedule for the normal courses
                generalSchedule(secondCourse, 1);
            }
        }
    }

    /**
     * A method to deal with instructors teaching 3 courses per semester
     */
    public static void solveThreeClassInstructor() {
        for(int i = 0; i < instructorList.size(); i++) {
            Instructor currentInstructor = instructorList.get(i);
            int numberTeachingCourse = currentInstructor.getTeachingList().size();
            if (numberTeachingCourse == 3) { 
                // There are three courses with three types of slots. Thus, each
                // course will be fitted in each type of slot.

                Course firstCourse = currentInstructor.getTeachingList().get(0);
                Course secondCourse = currentInstructor.getTeachingList().get(1);
                Course thirdCourse = currentInstructor.getTeachingList().get(2);

                // If the course is online, we can assign the class to the online venue so that
                // we can reduce slot for other courses
                if(firstCourse.isOnline() && !checkStatus.get(firstCourse)) {
                    scheduleOnlineCourse(firstCourse, 0);
                }

                // Process the Engineering course with priority (ie: functional room Maker Space)
                if(firstCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || firstCourse.getCourseId().indexOf(DST_CODE) != -1) {
                    scheduleEngineer(firstCourse, 0);
                }

                // Process the Science course with priority (ie: functional room Science Lab)
                if(firstCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || firstCourse.getCourseId().indexOf(SI_CODE) != -1) {
                    scheduleScience(firstCourse, 0);
                }

                // Schedule for the normal courses
                generalSchedule(firstCourse, 0);

                // If the course is online, we can assign the class to the online venue so that
                // we can reduce slot for other courses
                if(secondCourse.isOnline() && !checkStatus.get(secondCourse)) {
                    scheduleOnlineCourse(secondCourse, 1);
                }

                // Process the Engineering course with priority (ie: functional room Maker Space)
                if(secondCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || secondCourse.getCourseId().indexOf(DST_CODE) != -1) {
                    scheduleEngineer(secondCourse, 1);
                }

                // Process the Science course with priority (ie: functional room Science Lab)
                if(secondCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || secondCourse.getCourseId().indexOf(SI_CODE) != -1) {
                    scheduleScience(secondCourse, 1);
                }

                // Schedule for the normal courses
                generalSchedule(secondCourse, 1);

                // If the course is online, we can assign the class to the online venue so that
                // we can reduce slot for other courses
                if(thirdCourse.isOnline() && !checkStatus.get(thirdCourse)) {
                    scheduleOnlineCourse(thirdCourse, 2);
                }

                // Process the Engineering course with priority (ie: functional room Maker Space)
                if(thirdCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || thirdCourse.getCourseId().indexOf(DST_CODE) != -1) {
                    scheduleEngineer(thirdCourse, 2);
                }

                // Process the Science course with priority (ie: functional room Science Lab)
                if(thirdCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || thirdCourse.getCourseId().indexOf(SI_CODE) != -1) {
                    scheduleScience(thirdCourse, 2);
                }

                // Schedule for the normal courses
                generalSchedule(thirdCourse, 2);
            }
        }
    }

    /**
     * Convert a differnt type heaps to only one heaps containing all remaining slots
     * @return the merge heap
     */
    public static Queue<Slot> typeToAll() {
        // Merge all slots into one heap because now it is not neccessary to
        // divide the timeslot into three types anymore.
        mergeAllSlot = new PriorityQueue<Slot>(Collections.reverseOrder());

        for(int i = 0; i < allSlot.size(); i++) {
            Queue<Slot> currentTypeSlot = allSlot.get(i);
            while(!currentTypeSlot.isEmpty())
                mergeAllSlot.add(currentTypeSlot.remove());
        }

        return mergeAllSlot;
    }

    /**
     * A method to deal with instructors teaching 1 course per semester
     */
    public static void solveSingleClassInstructor() {
        // Go through each instructor and process similar to two classes and 
        // three classes. Online course -> Engineering -> Science -> Normal classes
        for(int i = 0; i < instructorList.size(); i++) {
            Instructor currentInstructor = instructorList.get(i);
            int numberTeachingCourse = currentInstructor.getTeachingList().size();
            if(numberTeachingCourse == 2 || numberTeachingCourse == 3)
                continue;

            Course onlyCourse = currentInstructor.getTeachingList().get(0);
            // If the course is online, we can assign the class to the online venue so that
            // we can reduce slot for other courses
            if(onlyCourse.isOnline() && !checkStatus.get(onlyCourse)) {
                random = new Random();
                int typeSlot = random.nextInt(3);
                int slotId = random.nextInt(allOnlineSlot.get(typeSlot).size());
                Class newClass = new Class(onlyCourse, allOnlineSlot.get(typeSlot).get(slotId));
                checkStatus.put(onlyCourse, true);
                answerSpace.add(newClass);
            }

            // Process the Engineering course with priority (ie: functional room Maker Space)
            if(onlyCourse.getCourseId().indexOf(ENGINEER_CODE) != -1 || onlyCourse.getCourseId().indexOf(DST_CODE) != -1) {
                List<Slot> uselessSlot = new ArrayList<Slot>();
                Slot bestCurrentSlot = null;

                while(!mergeAllSlot.isEmpty()) {
                    if(mergeAllSlot.peek().getRoom().getClassroom().equals(MAKER_SPACE)) {
                        bestCurrentSlot = mergeAllSlot.remove();
                        break;
                    } else {
                        uselessSlot.add(mergeAllSlot.remove());
                    }
                }

                if(bestCurrentSlot != null && !checkStatus.get(onlyCourse)) {
                    checkStatus.put(onlyCourse, true);
                    Class newClass = new Class(onlyCourse, bestCurrentSlot);
                    answerSpace.add(newClass);
                }

                for(int k = 0; k < uselessSlot.size(); k++)
                    mergeAllSlot.add(uselessSlot.get(k));
            }

            // Process the Science course with priority (ie: functional room Science Lab)
            // (the process of thoughts is identical to 2 classes and 3 classes)
            if(onlyCourse.getCourseId().indexOf(INTEGRATED_SCIENCE_CODE) != -1 || onlyCourse.getCourseId().indexOf(SI_CODE) != -1) {
                List<Slot> uselessSlot = new ArrayList<Slot>();
                Slot bestCurrentSlot = null;

                while(!mergeAllSlot.isEmpty()) {
                    if(mergeAllSlot.peek().getRoom().getClassroom().equals(SCIENCE_LAB)) {
                        bestCurrentSlot = mergeAllSlot.remove();
                        break;
                    } else {
                        uselessSlot.add(mergeAllSlot.remove());
                    }
                }

                if(bestCurrentSlot != null && !checkStatus.get(onlyCourse)) {
                    checkStatus.put(onlyCourse, true);
                    Class newClass = new Class(onlyCourse, bestCurrentSlot);
                    answerSpace.add(newClass);
                }

                for(int k = 0; k < uselessSlot.size(); k++)
                    mergeAllSlot.add(uselessSlot.get(k));
            }

            // Normal only course. The process off thoughts is identical to
            // the previous functions (ie: 2 classes and 3 classes)
            if (onlyCourse.getCapacity() <= SMALL_CAP) {
                List<Slot> uselessSlot = new ArrayList<Slot>();
                Slot bestCurrentSlot = null;

                while (!mergeAllSlot.isEmpty()) {
                    if (mergeAllSlot.peek().getRoom().getCapacity() == SMALL_CAP && !mergeAllSlot.peek().getRoom().getClassroom().equals(MAKER_SPACE) && !mergeAllSlot.peek().getRoom().getClassroom().equals(SCIENCE_LAB)) { 
                        bestCurrentSlot = mergeAllSlot.remove();
                        break;
                    } else {
                        uselessSlot.add(mergeAllSlot.remove()); 
                    }
                }

                if (bestCurrentSlot == null && !uselessSlot.isEmpty()) {
                    bestCurrentSlot = uselessSlot.get(uselessSlot.size() - 1);
                    uselessSlot.remove(uselessSlot.size() - 1);
                }

                if (bestCurrentSlot != null && !checkStatus.get(onlyCourse)) {
                    Class newClass = new Class(onlyCourse, bestCurrentSlot);
                    checkStatus.put(onlyCourse, false);
                    answerSpace.add(newClass);
                }

                for (int k = 0; k < uselessSlot.size(); k++) {
                    mergeAllSlot.add(uselessSlot.get(k));
                }
            }

            else {
                Slot bestCurrentSlot = null;
                if (!mergeAllSlot.isEmpty() && mergeAllSlot.peek().getRoom().getCapacity() == LARGE_CAP) {
                    bestCurrentSlot = mergeAllSlot.remove();
                }

                if (bestCurrentSlot != null && !checkStatus.get(onlyCourse)) {
                    Class newClass = new Class(onlyCourse, bestCurrentSlot);
                    checkStatus.put(onlyCourse, true);
                    answerSpace.add(newClass);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Obtain the list of course offerings, list of rooms, and list of class
        // time from the testcases. Read these inputs from the self-defined 
        // helper class so called ReadData.
        courseList = ReadData.readCourse(COURSE_OFFERING_PATH);
        roomList = ReadData.readRoom(ROOM_PATH);
        classTimeList = ReadData.readClassTime(CLASS_TIME_PATH);

        // Shuffle the room list and the class time to create some randomness
        Collections.shuffle(roomList); 
        Collections.shuffle(classTimeList);

        // Initiallize the marking map for all courses. In details, false means
        // has not yet scheduled while true means has been scheduled
        checkStatus = new HashMap<Course, Boolean>();
        for(int i = 0; i < courseList.size(); i++)
            checkStatus.put(courseList.get(i), false);

        // Generate all of the combinations of rooms and time, both online and
        // on-campus or hybrid version of course.
        allSlot = generateAllTimeSlot(roomList, classTimeList);
        allOnlineSlot = generateAllOnlineSlot(classTimeList);
        instructorList = generateInstructorTeachingSet(courseList);

        // Use this array list to store the solution schedule: a list of classes
        answerSpace = new ArrayList<Class>();

        // Greedy! Solve three classes first -> Then two classes -> And the last
        // is calling one class.
        solveThreeClassInstructor(); 
        solveTwoClassInstructor();

        mergeAllSlot = typeToAll();
        solveSingleClassInstructor();

        // The schedule is successful if the answer space size is equilvalent
        // to the length of the course offering.
        if(answerSpace.size() == courseList.size()) {
            System.out.println("The number of courses can be scheduled");
            System.out.println(answerSpace.size()); // State the number of courses in the answer
            System.out.println("The detailed list of scheduling courses");
            System.out.println(answerSpace); // Print out all of the answers
        } else {
            System.out.println("NO");
        }
    }
}
