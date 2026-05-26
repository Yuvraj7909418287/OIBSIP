package exam;

import javax.swing.*;
import java.awt.*;

public class AppController {

    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private User currentUser;
    private LoginPanel      loginPanel;
    private DashboardPanel  dashboardPanel;
    private ProfilePanel    profilePanel;
    private ExamPanel       examPanel;
    private ResultPanel     resultPanel;

    private static final String PAGE_LOGIN     = "LOGIN";
    private static final String PAGE_DASHBOARD = "DASHBOARD";
    private static final String PAGE_PROFILE   = "PROFILE";
    private static final String PAGE_EXAM      = "EXAM";
    private static final String PAGE_RESULT    = "RESULT";

    public void start() {
        mainFrame = new JFrame("Online Examination System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(Theme.WINDOW_W, Theme.WINDOW_H);
        mainFrame.setMinimumSize(new Dimension(800, 560));
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG);

        loginPanel     = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);
        profilePanel   = new ProfilePanel(this);
        examPanel      = new ExamPanel(this);
        resultPanel    = new ResultPanel(this);

        mainPanel.add(loginPanel,     PAGE_LOGIN);
        mainPanel.add(dashboardPanel, PAGE_DASHBOARD);
        mainPanel.add(profilePanel,   PAGE_PROFILE);
        mainPanel.add(examPanel,      PAGE_EXAM);
        mainPanel.add(resultPanel,    PAGE_RESULT);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        showLogin();
    }


    public void showLogin() {
        cardLayout.show(mainPanel, PAGE_LOGIN);
    }

    public void showDashboard() {
        examPanel.stopTimer();
        cardLayout.show(mainPanel, PAGE_DASHBOARD);
    }

    public void showProfile() {
        profilePanel.loadUser(currentUser);
        cardLayout.show(mainPanel, PAGE_PROFILE);
    }

    public void showExamSelection() {
        ExamSelectionDialog dlg = new ExamSelectionDialog(this, mainFrame);
        dlg.setVisible(true);
    }

    public void startExam(Exam exam) {
        examPanel.loadExam(exam);
        cardLayout.show(mainPanel, PAGE_EXAM);
    }

    public void showResults(Exam exam, int[] answers) {
        resultPanel.showResults(exam, answers);
        cardLayout.show(mainPanel, PAGE_RESULT);
    }

    
    public void onLoginSuccess(User user) {
        this.currentUser = user;
        dashboardPanel.refreshUser(user);
        showDashboard();
    }

    public void confirmLogout() {
        int result = JOptionPane.showConfirmDialog(
            mainFrame,
            "Are you sure you want to logout?\nYour session will be closed.",
            "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            examPanel.stopTimer();
            currentUser = null;
            showLogin();
        }
    }

    public void onUserUpdated() {
        if (currentUser != null) dashboardPanel.refreshUser(currentUser);
    }

    public User getCurrentUser() { return currentUser; }
    public JFrame getMainFrame() { return mainFrame; }
}
