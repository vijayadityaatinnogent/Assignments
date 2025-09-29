import java.util.*;

// Employee class
class Employee implements Comparable<Employee> {
    int id;
    String name;
    String department;
    double salary;

    // Constructor
    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Comparable: Department → Name → Salary
    @Override
    public int compareTo(Employee other) {
        int deptCompare = this.department.compareTo(other.department);
        if (deptCompare != 0) return deptCompare;

        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) return nameCompare;

        return Double.compare(this.salary, other.salary);
    }

    // toString for printing
    @Override
    public String toString() {
        return id + " - " + name + " - " + department + " - " + salary;
    }
}

// Comparator for Salary (Descending)
class SalaryDescComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e2.salary, e1.salary); // Descending order
    }
}

// Main class
public class EmployeeSortDemo {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        // Adding employees
        employees.add(new Employee(101, "Alice", "HR", 50000));
        employees.add(new Employee(102, "Bob", "IT", 70000));
        employees.add(new Employee(103, "Charlie", "IT", 60000));
        employees.add(new Employee(104, "David", "HR", 55000));
        employees.add(new Employee(105, "Eve", "Finance", 80000));

        // Sorting with Comparable (Department → Name → Salary)
        Collections.sort(employees);
        System.out.println("Sorted by Department → Name → Salary:");
        Iterator<Employee> itr1 = employees.iterator();
        while (itr1.hasNext()) {
            System.out.println(itr1.next());
        }

        // Sorting with Comparator (Salary Descending)
        Collections.sort(employees, new SalaryDescComparator());
        System.out.println("\nSorted by Salary (Descending):");
        Iterator<Employee> itr2 = employees.iterator();
        while (itr2.hasNext()) {
            System.out.println(itr2.next());
        }
    }
}
