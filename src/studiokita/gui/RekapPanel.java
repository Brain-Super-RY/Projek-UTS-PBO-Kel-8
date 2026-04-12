package studiokita.gui;

import studiokita.model.*;
import studiokita.DataStore;
import studiokita.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RekapPanel extends JPanel {

    private final Navigator nav;
    private DefaultTableModel model;
    private JLabel lblCustCount, lblSewaCount, lblJasaCount;
    private JTable tabel; 

    public RekapPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());

        JPanel northArea = new JPanel(new BorderLayout());
        northArea.setBackground(UIKit.BG);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIKit.SURFACE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(16, 28, 16, 28)));

        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);

        JLabel lblTitle = new JLabel("👥 TAMPILKAN DATA CUSTOMER");
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitle.setForeground(UIKit.GREEN);

        JLabel lblDesc = new JLabel("Rekap semua data pelanggan sewa & jasa foto");
        lblDesc.setFont(UIKit.FONT_NORMAL);
        lblDesc.setForeground(UIKit.MUTED);

        titleArea.add(lblTitle);
        titleArea.add(UIKit.gap(4));
        titleArea.add(lblDesc);

        headerPanel.add(titleArea, BorderLayout.WEST);
        northArea.add(headerPanel, BorderLayout.NORTH);
        northArea.add(buildKartu(), BorderLayout.SOUTH);
        
        add(northArea, BorderLayout.NORTH);
        add(buildTengah(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    private JPanel buildKartu() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        p.setBackground(UIKit.SURFACE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER));

        lblCustCount = new JLabel("0");
        lblSewaCount = new JLabel("0");
        lblJasaCount = new JLabel("0");

        p.add(infoTag("Total Pelanggan", lblCustCount));
        p.add(infoTag("Sewa Alat", lblSewaCount));
        p.add(infoTag("Jasa Foto", lblJasaCount));
        return p;
    }

    private JPanel infoTag(String title, JLabel valLabel) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        JLabel lTitle = new JLabel(title + ": ");
        lTitle.setForeground(UIKit.MUTED);
        lTitle.setFont(UIKit.FONT_SMALL);
        valLabel.setForeground(UIKit.TEXT);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lTitle, BorderLayout.WEST);
        p.add(valLabel, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTengah() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIKit.BG);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"No", "Customer", "Telepon", "Layanan", "Detail", "Total Bayar", "Tgl Input"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(model);
        tabel.setRowHeight(28);
        tabel.getTableHeader().setBackground(UIKit.CARD);
        tabel.getTableHeader().setForeground(Color.BLACK);
        tabel.setBackground(UIKit.SURFACE);
        tabel.setForeground(UIKit.TEXT);

        tabel.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.getViewport().setBackground(UIKit.BG);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        p.add(scroll, BorderLayout.CENTER);
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

    // ── INI KODE YANG MEMPERBAIKI ERROR ───────────────────────
    public void refresh() {
        model.setRowCount(0);
        int no = 1;
        int sewaC = 0, jasaC = 0;

        for (Transaksi r : DataStore.daftarTransaksi) {
            String jenisLayanan = "";
            
            if (r.getLayanan() instanceof SewaAlat) {
                jenisLayanan = "Sewa Alat";
                sewaC++;
            } else if (r.getLayanan() instanceof JasaFoto) {
                jenisLayanan = "Jasa Foto";
                jasaC++;
            }

            model.addRow(new Object[]{ 
                no++, 
                r.getCustomer().getNamaLengkap(),
                r.getCustomer().getNoTelepon(),
                jenisLayanan, 
                r.getLayanan().getDeskripsi(), 
                "Rp " + String.format("%,.0f", r.getTotalBiaya()), 
                r.getTglInput() 
            });
        }
        
        lblCustCount.setText(String.valueOf(no - 1));
        lblSewaCount.setText(String.valueOf(sewaC));
        lblJasaCount.setText(String.valueOf(jasaC));
    }
}