package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExamPanel extends JPanel {

    private AppController controller;
    private Exam exam;
    private int[] userAnswers;
    private int currentQ = 0;
    private int timeLeft;
    private Timer countdownTimer;
    private boolean submitted = false;

    // UI refs
    private JLabel timerLabel;
    private JLabel questionCounter;
    private JLabel questionText;
    private JPanel optionsPanel;
    private JPanel paletteGrid;
    private JProgressBar progressBar;
    private JLabel examTitleLabel;

    public ExamPanel(AppController controller) {
        this.controller = controller;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
    }

    public void loadExam(Exam exam) {
        this.exam       = exam;
        this.userAnswers = new int[exam.getTotalQuestions()];
        java.util.Arrays.fill(userAnswers, -1);
        this.currentQ  = 0;
        this.timeLeft  = exam.getDurationSeconds();
        this.submitted = false;
        removeAll();
        buildUI();
        renderQuestion();
        renderPalette();
        startTimer();
        revalidate(); repaint();
    }

    private void buildUI() {
        // EXAM HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.CARD);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Theme.BORDER),
            new EmptyBorder(10, 20, 10, 20)
        ));
        examTitleLabel = new JLabel("📝  " + exam.getName());
        examTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        examTitleLabel.setForeground(Theme.TEXT);
        header.add(examTitleLabel, BorderLayout.WEST);

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(Theme.MONO_FONT);
        timerLabel.setForeground(Theme.GOLD);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Theme.GOLD_DIM);
        timerLabel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(245, 166, 35, 100), 1, true),
            new EmptyBorder(4, 16, 4, 16)
        ));
        header.add(timerLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // MAIN SPLIT
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(Theme.BG);
        split.setBorder(null);
        split.setDividerSize(4);
        split.setDividerLocation(600);
        split.setResizeWeight(1.0);
        split.setLeftComponent(buildQuestionArea());
        split.setRightComponent(buildPaletteArea());
        add(split, BorderLayout.CENTER);
    }

    private JScrollPane buildQuestionArea() {
        JPanel area = new JPanel();
        area.setBackground(Theme.BG);
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.setBorder(new EmptyBorder(20, 24, 20, 16));

        // Progress bar
        progressBar = new JProgressBar(0, exam.getTotalQuestions());
        progressBar.setValue(1);
        progressBar.setStringPainted(false);
        progressBar.setBackground(Theme.BORDER);
        progressBar.setForeground(Theme.GOLD);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        progressBar.setBorder(null);
        area.add(progressBar);
        area.add(Box.createVerticalStrut(10));

        questionCounter = new JLabel("Question 1 of " + exam.getTotalQuestions());
        questionCounter.setFont(Theme.SMALL_FONT);
        questionCounter.setForeground(Theme.MUTED);
        area.add(questionCounter);
        area.add(Box.createVerticalStrut(16));

        // Question card
        JPanel qCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        qCard.setOpaque(false);
        qCard.setLayout(new BoxLayout(qCard, BoxLayout.Y_AXIS));
        qCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        qCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        questionText = new JLabel("<html><body style='width:420px'>Loading...</body></html>");
        questionText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        questionText.setForeground(Theme.TEXT);
        qCard.add(questionText);
        qCard.add(Box.createVerticalStrut(16));

        optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        qCard.add(optionsPanel);

        area.add(qCard);
        area.add(Box.createVerticalStrut(16));

        // Navigation buttons
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nav.setOpaque(false);
        nav.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton prev = Theme.createButton("  ← Prev  ", Theme.SURFACE, Theme.MUTED);
        prev.addActionListener(e -> { if (currentQ > 0) { currentQ--; renderQuestion(); } });

        JButton next = Theme.createButton("  Next →  ", Theme.SURFACE, Theme.MUTED);
        next.addActionListener(e -> { if (currentQ < exam.getTotalQuestions()-1) { currentQ++; renderQuestion(); } });

        JButton submit = Theme.createButton("  Submit Exam  ", Theme.GOLD, new Color(30, 18, 0));
        submit.setFont(new Font("SansSerif", Font.BOLD, 13));
        submit.addActionListener(e -> confirmSubmit());

        nav.add(prev); nav.add(next); nav.add(submit);
        area.add(nav);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        return scroll;
    }

    private JPanel buildPaletteArea() {
        JPanel side = new JPanel();
        side.setBackground(Theme.CARD);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, Theme.BORDER),
            new EmptyBorder(16, 14, 16, 14)
        ));

        JLabel title = new JLabel("Question Navigator");
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setForeground(Theme.MUTED);
        side.add(title);
        side.add(Box.createVerticalStrut(12));

        paletteGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        paletteGrid.setOpaque(false);
        side.add(paletteGrid);
        side.add(Box.createVerticalStrut(16));

        // Legend
        side.add(makeLegend("■", Theme.GOLD, "Answered"));
        side.add(Box.createVerticalStrut(5));
        side.add(makeLegend("■", Theme.INFO, "Current"));
        side.add(Box.createVerticalStrut(5));
        side.add(makeLegend("■", Theme.MUTED, "Not visited"));
        return side;
    }

    private JLabel makeLegend(String sym, Color col, String label) {
        JLabel l = new JLabel(sym + "  " + label);
        l.setFont(Theme.SMALL_FONT);
        l.setForeground(col);
        return l;
    }

    private void renderQuestion() {
        if (exam == null) return;
        List<Question> qs = exam.getQuestions();
        Question q = qs.get(currentQ);

        questionCounter.setText("Question " + (currentQ+1) + " of " + exam.getTotalQuestions());
        progressBar.setValue(currentQ+1);
        questionText.setText("<html><body style='width:420px'><b>Q" + (currentQ+1) + ".</b>  " + q.getQuestionText() + "</body></html>");

        optionsPanel.removeAll();
        String[] letters = {"A", "B", "C", "D"};
        for (int i = 0; i < q.getOptions().length; i++) {
            final int idx = i;
            boolean selected = userAnswers[currentQ] == i;

            JPanel opt = new JPanel(new BorderLayout(12, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(selected ? Theme.GOLD_DIM : Theme.SURFACE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(selected ? Theme.GOLD : Theme.BORDER);
                    g2.setStroke(new BasicStroke(selected ? 1.5f : 1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                    g2.dispose();
                }
            };
            opt.setOpaque(false);
            opt.setBorder(new EmptyBorder(10, 12, 10, 12));
            opt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            opt.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
            letter.setFont(new Font("SansSerif", Font.BOLD, 12));
            letter.setForeground(selected ? new Color(30, 18, 0) : Theme.MUTED);
            letter.setPreferredSize(new Dimension(28, 28));
            letter.setOpaque(true);
            letter.setBackground(selected ? Theme.GOLD : Theme.BORDER);
            letter.setBorder(BorderFactory.createLineBorder(selected ? Theme.GOLD : Theme.BORDER, 1, true));

            JLabel text = new JLabel(q.getOptions()[i]);
            text.setFont(Theme.BODY_FONT);
            text.setForeground(selected ? Theme.GOLD_LIGHT : Theme.TEXT);

            opt.add(letter, BorderLayout.WEST);
            opt.add(text, BorderLayout.CENTER);

            opt.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    userAnswers[currentQ] = idx;
                    renderQuestion();
                    renderPalette();
                }
            });
            optionsPanel.add(opt);
            optionsPanel.add(Box.createVerticalStrut(8));
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
        renderPalette();
    }

    private void renderPalette() {
        if (paletteGrid == null) return;
        paletteGrid.removeAll();
        for (int i = 0; i < exam.getTotalQuestions(); i++) {
            final int idx = i;
            boolean answered = userAnswers[i] != -1;
            boolean current  = i == currentQ;

            JLabel dot = new JLabel(String.valueOf(i+1), SwingConstants.CENTER);
            dot.setFont(new Font("SansSerif", Font.BOLD, 11));
            dot.setPreferredSize(new Dimension(32, 32));
            dot.setOpaque(true);
            if (current) {
                dot.setBackground(new Color(74, 142, 245, 50));
                dot.setForeground(Theme.INFO);
                dot.setBorder(BorderFactory.createLineBorder(Theme.INFO, 1, true));
            } else if (answered) {
                dot.setBackground(Theme.GOLD_DIM);
                dot.setForeground(Theme.GOLD);
                dot.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 1, true));
            } else {
                dot.setBackground(Theme.SURFACE);
                dot.setForeground(Theme.MUTED);
                dot.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
            }
            dot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dot.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { currentQ = idx; renderQuestion(); }
            });
            paletteGrid.add(dot);
        }
        paletteGrid.revalidate();
        paletteGrid.repaint();
    }

    private void startTimer() {
        updateTimerDisplay();
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                countdownTimer.stop();
                autoSubmit();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerDisplay() {
        int m = timeLeft / 60;
        int s = timeLeft % 60;
        String txt = String.format("%02d:%02d", m, s);
        timerLabel.setText(txt);
        if (timeLeft <= 60) {
            timerLabel.setForeground(Theme.DANGER);
            timerLabel.setBackground(new Color(232, 76, 76, 30));
        } else {
            timerLabel.setForeground(Theme.GOLD);
            timerLabel.setBackground(Theme.GOLD_DIM);
        }
    }

    private void confirmSubmit() {
        int answered = 0;
        for (int a : userAnswers) if (a != -1) answered++;
        int total = exam.getTotalQuestions();
        int result = JOptionPane.showConfirmDialog(this,
            "You have answered " + answered + " of " + total + " questions.\nAre you sure you want to submit?",
            "Submit Exam", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) doSubmit();
    }

    private void autoSubmit() {
        JOptionPane.showMessageDialog(this,
            "⏰  Time's up! Your exam has been automatically submitted.",
            "Time Over", JOptionPane.INFORMATION_MESSAGE);
        doSubmit();
    }

    private void doSubmit() {
        if (submitted) return;
        submitted = true;
        if (countdownTimer != null) countdownTimer.stop();
        controller.showResults(exam, userAnswers);
    }

    public void stopTimer() {
        if (countdownTimer != null) countdownTimer.stop();
    }
}
