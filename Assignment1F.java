import java.util.Scanner;

class StudentRecord {

    String name;
    int[] marks = new int[5];
    int total;
    double percentage;
    char grade;

    void inputData(Scanner sc) {

        System.out.print("Enter student name: ");
        name = sc.next();

        total = 0;

        System.out.println("Enter 5 subject marks:");
        for (int i = 0; i < 5; i++) {
            marks[i] = sc.nextInt();
            total += marks[i];
        }

        percentage = total / 5.0;
        calculateGrade();
    }

    void calculateGrade() {
        if (percentage >= 90)
            grade = 'A';
        else if (percentage >= 75)
            grade = 'B';
        else if (percentage >= 60)
            grade = 'C';
        else if (percentage >= 40)
            grade = 'D';
        else
            grade = 'F';
    }

    void displayResult() {
        System.out.println("\nStudent Name: " + name);
        System.out.println("Total Marks: " + total);
        System.out.println("Percentage: " + percentage);
        System.out.println("Grade: " + grade);
    }
}

public class Assignment1F {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentRecord[] students = new StudentRecord[5];

        for (int i = 0; i < 5; i++) {
            System.out.println("\n--- Enter details for Student " + (i + 1) + " ---");
            students[i] = new StudentRecord();
            students[i].inputData(sc);
        }

        System.out.println("\n===== RESULT =====");

        for (int i = 0; i < 5; i++) {
            students[i].displayResult();
        }

        sc.close();
    }
}
