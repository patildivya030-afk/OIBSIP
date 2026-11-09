import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class ExamApp extends JFrame {

    private static final String LOGIN_CARD = "LOGIN";
    private static final String SIGNUP_CARD = "SIGNUP";
    private static final String PROFILE_CARD = "PROFILE";
    private static final String EXAM_CARD = "EXAM";
    private static final String RESULT_CARD = "RESULT";

    private static final int EXAM_DURATION_SECONDS = 120;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    private String currentCard = LOGIN_CARD;

    private final UserStore userStore = new UserStore();
    private User currentUser;
    private final List<Question> questions = QuestionBank.getQuestions();
    private int[] selectedAnswers = new int[questions.size()];

    private int currentIndex = 0;
    private int secondsRemaining;
    private boolean examSubmitted = false;
    private Timer countdownTimer;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginErrorLabel;

    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JPasswordField signupConfirmField;
    private JTextField signupDisplayNameField;
    private JLabel signupErrorLabel;

    private JTextField displayNameField;
    private JPasswordField newPasswordField;

    private JLabel questionCounterLabel;
    private JLabel timerLabel;
    private JTextArea questionArea;
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionGroup = new ButtonGroup();
    private JButton prevButton;
    private JButton nextButton;
    private JButton submitButton;

    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JPanel resultContentPanel;

    public ExamApp() {
        super("Online Examination System");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        cardsPanel.add(buildLoginPanel(), LOGIN_CARD);
        cardsPanel.add(buildSignupPanel(), SIGNUP_CARD);
        cardsPanel.add(buildProfilePanel(), PROFILE_CARD);
        cardsPanel.add(buildExamPanel(), EXAM_CARD);
        cardsPanel.add(buildResultPanel(), RESULT_CARD);
        add(cardsPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
    }

    private void showCard(String name) {
        currentCard = name;
        cardLayout.show(cardsPanel, name);
    }

    private void handleWindowClose() {
        if (EXAM_CARD.equals(currentCard)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit? Your exam progress will be lost.",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Student Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        loginErrorLabel = new JLabel(" ");
        loginErrorLabel.setForeground(Color.RED);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginErrorLabel, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(loginButton, gbc);

        JButton goToSignupButton = new JButton("Sign Up");
        gbc.gridx = 1;
        panel.add(goToSignupButton, gbc);

        JLabel hintLabel = new JLabel(
                "<html><i>Demo credentials - username: student, password: pass123</i></html>");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(hintLabel, gbc);

        loginButton.addActionListener(e -> {
            String enteredUser = usernameField.getText().trim();
            String enteredPass = new String(passwordField.getPassword());

            User authenticated = userStore.authenticate(enteredUser, enteredPass);
            if (authenticated != null) {
                currentUser = authenticated;
                loginErrorLabel.setText(" ");
                displayNameField.setText(currentUser.getDisplayName());
                newPasswordField.setText("");
                showCard(PROFILE_CARD);
            } else {
                loginErrorLabel.setText("Incorrect username or password.");
            }
        });

        goToSignupButton.addActionListener(e -> {
            signupErrorLabel.setText(" ");
            signupUsernameField.setText("");
            signupPasswordField.setText("");
            signupConfirmField.setText("");
            signupDisplayNameField.setText("");
            showCard(SIGNUP_CARD);
        });

        return panel;
    }

    private JPanel buildSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        signupUsernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(signupUsernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Display Name:"), gbc);
        signupDisplayNameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(signupDisplayNameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        signupPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(signupPasswordField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Confirm Password:"), gbc);
        signupConfirmField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(signupConfirmField, gbc);

        signupErrorLabel = new JLabel(" ");
        signupErrorLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(signupErrorLabel, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(registerButton, gbc);

        JButton backToLoginButton = new JButton("Back to Login");
        gbc.gridx = 1;
        panel.add(backToLoginButton, gbc);

        registerButton.addActionListener(e -> {
            String username = signupUsernameField.getText().trim();
            String displayName = signupDisplayNameField.getText().trim();
            String password = new String(signupPasswordField.getPassword());
            String confirm = new String(signupConfirmField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                signupErrorLabel.setText("Username and password cannot be empty.");
                return;
            }
            if (userStore.usernameTaken(username)) {
                signupErrorLabel.setText("That username is already taken.");
                return;
            }
            if (!password.equals(confirm)) {
                signupErrorLabel.setText("Passwords do not match.");
                return;
            }

            String finalDisplayName = displayName.isEmpty() ? username : displayName;
            userStore.register(username, password, finalDisplayName);

            currentUser = userStore.authenticate(username, password);
            displayNameField.setText(currentUser.getDisplayName());
            newPasswordField.setText("");
            showCard(PROFILE_CARD);
        });

        backToLoginButton.addActionListener(e -> showCard(LOGIN_CARD));

        return panel;
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Update Your Profile");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Display Name:"), gbc);
        displayNameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(displayNameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("New Password:"), gbc);
        newPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(newPasswordField, gbc);

        JLabel hint = new JLabel("<html><i>Leave password blank to keep it unchanged.</i></html>");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(hint, gbc);

        JButton continueButton = new JButton("Continue to Exam");
        gbc.gridy = 4;
        panel.add(continueButton, gbc);

        continueButton.addActionListener(e -> {
            String newName = displayNameField.getText().trim();
            if (!newName.isEmpty()) {
                currentUser.setDisplayName(newName);
            }
            String newPass = new String(newPasswordField.getPassword());
            if (!newPass.isEmpty()) {
                currentUser.setPassword(newPass);
            }
            startExam();
        });

        return panel;
    }

    private JPanel buildExamPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        questionCounterLabel = new JLabel("Question 1 of " + questions.size());
        timerLabel = new JLabel("Time Remaining: 00:00");
        topPanel.add(questionCounterLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionArea = new JTextArea(3, 30);
        questionArea.setEditable(false);
        questionArea.setFocusable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionArea.setBackground(panel.getBackground());
        centerPanel.add(questionArea);
        centerPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
        }

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Exam");
        bottomPanel.add(prevButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> {
            saveCurrentSelection();
            if (currentIndex > 0) {
                currentIndex--;
                loadQuestion(currentIndex);
            }
        });

        nextButton.addActionListener(e -> {
            saveCurrentSelection();
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                loadQuestion(currentIndex);
            }
        });

        submitButton.addActionListener(e -> {
            saveCurrentSelection();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to submit the exam?",
                    "Confirm Submit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                finishExam();
            }
        });

        return panel;
    }

    private void startExam() {
        currentIndex = 0;
        selectedAnswers = new int[questions.size()];
        Arrays.fill(selectedAnswers, -1);
        secondsRemaining = EXAM_DURATION_SECONDS;
        examSubmitted = false;

        updateTimerLabel();
        loadQuestion(0);
        showCard(EXAM_CARD);

        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        countdownTimer = new Timer(1000, e -> {
            secondsRemaining--;
            updateTimerLabel();
            if (secondsRemaining <= 0) {
                countdownTimer.stop();
                saveCurrentSelection();
                JOptionPane.showMessageDialog(ExamApp.this, "Time's up! Submitting your exam.");
                finishExam();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerLabel() {
        int clamped = Math.max(secondsRemaining, 0);
        int minutes = clamped / 60;
        int seconds = clamped % 60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
    }

    private void loadQuestion(int idx) {
        Question q = questions.get(idx);
        questionCounterLabel.setText("Question " + (idx + 1) + " of " + questions.size());
        questionArea.setText(q.getText());

        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(opts[i]);
        }

        optionGroup.clearSelection();
        int selected = selectedAnswers[idx];
        if (selected != -1) {
            optionButtons[selected].setSelected(true);
        }

        prevButton.setEnabled(idx > 0);
        nextButton.setEnabled(idx < questions.size() - 1);
    }

    private void saveCurrentSelection() {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selectedAnswers[currentIndex] = i;
                return;
            }
        }
        selectedAnswers[currentIndex] = -1;
    }

    private void finishExam() {
        if (examSubmitted) {
            return;
        }
        examSubmitted = true;
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (selectedAnswers[i] == questions.get(i).getCorrectIndex()) {
                score++;
            }
        }

        long elapsedSeconds = EXAM_DURATION_SECONDS - Math.max(secondsRemaining, 0);
        showResults(score, elapsedSeconds);
        showCard(RESULT_CARD);
    }

    private JPanel buildResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        scoreLabel = new JLabel("Score: ");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timeLabel = new JLabel("Time Taken: ");
        headerPanel.add(scoreLabel);
        headerPanel.add(timeLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        resultContentPanel = new JPanel();
        resultContentPanel.setLayout(new BoxLayout(resultContentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultContentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            usernameField.setText("");
            passwordField.setText("");
            loginErrorLabel.setText(" ");
            showCard(LOGIN_CARD);
        });
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(logoutButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showResults(int score, long elapsedSeconds) {
        resultContentPanel.removeAll();

        scoreLabel.setText(String.format("Score: %d out of %d", score, questions.size()));
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timeLabel.setText(String.format("Time Taken: %02d:%02d", minutes, seconds));

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int selected = selectedAnswers[i];
            boolean correct = selected == q.getCorrectIndex();
            String selectedText = selected == -1 ? "No answer" : q.getOptions()[selected];
            String correctText = q.getOptions()[q.getCorrectIndex()];

            String line = "<html>Q" + (i + 1) + ": " + (correct ? "Correct" : "Incorrect")
                    + " &mdash; Your answer: " + selectedText
                    + (correct ? "" : " | Correct answer: " + correctText) + "</html>";

            JLabel lineLabel = new JLabel(line);
            lineLabel.setForeground(correct ? new Color(0, 128, 0) : new Color(200, 0, 0));
            resultContentPanel.add(lineLabel);
        }

        resultContentPanel.revalidate();
        resultContentPanel.repaint();
    }
}