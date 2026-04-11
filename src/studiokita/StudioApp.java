package studiokita;

import studiokita.gui.*;
import javax.swing.*;
import java.awt.*;

/**
 * ============================================================
 * CLASS UTAMA — Entry Point & Navigator Aplikasi Studio Kita
 * ============================================================
 */
public class StudioApp implements Navigator {

    private final JFrame       frame;
    private final CardLayout   cardLayout;
    private final JPanel       cards;

    // ── Panel-panel (Disimpan di variabel agar bisa di-refresh) ──
    private final LoginPanel       login;
    private final DashboardPanel   dashboard;
    private final InputMenuPanel   inputMenu;
    private final SewaPanel        sewa;
    private final JasaPanel        jasa;
    private final RekapPanel       rekap;
    private final PenghasilanPanel penghasilan;

    // ── Constructor ───────────────────────────────────────────
    public StudioApp() {
        // (Baris DataStore.inisialisasiDataSample() sudah Dihapus secara permanen)

        cardLayout = new CardLayout();
        cards      = new JPanel(cardLayout);

        // 1. Inisialisasi semua panel
        login       = new LoginPanel(this);
        dashboard   = new DashboardPanel(this);
        inputMenu   = new InputMenuPanel(this);
        sewa        = new SewaPanel(this);
        jasa        = new JasaPanel(this);
        rekap       = new RekapPanel(this);
        penghasilan = new PenghasilanPanel(this);

        // 2. Masukkan ke Tumpukan Kartu (CardLayout)
        cards.add(login,       "login");
        cards.add(dashboard,   "dashboard");
        cards.add(inputMenu,   "inputmenu");
        cards.add(sewa,        "sewa");
        cards.add(jasa,        "jasa");
        cards.add(rekap,       "rekap");
        cards.add(penghasilan, "penghasilan");

        // 3. Setup Jendela Utama
        frame = new JFrame("Aplikasi Studio Kita");
        frame.setContentPane(cards);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Tampilkan halaman pertama
        cardLayout.show(cards, "login");
    }

    // ── Fungsi Navigasi ──────────────────────────────────────
    @Override
    public void goTo(String screen) {
        // 1. Refresh data panel yang dituju
        switch (screen) {
            case "rekap"       -> rekap.refresh();
            case "penghasilan" -> penghasilan.refresh();
            case "sewa"        -> sewa.refresh();
            case "jasa"        -> jasa.refresh();
            case "dashboard"   -> dashboard.refresh();
        }

        // 2. Atur ukuran Window (Full Screen / Normal)
        if (!screen.equals("login")) {
            // Jika BUKAN layar login (berarti sudah masuk aplikasi)
            frame.setResizable(true); // Izinkan window di-resize
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Buat Full Screen (Maximize)
        } else {
            // Jika KEMBALI ke layar login (Logout)
            frame.setExtendedState(JFrame.NORMAL); // Kembalikan dari Maximize
            frame.setSize(960, 640); // Kembalikan ke ukuran semula
            frame.setLocationRelativeTo(null); // Posisikan kembali ke tengah layar
            frame.setResizable(false); // Kunci agar tidak bisa di-resize
        }

        // 3. Tampilkan layarnya
        cardLayout.show(cards, screen);
    }

    // ── Method Main ──────────────────────────────────────────
    public static void main(String[] args) {
        // Tema Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Jalankan
        SwingUtilities.invokeLater(() -> {
            new StudioApp();
        });
    }
}