package lib;

import lib.panels.*; // Yeni oluşturduğumuz modülleri içe aktarıyoruz
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LibDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel DashboardCard;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.noAuditoryCues"));
                LibDashboard frame = new LibDashboard(true); // Geliştirme aşamasında admin olarak başlatıyoruz
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LibDashboard(boolean isAdmin) {
        setTitle("Lib Kütüphane Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1297, 787);
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        // --- SOL MENÜ KURULUMU ---
        JPanel sideMenu = new JPanel(new GridLayout(isAdmin ? 5 : 4, 0, 0, 0));
        contentPane.add(sideMenu, BorderLayout.WEST);
        
        JButton btnAdmin = null;
        if (isAdmin) {
            btnAdmin = UIHelper.createColoredButton("Yönetici Paneli", null, Color.BLUE, UIHelper.FONT_NORMAL);
            sideMenu.add(btnAdmin);
        }
        
        JButton btnKitaplar = UIHelper.createButton("Kitaplar");
        JButton btnOdalar = UIHelper.createButton("Odalar");
        JButton btnGecmis = UIHelper.createButton("Okuma Geçmişim");
        JButton btnProfil = UIHelper.createButton("Profilim");
        
        sideMenu.add(btnKitaplar);
        sideMenu.add(btnOdalar);
        sideMenu.add(btnGecmis);
        sideMenu.add(btnProfil);

        // --- MERKEZ KART (PANEL) YÖNETİCİSİ ---
        DashboardCard = new JPanel(new CardLayout());
        contentPane.add(DashboardCard, BorderLayout.CENTER);
        
        // 1. Modüler Panelleri Örnekliyoruz
        BooksPanel booksPanel = new BooksPanel(isAdmin);
        RoomsPanel roomsPanel = new RoomsPanel(isAdmin);
        ReadlistPanel readlistPanel = new ReadlistPanel();
        ProfilePanel profilePanel = new ProfilePanel(isAdmin, this); 

        // 2. Panelleri CardLayout'a Ekliyoruz
        DashboardCard.add(booksPanel, "KITAPLAR");
        DashboardCard.add(roomsPanel, "ODALAR");
        DashboardCard.add(readlistPanel, "GECMIS");
        DashboardCard.add(profilePanel, "PROFIL");

        if (isAdmin) {
            // Admin paneli diğer tabloların verilerine ihtiyaç duyar
            AdminPanel adminPanel = new AdminPanel(booksPanel.getBooksTable(), roomsPanel.getRoomsTable());
            DashboardCard.add(adminPanel, "ADMIN");
        }

        // --- BUTON TIKLAMA OLAYLARI (ROUTING) ---
        // Bütün menü butonlarının yönlendirme işlemleri burada tanımlandı
        
        if (isAdmin && btnAdmin != null) {
            btnAdmin.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "ADMIN"));
        }
        
        btnKitaplar.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "KITAPLAR"));
        btnOdalar.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "ODALAR"));
        btnGecmis.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "GECMIS"));
        btnProfil.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "PROFIL"));
    }
}