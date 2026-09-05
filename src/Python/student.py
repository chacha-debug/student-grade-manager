# student.py
import json
from typing import List, Dict

class Student:
    """Student class representing a student with their grades"""
    
    def __init__(self, name: str, student_id: str):
        self.name = name
        self.student_id = student_id
        self._grades: List[float] = []
    
    @property
    def grades(self) -> List[float]:
        return self._grades.copy()
    
    def add_grade(self, grade: float) -> bool:
        if 0 <= grade <= 100:
            self._grades.append(round(grade, 2))
            return True
        print("Invalid grade! Please enter a grade between 0 and 100.")
        return False
    
    def calculate_average(self) -> float:
        if not self._grades:
            return 0.0
        return round(sum(self._grades) / len(self._grades), 2)
    
    def get_grade_letter(self) -> str:
        avg = self.calculate_average()
        if avg >= 90:
            return 'A'
        elif avg >= 80:
            return 'B'
        elif avg >= 70:
            return 'C'
        elif avg >= 60:
            return 'D'
        else:
            return 'F'
    
    def to_dict(self) -> Dict:
        return {
            'name': self.name,
            'student_id': self.student_id,
            'grades': self._grades
        }
    
    @classmethod
    def from_dict(cls, data: Dict) -> 'Student':
        student = cls(data['name'], data['student_id'])
        for grade in data['grades']:
            student.add_grade(grade)
        return student
    
    def __str__(self) -> str:
        return f"Student: {self.name} (ID: {self.student_id}) | Average: {self.calculate_average():.2f} | Grade: {self.get_grade_letter()}"