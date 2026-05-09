package lib.panels;

import lib.LoginFrame;
import lib.UIHelper;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfilePanel extends JPanel {

    public ProfilePanel(boolean isAdmin, JFrame parentFrame) {
        setBorder(new EmptyBorder(50, 50, 50, 50));
        setLayout(new GridLayout(4, 1, 20, 20));

        // Karşılama
        String title = isAdmin ? "Hoş Geldiniz, Yönetici" : "Hoş Geldiniz, Öğrenci";
        add(UIHelper.createLabel(title, UIHelper.FONT_TITLE, SwingConstants.CENTER));

        if (!isAdmin) {
            // Güven Puanı
            JPanel panelTrustScore = new JPanel(new BorderLayout(0, 10));
            panelTrustScore.add(UIHelper.createLabel("Dinamik Güven Puanınız:", UIHelper.FONT_NORMAL, SwingConstants.CENTER), BorderLayout.NORTH);
            JProgressBar trustProgressBar = new JProgressBar(0, 100);
            trustProgressBar.setValue(85);
            trustProgressBar.setStringPainted(true);
            trustProgressBar.setString("85 / 100");
            trustProgressBar.setFont(UIHelper.FONT_BOLD);
            trustProgressBar.setForeground(new Color(50, 205, 50));
            panelTrustScore.add(trustProgressBar, BorderLayout.CENTER);
            add(panelTrustScore);

            // Aktif İşlemler
            JPanel panelActiveInfo = new JPanel(new GridLayout(2, 1, 10, 10));
            panelActiveInfo.add(UIHelper.createLabel("Kitap Durumu: Data Structures (İadeye 5 Gün Kaldı)", UIHelper.FONT_NORMAL, SwingConstants.CENTER));
            panelActiveInfo.add(UIHelper.createLabel("Oda Rezervasyonu: Yok", UIHelper.FONT_NORMAL, SwingConstants.CENTER));
            add(panelActiveInfo);
        }

        // Butonlar
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        JButton btnChangePassword = UIHelper.createButton("Şifre Değiştir");
        JButton btnLogout = UIHelper.createColoredButton("Çıkış Yap", null, Color.RED, UIHelper.FONT_NORMAL);
        
        panelButtons.add(btnChangePassword);
        panelButtons.add(btnLogout);
        add(panelButtons);

        // Olaylar
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            parentFrame.dispose(); // Ana Dashboard'u kapatır
        });

        btnChangePassword.addActionListener(e -> JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı bekleniyor."));
    }
}