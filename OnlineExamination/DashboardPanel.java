package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private AppController controller;
    private JLabel welcomeLabel;
    private JLabel avatarLabel;

    public DashboardPanel(AppController controller) {
        this.controller = controller;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        // --- TOP NAV BAR ---
        JPanel navbar = buildNavBar();
        add(navbar, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Theme.CARD);
        nav.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Theme.BORDER),
            new EmptyBorder(10, 20, 10, 20)
        ));

        JLabel brand = new JLabel("🎓 ExamPortal");
        brand.setFont(new Font("SansSerif", Font.BOLD, 16));
        brand.setForeground(Theme.GOLD);
        nav.add(brand, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        avatarLabel = new JLabel("A");
        avatarLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        avatarLabel.setForeground(Theme.GOLD);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(32, 32));
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Theme.GOLD_DIM);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 1));

        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(Theme.SMALL_FONT);
        welcomeLabel.setForeground(Theme.MUTED);

        JButton profileBtn = Theme.createButton("Profile", Theme.SURFACE, Theme.MUTED);
        profileBtn.setPreferredSize(new Dimension(80, 30));
        profileBtn.setFont(Theme.SMALL_FONT);
        profileBtn.addActionListener(e -> controller.showProfile());

        JButton logoutBtn = Theme.createButton("Logout", Theme.SURFACE, Theme.MUTED);
        logoutBtn.setPreferredSize(new Dimension(80, 30));
        logoutBtn.setFont(Theme.SMALL_FONT);
        logoutBtn.addActionListener(e -> controller.confirmLogout());

        right.add(avatarLabel);
        right.add(welcomeLabel);
        right.add(profileBtn);
        right.add(logoutBtn);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setBackground(Theme.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Welcome Banner
        content.add(buildWelcomeBanner());
        content.add(Box.createVerticalStrut(24));

        // Quick Actions
        content.add(makeSectionTitle("QUICK ACTIONS"));
        content.add(Box.createVerticalStrut(10));
        content.add(buildQuickActions());
        content.add(Box.createVerticalStrut(24));

        // Available Exams
        content.add(makeSectionTitle("AVAILABLE EXAMS"));
        content.add(Box.createVerticalStrut(10));
        content.add(buildExamList());
        return content;
    }

    private JPanel buildWelcomeBanner() {
        JPanel banner = new JPanel(new BorderLayout(16, 0)) {
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
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(20, 20, 20, 20));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel bigAvatar = new JLabel("A", SwingConstants.CENTER);
        bigAvatar.setFont(new Font("SansSerif", Font.BOLD, 22));
        bigAvatar.setForeground(Theme.GOLD);
        bigAvatar.setPreferredSize(new Dimension(52, 52));
        bigAvatar.setOpaque(true);
        bigAvatar.setBackground(Theme.GOLD_DIM);
        bigAvatar.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 2));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel wName = new JLabel("Welcome back, Admin!");
        wName.setFont(new Font("SansSerif", Font.BOLD, 16));
        wName.setForeground(Theme.TEXT);
        wName.setName("wName");
        JLabel wSub = new JLabel("Ready to attempt an exam today?");
        wSub.setFont(Theme.SMALL_FONT);
        wSub.setForeground(Theme.MUTED);
        textPanel.add(wName);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(wSub);

        banner.add(bigAvatar, BorderLayout.WEST);
        banner.add(textPanel, BorderLayout.CENTER);
        banner.putClientProperty("bigAvatar", bigAvatar);
        banner.putClientProperty("wName", wName);
        return banner;
    }

    private JPanel buildQuickActions() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        row.add(makeDashCard("📝", "Start Exam", "Attempt available exams", () -> {
            List<Exam> exams = DataStore.getExams();
            if (!exams.isEmpty()) controller.showExamSelection();
        }));
        row.add(makeDashCard("👤", "My Profile", "Update info & password", () -> controller.showProfile()));
        return row;
    }

    private JPanel buildExamList() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (Exam exam : DataStore.getExams()) {
            list.add(makeExamItem(exam));
            list.add(Box.createVerticalStrut(10));
        }
        return list;
    }

    private JPanel makeExamItem(Exam exam) {
        JPanel item = new JPanel(new BorderLayout()) {
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
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(14, 18, 14, 18));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(exam.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(Theme.TEXT);
        int mins = exam.getDurationSeconds() / 60;
        JLabel detail = new JLabel(exam.getTotalQuestions() + " Questions  •  " + mins + " Minutes");
        detail.setFont(Theme.SMALL_FONT);
        detail.setForeground(Theme.MUTED);
        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(detail);

        JLabel badge = new JLabel("  Active  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(Theme.GOLD);
        badge.setOpaque(true);
        badge.setBackground(Theme.GOLD_DIM);
        badge.setBorder(BorderFactory.createLineBorder(new Color(245, 166, 35, 80), 1, true));

        item.add(info, BorderLayout.CENTER);
        item.add(badge, BorderLayout.EAST);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { controller.startExam(exam); }
            @Override public void mouseEntered(MouseEvent e) { item.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Theme.GOLD, 1, true), new EmptyBorder(13, 17, 13, 17))); }
            @Override public void mouseExited(MouseEvent e)  { item.setBorder(new EmptyBorder(14, 18, 14, 18)); }
        });
        return item;
    }

    private JPanel makeDashCard(String emoji, String title, String subtitle, Runnable action) {
        JPanel card = new JPanel() {
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
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(emoji);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 28));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Theme.TEXT);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(Theme.SMALL_FONT);
        sub.setForeground(Theme.MUTED);
        card.add(ico);
        card.add(Box.createVerticalStrut(8));
        card.add(lbl);
        card.add(Box.createVerticalStrut(3));
        card.add(sub);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { action.run(); }
            @Override public void mouseEntered(MouseEvent e) { card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Theme.GOLD, 1, true), new EmptyBorder(17, 17, 17, 17))); }
            @Override public void mouseExited(MouseEvent e)  { card.setBorder(new EmptyBorder(18, 18, 18, 18)); }
        });
        return card;
    }

    private JLabel makeSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(Theme.MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    public void refreshUser(User user) {
        welcomeLabel.setText(user.getFirstName());
        avatarLabel.setText(user.getInitials());
    }
}
