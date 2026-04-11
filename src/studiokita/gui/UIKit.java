package studiokita.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** UIKit — Tema warna & helper komponen terpusat. */
public class UIKit {

    // ── Palet Warna ───────────────────────────────────────────
    public static final Color BG       = new Color(22, 22, 35);
    public static final Color SURFACE  = new Color(32, 32, 50);
    public static final Color CARD     = new Color(42, 42, 62);
    public static final Color FIELD    = new Color(52, 52, 75);
    public static final Color BORDER   = new Color(70, 70, 100);
    public static final Color TEXT     = new Color(235, 235, 245);
    public static final Color MUTED    = new Color(140, 140, 165);
    public static final Color ACCENT   = new Color(255, 185, 55);   // kuning emas
    public static final Color BLUE     = new Color(75, 155, 230);
    public static final Color GREEN    = new Color(55, 165, 110);
    public static final Color RED      = new Color(210, 65, 65);
    public static final Color ORANGE   = new Color(220, 130, 50);

    // ── Font ─────────────────────────────────────────────────
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_BODY  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_H1    = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_H2    = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_H3    = new Font("Segoe UI", Font.BOLD, 15);

    // ── Komponen ─────────────────────────────────────────────

    /** Tombol bergaya dengan warna custom. */
    public static JButton btn(String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BOLD);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(140, 36));
        return b;
    }

    /** Tombol lebar penuh. */
    public static JButton btnFull(String label, Color bg) {
        JButton b = btn(label, bg);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    /** TextField bergaya. */
    public static JTextField field() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    /** PasswordField bergaya. */
    public static JPasswordField passField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    private static void styleField(JTextField f) {
        f.setBackground(FIELD);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(5, 10, 5, 10)));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    /** ComboBox bergaya. */
    public static <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> c = new JComboBox<>(items);
        c.setBackground(FIELD);
        c.setForeground(TEXT);
        c.setFont(FONT_BODY);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return c;
    }

    /** Spinner bergaya. */
    public static JSpinner spinner(int val, int min, int max, int step) {
        JSpinner s = new JSpinner(new SpinnerNumberModel(val, min, max, step));
        s.getEditor().getComponent(0).setBackground(FIELD);
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setForeground(TEXT);
        s.setFont(FONT_BODY);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return s;
    }

    /** Label kecil untuk form. */
    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /** Label besar untuk heading. */
    public static JLabel heading(String text, Color color, Font font) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    /** Terapkan styling tabel. */
    public static void styleTable(JTable t, Color accent) {
        t.setBackground(CARD);
        t.setForeground(TEXT);
        t.setGridColor(new Color(55, 55, 80));
        t.setRowHeight(28);
        t.setFont(FONT_BODY);
        t.setSelectionBackground(new Color(80, 80, 150));
        t.setSelectionForeground(TEXT);
        t.getTableHeader().setBackground(SURFACE);
        t.getTableHeader().setForeground(accent);
        t.getTableHeader().setFont(FONT_BOLD);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
    }

    /** ScrollPane untuk tabel. */
    public static JScrollPane scroll(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        return sp;
    }

    /** Panel form sidebar kanan. */
    public static JPanel formPanel(int width) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(SURFACE);
        p.setPreferredSize(new Dimension(width, 0));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER),
                new EmptyBorder(18, 18, 18, 18)));
        return p;
    }

    /** Spacer vertikal. */
    public static Component gap(int h) {
        return Box.createRigidArea(new Dimension(0, h));
    }

    /** Separator horizontal. */
    public static void addSep(JPanel p) {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(sep);
    }

    /** Label biaya preview (besar, berwarna). */
    public static JLabel previewBiaya(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(GREEN);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /** Panel header halaman. */
    public static JPanel pageHeader(String icon, String title, String subtitle, Color accentColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(16, 24, 16, 24)));

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        JLabel lTitle = new JLabel(icon + "  " + title);
        lTitle.setFont(FONT_H2);
        lTitle.setForeground(accentColor);

        JLabel lSub = new JLabel(subtitle);
        lSub.setFont(FONT_SMALL);
        lSub.setForeground(MUTED);

        titles.add(lTitle);
        titles.add(gap(3));
        titles.add(lSub);
        p.add(titles, BorderLayout.WEST);
        return p;
    }
}
