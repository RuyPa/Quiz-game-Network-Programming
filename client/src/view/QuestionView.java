package viewNew;

import model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionView extends JFrame {
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

    private String[] questionsArray = {
            "Câu hỏi 1: ...",
            "Câu hỏi 2: ...",
            "Câu hỏi 3: ...",
            "Câu hỏi 4: ...",
            "Câu hỏi 5: ..."
    };
    private String[][] answersArray = {
            {"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"},
            {"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"},
            {"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"},
            {"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"},
            {"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"}
    };
    private int currentQuestionIndex = 0;

    private BufferedWriter resultsWriter;

    public QuestionView(Game game) {
        this.game = game;
        try {
            resultsWriter = new BufferedWriter(new FileWriter("results.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(resultsWriter);
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
                showNextQuestion();
            }
        });
    }

    private void showNextQuestion() {
        boolean answerSelected = false;

        for (int i = 0; i < 4; i++) {
            if (answerButtons[i].isSelected()) {
                answerSelected = true;
                break;
            }
        }

        if (!answerSelected) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đáp án trước khi chuyển sang câu hỏi tiếp theo.");
            return;
        }

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
                        JOptionPane.showMessageDialog(viewNew.QuestionView.this, "Hết thời gian! Vui lòng chọn một đáp án.");
                        showNextQuestion();
                    } else {
                        showNextQuestion();
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
        saveResults();  // Lưu kết quả khi kết thúc
        closeResultsFile();  // Đóng tệp khi kết thúc
        JOptionPane.showMessageDialog(this, "Bài kiểm tra đã kết thúc");
        dispose();
    }

    private void saveResults() {
        try {
            resultsWriter.write("Kết quả câu hỏi: " + currentQuestionIndex + "\n");
            resultsWriter.write("Đáp án đã chọn: ");
            for (int i = 0; i < 4; i++) {
                if (answerButtons[i].isSelected()) {
                    resultsWriter.write((char) ('A' + i) + " ");
                    break;
                }
            }
            resultsWriter.write("\nThời gian còn lại: " + timeRemaining + " giây\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeResultsFile() {
        try {
            resultsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game1= new Game();
        List<model.Question> questionList = new ArrayList<>();

        List<String> answers = new ArrayList<>();
        answers.add("6");
        answers.add("7");
        answers.add("8");
        answers.add("9");
        model.Question question = new model.Question("1 + 7 = ?", "8", answers);
        questionList.add(question);
        questionList.add(new model.Question("2 + 5 = ?", "7", answers));
        questionList.add(new model.Question("4 + 2 = ?", "6", answers));
        questionList.add(new model.Question("5 + 1 = ?", "6", answers));
        questionList.add(new model.Question("3 + 4 = ?", "7", answers));
        game1.setQuestionList(questionList);
        QuestionView questionView = new viewNew.QuestionView(game1);
        questionView.setVisible(true);
    }
}
