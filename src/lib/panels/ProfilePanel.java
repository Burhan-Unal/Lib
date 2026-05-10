package lib.panels;

import lib.LoginFrame;
import lib.UIHelper;
import lib.DatabaseManager;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfilePanel extends JPanel {
    
    private JProgressBar trustProgressBar;

    public ProfilePanel(boolean isAdmin, JFrame parentFrame) {
        setBorder(new EmptyBorder(50, 50, 50, 50));
        setLayout(new GridLayout(4, 1, 20, 20));

        // Karşılama
        String title = isAdmin ? "Hoş Geldiniz, Yönetici" : "Hoş Geldiniz, Öğrenci";
        add(UIHelper.createLabel(title, UIHelper.FONT_TITLE, SwingConstants.CENTER));

        if (!isAdmin) {
            // Güven Puanı Paneli
            JPanel panelTrustScore = new JPanel(new BorderLayout(0, 10));
            panelTrustScore.add(UIHelper.createLabel("Dinamik Güven Puanınız:", UIHelper.FONT_NORMAL, SwingConstants.CENTER), BorderLayout.NORTH);
            
            trustProgressBar = new JProgressBar(0, 100);
            trustProgressBar.setStringPainted(true);
            trustProgressBar.setFont(UIHelper.FONT_BOLD);
            
            trustProgressBar.setPreferredSize(new Dimension(400, 30));
            JPanel barWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            barWrapper.add(trustProgressBar);
            
            panelTrustScore.add(barWrapper, BorderLayout.CENTER);
            add(panelTrustScore);

            // Aktif İşlemler
            JPanel panelActiveInfo = new JPanel(new GridLayout(2, 1, 10, 10));
            panelActiveInfo.add(UIHelper.createLabel("Kitap Durumu: Aktif ödünç kaydı kontrol ediliyor...", UIHelper.FONT_NORMAL, SwingConstants.CENTER));
            panelActiveInfo.add(UIHelper.createLabel("Oda Rezervasyonu: Bilgi için geçmişe bakınız", UIHelper.FONT_NORMAL, SwingConstants.CENTER));
            add(panelActiveInfo);
            
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    puanGuncelle();
                }
            });
            puanGuncelle();
        }

        // Butonlar 
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        JButton btnLogout = UIHelper.createColoredButton("Çıkış Yap", null, Color.RED, UIHelper.FONT_NORMAL);
        
        panelButtons.add(btnLogout);
        add(panelButtons);

        // Olaylar
        btnLogout.addActionListener(e -> {
            DatabaseManager.aktifKullaniciId = -1; 
            new LoginFrame().setVisible(true);
            parentFrame.dispose(); 
        });
    }
    
    private void puanGuncelle() {
        int puan = DatabaseManager.guvenPuaniGetir(DatabaseManager.aktifKullaniciId);
        trustProgressBar.setValue(puan);
        trustProgressBar.setString(puan + " / 100");
        
        if (puan >= 80) {
            trustProgressBar.setForeground(new Color(50, 205, 50)); // Yeşil
        } else if (puan >= 50) {
            trustProgressBar.setForeground(new Color(255, 140, 0)); // Turuncu
        } else {
            trustProgressBar.setForeground(new Color(220, 53, 69)); // Kırmızı
        }
    }
}