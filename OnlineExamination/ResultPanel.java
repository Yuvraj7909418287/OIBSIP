package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {

    private AppController controller;

    public ResultPanel(AppController controller) {
        this.controller = controller;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
    }

    public void showResults(Exam exam, int[] answers) {
        removeAll();
        List<Question> qs = exam.getQuestions();
        int total   = qs.size();
        int correct = 0, wrong = 0, skip = 0;
        for (int i = 0; i < total; i++) {
            if (answers[i] == -1) skip++;
            else if (answers[i] == qs.get(i).getCorrectIndex()) correct++;
            else wrong++;
        }
        int pct = (int) Math.round((double) correct / total * 100);

        JScrollPane scroll = new JScrollPane(buildContent(exam, answers, correct, wrong, skip, pct, total));
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        add(scroll, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private JPanel buildContent(Exam exam, int[] answers, int correct, int wrong, int skip, int pct, int total) {
        JPanel content = new JPanel();
        content.setBackground(Theme.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Result box
        JPanel box = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(30, 30, 30, 30));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel done = new JLabel("🎉  Exam Completed!", SwingConstants.CENTER);
        done.setFont(new Font("SansSerif", Font.BOLD, 22));
        done.setForeground(Theme.TEXT);
        done.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(done);
        box.add(Box.createVerticalStrut(4));

        JLabel examName = new JLabel(exam.getName(), SwingConstants.CENTER);
        examName.setFont(Theme.SMALL_FONT);
        examName.setForeground(Theme.MUTED);
        examName.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(examName);
        box.add(Box.createVerticalStrut(20));

        // Score Ring (drawn manually)
        JPanel ring = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2, r = 52;
                g2.setColor(Theme.GOLD_DIM);
                g2.fillOval(cx-r, cy-r, 2*r, 2*r);
                g2.setColor(Theme.GOLD);
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(cx-r, cy-r, 2*r, 2*r);
                g2.setFont(new Font("SansSerif", Font.BOLD, 26));
                g2.setColor(Theme.GOLD);
                FontMetrics fm = g2.getFontMetrics();
                String s = pct + "%";
                g2.drawString(s, cx - fm.stringWidth(s)/2, cy + 6);
                g2.setFont(Theme.SMALL_FONT);
                g2.setColor(Theme.MUTED);
                String lbl = "Score";
                g2.drawString(lbl, cx - fm.stringWidth(lbl)/2 + 6, cy + 22);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(120, 120); }
        };
        ring.setOpaque(false);
        ring.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(ring);
        box.add(Box.createVerticalStrut(20));

        // Stats
        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        stats.add(makeStatCell(String.valueOf(correct), "Correct", Theme.SUCCESS));
        stats.add(makeStatCell(String.valueOf(wrong), "Wrong", Theme.DANGER));
        stats.add(makeStatCell(String.valueOf(skip), "Skipped", Theme.INFO));
        box.add(stats);
        box.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton reviewBtn = Theme.createButton("  View Answer Key  ", Theme.SURFACE, Theme.MUTED);
        reviewBtn.addActionListener(e -> {
            JDialog dlg = buildReviewDialog(exam, answers);
            dlg.setVisible(true);
        });

        JButton homeBtn = Theme.createButton("  Back to Home  ", Theme.GOLD, new Color(30, 18, 0));
        homeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        homeBtn.addActionListener(e -> controller.showDashboard());

        btnRow.add(reviewBtn);
        btnRow.add(homeBtn);
        box.add(btnRow);
        content.add(box);
        return content;
    }

    private JPanel makeStatCell(String value, String label, Color color) {
        JPanel cell = new JPanel();
        cell.setBackground(Theme.SURFACE);
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setBorder(new EmptyBorder(12, 8, 12, 8));
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("SansSerif", Font.BOLD, 24));
        val.setForeground(color);
        val.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(Theme.SMALL_FONT);
        lbl.setForeground(Theme.MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        cell.add(val);
        cell.add(Box.createVerticalStrut(3));
        cell.add(lbl);
        return cell;
    }

    private JDialog buildReviewDialog(Exam exam, int[] answers) {
        JDialog dlg = new JDialog((Frame) null, "Answer Key – " + exam.getName(), true);
        dlg.setSize(600, 500);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG);

        JPanel panel = new JPanel();
        panel.setBackground(Theme.BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        List<Question> qs = exam.getQuestions();
        String[] letters = {"A", "B", "C", "D"};
        for (int i = 0; i < qs.size(); i++) {
            Question q  = qs.get(i);
            int ua      = answers[i];
            boolean right  = ua == q.getCorrectIndex();
            boolean skipped = ua == -1;
            Color borderColor = skipped ? Theme.MUTED : (right ? Theme.SUCCESS : Theme.DANGER);
            String icon = skipped ? "⬜" : (right ? "✅" : "❌");

            JPanel item = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.SURFACE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                    g2.dispose();
                }
            };
            item.setOpaque(false);
            item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
            item.setBorder(new EmptyBorder(10, 14, 10, 14));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            item.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel qLabel = new JLabel(icon + "  Q" + (i+1) + ": " + q.getQuestionText());
            qLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            qLabel.setForeground(Theme.TEXT);
            item.add(qLabel);
            item.add(Box.createVerticalStrut(4));

            String yourAns = ua == -1 ? "Skipped" : letters[ua] + ". " + q.getOptions()[ua];
            JLabel yourLabel = new JLabel("Your answer: " + yourAns);
            yourLabel.setFont(Theme.SMALL_FONT);
            yourLabel.setForeground(right ? Theme.SUCCESS : Theme.DANGER);
            item.add(yourLabel);

            if (!right) {
                JLabel corrLabel = new JLabel("Correct: " + letters[q.getCorrectIndex()] + ". " + q.getCorrectAnswer());
                corrLabel.setFont(Theme.SMALL_FONT);
                corrLabel.setForeground(Theme.SUCCESS);
                item.add(corrLabel);
            }

            panel.add(item);
            panel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        dlg.add(scroll);
        return dlg;
    }
}
