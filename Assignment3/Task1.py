"""
Student Management System
- Implements business rules and functional requirements from uploaded doc.
- Usage: run as a script; sample queries at bottom demonstrate functionality.
"""

from typing import List, Dict, Optional, Any, Tuple
from dataclasses import dataclass, field
import copy

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
# In-memory "DB"
# ---------------------------
class InMemoryDB:
    def __init__(self):
        self.classes: Dict[int, ClassObj] = {}
        self.students: Dict[int, StudentObj] = {}
        self.addresses: Dict[int, AddressObj] = {}

    # utility to find class by name
    def find_class_by_name(self, name: str) -> Optional[ClassObj]:
        for cl in self.classes.values():
            if cl.name.lower() == name.lower():
                return cl
        return None

# ---------------------------
# Business logic
# ---------------------------
class StudentService:
    def __init__(self, db: InMemoryDB):
        self.db = db

    # Validation rule: age <= 20, otherwise reject
    def insert_student(self, s: StudentObj, addresses: List[AddressObj]) -> Tuple[bool,str]:
        if s.age > 20:
            return False, f"Student {s.id} NOT inserted: age {s.age} > 20 (violates rule)."
        # ensure class exists
        if s.class_id not in self.db.classes:
            return False, f"Student {s.id} NOT inserted: class_id {s.class_id} does not exist."
        # insert student
        self.db.students[s.id] = s
        for a in addresses:
            self.db.addresses[a.id] = a
        return True, f"Student {s.id} inserted."

    # Evaluate Pass/Fail and ranks
    def evaluate_pass_fail_and_rank(self):
        # Pass/Fail
        for s in self.db.students.values():
            s.status = "Pass" if s.marks >= 50 else "Fail"
        # Ranking: competition ranking based on marks desc
        sorted_students = sorted(self.db.students.values(), key=lambda x: (-x.marks, x.name))
        ranks = {}
        last_marks = None
        last_rank = 0
        count_seen = 0
        for stu in sorted_students:
            count_seen += 1
            if stu.marks != last_marks:
                # new rank equals count_seen
                rank_to_assign = count_seen
                last_marks = stu.marks
                last_rank = rank_to_assign
            else:
                # same marks => same rank
                rank_to_assign = last_rank
            stu.rank = rank_to_assign

    # Generic filter helper
    def _apply_filters(self, students: List[StudentObj],
                       gender: Optional[str]=None,
                       age: Optional[int]=None,
                       min_age: Optional[int]=None,
                       max_age: Optional[int]=None,
                       class_name: Optional[str]=None,
                       pincode: Optional[str]=None,
                       city: Optional[str]=None) -> List[StudentObj]:
        result = []
        for s in students:
            if gender and s.gender.lower() != gender.lower():
                continue
            if age is not None and s.age != age:
                continue
            if min_age is not None and s.age < min_age:
                continue
            if max_age is not None and s.age > max_age:
                continue
            if class_name:
                cl = self.db.classes.get(s.class_id)
                if not cl or cl.name.lower() != class_name.lower():
                    continue
            if pincode or city:
                # find any address for this student that matches
                addrs = [a for a in self.db.addresses.values() if a.student_id == s.id]
                if pincode:
                    if not any(a.pin_code == str(pincode) for a in addrs):
                        continue
                if city:
                    if not any(a.city.lower() == city.lower() for a in addrs):
                        continue
            result.append(s)
        return result

    # Functional requirements
    def find_by_pincode(self, pin_code: str, **filters) -> List[StudentObj]:
        students = list(self.db.students.values())
        return self._apply_filters(students, pincode=str(pin_code), **filters)

    def find_by_city(self, city: str, **filters) -> List[StudentObj]:
        students = list(self.db.students.values())
        return self._apply_filters(students, city=city, **filters)

    def find_by_class(self, class_name: str, **filters) -> List[StudentObj]:
        students = list(self.db.students.values())
        return self._apply_filters(students, class_name=class_name, **filters)

    def get_passed_students(self, **filters) -> List[StudentObj]:
        students = [s for s in self.db.students.values() if s.status == "Pass"]
        return self._apply_filters(students, **filters)

    def get_failed_students(self, **filters) -> List[StudentObj]:
        students = [s for s in self.db.students.values() if s.status == "Fail"]
        return self._apply_filters(students, **filters)

    # Delete student and related addresses; also delete class if it becomes empty
    def delete_student(self, student_id: int) -> Tuple[bool,str]:
        if student_id not in self.db.students:
            return False, f"Student {student_id} not found."
        # delete addresses
        addr_ids = [aid for aid,a in self.db.addresses.items() if a.student_id == student_id]
        for aid in addr_ids:
            del self.db.addresses[aid]
        # remember class id
        class_id = self.db.students[student_id].class_id
        # delete student
        del self.db.students[student_id]
        # if no students left in that class, delete class
        still_in_class = any(s.class_id == class_id for s in self.db.students.values())
        if not still_in_class and class_id in self.db.classes:
            del self.db.classes[class_id]
            return True, f"Student {student_id} and related addresses deleted. Class {class_id} deleted (now empty)."
        return True, f"Student {student_id} and related addresses deleted."

# ---------------------------
# Pagination utility
# ---------------------------
def paginate(items: List[Any],
             start: Optional[int]=1,
             end: Optional[int]=None,
             order_by: Optional[str]=None,
             ascending: bool=True) -> List[Any]:
    """
    start/end are 1-indexed inclusive positions.
    If end is None, returns from start to end of list.
    order_by: attribute name to sort by (e.g., "name" or "marks"). If None, preserves order.
    ascending: True for ascending order, False for descending.
    """
    arr = items[:]
    if order_by:
        arr.sort(key=lambda x: getattr(x, order_by), reverse=not ascending)
    # convert to 0-indexed slice
    if start is None:
        start_idx = 0
    else:
        start_idx = max(0, start-1)
    if end is None:
        end_idx = None
    else:
        end_idx = max(0, end)  # slicing end is exclusive and already works as count
    sliced = arr[start_idx:end_idx]
    return sliced

# ---------------------------
# Helpers for nicer output
# ---------------------------
def student_to_dict(s: StudentObj, db: InMemoryDB) -> Dict[str,Any]:
    addr = [a for a in db.addresses.values() if a.student_id == s.id]
    class_name = db.classes[s.class_id].name if s.class_id in db.classes else None
    return {
        "id": s.id,
        "name": s.name,
        "class": class_name,
        "marks": s.marks,
        "gender": s.gender,
        "age": s.age,
        "status": s.status,
        "rank": s.rank,
        "addresses": [{"id": a.id, "pin_code": a.pin_code, "city": a.city} for a in addr]
    }

# ---------------------------
# Bootstrapping sample data from the doc
# ---------------------------
def load_sample_data(db: InMemoryDB, svc: StudentService):
    # Classes
    classes_sample = [
        (1,"A"), (2,"B"), (3,"C"), (4,"D")
    ]
    for cid, name in classes_sample:
        db.classes[cid] = ClassObj(cid, name)

    # Student Table (from your doc)
    students_sample = [
        # id, name, class_id, marks, gender, age
        (1, "stud1", 1, 88, "F", 10),
        (2, "stud2", 1, 70, "F", 11),
        (3, "stud3", 2, 88, "M", 22),  # age 22 -> must be rejected by age rule
        (4, "stud4", 2, 55, "M", 33),  # age 33 -> rejected
        (5, "stud5", 1, 30, "F", 44),  # age 44 -> rejected
        (6, "stud6", 3, 30, "F", 33),  # rejected
        (7, "stud6", 3, 10, "F", 22),  # age 22 -> rejected
        (8, "stud6", 3, 0,  "M", 11),
    ]
    # Address Table
    addresses_sample = [
        (1, "452002", "indore", 1),
        (2, "422002", "delhi", 1),
        (3, "442002", "indore", 2),
        (4, "462002", "delhi", 3),
        (5, "472002", "indore", 4),
        (6, "452002", "indore", 5),
        (7, "452002", "delhi", 5),
        (8, "482002", "mumbai", 6),
        (9, "482002", "bhopal", 7),
        (10,"482002", "indore", 8),
    ]
    # Insert students & addresses while respecting age rule: we will pair addresses for each student id
    addr_by_student = {}
    for aid, pin, city, sid in addresses_sample:
        addr_by_student.setdefault(sid, []).append(AddressObj(aid, pin, city, sid))

    for sdata in students_sample:
        sid, name, class_id, marks, gender, age = sdata
        student_obj = StudentObj(sid, name, class_id, marks, gender, age)
        addrs = addr_by_student.get(sid, [])
        ok, msg = svc.insert_student(student_obj, addrs)
        # For clarity while running, print insertion result
        print(msg)

# ---------------------------
# Example usage & tests
# ---------------------------
def demo():
    db = InMemoryDB()
    svc = StudentService(db)
    print("=== Loading sample data (age > 20 will be rejected) ===")
    load_sample_data(db, svc)
    print()

    print("=== Evaluate pass/fail and ranks ===")
    svc.evaluate_pass_fail_and_rank()

    print("All students in DB (post validation):")
    for s in db.students.values():
        print(student_to_dict(s, db))
    print()

    # Example queries from requirements
    print("== Find students by pincode=482002 ==")
    found = svc.find_by_pincode("482002")
    for s in found:
        print(student_to_dict(s, db))
    print()

    print("== Find students by city=indore, filter gender=F ==")
    found = svc.find_by_city("indore", gender="F")
    for s in found:
        print(student_to_dict(s, db))
    print()

    print("== Get passed students (no filters) ==")
    passed = svc.get_passed_students()
    for s in passed:
        print(student_to_dict(s, db))
    print()

    print("== Get failed students (filters: gender=F) ==")
    failed_f = svc.get_failed_students(gender="F")
    for s in failed_f:
        print(student_to_dict(s, db))
    print()

    # Pagination examples
    print("== Pagination examples ==")
    female_students = svc._apply_filters(list(db.students.values()), gender="F")
    # Example: Read female students, records 1–9
    page1 = paginate(female_students, start=1, end=9, order_by=None, ascending=True)
    print("Female students records 1-9 (default order):", [s.id for s in page1])

    # Read female students, records 7–8, ordered by name
    page2 = paginate(female_students, start=7, end=8, order_by="name", ascending=True)
    print("Female students records 7-8 ordered by name:", [s.id for s in page2])

    # Read female students, records 1–5, ordered by marks (desc)
    page3 = paginate(female_students, start=1, end=5, order_by="marks", ascending=False)
    print("Female students records 1-5 ordered by marks desc:", [s.id for s in page3])

    # Deletion example
    print("== Delete student with id=1 ==")
    ok, msg = svc.delete_student(1)
    print(msg)
    svc.evaluate_pass_fail_and_rank()  # recompute ranks after delete
    print("Classes after deletion:", {cid: cl.name for cid,cl in db.classes.items()})
    print("Students after deletion:", [s.id for s in db.students.values()])

if __name__ == "__main__":
    demo()
