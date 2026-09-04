/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package studentgrademanager;

// GradeManager.java
import java.io.*;
import java.util.*;

public class GradeManager {
    // Haspmap stores students with their ID as the KEY
    private Map<String, Student> students; // Key: studentId, Value: Student object
    private Scanner scanner;
    private static final String DATA_FILE = "students_data.ser";
    
    // Constructor
    public GradeManager() {
        this.students = new HashMap<>();
        this.scanner = new Scanner(System.in);
        loadData(); // Auto-load on startup
    }
    
    // Add a new student
    public void addStudent() {
        System.out.println("\n=== ADD NEW STUDENT ===");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();
        
        if (students.containsKey(id)) {
            System.out.println("Student with this ID already exists!");
            return;
        }
        
        // create the student
        Student student = new Student(name, id);
        
        // add to the hashmap
        students.put(id, student);
        System.out.println("✅ Student added successfully!");
        saveData(); // Auto-save
    }
    
    // Add grade to existing student
    public void addGrade() {
        System.out.println("\n=== ADD GRADE ===");
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();
        
        // find the student
        Student student = students.get(id); // hashmap lookup
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.print("Enter grade (0-100): ");
        try {
            double grade = Double.parseDouble(scanner.nextLine().trim());
            student.addGrade(grade);
            System.out.println("✅ Grade added successfully!");
            saveData();
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade format! Please enter a number.");
        }
    }
    
    // Display all students
    public void displayAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        
        // Sort students by name
        List<Student> sortedStudents = new ArrayList<>(students.values());
        // Comparator.comparing: create a comparator that can compare two students based on thier name alphabetically
        // Student::getName: method reference similar to student -> student.getName()
        sortedStudents.sort(Comparator.comparing(Student::getName)); 
        
        System.out.println("\n📊 STUDENT REPORT");
        System.out.println("─".repeat(70));//Creates a string that consists of 70 dashes
        System.out.printf("%-15s %-15s %-15s %-10s%n", "Name", "ID", "Average", "Grade");
        System.out.println("─".repeat(70));
        
        for (Student student : sortedStudents) {
            System.out.printf("%-15s %-15s %-15.2f %-10s%n", 
                    student.getName(), 
                    student.getStudentId(), 
                    student.calculateAverage(),
                    student.getGradeLetter());
        }
        System.out.println("─".repeat(70));
    }
    
    // Show detailed view of a specific student
    public void viewStudentDetails() {
        System.out.println("\n=== STUDENT DETAILS ===");
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();
        
        Student student = students.get(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n📋 " + student);
        System.out.println("Grades: " + student.getGrades());
        System.out.println("Number of grades: " + student.getGrades().size());
        System.out.println("Average: " + String.format("%.2f", student.calculateAverage()));
        System.out.println("Letter Grade: " + student.getGradeLetter());
    }
    
    // Calculate class statistics
    public void showStatistics() {
        System.out.println("\n=== CLASS STATISTICS ===");
        if (students.isEmpty()) {
            System.out.println("No students to calculate statistics.");
            return;
        }
        
        double totalAverage = 0.0;
        int highestGrade = 0;
        int lowestGrade = 100;
        int studentsWithA = 0;
        int studentsWithF = 0;
        
        for (Student student : students.values()) {
            double avg = student.calculateAverage();
            totalAverage += avg;
            
            if (avg >= 90) studentsWithA++;
            if (avg < 60 && avg > 0) studentsWithF++;
            
            // Track highest and lowest (excluding empty grade lists)
            if (student.getGrades().size() > 0) {
                for (double grade : student.getGrades()) {
                    if (grade > highestGrade) highestGrade = (int)grade;
                    if (grade < lowestGrade) lowestGrade = (int)grade;
                }
            }
        }
        
        double classAverage = totalAverage / students.size();
        
        System.out.println("\n📈 STATISTICS SUMMARY");
        System.out.println("─".repeat(40));
        System.out.printf("Total Students: %d%n", students.size());
        System.out.printf("Class Average: %.2f%n", classAverage);
        System.out.printf("Highest Grade: %d%n", highestGrade);
        System.out.printf("Lowest Grade: %d%n", lowestGrade);
        System.out.printf("Students with A: %d%n", studentsWithA);
        System.out.printf("Students with F: %d%n", studentsWithF);
        System.out.println("─".repeat(40));
    }
    
    // Remove a student
    public void removeStudent() {
        System.out.println("\n=== REMOVE STUDENT ===");
        System.out.print("Enter student ID to remove: ");
        String id = scanner.nextLine().trim();
        
        Student removed = students.remove(id);
        if (removed != null) {
            System.out.println("✅ Student removed: " + removed.getName());
            saveData();
        } else {
            System.out.println("Student not found!");
        }
    }
    
    // Save data to file using serialization
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
            System.out.println("💾 Data saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }
    
    // Load data from file
    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("ℹ️ No saved data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (Map<String, Student>) ois.readObject();
            System.out.println("📂 Data loaded successfully! Found " + students.size() + " students.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
            students = new HashMap<>();
        }
    }
    
    
    public void displayMenu() {
        System.out.println("\n📚 STUDENT GRADE MANAGER");
        System.out.println("═".repeat(40));
        System.out.println("1. Add New Student");
        System.out.println("2. Add Grade to Student");
        System.out.println("3. Display All Students");
        System.out.println("4. View Student Details");
        System.out.println("5. Class Statistics");
        System.out.println("6. Remove Student");
        System.out.println("7. Save & Exit");
        System.out.println("═".repeat(40));
        System.out.print("Choose an option: ");
    }
    
    public void run() {
        System.out.println("🎓 Welcome to Student Grade Manager!");
        System.out.println("Type 'exit' at any time to quit.\n");
        
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("exit")) {
                saveData();
                System.out.println("👋 Goodbye!");
                break;
            }
            
            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    addGrade();
                    break;
                case "3":
                    displayAllStudents();
                    break;
                case "4":
                    viewStudentDetails();
                    break;
                case "5":
                    showStatistics();
                    break;
                case "6":
                    removeStudent();
                    break;
                case "7":
                    saveData();
                    System.out.println("👋 Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid option! Please try again.");
            }
            
            // Wait for user to press Enter to continue
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        scanner.close();
    }
    
    public static void main(String[] args) {
        GradeManager manager = new GradeManager();
        manager.run();
    }
}
