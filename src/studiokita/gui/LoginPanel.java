package studiokita.gui;

import studiokita.DataStore;
import studiokita.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Panel Login — validasi username & password admin. */
public class LoginPanel extends JPanel {

    private final Navigator    nav;
    private       JTextField   txtUser;
    private       JPasswordField txtPass;
    private       JLabel       lblStatus;

    public LoginPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new GridBagLayout());
        add(buildCard());
    }

    // ── Card login di tengah layar ────────────────────────────
    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIKit.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIKit.BORDER),
                new EmptyBorder(40, 50, 40, 50)));
        card.setPreferredSize(new Dimension(400, 500));

        // ── Logo ──────────────────────────────────────────────
        JLabel logo = new JLabel("📷", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = UIKit.heading("STUDIO KITA", UIKit.ACCENT, UIKit.FONT_H1);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = UIKit.heading("Login Aplikasi", UIKit.MUTED, UIKit.FONT_NORMAL);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Form ──────────────────────────────────────────────
        JLabel lUser = UIKit.heading("Username", UIKit.TEXT, UIKit.FONT_SMALL);
        lUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUser = new JTextField();
        txtUser.setMaximumSize(new Dimension(280, 40));
        txtUser.setCaretColor(UIKit.TEXT);
        txtUser.setBackground(UIKit.FIELD);
        txtUser.setForeground(UIKit.TEXT);
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIKit.BORDER),
                new EmptyBorder(5, 10, 5, 10)));

        JLabel lPass = UIKit.heading("Password", UIKit.TEXT, UIKit.FONT_SMALL);
        lPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(280, 40));
        txtPass.setCaretColor(UIKit.TEXT);
        txtPass.setBackground(UIKit.FIELD);
        txtPass.setForeground(UIKit.TEXT);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIKit.BORDER),
                new EmptyBorder(5, 10, 5, 10)));

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(UIKit.RED);
        lblStatus.setFont(UIKit.FONT_SMALL);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Tombol ────────────────────────────────────────────
        JButton btnMasuk = UIKit.btn("Masuk", UIKit.ACCENT);
        btnMasuk.setForeground(UIKit.BG);
        btnMasuk.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMasuk.addActionListener(e -> prosesLogin());

        JButton btnKeluar = UIKit.btn("Keluar", UIKit.SURFACE);
        btnKeluar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKeluar.addActionListener(e -> System.exit(0));

        JLabel hint = new JLabel("Hint: admin / admin123  ATAU  user / user123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(UIKit.BORDER);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Rakit ─────────────────────────────────────────────
        card.add(logo);
        card.add(UIKit.gap(6));
        card.add(title);
        card.add(UIKit.gap(4));
        card.add(sub);
        card.add(UIKit.gap(32));
        card.add(lUser);
        card.add(UIKit.gap(5));
        card.add(txtUser);
        card.add(UIKit.gap(14));
        card.add(lPass);
        card.add(UIKit.gap(5));
        card.add(txtPass);
        card.add(UIKit.gap(8));
        card.add(lblStatus);
        card.add(UIKit.gap(16));
        card.add(btnMasuk);
        card.add(UIKit.gap(8));
        card.add(btnKeluar);
        card.add(UIKit.gap(20));
        card.add(hint);

        // Enter → login
        txtPass.addActionListener(e -> prosesLogin());
        return card;
    }

   // ── Logika Login yang Baru (OOP Based) ─────────────────────
    private void prosesLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        // Menggunakan fungsi cekLogin dari DataStore
        // Fungsi ini akan otomatis mencari kecocokan dari List User
        if (DataStore.cekLogin(user, pass)) {
            lblStatus.setText(" ");
            txtUser.setText("");
            txtPass.setText("");
            
            // Pindah ke dashboard setelah berhasil login
            nav.goTo("dashboard");
            
        } else {
            lblStatus.setText("Username atau password salah!");
        }
    }
}