package studiokita.gui;

import studiokita.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Panel sub-menu Input: pilih Sewa Alat atau Jasa Foto. */
public class InputMenuPanel extends JPanel {

    private final Navigator nav;

    public InputMenuPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());
        add(UIKit.pageHeader("📝", "Input Data Customer",
                "Pilih jenis layanan yang ingin diinput", UIKit.BLUE), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    // ── Konten: 2 pilihan besar ───────────────────────────────
    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIKit.BG);

        JPanel row = new JPanel(new GridLayout(1, 2, 30, 0));
        row.setBackground(UIKit.BG);
        row.setBorder(new EmptyBorder(60, 100, 60, 100));
        row.setPreferredSize(new Dimension(800, 360));

        row.add(buatKartu("🎒", "Sewa Alat",
                "Input data penyewaan kamera, lensa, tripod, dll.",
                UIKit.ORANGE, "sewa"));

        row.add(buatKartu("📸", "Jasa Foto",
                "Input booking sesi foto untuk pelanggan",
                UIKit.BLUE, "jasa"));

        wrapper.add(row);
        return wrapper;
    }

    private JPanel buatKartu(String icon, String title, String desc, Color accent, String dest) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIKit.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(
                        accent.getRed(), accent.getGreen(), accent.getBlue(), 100), 1),
                new EmptyBorder(35, 30, 35, 30)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lIcon = new JLabel(icon, SwingConstants.CENTER);
        lIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        lIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lTitle = UIKit.heading(title, accent, UIKit.FONT_H2);
        lTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lDesc = UIKit.heading(desc, UIKit.MUTED, UIKit.FONT_BODY);
        lDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lDesc.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnMasuk = UIKit.btn("Pilih  →", accent);
        btnMasuk.setForeground(UIKit.BG);
        btnMasuk.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMasuk.addActionListener(e -> nav.goTo(dest));

        card.add(lIcon);
        card.add(UIKit.gap(16));
        card.add(lTitle);
        card.add(UIKit.gap(10));
        card.add(lDesc);
        card.add(UIKit.gap(24));
        card.add(btnMasuk);

        Color hover = new Color(
                Math.min(UIKit.CARD.getRed() + 12, 255),
                Math.min(UIKit.CARD.getGreen() + 12, 255),
                Math.min(UIKit.CARD.getBlue() + 20, 255));
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(UIKit.CARD); }
        });
        return card;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(UIKit.SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIKit.BORDER),
                new EmptyBorder(10, 20, 10, 20)));
        JButton btnBack = UIKit.btn("← Kembali ke Dashboard", new Color(65, 65, 95));
        btnBack.addActionListener(e -> nav.goTo("dashboard"));
        p.add(btnBack);
        return p;
    }
}
