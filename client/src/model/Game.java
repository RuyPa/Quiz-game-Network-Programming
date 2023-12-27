package model;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Question> questionList;

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public Game() {
    }

    public Game(List<Question> questionList) {
        this.questionList = questionList;
    }
}
