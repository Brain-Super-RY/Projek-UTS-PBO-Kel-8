package studiokita.gui;

import studiokita.model.*;
import studiokita.DataStore;
import studiokita.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Panel Penghasilan — rekap penghasilan hari ini. */
public class PenghasilanPanel extends JPanel {

    private final Navigator       nav;
    private       DefaultTableModel model;
    private       JLabel            lblTglHariIni;
    private       JLabel            lblJmlTransaksi;
    private       JLabel            lblTotalSewa;
    private       JLabel            lblTotalJasa;
    private       JLabel            lblGrandTotal;

    public PenghasilanPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());

        // Inisialisasi labels dulu sebelum build
        lblTglHariIni   = new JLabel("-");
        lblJmlTransaksi = new JLabel("0");
        lblTotalSewa    = new JLabel("Rp 0");
        lblTotalJasa    = new JLabel("Rp 0");
        lblGrandTotal   = new JLabel("TOTAL HARI INI:  Rp 0");

        JPanel northArea = new JPanel(new BorderLayout());
        northArea.setBackground(UIKit.BG);

        // ── INI BAGIAN YANG DIUBAH (Header Custom dengan Emoji) ─────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIKit.SURFACE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(16, 28, 16, 28)));

        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);

        JLabel lblTitle = new JLabel("💰 DETAIL PENGHASILAN");
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitle.setForeground(UIKit.ACCENT);

        JLabel lblDesc = new JLabel("Rekap penghasilan masuk hari ini");
        lblDesc.setFont(UIKit.FONT_NORMAL);
        lblDesc.setForeground(UIKit.MUTED);

        titleArea.add(lblTitle);
        titleArea.add(UIKit.gap(4));
        titleArea.add(lblDesc);

        headerPanel.add(titleArea, BorderLayout.WEST);
        northArea.add(headerPanel, BorderLayout.NORTH);
        northArea.add(buildKartuRingkasan(), BorderLayout.SOUTH);

        add(northArea,      BorderLayout.NORTH);
        add(buildTengah(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    private JPanel buildKartuRingkasan() {
        JPanel p = new JPanel(new GridLayout(1, 4, 16, 0));
        p.setBackground(UIKit.SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(14, 24, 14, 24)));

        p.add(kartuPanel(lblTglHariIni,   "Tanggal",            UIKit.MUTED,   new Font("Segoe UI", Font.BOLD, 15)));
        p.add(kartuPanel(lblJmlTransaksi, "Transaksi Hari Ini", UIKit.BLUE,    new Font("Segoe UI", Font.BOLD, 26)));
        p.add(kartuPanel(lblTotalSewa,    "Dari Sewa Alat",     UIKit.ORANGE,  new Font("Segoe UI", Font.BOLD, 16)));
        p.add(kartuPanel(lblTotalJasa,    "Dari Jasa Foto",     UIKit.GREEN,   new Font("Segoe UI", Font.BOLD, 16)));
        return p;
    }

    private JPanel kartuPanel(JLabel nilaiLabel, String labelText, Color color, Font font) {
        nilaiLabel.setFont(font);
        nilaiLabel.setForeground(color);
        nilaiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lLabel = UIKit.label(labelText);
        lLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(color.getRed()/7, color.getGreen()/7, color.getBlue()/7, 160));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)),
                new EmptyBorder(10, 14, 10, 14)));
        card.add(lLabel);
        card.add(UIKit.gap(5));
        card.add(nilaiLabel);
        return card;
    }

    private JPanel buildTengah() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UIKit.BG);
        p.setBorder(new EmptyBorder(16, 20, 10, 20));

        String[] cols = {"No", "Jenis", "Nama Customer", "Detail Layanan", "Total Biaya", "Tgl Input"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabel = new JTable(model);
        UIKit.styleTable(tabel, UIKit.ACCENT);
        tabel.setForeground(Color.BLACK);
        tabel.getTableHeader().setForeground(Color.BLACK);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(260);

        // Footer grand total
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGrandTotal.setForeground(UIKit.ACCENT);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        footer.setBackground(UIKit.BG);
        footer.add(lblGrandTotal);

        p.add(UIKit.scroll(tabel), BorderLayout.CENTER);
        p.add(footer,               BorderLayout.SOUTH);
        return p;
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

    // ── KODE BARU OOP ─────────────────────────────────────────
    public void refresh() {
        model.setRowCount(0);
        java.time.LocalDate hari = java.time.LocalDate.now();
        double totalSewa = 0, totalJasa = 0;
        int no = 1, jmlTrx = 0;

        for (studiokita.model.Transaksi r : studiokita.DataStore.daftarTransaksi) {
            if (hari.equals(r.getTglInput())) {
                String jenis = "";
                if (r.getLayanan() instanceof studiokita.model.SewaAlat) {
                    jenis = "Sewa Alat";
                    totalSewa += r.getTotalBiaya();
                } else {
                    jenis = "Jasa Foto";
                    totalJasa += r.getTotalBiaya();
                }

                model.addRow(new Object[]{ 
                    no++, 
                    jenis, 
                    r.getCustomer().getNamaLengkap(), // <-- PERBAIKAN DI SINI
                    r.getLayanan().getDeskripsi(),
                    "Rp " + String.format("%,.0f", r.getTotalBiaya()), 
                    r.getTglInput() 
                });
                jmlTrx++;
            }
        }

        lblTglHariIni.setText(hari.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblJmlTransaksi.setText(String.valueOf(jmlTrx));
        lblTotalSewa.setText("Rp " + String.format("%,.0f", totalSewa));
        lblTotalJasa.setText("Rp " + String.format("%,.0f", totalJasa));
        lblGrandTotal.setText("TOTAL HARI INI:  Rp " + String.format("%,.0f", (totalSewa + totalJasa)));
    }
}