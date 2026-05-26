package exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {

    private AppController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginPanel(AppController controller) {
        this.controller = controller;
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(420, 480));

        
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel("🎓", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 44));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("EXAM PORTAL", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Theme.GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Online Examination System", SwingConstants.CENTER);
        subtitle.setFont(Theme.SMALL_FONT);
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        brand.add(icon);
        brand.add(Box.createVerticalStrut(8));
        brand.add(title);
        brand.add(Box.createVerticalStrut(4));
        brand.add(subtitle);
        card.add(brand);
        card.add(Box.createVerticalStrut(30));


        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(Theme.SMALL_FONT);
        errorLabel.setForeground(Theme.DANGER);
        errorLabel.setOpaque(true);
        errorLabel.setBackground(new Color(232, 76, 76, 30));
        errorLabel.setBorder(new EmptyBorder(6, 10, 6, 10));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setMaximumSize(new Dimension(340, 30));
        errorLabel.setVisible(false);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(10));

        
        card.add(makeFieldLabel("USERNAME"));
        card.add(Box.createVerticalStrut(6));
        usernameField = Theme.createTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));

        
        card.add(makeFieldLabel("PASSWORD"));
        card.add(Box.createVerticalStrut(6));
        passwordField = Theme.createPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(22));

        
        JButton loginBtn = Theme.createButton("  LOGIN  →", Theme.GOLD, new Color(30, 18, 0));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);

        card.add(Box.createVerticalStrut(16));
        JLabel hint = new JLabel("<html><center>Demo credentials: <b>admin</b> / <b>admin123</b></center></html>", SwingConstants.CENTER);
        hint.setFont(Theme.SMALL_FONT);
        hint.setForeground(Theme.MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        
        passwordField.addActionListener(e -> doLogin());

        add(card);
    }

    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(Theme.MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        User user = DataStore.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            errorLabel.setVisible(false);
            usernameField.setText("");
            passwordField.setText("");
            controller.onLoginSuccess(user);
        } else {
            errorLabel.setText("❌  Invalid username or password.");
            errorLabel.setVisible(true);
            passwordField.setText("");
        }
    }
}
