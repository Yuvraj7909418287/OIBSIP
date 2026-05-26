package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private AppController controller;
    private User user;

    private JTextField fnameField, lnameField, emailField, phoneField;
    private JPasswordField curPassField, newPassField, confPassField;
    private JLabel statusLabel;
    private JLabel avatarLabel, nameLabel, emailLabel;

    public ProfilePanel(AppController controller) {
        this.controller = controller;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Theme.CARD);
        nav.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Theme.BORDER),
            new EmptyBorder(10, 20, 10, 20)
        ));
        JLabel title = new JLabel("👤  My Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Theme.GOLD);
        nav.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton back = Theme.createButton("← Back to Dashboard", Theme.SURFACE, Theme.MUTED);
        back.setFont(Theme.SMALL_FONT);
        back.addActionListener(e -> controller.showDashboard());
        JButton logout = Theme.createButton("Logout", Theme.SURFACE, Theme.MUTED);
        logout.setFont(Theme.SMALL_FONT);
        logout.addActionListener(e -> controller.confirmLogout());
        right.add(back);
        right.add(logout);
        nav.add(right, BorderLayout.EAST);
        add(nav, BorderLayout.NORTH);

    
        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setBackground(Theme.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 80, 24, 80));

        
        JPanel header = buildHeaderCard();
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        content.add(header);
        content.add(Box.createVerticalStrut(20));

        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(Theme.SMALL_FONT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setVisible(false);

    
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.CARD);
        tabs.setForeground(Theme.TEXT);
        tabs.setFont(Theme.BODY_FONT);
        tabs.addTab("  Personal Info  ", buildInfoTab());
        tabs.addTab("  Change Password  ", buildPasswordTab());
        styleTabPane(tabs);
        content.add(tabs);
        return content;
    }

    private void styleTabPane(JTabbedPane tabs) {
        tabs.setBackground(Theme.CARD);
        tabs.setForeground(Theme.TEXT);
        UIManager.put("TabbedPane.selected", Theme.GOLD_DIM);
        UIManager.put("TabbedPane.background", Theme.CARD);
        UIManager.put("TabbedPane.foreground", Theme.TEXT);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
    }

    private JPanel buildHeaderCard() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16)) {
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
        card.setOpaque(false);

        avatarLabel = new JLabel("A", SwingConstants.CENTER);
        avatarLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        avatarLabel.setForeground(Theme.GOLD);
        avatarLabel.setPreferredSize(new Dimension(70, 70));
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Theme.GOLD_DIM);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 2));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        nameLabel = new JLabel("Admin User");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(Theme.TEXT);
        emailLabel = new JLabel("admin@examportal.com");
        emailLabel.setFont(Theme.SMALL_FONT);
        emailLabel.setForeground(Theme.MUTED);
        info.add(nameLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(emailLabel);

        card.add(avatarLabel);
        card.add(info);
        return card;
    }

    private JPanel buildInfoTab() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 14, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel fnameG = makeFormGroup("FIRST NAME");
        fnameField = Theme.createTextField();
        fnameG.add(fnameField);
        row1.add(fnameG);

        JPanel lnameG = makeFormGroup("LAST NAME");
        lnameField = Theme.createTextField();
        lnameG.add(lnameField);
        row1.add(lnameG);
        panel.add(row1);
        panel.add(Box.createVerticalStrut(14));

        JPanel emailG = makeFormGroup("EMAIL ADDRESS");
        emailField = Theme.createTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        emailG.add(emailField);
        emailG.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.add(emailG);
        panel.add(Box.createVerticalStrut(14));

        JPanel phoneG = makeFormGroup("PHONE NUMBER");
        phoneField = Theme.createTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        phoneG.add(phoneField);
        phoneG.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.add(phoneG);
        panel.add(Box.createVerticalStrut(20));

        JLabel infoStatus = new JLabel(" ");
        infoStatus.setFont(Theme.SMALL_FONT);
        infoStatus.setName("infoStatus");
        panel.add(infoStatus);
        panel.add(Box.createVerticalStrut(8));

        JButton save = Theme.createButton("  Save Changes  ", Theme.GOLD, new Color(30, 18, 0));
        save.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        save.setFont(new Font("SansSerif", Font.BOLD, 14));
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e -> saveInfo(infoStatus));
        panel.add(save);
        return panel;
    }

    private JPanel buildPasswordTab() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel curG = makeFormGroup("CURRENT PASSWORD");
        curPassField = Theme.createPasswordField();
        curPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        curG.add(curPassField);
        curG.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.add(curG);
        panel.add(Box.createVerticalStrut(14));

        JPanel newG = makeFormGroup("NEW PASSWORD");
        newPassField = Theme.createPasswordField();
        newPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newG.add(newPassField);
        newG.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.add(newG);
        panel.add(Box.createVerticalStrut(14));

        JPanel confG = makeFormGroup("CONFIRM NEW PASSWORD");
        confPassField = Theme.createPasswordField();
        confPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        confG.add(confPassField);
        confG.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.add(confG);
        panel.add(Box.createVerticalStrut(20));

        JLabel passStatus = new JLabel(" ");
        passStatus.setFont(Theme.SMALL_FONT);
        panel.add(passStatus);
        panel.add(Box.createVerticalStrut(8));

        JButton change = Theme.createButton("  Update Password  ", Theme.GOLD, new Color(30, 18, 0));
        change.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        change.setFont(new Font("SansSerif", Font.BOLD, 14));
        change.setAlignmentX(Component.LEFT_ALIGNMENT);
        change.addActionListener(e -> changePassword(passStatus));
        panel.add(change);
        return panel;
    }

    private JPanel makeFormGroup(String labelText) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(Theme.MUTED);
        g.add(lbl);
        g.add(Box.createVerticalStrut(5));
        return g;
    }

    private void saveInfo(JLabel status) {
        String fname = fnameField.getText().trim();
        String lname = lnameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (fname.isEmpty()) {
            status.setText("❌  First name cannot be empty.");
            status.setForeground(Theme.DANGER);
            return;
        }
        user.setFirstName(fname);
        user.setLastName(lname);
        user.setEmail(email);
        user.setPhone(phone);
        nameLabel.setText(user.getFullName());
        emailLabel.setText(user.getEmail());
        avatarLabel.setText(user.getInitials());
        controller.onUserUpdated();
        status.setText("✅  Profile updated successfully!");
        status.setForeground(Theme.SUCCESS);
        Timer t = new Timer(3000, ev -> status.setText(" "));
        t.setRepeats(false); t.start();
    }

    private void changePassword(JLabel status) {
        String cur  = new String(curPassField.getPassword());
        String nw   = new String(newPassField.getPassword());
        String conf = new String(confPassField.getPassword());

        if (!cur.equals(user.getPassword())) {
            status.setText("❌  Current password is incorrect.");
            status.setForeground(Theme.DANGER); return;
        }
        if (nw.length() < 4) {
            status.setText("❌  New password must be at least 4 characters.");
            status.setForeground(Theme.DANGER); return;
        }
        if (!nw.equals(conf)) {
            status.setText("❌  Passwords do not match.");
            status.setForeground(Theme.DANGER); return;
        }
        user.setPassword(nw);
        curPassField.setText(""); newPassField.setText(""); confPassField.setText("");
        status.setText("✅  Password updated successfully!");
        status.setForeground(Theme.SUCCESS);
        Timer t = new Timer(3000, ev -> status.setText(" "));
        t.setRepeats(false); t.start();
    }

    public void loadUser(User u) {
        this.user = u;
        avatarLabel.setText(u.getInitials());
        nameLabel.setText(u.getFullName());
        emailLabel.setText(u.getEmail());
        fnameField.setText(u.getFirstName());
        lnameField.setText(u.getLastName());
        emailField.setText(u.getEmail());
        phoneField.setText(u.getPhone());
        curPassField.setText(""); newPassField.setText(""); confPassField.setText("");
    }
}
