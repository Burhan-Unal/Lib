package lib;

import lib.panels.*; 
import lib.time.*;   
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LibDashboard extends JFrame implements ReservationObserver {

    private static final long serialVersionUID = 1L;
    private JPanel DashboardCard;
    
    private RoomsPanel roomsPanel; 
    private TimeManager timeManager; 

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.noAuditoryCues"));
                
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LibDashboard(boolean isAdmin) {
        setTitle("Lib Kütüphane Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1297, 787);
        
        
        this.timeManager = new TimeManager(this);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
       
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

        
        DashboardCard = new JPanel(new CardLayout());
        contentPane.add(DashboardCard, BorderLayout.CENTER);
        
        
        BooksPanel booksPanel = new BooksPanel(isAdmin);
        this.roomsPanel = new RoomsPanel(isAdmin); // DEĞİŞİKLİK: Global değişkene atandı
        ReadlistPanel readlistPanel = new ReadlistPanel();
        ProfilePanel profilePanel = new ProfilePanel(isAdmin, this); 

        
        DashboardCard.add(booksPanel, "KITAPLAR");
        DashboardCard.add(roomsPanel, "ODALAR");
        DashboardCard.add(readlistPanel, "GECMIS");
        DashboardCard.add(profilePanel, "PROFIL");

        if (isAdmin) {
            
            AdminPanel adminPanel = new AdminPanel(booksPanel.getBooksTable(), roomsPanel.getRoomsTable());
            DashboardCard.add(adminPanel, "ADMIN");
        }

        
        timeManager.startScheduler(roomsPanel.getRoomsTable());


        if (isAdmin && btnAdmin != null) {
            btnAdmin.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "ADMIN"));
        }
        
        btnKitaplar.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "KITAPLAR"));
        btnOdalar.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "ODALAR"));
        btnGecmis.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "GECMIS"));
        btnProfil.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "PROFIL"));
    }

    
    @Override
    public void onReservationTimeout(String roomName, int rowIndex) {
        SwingUtilities.invokeLater(() -> {
            String alertMessage = roomName + " için 30 dakikalık onay süresi doldu!\n" +
                                "Rezervasyon otomatik olarak iptal edildi ve güven puanınız düşürüldü.";
            JOptionPane.showMessageDialog(this, alertMessage, "Sistem Uyarısı: Kural İhlali", JOptionPane.WARNING_MESSAGE);
            
            // Tablodaki veriyi anlık olarak "Müsait" durumuna çek
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) roomsPanel.getRoomsTable().getModel();
            model.setValueAt("Müsait", rowIndex, 2);
            model.setValueAt("-", rowIndex, 3);
        });
    }
}