package studiokita.gui;

import studiokita.DataStore;
import studiokita.Navigator;
import studiokita.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {

    private final Navigator nav;
    private JLabel lblTrxHariIni, lblTotalData, lblPenghasilan;
    private JLabel lblWelcome;

    public DashboardPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());

        // Mempertahankan Top Bar yang baru
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIKit.SURFACE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(12, 28, 12, 28)));
        bar.setPreferredSize(new Dimension(0, 60));

        JLabel lblApp = new JLabel("📸 STUDIO KITA");
        lblApp.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24)); 
        lblApp.setForeground(UIKit.ACCENT);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);

        lblWelcome = new JLabel("Halo, ...");
        lblWelcome.setFont(UIKit.FONT_NORMAL);
        lblWelcome.setForeground(UIKit.MUTED);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(UIKit.RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Kembali ke halaman login?", "Konfirmasi Exit",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                nav.goTo("login");
            }
        });

        rightPanel.add(lblWelcome);
        rightPanel.add(btnLogout);

        bar.add(lblApp, BorderLayout.WEST);
        bar.add(rightPanel, BorderLayout.EAST);
        return bar;
    }

    // ── KONTEN DIKEMBALIKAN KE DESAIN ASLI ────────────────────
    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIKit.BG);

        JPanel grid = new JPanel(new GridLayout(2, 2, 24, 24));
        grid.setBackground(UIKit.BG);
        grid.setBorder(new EmptyBorder(40, 60, 40, 60));
        grid.setPreferredSize(new Dimension(800, 400));

        // KOTAK 1: Input Layanan (Ke menu pilihan sewa/jasa)
        lblTrxHariIni = UIKit.heading("0 Transaksi Hari Ini", UIKit.ORANGE, UIKit.FONT_H2);
        grid.add(buatKartu("📝", "Input Layanan", lblTrxHariIni, "inputmenu"));

        // KOTAK 2: Data Customer (Ke tabel Rekap - SEPERTI PERMINTAANMU)
        lblTotalData = UIKit.heading("0 Data", UIKit.GREEN, UIKit.FONT_H2);
        grid.add(buatKartu("👥", "Data Customer", lblTotalData, "rekap"));

        // KOTAK 3: Penghasilan
        lblPenghasilan = UIKit.heading("Rp 0", UIKit.BLUE, UIKit.FONT_H2);
        grid.add(buatKartu("💰", "Penghasilan", lblPenghasilan, "penghasilan"));

        // KOTAK 4: Keluar
        JLabel lblLogout = UIKit.heading("Logout Aplikasi", UIKit.RED, UIKit.FONT_H2);
        grid.add(buatKartu("🚪", "Keluar", lblLogout, "exit"));

        wrapper.add(grid);
        return wrapper;
    }

    private JPanel buatKartu(String icon, String title, JLabel lDesc, String action) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIKit.CARD);
        card.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        JLabel lIcon = new JLabel(icon, SwingConstants.CENTER);
        lIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        lIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(UIKit.FONT_NORMAL);
        lTitle.setForeground(UIKit.TEXT);
        lTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lDesc.setHorizontalAlignment(SwingConstants.CENTER);

        inner.add(lIcon);
        inner.add(UIKit.gap(12));
        inner.add(lTitle);
        inner.add(UIKit.gap(6));
        inner.add(lDesc);
        card.add(inner);

        Color normalBg = UIKit.CARD;
        Color hoverBg = new Color(
                Math.min(UIKit.CARD.getRed() + 15, 255),
                Math.min(UIKit.CARD.getGreen() + 15, 255),
                Math.min(UIKit.CARD.getBlue() + 20, 255));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(normalBg);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Logika klik dikembalikan seperti semula
                if ("exit".equals(action)) {
                    int ok = JOptionPane.showConfirmDialog(card,
                            "Kembali ke halaman login?", "Konfirmasi Exit",
                            JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.YES_OPTION) {
                        nav.goTo("login");
                    }
                } else {
                    nav.goTo(action);
                }
            }
        });
        return card;
    }

    // ── UPDATE OTOMATIS DATA DI DASHBOARD ────────────────────
    public void refresh() {
        int trxHariIni = 0;
        double incomeToday = 0;
        LocalDate today = LocalDate.now();

        // Hitung ulang data terbaru dari DataStore (menggunakan OOP)
        for (studiokita.model.Transaksi t : DataStore.daftarTransaksi) {
            if (t.getTglInput().equals(today)) {
                trxHariIni++;
                incomeToday += t.getTotalBiaya();
            }
        }

        // Tampilkan ke teks
        lblTrxHariIni.setText(trxHariIni + " Transaksi Hari Ini");
        lblTotalData.setText(DataStore.daftarTransaksi.size() + " Data");
        lblPenghasilan.setText("Rp " + String.format("%,.0f", incomeToday));

        // --- PERBAIKAN DI SINI ---
        // Cek apakah ada user yang sedang login menggunakan sistem User OOP yang baru
        if (DataStore.userLoginSekarang != null) {
            String nama = DataStore.userLoginSekarang.getNamaLengkap();
            String role = DataStore.userLoginSekarang.getRole();
            lblWelcome.setText("Halo, " + nama + "  [" + role + "]");
        } else {
            lblWelcome.setText("Halo, Guest");
        }
    }
}