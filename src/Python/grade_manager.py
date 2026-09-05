# grade_manager.py
import json
import os
from typing import Dict, List, Optional
from student import Student

class GradeManager:
    """Main application class for managing student grades"""
    
    DATA_FILE = "students_data.json"
    
    def __init__(self):
        self.students: Dict[str, Student] = {}
        self.load_data()
    
    def add_student(self, name: str, student_id: str) -> bool:
        if not name.strip():
            print("Name cannot be empty!")
            return False
        
        if student_id in self.students:
            print("Student with this ID already exists!")
            return False
        
        student = Student(name.strip(), student_id.strip())
        self.students[student_id] = student
        print(f"✅ Student '{name}' added successfully!")
        self.save_data()
        return True
    
    def add_grade(self, student_id: str, grade: float) -> bool:
        student = self.students.get(student_id)
        if not student:
            print("Student not found!")
            return False
        
        if student.add_grade(grade):
            print(f"✅ Grade added successfully for {student.name}!")
            self.save_data()
            return True
        return False
    
    def get_all_students(self) -> List[Student]:
        return sorted(self.students.values(), key=lambda s: s.name.lower())
    
    def display_all_students(self) -> None:
        print("\n=== ALL STUDENTS ===")
        if not self.students:
            print("No students registered.")
            return
        
        students = self.get_all_students()
        
        print("\n📊 STUDENT REPORT")
        print("-" * 70)
        print(f"{'Name':<15} {'ID':<15} {'Average':<15} {'Grade':<10}")
        print("-" * 70)
        
        for student in students:
            print(f"{student.name:<15} {student.student_id:<15} {student.calculate_average():<15.2f} {student.get_grade_letter():<10}")
        
        print("-" * 70)
    
    def view_student_details(self, student_id: str) -> Optional[Student]:
        student = self.students.get(student_id)
        if not student:
            print("Student not found!")
            return None
        
        print("\n📋 STUDENT DETAILS")
        print("-" * 40)
        print(f"Name: {student.name}")
        print(f"ID: {student.student_id}")
        print(f"Grades: {student.grades}")
        print(f"Number of Grades: {len(student.grades)}")
        print(f"Average: {student.calculate_average():.2f}")
        print(f"Letter Grade: {student.get_grade_letter()}")
        print("-" * 40)
        return student
    
    def show_statistics(self) -> None:
        print("\n=== CLASS STATISTICS ===")
        if not self.students:
            print("No students to calculate statistics.")
            return
        
        total_avg = 0.0
        highest_grade = 0.0
        lowest_grade = 100.0
        students_with_a = 0
        students_with_f = 0
        total_grades = 0
        
        for student in self.students.values():
            avg = student.calculate_average()
            total_avg += avg
            
            if avg >= 90:
                students_with_a += 1
            if avg < 60 and avg > 0:
                students_with_f += 1
            
            if student.grades:
                highest_grade = max(highest_grade, max(student.grades))
                lowest_grade = min(lowest_grade, min(student.grades))
                total_grades += len(student.grades)
        
        class_average = round(total_avg / len(self.students), 2)
        
        print("\n📈 STATISTICS SUMMARY")
        print("-" * 40)
        print(f"Total Students: {len(self.students)}")
        print(f"Total Grades: {total_grades}")
        print(f"Class Average: {class_average:.2f}")
        print(f"Highest Grade: {highest_grade:.2f}")
        print(f"Lowest Grade: {lowest_grade:.2f}")
        print(f"Students with A: {students_with_a}")
        print(f"Students with F: {students_with_f}")
        print("-" * 40)
    
    def remove_student(self, student_id: str) -> bool:
        student = self.students.pop(student_id, None)
        if student:
            print(f"✅ Student '{student.name}' removed successfully!")
            self.save_data()
            return True
        print("Student not found!")
        return False
    
    def save_data(self) -> None:
        try:
            data = {
                student_id: student.to_dict() 
                for student_id, student in self.students.items()
            }
            with open(self.DATA_FILE, 'w') as file:
                json.dump(data, file, indent=4)
            print("💾 Data saved successfully.")
        except Exception as e:
            print(f"❌ Error saving data: {e}")
    
    def load_data(self) -> None:
        if not os.path.exists(self.DATA_FILE):
            print("ℹ️ No saved data found. Starting fresh.")
            return
        
        try:
            with open(self.DATA_FILE, 'r') as file:
                data = json.load(file)
                for student_id, student_data in data.items():
                    self.students[student_id] = Student.from_dict(student_data)
            print(f"📂 Data loaded successfully! Found {len(self.students)} students.")
        except Exception as e:
            print(f"❌ Error loading data: {e}")
            self.students = {}
    
    def display_menu(self) -> None:
        print("\n📚 STUDENT GRADE MANAGER")
        print("=" * 40)
        print("1. Add New Student")
        print("2. Add Grade to Student")
        print("3. Display All Students")
        print("4. View Student Details")
        print("5. Class Statistics")
        print("6. Remove Student")
        print("7. Save & Exit")
        print("=" * 40)
    
    def run(self) -> None:
        print("🎓 Welcome to Student Grade Manager!")
        print("Type 'exit' at any time to quit.\n")
        
        while True:
            self.display_menu()
            choice = input("Choose an option: ").strip().lower()
            
            if choice == 'exit':
                self.save_data()
                print("👋 Goodbye!")
                break
            
            try:
                if choice == '1':
                    name = input("Enter student name: ").strip()
                    if name.lower() == 'exit':
                        continue
                    student_id = input("Enter student ID: ").strip()
                    if student_id.lower() == 'exit':
                        continue
                    self.add_student(name, student_id)
                
                elif choice == '2':
                    student_id = input("Enter student ID: ").strip()
                    if student_id.lower() == 'exit':
                        continue
                    grade_input = input("Enter grade (0-100): ").strip()
                    if grade_input.lower() == 'exit':
                        continue
                    try:
                        grade = float(grade_input)
                        self.add_grade(student_id, grade)
                    except ValueError:
                        print("Invalid grade format! Please enter a number.")
                
                elif choice == '3':
                    self.display_all_students()
                
                elif choice == '4':
                    student_id = input("Enter student ID: ").strip()
                    if student_id.lower() == 'exit':
                        continue
                    self.view_student_details(student_id)
                
                elif choice == '5':
                    self.show_statistics()
                
                elif choice == '6':
                    student_id = input("Enter student ID to remove: ").strip()
                    if student_id.lower() == 'exit':
                        continue
                    self.remove_student(student_id)
                
                elif choice == '7':
                    self.save_data()
                    print("👋 Goodbye!")
                    break
                
                else:
                    print("❌ Invalid option! Please try again.")
            
            except KeyboardInterrupt:
                print("\n\n👋 Goodbye!")
                self.save_data()
                break
            
            except Exception as e:
                print(f"❌ An error occurred: {e}")
            
            print("\nPress Enter to continue...")
            input()

if __name__ == "__main__":
    manager = GradeManager()
    manager.run()