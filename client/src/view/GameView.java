package view;

import model.Game;
import model.Question;
import model.Request;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JFrame {
    private JPanel panel;
    private Timer timer;
    private int totalTime = 60;
    private int questionTime = 12;
    private int timeRemaining = totalTime;
    private int questionTimeRemaining = questionTime;
    private JLabel questionLabel;
    private JLabel totalTimeLabel;
    private JLabel questionTimeLabel;
    private JRadioButton[] answerButtons;
    private JButton nextButton;

    private Game game;
    private Socket socket;
    private GameView gameView;
    private int currentQuestionIndex = 0;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Question> selectedQuestions = new ArrayList<>();

    public GameView(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket, Game gameFromServer) {

        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;

        this.socket = socket;
        gameView = this;

        this.game = gameFromServer;

        setTitle("Bài kiểm tra");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        add(panel);
        placeQuizComponents();

        startQuizTimer();

    }

    private void placeQuizComponents() {
        panel.setLayout(null);

        questionLabel = new JLabel(game.getQuestionList().get(currentQuestionIndex).getName());
        questionLabel.setBounds(10, 20, 300, 25);
        panel.add(questionLabel);

        answerButtons = new JRadioButton[4];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton(game.getQuestionList().get(currentQuestionIndex).getAnswers().get(i));
            answerButtons[i].setBounds(10, 80 + i * 30, 300, 25);
            group.add(answerButtons[i]);
            panel.add(answerButtons[i]);
        }

        totalTimeLabel = new JLabel("Thời gian tổng: " + timeRemaining + " giây");
        totalTimeLabel.setBounds(10, 250, 200, 25);
        panel.add(totalTimeLabel);

        questionTimeLabel = new JLabel("Thời gian câu hỏi: " + questionTimeRemaining + " giây");
        questionTimeLabel.setBounds(10, 280, 200, 25);
        panel.add(questionTimeLabel);

        nextButton = new JButton("Câu tiếp theo");
        nextButton.setBounds(10, 350, 200, 25);
        panel.add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showNextQuestion();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void showNextQuestion() throws IOException {
        boolean answerSelected = false;
        String selectedAnswer = null;

        for (int i = 0; i < 4; i++) {
            if (answerButtons[i].isSelected()) {
                answerSelected = true;
                selectedAnswer = game.getQuestionList().get(currentQuestionIndex).getAnswers().get(i);
                break;
            }
        }

        if (!answerSelected) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đáp án trước khi chuyển sang câu hỏi tiếp theo.");
            return;
        }

        Question question = game.getQuestionList().get(currentQuestionIndex);
        Question selectedQuestion = new Question(question.getName(), selectedAnswer, question.getAnswers());
        selectedQuestions.add(selectedQuestion);

        // Print the previous question and answer
//        System.out.println("Previous Question: " + question.getName());
//        System.out.println("Previous Answer: " + selectedAnswer);
        int timeTaken = questionTime - questionTimeRemaining;

        Request request = new Request();
        List<Object> objectList = new ArrayList<>();
        request.setObjects(objectList);

        request.getObjects().add(question.getName());
        request.getObjects().add(selectedAnswer);
        request.getObjects().add(timeTaken);
        request.getObjects().add(new String("EACH"));

        objectOutputStream.writeObject(request);

        currentQuestionIndex++;

        if (currentQuestionIndex < game.getQuestionList().size()) {
            questionLabel.setText(game.getQuestionList().get(currentQuestionIndex).getName());

            for (int i = 0; i < 4; i++) {
                answerButtons[i].setText(game.getQuestionList().get(currentQuestionIndex).getAnswers().get(i));
            }

            clearSelectedAnswers();
            resetQuestionTimer();
        } else {
            endQuiz();
        }

        if (currentQuestionIndex == game.getQuestionList().size() - 1) {
            nextButton.setText("Kết thúc");
        }
    }

    private void clearSelectedAnswers() {
        for (JRadioButton button : answerButtons) {
            button.setSelected(false);
        }
    }

    private void startQuizTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;

                if (timeRemaining <= 0) {
                    endQuiz();
                } else {
                    updateTotalTimeLabel();
                }

                if (questionTimeRemaining > 0) {
                    questionTimeRemaining--;
                    updateQuestionTimeLabel();
                } else {
                    boolean answerSelected = false;

                    for (int i = 0; i < 4; i++) {
                        if (answerButtons[i].isSelected()) {
                            answerSelected = true;
                            break;
                        }
                    }

                    if (!answerSelected) {
                        JOptionPane.showMessageDialog(GameView.this, "Hết thời gian! Vui lòng chọn một đáp án.");
                        try {
                            showNextQuestion();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            showNextQuestion();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    private void updateTotalTimeLabel() {
        totalTimeLabel.setText("Thời gian tổng: " + timeRemaining + " giây");
    }

    private void updateQuestionTimeLabel() {
        questionTimeLabel.setText("Thời gian câu hỏi: " + questionTimeRemaining + " giây");
    }

    private void resetQuestionTimer() {
        questionTimeRemaining = questionTime;
        updateQuestionTimeLabel();
    }

    private void endQuiz() {
        timer.stop();
        int totalTimeUsed = totalTime - timeRemaining;
        System.out.println("diu");
        for (Question selectedQuestion : selectedQuestions) {
            System.out.println("Question: " + selectedQuestion.getName());
            System.out.println("Selected Answer: " + selectedQuestion.getCorrectAnswer());
            // Perform any additional processing or storage as needed
        }

        try {
            Request request = new Request();
            List<Object> objectList = new ArrayList<>();
            request.setObjects(objectList);

            request.getObjects().add(selectedQuestions);
            request.getObjects().add(totalTimeUsed);
            request.getObjects().add(new String("ANSWER"));

            objectOutputStream.writeObject(request);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(this, "Bài kiểm tra đã kết thúc");
        dispose();
    }

//    public static void main(String[] args) {
//        Game game1= new Game();
//        List<model.Question> questionList = new ArrayList<>();
//
//        List<String> answers = new ArrayList<>();
//        answers.add("6");
//        answers.add("7");
//        answers.add("8");
//        answers.add("9");
//        model.Question question = new model.Question("1 + 7 = ?", "8", answers);
//        questionList.add(question);
//        questionList.add(new model.Question("2 + 5 = ?", "7", answers));
//        questionList.add(new model.Question("4 + 2 = ?", "6", answers));
//        questionList.add(new model.Question("5 + 1 = ?", "6", answers));
//        questionList.add(new model.Question("3 + 4 = ?", "7", answers));
//        game1.setQuestionList(questionList);
//        GameView questionView = new GameView(socket,game1);
//        questionView.setVisible(true);
//    }
}