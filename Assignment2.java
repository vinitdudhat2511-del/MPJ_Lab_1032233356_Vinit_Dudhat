import java.util.Scanner;

class Employee {

    double salary;

    Employee(double salary) {
        this.salary = salary;
    }

    void displaySalary() {
        System.out.println("Salary before hike: " + salary);
    }
}

class FullTimeEmployee extends Employee {

    FullTimeEmployee(double salary) {
        super(salary);
    }

    void calculateSalary() {
        double hike = salary * 0.50;
        salary = salary + hike;
        System.out.println("Salary after 50% hike: " + salary);
    }
}

class InternEmployee extends Employee {

    InternEmployee(double salary) {
        super(salary);
    }

    void calculateSalary() {
        double hike = salary * 0.25;
        salary = salary + hike;
        System.out.println("Salary after 25% hike: " + salary);
    }
}

public class Assignment2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Employee Menu =====");
            System.out.println("1. Full Time Employee");
            System.out.println("2. Intern Employee");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter salary for Full Time Employee: ");
                    double fullSalary = sc.nextDouble();
                    FullTimeEmployee f1 = new FullTimeEmployee(fullSalary);
                    f1.displaySalary();
                    f1.calculateSalary();
                    break;

                case 2:
                    System.out.print("Enter salary for Intern Employee: ");
                    double internSalary = sc.nextDouble();
                    InternEmployee i1 = new InternEmployee(internSalary);
                    i1.displaySalary();
                    i1.calculateSalary();
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 3);

        sc.close();
    }
}

