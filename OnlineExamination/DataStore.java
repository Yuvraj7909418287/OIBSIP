package exam;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static Map<String, User> users = new HashMap<>();
    private static List<Exam> exams        = new ArrayList<>();

    static {
        // Default users
        users.put("admin",   new User("admin",   "admin123", "Admin",   "User",   "admin@examportal.com",  "9876543210"));
        users.put("student", new User("student", "student1", "Ravi",    "Sharma", "ravi@example.com",      "9123456789"));
        users.put("rahul",   new User("rahul",   "rahul123", "Rahul",   "Verma",  "rahul@example.com",     ""));

        // Exam 1: General Knowledge
        Exam gk = new Exam("General Knowledge", 600);
        gk.addQuestion(new Question("What is the capital of India?",
            new String[]{"Mumbai", "New Delhi", "Kolkata", "Chennai"}, 1));
        gk.addQuestion(new Question("Who wrote 'Discovery of India'?",
            new String[]{"Mahatma Gandhi", "Rabindranath Tagore", "Jawaharlal Nehru", "B.R. Ambedkar"}, 2));
        gk.addQuestion(new Question("Which planet is called the Red Planet?",
            new String[]{"Venus", "Jupiter", "Saturn", "Mars"}, 3));
        gk.addQuestion(new Question("The Great Wall of China was built during which dynasty?",
            new String[]{"Tang", "Ming", "Han", "Qin"}, 1));
        gk.addQuestion(new Question("Which is the largest ocean on Earth?",
            new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
        gk.addQuestion(new Question("What is the chemical symbol for Gold?",
            new String[]{"Gd", "Au", "Ag", "Go"}, 1));
        gk.addQuestion(new Question("Which country is the Land of the Rising Sun?",
            new String[]{"China", "South Korea", "Japan", "Thailand"}, 2));
        gk.addQuestion(new Question("How many bones are in the adult human body?",
            new String[]{"206", "208", "212", "200"}, 0));
        gk.addQuestion(new Question("Who painted the Mona Lisa?",
            new String[]{"Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Raphael"}, 2));
        gk.addQuestion(new Question("Which is the longest river in the world?",
            new String[]{"Amazon", "Mississippi", "Yangtze", "Nile"}, 3));
        exams.add(gk);

        // Exam 2: Computer Science
        Exam cs = new Exam("Computer Science", 900);
        cs.addQuestion(new Question("What does HTML stand for?",
            new String[]{"HyperText Markup Language", "HighText Machine Language", "Hyperlinks and Text Markup", "None of above"}, 0));
        cs.addQuestion(new Question("Which of the following is NOT a programming language?",
            new String[]{"Python", "Java", "HTML", "C++"}, 2));
        cs.addQuestion(new Question("What is the base of the binary number system?",
            new String[]{"8", "10", "2", "16"}, 2));
        cs.addQuestion(new Question("CPU stands for:",
            new String[]{"Central Processing Unit", "Computer Personal Unit", "Central Personal Unit", "Computer Processing Utility"}, 0));
        cs.addQuestion(new Question("Which data structure uses LIFO (Last In First Out)?",
            new String[]{"Queue", "Stack", "Array", "Linked List"}, 1));
        cs.addQuestion(new Question("What does RAM stand for?",
            new String[]{"Read Access Memory", "Random Access Memory", "Rapid Access Memory", "None"}, 1));
        cs.addQuestion(new Question("Which protocol is used to send emails?",
            new String[]{"FTP", "HTTP", "SMTP", "TCP"}, 2));
        cs.addQuestion(new Question("What is the full form of IP in networking?",
            new String[]{"Internal Protocol", "Internet Protocol", "Intranet Protocol", "Integrated Protocol"}, 1));
        cs.addQuestion(new Question("Which language is used for web page styling?",
            new String[]{"HTML", "Python", "CSS", "Java"}, 2));
        cs.addQuestion(new Question("A 'bug' in software means:",
            new String[]{"A virus", "An error or defect", "A feature", "A file"}, 1));
        exams.add(cs);

        // Exam 3: Mathematics
        Exam math = new Exam("Mathematics", 1200);
        math.addQuestion(new Question("What is the value of Pi (π) approximately?",
            new String[]{"3.14", "2.71", "1.61", "3.41"}, 0));
        math.addQuestion(new Question("What is the square root of 144?",
            new String[]{"10", "11", "12", "13"}, 2));
        math.addQuestion(new Question("What is 15% of 200?",
            new String[]{"25", "30", "35", "40"}, 1));
        math.addQuestion(new Question("Solve: 2x + 5 = 15. What is x?",
            new String[]{"4", "5", "6", "7"}, 1));
        math.addQuestion(new Question("What is the area of a circle with radius 7? (π ≈ 22/7)",
            new String[]{"144", "154", "164", "174"}, 1));
        math.addQuestion(new Question("Which is a prime number?",
            new String[]{"9", "15", "17", "21"}, 2));
        math.addQuestion(new Question("What is log₁₀(1000)?",
            new String[]{"2", "3", "4", "10"}, 1));
        math.addQuestion(new Question("If a triangle has sides 3, 4, 5 — what type is it?",
            new String[]{"Equilateral", "Isosceles", "Right-angled", "Obtuse"}, 2));
        math.addQuestion(new Question("What is 7! (7 factorial)?",
            new String[]{"2520", "5040", "720", "40320"}, 1));
        math.addQuestion(new Question("What is the HCF of 36 and 48?",
            new String[]{"6", "12", "18", "24"}, 1));
        exams.add(math);
    }

    public static User getUser(String username)           { return users.get(username); }
    public static boolean userExists(String username)     { return users.containsKey(username); }
    public static List<Exam> getExams()                   { return exams; }
    public static Exam getExamByName(String name) {
        for (Exam e : exams) if (e.getName().equals(name)) return e;
        return null;
    }
}
