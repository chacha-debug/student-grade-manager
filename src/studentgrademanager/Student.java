/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package studentgrademanager;

/// Student.java
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String studentId;
    private List<Double> grades;
    
    // Constructor
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.grades = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public List<Double> getGrades() { return grades; }
    
    // Business methods
    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            grades.add(grade);
            System.out.println("Grade added: " + grade);
        } else {
            System.out.println("Invalid grade! Please enter a grade between 0 and 100.");
        }
    }
    
    public double calculateAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }
    
    public String getGradeLetter() {
        double avg = calculateAverage();
        if (avg >= 90) return "A";
        else if (avg >= 80) return "B";
        else if (avg >= 70) return "C";
        else if (avg >= 60) return "D";
        else return "F";
    }
    
    @Override
    public String toString() {
        return String.format("Student: %s (ID: %s) | Average: %.2f | Grade: %s | Grades: %s", 
                name, studentId, calculateAverage(), getGradeLetter(), grades);
    }
}
