import json
from typing import List, Dict, Optional, Any
from dataclasses import dataclass, asdict

# ---------------------------
# Data classes / containers
# ---------------------------
@dataclass
class ClassObj:
    id: int
    name: str

@dataclass
class StudentObj:
    id: int
    name: str
    class_id: int
    marks: int
    gender: str
    age: int
    status: Optional[str] = None  # "Pass" or "Fail"
    rank: Optional[int] = None

@dataclass
class AddressObj:
    id: int
    pin_code: str
    city: str
    student_id: int

# ---------------------------
# In-memory "DB" with file handling
# ---------------------------
class InMemoryDB:
    def __init__(self, classes_file="classes.json", students_file="students.json", addresses_file="addresses.json"):
        self.classes: Dict[int, ClassObj] = {}
        self.students: Dict[int, StudentObj] = {}
        self.addresses: Dict[int, AddressObj] = {}

        self.classes_file = classes_file
        self.students_file = students_file
        self.addresses_file = addresses_file

        # load existing data if available
        self.load_from_files()

        # load sample if files were empty
        if not self.students:
            self.load_sample_data()
            self.save_to_files()

    def save_to_files(self):
        with open(self.classes_file, "w") as f:
            json.dump({cid: asdict(c) for cid, c in self.classes.items()}, f, indent=4)
        with open(self.students_file, "w") as f:
            json.dump({sid: asdict(s) for sid, s in self.students.items()}, f, indent=4)
        with open(self.addresses_file, "w") as f:
            json.dump({aid: asdict(a) for aid, a in self.addresses.items()}, f, indent=4)

    def load_from_files(self):
        try:
            with open(self.classes_file, "r") as f:
                data = json.load(f)
                self.classes = {int(cid): ClassObj(**c) for cid, c in data.items()}
        except FileNotFoundError:
            self.classes = {}

        try:
            with open(self.students_file, "r") as f:
                data = json.load(f)
                self.students = {int(sid): StudentObj(**s) for sid, s in data.items()}
        except FileNotFoundError:
            self.students = {}

        try:
            with open(self.addresses_file, "r") as f:
                data = json.load(f)
                self.addresses = {int(aid): AddressObj(**a) for aid, a in data.items()}
        except FileNotFoundError:
            self.addresses = {}

    # Hardcoded sample data
    def load_sample_data(self):
        print("Loading sample data...")
        self.classes = {
            1: ClassObj(1, "A"),
            2: ClassObj(2, "B"),
            3: ClassObj(3, "C"),
            4: ClassObj(4, "D"),
        }

        sample_students = [
            StudentObj(1, "Alice", 1, 88, "F", 18),
            StudentObj(2, "Bob", 1, 70, "M", 19),
            StudentObj(3, "Charlie", 2, 55, "M", 20),
            StudentObj(4, "Daisy", 2, 40, "F", 18),
            StudentObj(5, "Ethan", 3, 92, "M", 19),
            StudentObj(6, "Fiona", 3, 33, "F", 17),
            StudentObj(7, "George", 4, 65, "M", 20),
            StudentObj(8, "Hannah", 4, 48, "F", 19),
            StudentObj(9, "Ivy", 1, 77, "F", 18),
            StudentObj(10, "Jack", 2, 29, "M", 17),
        ]

        sample_addresses = [
            AddressObj(1, "452001", "Indore", 1),
            AddressObj(2, "452002", "Delhi", 2),
            AddressObj(3, "452003", "Mumbai", 3),
            AddressObj(4, "452004", "Bhopal", 4),
            AddressObj(5, "452005", "Indore", 5),
            AddressObj(6, "452006", "Pune", 6),
            AddressObj(7, "452007", "Delhi", 7),
            AddressObj(8, "452008", "Indore", 8),
            AddressObj(9, "452009", "Bhopal", 9),
            AddressObj(10, "452010", "Mumbai", 10),
        ]

        self.students = {s.id: s for s in sample_students}
        self.addresses = {a.id: a for a in sample_addresses}

# ---------------------------
# Business logic (menu service)
# ---------------------------
class StudentService:
    def __init__(self, db: InMemoryDB):
        self.db = db

    def add_student(self, student: StudentObj, addresses: List[AddressObj]) -> str:
        if student.age > 20:
            return f"Student {student.id} NOT added: age {student.age} > 20."
        if student.class_id not in self.db.classes:
            return f"Class ID {student.class_id} does not exist."

        self.db.students[student.id] = student
        for addr in addresses:
            self.db.addresses[addr.id] = addr
        self.db.save_to_files()
        return f"Student {student.id} added successfully."

    def delete_student(self, student_id: int) -> str:
        if student_id not in self.db.students:
            return f"Student {student_id} not found."

        addr_ids = [aid for aid, a in self.db.addresses.items() if a.student_id == student_id]
        for aid in addr_ids:
            del self.db.addresses[aid]

        class_id = self.db.students[student_id].class_id
        del self.db.students[student_id]

        still_in_class = any(s.class_id == class_id for s in self.db.students.values())
        if not still_in_class and class_id in self.db.classes:
            del self.db.classes[class_id]

        self.db.save_to_files()
        return f"Student {student_id} deleted successfully."

    def list_students(self) -> List[StudentObj]:
        return list(self.db.students.values())

    def evaluate_results(self):
        for s in self.db.students.values():
            s.status = "Pass" if s.marks >= 50 else "Fail"
        sorted_students = sorted(self.db.students.values(), key=lambda x: -x.marks)
        rank = 1
        for stu in sorted_students:
            stu.rank = rank
            rank += 1
        self.db.save_to_files()

# ---------------------------
# Helper for pagination
# ---------------------------
def paginate(items: List[Any], start: int, end: int) -> List[Any]:
    return items[start-1:end]

# ---------------------------
# Interactive Menu
# ---------------------------
def main():
    db = InMemoryDB()
    svc = StudentService(db)

    while True:
        print("\n=== Student Management System ===")
        print("1. Add Student")
        print("2. Find Student")
        print("3. Student Result")
        print("4. Delete Student")
        print("5. List Students")
        print("6. Pagination")
        print("7. Exit")

        choice = input("Enter choice: ")

        if choice == "1":
            sid = int(input("Enter Student ID: "))
            name = input("Enter Name: ")
            class_id = int(input("Enter Class ID: "))
            marks = int(input("Enter Marks: "))
            gender = input("Enter Gender (M/F): ")
            age = int(input("Enter Age: "))

            addr_id = int(input("Enter Address ID: "))
            pin = input("Enter Pincode: ")
            city = input("Enter City: ")

            student = StudentObj(sid, name, class_id, marks, gender, age)
            address = AddressObj(addr_id, pin, city, sid)

            print(svc.add_student(student, [address]))

        elif choice == "2":
            sid = int(input("Enter Student ID to find: "))
            if sid in db.students:
                print(asdict(db.students[sid]))
            else:
                print("Student not found.")

        elif choice == "3":
            svc.evaluate_results()
            for s in db.students.values():
                print(asdict(s))

        elif choice == "4":
            sid = int(input("Enter Student ID to delete: "))
            print(svc.delete_student(sid))

        elif choice == "5":
            for s in svc.list_students():
                print(asdict(s))

        elif choice == "6":
            all_students = svc.list_students()
            start = int(input("Enter start index: "))
            end = int(input("Enter end index: "))
            paginated = paginate(all_students, start, end)
            for s in paginated:
                print(asdict(s))

        elif choice == "7":
            print("Exiting...")
            break
        else:
            print("Invalid choice, try again.")

if __name__ == "__main__":
    main()
