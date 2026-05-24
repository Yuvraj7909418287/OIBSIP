package exam;

import java.util.List;
import java.util.ArrayList;

public class Exam {
    private String name;
    private int durationSeconds;
    private List<Question> questions;

    public Exam(String name, int durationSeconds) {
        this.name            = name;
        this.durationSeconds = durationSeconds;
        this.questions       = new ArrayList<>();
    }

    public void addQuestion(Question q) { questions.add(q); }

    public String getName()              { return name; }
    public int getDurationSeconds()      { return durationSeconds; }
    public List<Question> getQuestions() { return questions; }
    public int getTotalQuestions()       { return questions.size(); }
}
