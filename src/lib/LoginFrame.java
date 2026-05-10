package lib;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    private JTextField txtLoginOgrenciNo;
    private JTextField txtRegAdSoyad;
    private JTextField txtRegOgrenciNo;

    private void openDashboard(boolean isAdmin, int kullaniciId) {
        LibDashboard dashboard = new LibDashboard(isAdmin);        
        dashboard.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseManager.initializeDatabase();
                    
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginFrame() {
        setTitle("Lib Kütüphane Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1031, 702);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JPanel CardPanel = new JPanel();
        contentPane.add(CardPanel);
        CardPanel.setLayout(new CardLayout(0, 0));
        
        JPanel RegisterPanel = new JPanel();
        CardPanel.add(RegisterPanel, "REGISTER");
        RegisterPanel.setLayout(null);
        
        JLabel lblAdSoyad = new JLabel("Ad Soyad:");
        lblAdSoyad.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblAdSoyad.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAdSoyad.setBounds(250, 280, 150, 32);
        RegisterPanel.add(lblAdSoyad);
        
        txtRegAdSoyad = new JTextField();
        txtRegAdSoyad.setBounds(417, 280, 157, 32);
        RegisterPanel.add(txtRegAdSoyad);
        
        JLabel lblRegOgrenciNo = new JLabel("Öğrenci No:");
        lblRegOgrenciNo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblRegOgrenciNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRegOgrenciNo.setBounds(250, 339, 150, 32);
        RegisterPanel.add(lblRegOgrenciNo);
        
        txtRegOgrenciNo = new JTextField();
        txtRegOgrenciNo.setBounds(417, 339, 157, 32);
        RegisterPanel.add(txtRegOgrenciNo);
        
        JButton btnToLogin = new JButton("Zaten Hesabım Var");
        btnToLogin.setBounds(417, 445, 157, 52);
        RegisterPanel.add(btnToLogin);
        
        JButton Register = new JButton("Kayıt Ol");
        Register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ad = txtRegAdSoyad.getText();
                String no = txtRegOgrenciNo.getText();
                
                if (ad.isEmpty() || no.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun!");
                    return;
                }
                
                boolean basarili = DatabaseManager.kullaniciKaydet(ad, no);
                
                if (basarili) {
                    JOptionPane.showMessageDialog(null, "Kayıt Jilet Gibi Başarılı! Giriş yapabilirsiniz.");
                    txtRegAdSoyad.setText("");
                    txtRegOgrenciNo.setText("");
                    UIHelper.switchPanel(CardPanel, "LOGIN");
                } else {
                    JOptionPane.showMessageDialog(null, "Kayıt başarısız! Bu öğrenci numarası zaten sistemde olabilir.");
                }
            }
        });
        Register.setBounds(417, 382, 157, 52);
        RegisterPanel.add(Register);
        
        
        JPanel LoginPanel = new JPanel();
        CardPanel.add(LoginPanel, "LOGIN");
        LoginPanel.setLayout(null);
        
        JLabel lblLoginOgrenciNo = new JLabel("Öğrenci No:");
        lblLoginOgrenciNo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblLoginOgrenciNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLoginOgrenciNo.setBounds(250, 310, 150, 32);
        LoginPanel.add(lblLoginOgrenciNo);
        
        txtLoginOgrenciNo = new JTextField();
        txtLoginOgrenciNo.setBounds(417, 310, 157, 32);
        LoginPanel.add(txtLoginOgrenciNo);
        
        JButton btnToRegister = new JButton("Yeni Kayıt");
        btnToRegister.setBounds(417, 445, 157, 52);
        LoginPanel.add(btnToRegister);

        JButton Login = new JButton("Giriş Yap");
        Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String no = txtLoginOgrenciNo.getText();
                
                if(no.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Öğrenci numarası boş bırakılamaz!");
                    return;
                }
                
                if (no.equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Admin yetkisiyle giriş yapıldı.");
                    openDashboard(true, 0);
                    return;
                }
                
                int id = DatabaseManager.kullaniciGirisYap(no);
                
                if (id != -1) {
                    JOptionPane.showMessageDialog(null, "Giriş Başarılı! Hoş Geldin.");
                    openDashboard(false, id);
                } else {
                    JOptionPane.showMessageDialog(null, "Böyle bir öğrenci numarası bulunamadı. Önce kayıt olun!");
                }
            }
        });
        Login.setBounds(417, 382, 157, 52);
        LoginPanel.add(Login);

        btnToLogin.addActionListener((e -> UIHelper.switchPanel(CardPanel,"LOGIN")));
        btnToRegister.addActionListener((e -> UIHelper.switchPanel(CardPanel,"REGISTER")));
    }
}