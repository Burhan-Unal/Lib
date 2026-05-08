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
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	boolean isAdmin;//User classde tanımlanana kadar placeholder
	private JPasswordField passwordField;
	private void openDashboard(boolean isAdmin) {
		LibDashboard dashboard = new LibDashboard(isAdmin);
		dashboard.setVisible(true);
		this.dispose();
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
		CardPanel.add(RegisterPanel,"REGISTER");
		
		JPanel LoginPanel = new JPanel();
		CardPanel.add(LoginPanel,"LOGIN");
		LoginPanel.setLayout(null);
		
		JButton btnToRegister = new JButton("Kayıt Ol");
		btnToRegister.setBounds(417, 445, 157, 52);
		LoginPanel.add(btnToRegister);
		
		JButton Login = new JButton("Giriş Yap");
		Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isAdmin=true;//User classde tanımlanana kadar placeholder
				openDashboard(isAdmin);
			}
		});
		Login.setBounds(417, 382, 157, 52);
		LoginPanel.add(Login);
		RegisterPanel.setLayout(null);
		
		JButton btnToLogin = new JButton("Giriş Yap");
		btnToLogin.setBounds(417, 445, 157, 52);
		RegisterPanel.add(btnToLogin);
		
		JButton Register = new JButton("Kayıt Ol");
		Register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isAdmin=false;//User classde tanımlanana kadar placeholder
				openDashboard(isAdmin);
			}
		});
		Register.setBounds(417, 382, 157, 52);
		RegisterPanel.add(Register);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(417, 339, 157, 32);
		RegisterPanel.add(passwordField);
		
		JLabel lblRegisterPass = new JLabel("Şifre");
		lblRegisterPass.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblRegisterPass.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRegisterPass.setBounds(306, 339, 101, 32);
		RegisterPanel.add(lblRegisterPass);
		

		btnToLogin.addActionListener((e -> UIHelper.switchPanel(CardPanel,"LOGIN")));
		btnToRegister.addActionListener((e -> UIHelper.switchPanel(CardPanel,"REGISTER")));
		

	}
}
