package consts;

import model.Game;
import model.Question;

import java.util.ArrayList;
import java.util.List;

public class GameConstant {
    public static Game game1 = new Game();
    public static Game game2 = new Game();

    static {
        List<Question> questionList = new ArrayList<>();

        List<String> answers = new ArrayList<>();
        answers.add("6");
        answers.add("7");
        answers.add("8");
        answers.add("9");
        Question question = new Question("1 + 7 = ?", "8", answers);
        questionList.add(question);
        questionList.add(new Question("2 + 5 = ?", "7", answers));
        questionList.add(new Question("4 + 2 = ?", "6", answers));
        questionList.add(new Question("5 + 1 = ?", "6", answers));
        questionList.add(new Question("3 + 4 = ?", "7", answers));
        game1.setQuestionList(questionList);
    }
}
