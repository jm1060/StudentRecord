import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Class to represent a course with grade and credits
class Course implements Serializable {
    String grade;
    int credits;

    public Course(String grade, int credits) {
        this.grade = grade;
        this.credits = credits;
    }
}

// Class to represent a student
class Student implements Serializable {
    String name;
    int age;
    String year; // Academic year (Freshman, Sophomore, Junior, Senior)
    String major;
    String minor;
    Map<String, Course> coursesGrades; // Map to store courses and their corresponding (grade, credits)

    // Constructor
    public Student(String name, int age, String year, String major, String minor) {
        this.name = name;
        this.age = age;
        this.year = year;
        this.major = major;
        this.minor = minor;
        this.coursesGrades = new HashMap<>();
    }

    // Method to add a course with grade and credits
    public void addCourse(String courseName, String grade, int credits) {
        coursesGrades.put(courseName, new Course(grade, credits));
    }

    // Method to edit a course grade and credits
    public void editCourseGrade(String courseName, String newGrade, int newCredits) {
        if (coursesGrades.containsKey(courseName)) {
            coursesGrades.put(courseName, new Course(newGrade, newCredits));
            System.out.println("Grade and credits updated for " + courseName);
        } else {
            System.out.println("Course not found.");
        }
    }

    // Method to remove a course
    public void removeCourse(String courseName) {
        if (coursesGrades.remove(courseName) != null) {
            System.out.println("Course removed successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    // Method to calculate GPA
    public double calculateGPA() {
        if (coursesGrades.isEmpty()) return 0.0;

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Course course : coursesGrades.values()) {
            double gradePoints = convertGradeToPoints(course.grade);
            totalGradePoints += gradePoints * course.credits;
            totalCredits += course.credits;
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    // Convert letter grade to grade points
    private double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    // Method to print student details and courses/grades
    public void printStudent() {
        int totalCredits = 0;
        System.out.println("Name: " + name + ", Age: " + age + ", Year: " + year + ", Major: "+ major + ", Minor: "+minor);
        if (coursesGrades.isEmpty()) {
            System.out.println("No courses added.");
        } else {
            for (Map.Entry<String, Course> entry : coursesGrades.entrySet()) {
                System.out.println("Course: " + entry.getKey() + ", Grade: " + entry.getValue().grade + ", Credits: " + entry.getValue().credits);
                totalCredits += entry.getValue().credits;
            }
        }
        double gpa = calculateGPA();
        boolean deanslist = false;
        if(gpa < 2.0 && totalCredits > 0)
        {
            System.out.println("* Academic Probation");
        }
        else if(gpa >= 3.4 && totalCredits >=12){
            for(Map.Entry<String, Course> e : coursesGrades.entrySet())
            {
                if(e.getValue().grade.equals("D") || e.getValue().grade.equals("F"))
                {
                    deanslist = false;
                    break;
                }
                else
                {
                    deanslist = true;
                }
            }
            if(deanslist)
            {
                System.out.println("* Dean's List");
            }
        }
        System.out.println("\n");
    }
}

// Main class to manage the student management system
public class StudentManagementSystem {

    private static final String FILE_NAME = "students.ser";
    private static ArrayList<Student> students;

    // Method to load students from file
    private static void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
            System.out.println("Student data loaded successfully.");
        } catch (FileNotFoundException e) {
            students = new ArrayList<>();
            System.out.println("No existing data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            students = new ArrayList<>();
            System.out.println("Error loading data. Starting fresh.");
        }
    }

    // Method to save students to file
    private static void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
            System.out.println("Student data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // Method to add a new student
    public static void addStudent(String name, int age, String year, String major, String minor) {
        students.add(new Student(name, age, year, major, minor));
        System.out.println("Student added successfully.");
    }

    // Method to add a course to a student
    public static void addCourseToStudent(int studentIndex, String courseName, String grade, int credits) {
        if (studentIndex >= 0 && studentIndex < students.size()) {
            students.get(studentIndex).addCourse(courseName, grade, credits);
            System.out.println("Course and grade added successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    // Method to edit a student's course grade and credits
    public static void editStudentCourseGrade(int studentIndex, String courseName, String newGrade, int newCredits) {
        if (studentIndex >= 0 && studentIndex < students.size()) {
            students.get(studentIndex).editCourseGrade(courseName, newGrade, newCredits);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Method to remove a course from a student
    public static void removeStudentCourse(int studentIndex, String courseName) {
        if (studentIndex >= 0 && studentIndex < students.size()) {
            students.get(studentIndex).removeCourse(courseName);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Method to calculate GPA for a student
    public static void calculateGPAForStudent(int studentIndex) {
        if (studentIndex >= 0 && studentIndex < students.size()) {
            double gpa = students.get(studentIndex).calculateGPA();
            System.out.format("GPA for " + students.get(studentIndex).name + ": " + "%.2f", gpa);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Method to print all students
    public static void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            for (Student student : students) {
                student.printStudent();
            }
        }
    }

    // Main method for the application
    public static void main(String[] args) {
        loadStudents(); // Load data at the start of the program
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Add Course to Student");
            System.out.println("3. Edit Student Course Grade and Credits");
            System.out.println("4. Remove Course from Student");
            System.out.println("5. Calculate GPA for Student");
            System.out.println("6. Print All Students");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter student age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter student year (Freshman, Sophomore, Junior, Senior): ");
                    String year = scanner.nextLine();
                    System.out.println("Enter student's major:");
                    String major = scanner.nextLine();
                    System.out.println("Enter student's minor");
                    String minor = scanner.nextLine();
                    addStudent(name, age, year, major, minor);
                    break;

                case 2:
                    System.out.print("Enter student index: ");
                    int studentIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter grade: ");
                    String grade = scanner.nextLine();
                    System.out.print("Enter credits: ");
                    int credits = scanner.nextInt();
                    addCourseToStudent(studentIndex, courseName, grade, credits);
                    break;

                case 3:
                    System.out.print("Enter student index: ");
                    studentIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter course name: ");
                    courseName = scanner.nextLine();
                    System.out.print("Enter new grade: ");
                    String newGrade = scanner.nextLine();
                    System.out.print("Enter new credits: ");
                    int newCredits = scanner.nextInt();
                    editStudentCourseGrade(studentIndex, courseName, newGrade, newCredits);
                    break;

                case 4:
                    System.out.print("Enter student index: ");
                    studentIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter course name to remove: ");
                    courseName = scanner.nextLine();
                    removeStudentCourse(studentIndex, courseName);
                    break;

                case 5:
                    System.out.print("Enter student index: ");
                    studentIndex = scanner.nextInt();
                    calculateGPAForStudent(studentIndex);
                    break;

                case 6:
                    printAllStudents();
                    break;

                case 7:
                    saveStudents();
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
