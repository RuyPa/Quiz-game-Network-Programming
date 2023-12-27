package model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String correctAnswer;
    private List<String> answers;

    public void setName(String name) {
        this.name = name;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getName() {
        return name;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public Question(String name, String correctAnswer, List<String> answers) {
        this.name = name;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public Question() {
    }
}
