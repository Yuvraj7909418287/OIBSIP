package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExamSelectionDialog extends JDialog {

    private AppController controller;
    private Exam selectedExam = null;

    public ExamSelectionDialog(AppController controller, JFrame parent) {
        super(parent, "Select Exam", true);
        this.controller = controller;
        setSize(480, 420);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG);
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel();
        content.setBackground(Theme.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Select an Exam to Attempt");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Theme.GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Choose from available exams below");
        sub.setFont(Theme.SMALL_FONT);
        sub.setForeground(Theme.MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(sub);
        content.add(Box.createVerticalStrut(20));

        for (Exam exam : DataStore.getExams()) {
            content.add(makeExamCard(exam));
            content.add(Box.createVerticalStrut(10));
        }

        content.add(Box.createVerticalStrut(10));
        JButton cancel = Theme.createButton("  Cancel  ", Theme.SURFACE, Theme.MUTED);
        cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancel.addActionListener(e -> dispose());
        content.add(cancel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        add(scroll);
    }

    private JPanel makeExamCard(Exam exam) {
        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(exam.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(Theme.TEXT);
        int mins = exam.getDurationSeconds()/60;
        JLabel detail = new JLabel(exam.getTotalQuestions() + " Questions  •  " + mins + " Minutes");
        detail.setFont(Theme.SMALL_FONT);
        detail.setForeground(Theme.MUTED);
        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(detail);
        card.add(info, BorderLayout.CENTER);

        JButton startBtn = Theme.createButton("Start →", Theme.GOLD, new Color(30, 18, 0));
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        startBtn.addActionListener(e -> {
            dispose();
            controller.startExam(exam);
        });
        card.add(startBtn, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                    BorderFactory.createLineBorder(Theme.GOLD, 1, true),
                    new EmptyBorder(13, 15, 13, 15)));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(14, 16, 14, 16));
            }
        });
        return card;
    }
}
