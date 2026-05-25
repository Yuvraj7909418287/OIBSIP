package exam;

import java.awt.*;

public class Theme {
    // Colors
    public static final Color BG          = new Color(15, 17, 23);
    public static final Color SURFACE     = new Color(24, 28, 39);
    public static final Color CARD        = new Color(30, 35, 54);
    public static final Color BORDER      = new Color(42, 48, 80);
    public static final Color GOLD        = new Color(245, 166, 35);
    public static final Color GOLD_LIGHT  = new Color(255, 200, 90);
    public static final Color GOLD_DIM    = new Color(40, 32, 8);
    public static final Color TEXT        = new Color(232, 234, 242);
    public static final Color MUTED       = new Color(138, 144, 168);
    public static final Color DANGER      = new Color(232, 76, 76);
    public static final Color SUCCESS     = new Color(46, 204, 113);
    public static final Color INFO        = new Color(74, 142, 245);
    public static final Color WHITE       = Color.WHITE;

    // Fonts
    public static final Font TITLE_FONT  = new Font("SansSerif", Font.BOLD, 22);
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font BODY_FONT   = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font SMALL_FONT  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font MONO_FONT   = new Font("Monospaced", Font.BOLD, 20);
    public static final Font BOLD_FONT   = new Font("SansSerif", Font.BOLD, 14);

    // Dimensions
    public static final int WINDOW_W = 900;
    public static final int WINDOW_H = 650;

    // Helper: Rounded Rectangle Button
    public static javax.swing.JButton createButton(String text, Color bg, Color fg) {
        javax.swing.JButton btn = new javax.swing.JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg.darker());
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        btn.setFont(BOLD_FONT);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static javax.swing.JTextField createTextField() {
        javax.swing.JTextField tf = new javax.swing.JTextField();
        tf.setFont(BODY_FONT);
        tf.setBackground(SURFACE);
        tf.setForeground(TEXT);
        tf.setCaretColor(GOLD);
        tf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    public static javax.swing.JPasswordField createPasswordField() {
        javax.swing.JPasswordField pf = new javax.swing.JPasswordField();
        pf.setFont(BODY_FONT);
        pf.setBackground(SURFACE);
        pf.setForeground(TEXT);
        pf.setCaretColor(GOLD);
        pf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    public static javax.swing.JLabel createLabel(String text, Font font, Color color) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }
}
