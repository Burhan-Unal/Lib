package lib;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultEditorKit;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class LibDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTable table_books;
	private JTable table_rooms;
	private JTable table_readlist;
	private JPanel DashboardCard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Java Swing'in arkaplanda ürettiği tüm uyarı seslerini (Beep) global olarak susturur
	                UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.noAuditoryCues"));
	                
	                LibDashboard frame = new LibDashboard(true);
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
	public LibDashboard(boolean isAdmin) {
		setTitle("Lib Kütüphane Yönetim Sistemi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1297, 787);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new GridLayout(isAdmin ? 5 : 4, 0, 0, 0));
		
		// Sadece yöneticiyse bu butonu oluştur ve menüye ekle
		if (isAdmin) {
		    JButton btnAdminPaneli = new JButton("Yönetici Paneli");
		    btnAdminPaneli.setForeground(Color.BLUE); // Diğerlerinden ayırmak için mavi yapabilirsin
		    panel.add(btnAdminPaneli);
		    
		    // Tıklama olayını da sadece buradayken ekliyoruz
		    btnAdminPaneli.addActionListener(e -> UIHelper.switchPanel(DashboardCard, "ADMIN"));
		}
		
		JButton btnKitaplar = new JButton("Kitaplar");
		panel.add(btnKitaplar);
		
		JButton btnOdalar = new JButton("Odalar");
		panel.add(btnOdalar);
		
		JButton btnGecmis = new JButton("Okuma Geçmişim");
		panel.add(btnGecmis);
		
		JButton btnProfil = new JButton("Profilim");
		panel.add(btnProfil);
		
		DashboardCard = new JPanel();
		contentPane.add(DashboardCard, BorderLayout.CENTER);
		DashboardCard.setLayout(new CardLayout(0, 0));
		
		JPanel Books = new JPanel();
		DashboardCard.add(Books, "KITAPLAR");
		Books.setLayout(new BorderLayout(0, 0));
		
		JPanel SearchBar = new JPanel();
		Books.add(SearchBar, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Kitap Ara");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		SearchBar.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 24));
		SearchBar.add(textField);
		textField.setColumns(50);
		
		JPanel BorrowButtons = new JPanel();
		Books.add(BorrowButtons, BorderLayout.SOUTH);
		
		JButton btnNewButton_3 = new JButton("Ödünç Al");
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 24));
		BorrowButtons.add(btnNewButton_3);
		
		JButton btnNewButton_3_1 = new JButton("Kuyruğa Katıl");
		btnNewButton_3_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		BorrowButtons.add(btnNewButton_3_1);
		
		BorrowButtons.setVisible(!isAdmin);
		
		JScrollPane Booktable = new JScrollPane();
		Books.add(Booktable, BorderLayout.CENTER);
		
		table_books = new JTable();
		table_books.setFont(new Font("Tahoma", Font.PLAIN, 24));
		// 1. Dinamik Modelin Kurulması (Hücre koruması eklenerek)
		String[] bookColumns = {"ID", "Kitap Ad\u0131", "Yazar", "Kategori", "Durum"};
		DefaultTableModel bookModel_dynamic = new DefaultTableModel(null, bookColumns) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // Hücrelerin elle değiştirilmesini kilitliyoruz
		    }
		};

		// 2. Modeli Tabloya Atama
		table_books.setModel(bookModel_dynamic);

		// 3. Verileri Dinamik Olarak (Satır Satır) Ekleme
		bookModel_dynamic.addRow(new Object[]{"B001", "Clean Code", "Robert C. Martin", "Yazılım", "Müsait"});
		bookModel_dynamic.addRow(new Object[]{"B002", "Data Structures", "Alan Turing", "Mühendislik", "Ödünç Alındı"});
		bookModel_dynamic.addRow(new Object[]{"B003", "1984", "George Orwell", "Roman", "Müsait"});
		bookModel_dynamic.addRow(new Object[]{"B004", "Design Patterns", "GoF", "Yazılım", "Ödünç Alındı"});
		bookModel_dynamic.addRow(new Object[]{"B005", "Suç ve Ceza", "Dostoyevski", "Edebiyat", "Müsait"});
		table_books.setFillsViewportHeight(true);
		Booktable.setViewportView(table_books);
		
		JPanel Rooms = new JPanel();
		DashboardCard.add(Rooms,"ODALAR");
		Rooms.setLayout(new BorderLayout(0, 0));
		
		JPanel RoomButtons = new JPanel();
		Rooms.add(RoomButtons, BorderLayout.SOUTH);
		
		JButton btnNewButton_4 = new JButton("Rezerve Et");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 24));
		RoomButtons.add(btnNewButton_4);
		
		JButton btnNewButton_4_1 = new JButton("Odayı Onayla");
		btnNewButton_4_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		RoomButtons.add(btnNewButton_4_1);
		
		RoomButtons.setVisible(!isAdmin);
		
		JScrollPane roomScrollPane = new JScrollPane();
		Rooms.add(roomScrollPane, BorderLayout.CENTER);
		
		table_rooms = new JTable();
		String[] roomColumns = {"Oda Numaras\u0131", "Kapasite", "Durum", "Kalan Onay S\u00FCresi"};
		DefaultTableModel roomModel_dynamic = new DefaultTableModel(null, roomColumns) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};

		table_rooms.setModel(roomModel_dynamic);

		roomModel_dynamic.addRow(new Object[]{"Oda 101", "4 Kişi", "Müsait", "-"});
		roomModel_dynamic.addRow(new Object[]{"Oda 102", "2 Kişi", "Rezerve edildi", "15 Dk Kaldı"});
		roomModel_dynamic.addRow(new Object[]{"Oda 103", "6 Kişi", "Müsait", "-"});
		roomModel_dynamic.addRow(new Object[]{"Oda 104", "1 Kişi", "Rezerve edildi", "05 Dk Kaldı (Riskli)"});
		roomModel_dynamic.addRow(new Object[]{"Oda 105", "4 Kişi", "Dolu", "-"});
		table_rooms.setFont(new Font("Tahoma", Font.PLAIN, 24));
		table_rooms.setFillsViewportHeight(true);
		roomScrollPane.setViewportView(table_rooms);
		
		JPanel Readlist = new JPanel();
		DashboardCard.add(Readlist, "GECMIS");
		Readlist.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		Readlist.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("Geçmiş İşlemleriniz");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		Readlist.add(scrollPane, BorderLayout.CENTER);
		
		table_readlist = new JTable();
		String[] readlistColumns = {"\u0130\u015Flem ID", "Kitap Ad\u0131", "Al\u0131\u015F Tarihi", "\u0130ade Tarihi", "Durum"};
		DefaultTableModel readlistModel_dynamic = new DefaultTableModel(null, readlistColumns) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};

		table_readlist.setModel(readlistModel_dynamic);

		readlistModel_dynamic.addRow(new Object[]{"TRX-101", "Clean Code", "01.04.2026", "15.04.2026", "İade Edildi"});
		readlistModel_dynamic.addRow(new Object[]{"TRX-102", "Suç ve Ceza", "10.04.2026", "24.04.2026", "Süresi Geçti (Cezalı)"});
		readlistModel_dynamic.addRow(new Object[]{"TRX-103", "Data Structures", "05.05.2026", "-", "Okunuyor"});
		table_readlist.setFont(new Font("Tahoma", Font.PLAIN, 24));
		table_readlist.setFillsViewportHeight(true);
		scrollPane.setViewportView(table_readlist);
		
		table_books.setRowHeight(40);
		table_rooms.setRowHeight(40);
		table_readlist.setRowHeight(40);
		
		Font headerFont = new Font("Tahoma", Font.BOLD, 24);

		table_books.getTableHeader().setFont(headerFont);
		table_rooms.getTableHeader().setFont(headerFont);
		table_readlist.getTableHeader().setFont(headerFont);
		
		// --- PROFIL PANELİ TASARIMI ---
		JPanel Profile = new JPanel();
		Profile.setBorder(new EmptyBorder(50, 50, 50, 50)); // Kenarlardan ferah boşluklar bırakıyoruz
		DashboardCard.add(Profile, "PROFIL");
		Profile.setLayout(new GridLayout(4, 1, 20, 20)); // Alt alta 4 ana satır, aralarında 20px boşluk

		// 1. SATIR: Karşılama Mesajı
		JLabel lblWelcome = new JLabel("Hoş Geldiniz, Öğrenci");// İleride veritabanından isim gelecek}
		if(isAdmin) {lblWelcome = new JLabel("Hoş Geldiniz, Yönetici");}
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		Profile.add(lblWelcome);
        if(!isAdmin) {
		// 2. SATIR: Dinamik Güven Puanı (Ceza Sistemi Arayüzü)
        JPanel panelTrustScore = new JPanel();
		panelTrustScore.setLayout(new BorderLayout(0, 10));

		JLabel lblTrustScoreTitle = new JLabel("Dinamik Güven Puanınız:");
		lblTrustScoreTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTrustScoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelTrustScore.add(lblTrustScoreTitle, BorderLayout.NORTH);

		javax.swing.JProgressBar trustProgressBar = new javax.swing.JProgressBar(0, 100);
		trustProgressBar.setValue(85); // Örnek başlangıç puanı
		trustProgressBar.setStringPainted(true); // Çubuğun içinde metin yazmasını sağlar
		trustProgressBar.setString("85 / 100");
		trustProgressBar.setFont(new Font("Tahoma", Font.BOLD, 24));
		// Puan yüksekse yeşil, düşükse kırmızı olacak şekilde ileride güncelleyebilirsin:
		trustProgressBar.setForeground(new Color(50, 205, 50)); 
		panelTrustScore.add(trustProgressBar, BorderLayout.CENTER);

		Profile.add(panelTrustScore);

		// 3. SATIR: Aktif İşlemler Özeti
		JPanel panelActiveInfo = new JPanel();
		panelActiveInfo.setLayout(new GridLayout(2, 1, 10, 10));

		JLabel lblActiveBook = new JLabel("Kitap Durumu: Data Structures (İadeye 5 Gün Kaldı)");
		lblActiveBook.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblActiveBook.setHorizontalAlignment(SwingConstants.CENTER);
		panelActiveInfo.add(lblActiveBook);

		JLabel lblActiveRoom = new JLabel("Oda Rezervasyonu: Yok");
		lblActiveRoom.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblActiveRoom.setHorizontalAlignment(SwingConstants.CENTER);
		panelActiveInfo.add(lblActiveRoom);

		Profile.add(panelActiveInfo);}

		// 4. SATIR: İşlem Butonları (Şifre Değiştir / Çıkış)
		JPanel panelProfileButtons = new JPanel();
		// Butonları ortalamak için FlowLayout kullanıyoruz
		panelProfileButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 0));

		JButton btnChangePassword = new JButton("Şifre Değiştir");
		btnChangePassword.setFont(new Font("Tahoma", Font.PLAIN, 24));
		panelProfileButtons.add(btnChangePassword);

		JButton btnLogout = new JButton("Çıkış Yap");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnLogout.setForeground(Color.RED); // Çıkış butonunu vurgulamak için kırmızı yapıyoruz
		panelProfileButtons.add(btnLogout);

		Profile.add(panelProfileButtons);
		
		// --- YÖNETİCİ (ADMIN) PANELİ TASARIMI ---
		// --- ADMIN PANELİ GENEL YAPISI ---
		JPanel AdminPanel = new JPanel();
		DashboardCard.add(AdminPanel, "ADMIN");
		AdminPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane adminTabs = new JTabbedPane(JTabbedPane.TOP);
		adminTabs.setFont(new Font("Tahoma", Font.BOLD, 18));
		AdminPanel.add(adminTabs, BorderLayout.CENTER);

		// Üç Ana Sekme Oluşturuyoruz
		JPanel tabBookMgmt = new JPanel(new BorderLayout());
		JPanel tabRoomMgmt = new JPanel(new BorderLayout());
		JPanel tabUserMgmt = new JPanel(new BorderLayout());

		adminTabs.addTab("Kitap Yönetimi", tabBookMgmt);
		adminTabs.addTab("Oda Yönetimi", tabRoomMgmt);
		adminTabs.addTab("Kullanıcı Yönetimi", tabUserMgmt);

		JLabel lblAdminTitle = new JLabel("Yönetici Kontrol Paneli - Yeni Kitap Ekleme");
		lblAdminTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblAdminTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
		AdminPanel.add(lblAdminTitle, BorderLayout.NORTH);

		// Form Alanı (Kenarlardan boşluk bırakarak inputların aşırı büyümesini engelliyoruz)
				JPanel formPanel = new JPanel();
				formPanel.setLayout(new GridLayout(5, 2, 20, 20));
				formPanel.setBorder(new EmptyBorder(50, 200, 300, 200)); 

				// 1. Kitap ID
				formPanel.add(new JLabel("Kitap ID:"));
				JTextField txtAddId = new JTextField();
				formPanel.add(txtAddId);

				// 2. Kitap Adı
				formPanel.add(new JLabel("Kitap Adı:"));
				JTextField txtAddName = new JTextField();
				formPanel.add(txtAddName);

				// 3. Yazar
				formPanel.add(new JLabel("Yazar:"));
				JTextField txtAddAuthor = new JTextField();
				formPanel.add(txtAddAuthor);

				// 4. Kategori
				formPanel.add(new JLabel("Kategori:"));
				JTextField txtAddCategory = new JTextField();
				formPanel.add(txtAddCategory);

				// 5. Ekleme Butonu
				JButton btnAddBook = new JButton("Kataloğa Ekle");
				btnAddBook.setFont(new Font("Tahoma", Font.BOLD, 18));
				formPanel.add(new JLabel("")); // Düzeni korumak için sol tarafı boş bırakıyoruz
				formPanel.add(btnAddBook);

				// DOĞRUSU: Formu SADECE Kitap Yönetimi sekmesinin merkezine ekliyoruz!
				tabBookMgmt.add(formPanel, BorderLayout.CENTER);

		// --- KİTAP EKLEME BUTONU MANTIĞI ---
		btnAddBook.addActionListener(e -> {
		    // Inputlardan verileri alıyoruz
		    String newId = txtAddId.getText();
		    String newName = txtAddName.getText();
		    String newAuthor = txtAddAuthor.getText();
		    String newCategory = txtAddCategory.getText();
		    String defaultStatus = "Müsait"; // Yeni eklenen kitap her zaman müsaittir
		    
		    if (newId.isEmpty() || newName.isEmpty() || newAuthor.isEmpty() || newCategory.isEmpty()) {
		        javax.swing.JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", javax.swing.JOptionPane.ERROR_MESSAGE);
		    } else {
		        // Dinamik tablomuza (bookModel_dynamic) yeni satırı ekliyoruz
		        DefaultTableModel model = (DefaultTableModel) table_books.getModel();
		        model.addRow(new Object[]{newId, newName, newAuthor, newCategory, defaultStatus});
		        
		        javax.swing.JOptionPane.showMessageDialog(this, "Kitap başarıyla kütüphaneye eklendi!");
		        
		        // Inputları temizle
		        txtAddId.setText("");
		        txtAddName.setText("");
		        txtAddAuthor.setText("");
		        txtAddCategory.setText("");
		    }
		});
		
		// Kitap Ekleme Formunu buraya (NORTH) ekle (Önceki yazdığımız form kodları...)
		// Alt kısma ise Silme Butonu:
		JButton btnDeleteBook = new JButton("Tablodan Seçili Kitabı SİL");
		btnDeleteBook.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnDeleteBook.setBackground(new Color(255, 102, 102)); // Kırmızı tonu
		tabBookMgmt.add(btnDeleteBook, BorderLayout.SOUTH);

		btnDeleteBook.addActionListener(e -> {
		    int selectedRow = table_books.getSelectedRow();
		    if (selectedRow != -1) {
		        // Filtreleme yapıldıysa doğru satırı bulmak için:
		        int modelRow = table_books.convertRowIndexToModel(selectedRow);
		        DefaultTableModel model = (DefaultTableModel) table_books.getModel();
		        model.removeRow(modelRow);
		        javax.swing.JOptionPane.showMessageDialog(this, "Kitap başarıyla silindi.");
		    } else {
		        javax.swing.JOptionPane.showMessageDialog(this, "Lütfen silinecek kitabı tablodan seçin.");
		    }
		});
		
		JPanel roomForm = new JPanel(new GridLayout(3, 2, 10, 10));
		roomForm.setBorder(new EmptyBorder(30, 100, 30, 100));

		JTextField txtRoomId = new JTextField();
		JTextField txtCapacity = new JTextField();
		JButton btnAddRoom = new JButton("Oda Ekle");
		JButton btnDeleteRoom = new JButton("Seçili Odayı Sil");

		roomForm.add(new JLabel("Oda Numarası:"));
		roomForm.add(txtRoomId);
		roomForm.add(new JLabel("Kapasite (Kişi):"));
		roomForm.add(txtCapacity);
		roomForm.add(btnAddRoom);
		roomForm.add(btnDeleteRoom);

		tabRoomMgmt.add(roomForm, BorderLayout.NORTH);

		// Oda Ekleme Butonu
		btnAddRoom.addActionListener(e -> {
		    DefaultTableModel model = (DefaultTableModel) table_rooms.getModel();
		    model.addRow(new Object[]{txtRoomId.getText(), txtCapacity.getText() + " Kişi", "Müsait", "-"});
		    javax.swing.JOptionPane.showMessageDialog(this, "Oda eklendi.");
		});

		// Oda Silme Butonu
		btnDeleteRoom.addActionListener(e -> {
		    int selected = table_rooms.getSelectedRow();
		    if(selected != -1) {
		        ((DefaultTableModel)table_rooms.getModel()).removeRow(table_rooms.convertRowIndexToModel(selected));
		        javax.swing.JOptionPane.showMessageDialog(this, "Oda başarıyla silindi.");
		    }
		    else {
		        javax.swing.JOptionPane.showMessageDialog(this, "Lütfen silinecek odayı tablodan seçin.");
		    }
		});
		
		// --- YENİ KULLANICI YÖNETİMİ SEKMESİ (Ceza Sistemi) ---
		// 4 satır yerine 6 satırlık bir ızgara oluşturuyoruz
		JPanel userActionPanel = new JPanel(new GridLayout(6, 1, 15, 15)); 
		userActionPanel.setBorder(new EmptyBorder(30, 200, 30, 200));

		JTextField txtTargetUserId = new JTextField();
		txtTargetUserId.setFont(new Font("Tahoma", Font.PLAIN, 24));

		// 1. Puan Düşürme Butonu
		JButton btnPunish = new JButton("Güven Puanını 10 Puan DÜŞÜR");
		btnPunish.setBackground(new Color(255, 153, 51)); // Turuncu
		btnPunish.setFont(new Font("Tahoma", Font.BOLD, 16));

		// 2. Askıya Alma (Suspend) Butonu
		JButton btnSuspend = new JButton("Hesabı ASKIYA AL (Geçici Uzaklaştırma)");
		btnSuspend.setBackground(new Color(255, 204, 0)); // Sarı
		btnSuspend.setFont(new Font("Tahoma", Font.BOLD, 16));

		// 3. Kalıcı Ban (Ban) Butonu
		JButton btnBan = new JButton("Sistemden KALICI OLARAK BANLA");
		btnBan.setBackground(new Color(220, 53, 69)); // Kırmızı
		btnBan.setForeground(Color.WHITE); // Yazısı beyaz olsun
		btnBan.setFont(new Font("Tahoma", Font.BOLD, 16));

		userActionPanel.add(new JLabel("İşlem Yapılacak Kullanıcı ID:", SwingConstants.CENTER));
		userActionPanel.add(txtTargetUserId);
		userActionPanel.add(btnPunish);
		userActionPanel.add(btnSuspend);
		userActionPanel.add(btnBan);

		tabUserMgmt.add(userActionPanel, BorderLayout.CENTER);

		// --- CEZA BUTONLARININ OLAYLARI (EVENTS) ---

		// Puan Düşürme Mantığı
		btnPunish.addActionListener(e -> {
		    String userId = txtTargetUserId.getText();
		    if(!userId.isEmpty()) {
		        javax.swing.JOptionPane.showMessageDialog(this, userId + " ID'li kullanıcının güven puanı 10 düşürüldü.");
		        txtTargetUserId.setText("");
		    }
		});

		// Suspend Mantığı
		btnSuspend.addActionListener(e -> {
		    String userId = txtTargetUserId.getText();
		    if(!userId.isEmpty()) {
		        // İleride burası: DB.exec("UPDATE Users SET status = 'SUSPENDED' WHERE id = " + userId)
		        javax.swing.JOptionPane.showMessageDialog(this, 
		            userId + " ID'li kullanıcının kütüphane hizmetleri geçici olarak ASKIYA ALINDI!", 
		            "Askıya Alındı", 
		            javax.swing.JOptionPane.WARNING_MESSAGE);
		        txtTargetUserId.setText("");
		    }
		});

		// Ban Mantığı (Yanlış tıklamalara karşı onay mekanizmalı)
		btnBan.addActionListener(e -> {
		    String userId = txtTargetUserId.getText();
		    if(!userId.isEmpty()) {
		        // Banlama ciddi bir işlem olduğu için yöneticiye "Emin misin?" diye soruyoruz
		        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
		            userId + " ID'li kullanıcıyı sistemden KALICI OLARAK BANLAMAK istediğinize emin misiniz?", 
		            "Kalıcı Ban Onayı", 
		            javax.swing.JOptionPane.YES_NO_OPTION, 
		            javax.swing.JOptionPane.ERROR_MESSAGE);
		            
		        if(confirm == javax.swing.JOptionPane.YES_OPTION) {
		             // İleride burası: DB.exec("UPDATE Users SET status = 'BANNED' WHERE id = " + userId)
		             javax.swing.JOptionPane.showMessageDialog(this, 
		                 userId + " ID'li kullanıcı sistemden kalıcı olarak BANLANDI.", 
		                 "İşlem Tamamlandı", 
		                 javax.swing.JOptionPane.INFORMATION_MESSAGE);
		             txtTargetUserId.setText("");
		        }
		    }
		});
		
		// --- AKILLI CSV RAPORLAMA BÖLÜMÜ (YÖNETİCİYE ÖZEL) ---
				JPanel adminReportPanel = new JPanel();
				adminReportPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
				JButton btnExportCSV = new JButton("Kitap Envanterini CSV Olarak İndir");
				btnExportCSV.setFont(new Font("Tahoma", Font.BOLD, 16));
				btnExportCSV.setForeground(new Color(0, 100, 0)); 
				adminReportPanel.add(btnExportCSV);

				AdminPanel.add(adminReportPanel, BorderLayout.SOUTH);

				// 1. Sekme (Tab) değiştikçe butonun metnini ve durumunu güncelle
				adminTabs.addChangeListener(e -> {
				    int selectedIndex = adminTabs.getSelectedIndex();
				    if (selectedIndex == 0) {
				        btnExportCSV.setText("Kitap Envanterini CSV Olarak İndir");
				        btnExportCSV.setVisible(true); // Göster
				    } else if (selectedIndex == 1) {
				        btnExportCSV.setText("Oda Envanterini CSV Olarak İndir");
				        btnExportCSV.setVisible(true); // Göster
				    } else {
				        // Kullanıcı Yönetimi sekmesinde indirilecek tablo olmadığı için butonu gizle
				        btnExportCSV.setVisible(false); 
				    }
				});

				// ... önceki buton kodları ...

				btnExportCSV.addActionListener(e -> {
				    // 1. Kullanıcıdan dosyayı kaydedeceği konumu al (Bu işlem UI'a aittir, burada kalmalı)
				    JFileChooser fileChooser = new JFileChooser();
				    fileChooser.setDialogTitle("Raporu Kaydet");
				    
				    int userSelection = fileChooser.showSaveDialog(this);
				    if (userSelection == JFileChooser.APPROVE_OPTION) {
				        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				        if (!filePath.toLowerCase().endsWith(".csv")) {
				            filePath += ".csv";
				        }
				        
				        try {
				            // 2. Hangi sekmedeysek o tabloyu seç
				            int selectedIndex = adminTabs.getSelectedIndex();
				            JTable tableToExport = (selectedIndex == 0) ? table_books : table_rooms;
				            
				            // 3. İŞİ UZMANA DEVRET! (Ayrı sınıftaki metodumuzu çağırıyoruz)
				            AdminReportManager.exportTableToCSV(tableToExport, filePath);
				            
				            // Başarı mesajını göster
				            javax.swing.JOptionPane.showMessageDialog(this, "Rapor başarıyla oluşturuldu:\n" + filePath);
				            
				        } catch (Exception ex) {
				            javax.swing.JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Kayıt Hatası", javax.swing.JOptionPane.ERROR_MESSAGE);
				        }
				    }
				});
		
		btnKitaplar.addActionListener(e -> UIHelper.switchPanel(DashboardCard,"KITAPLAR"));
		btnOdalar.addActionListener(e -> UIHelper.switchPanel(DashboardCard,"ODALAR"));
		btnGecmis.addActionListener(e -> UIHelper.switchPanel(DashboardCard,"GECMIS"));
		btnProfil.addActionListener(e -> UIHelper.switchPanel(DashboardCard,"PROFIL"));
		
		// --- ÇIKIŞ YAP VE ŞİFRE DEĞİŞTİR BUTON OLAYLARI ---

		btnLogout.addActionListener(e -> {
		    // 1. Yeni bir giriş ekranı (LoginFrame) oluştur ve kullanıcıya göster
		    LoginFrame loginScreen = new LoginFrame();
		    loginScreen.setVisible(true);
		    
		    // 2. Mevcut Dashboard penceresini işletim sisteminin hafızasından tamamen sil
		    LibDashboard.this.dispose(); 
		});

		// Şifre Değiştir butonu için şimdilik geçici bir mesaj bırakalım 
		// (İleride SQLite UPDATE sorgusu ile burayı dolduracağız)
		btnChangePassword.addActionListener(e -> {
		    javax.swing.JOptionPane.showMessageDialog(this, 
		        "Şifre değiştirme ekranı veritabanı bağlantısından sonra aktif edilecektir.", 
		        "Bilgi", 
		        javax.swing.JOptionPane.INFORMATION_MESSAGE);
		});
		
		// --- PLACEHOLDER KODLARI ---
		textField.setForeground(Color.GRAY);
		textField.setText("Kitap adı veya ID giriniz...");

		textField.addFocusListener(new FocusAdapter() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        // Kullanıcı kutuya tıkladığında, eğer içinde placeholder varsa temizle
		        if (textField.getText().equals("Kitap adı veya ID giriniz...")) {
		            textField.setText("");
		            textField.setForeground(Color.BLACK);
		        }
		    }

		    @Override
		    public void focusLost(FocusEvent e) {
		        // Kullanıcı başka yere tıkladığında kutu boşsa placeholder'ı geri getir
		        if (textField.getText().isEmpty()) {
		            textField.setForeground(Color.GRAY);
		            textField.setText("Kitap adı veya ID giriniz...");
		        }
		    }
		});
		
		// --- ROW SORTER (DİNAMİK ARAMA) KODLARI ---
		// 1. Tablonun modelini alıp bir Sorter (Sıralayıcı/Filtreleyici) oluşturuyoruz
		DefaultTableModel bookModel = (DefaultTableModel) table_books.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(bookModel);
		table_books.setRowSorter(sorter);

		// 2. Arama çubuğundaki (textField) metin değişikliklerini anlık dinliyoruz
		textField.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        filterTable();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        filterTable();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        filterTable();
		    }

		    private void filterTable() {
		        // Filtreleme işlemini arayüz kuyruğunun (Event Queue) sonuna güvenle ekliyoruz
		        javax.swing.SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                String searchText = textField.getText();
		                
		                // Eğer arama kutusu boşsa veya içinde placeholder metni varsa filtreyi kaldır
		                if (searchText.trim().length() == 0 || searchText.equals("Kitap adı veya ID giriniz...")) {
		                    sorter.setRowFilter(null);
		                } else {
		                    try {
		                        // Pattern.quote ile metni güvenli hale getiriyoruz
		                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(searchText)));
		                    } catch (Exception ex) {
		                        // Olası her türlü hatayı sessizce yut
		                    }
		                }
		            }
		        });
		    }
		});

	
	// --- BACKSPACE (SİLME) SESİNİ ENGELLEME KODU ---
	textField.addKeyListener(new java.awt.event.KeyAdapter() {
	    public void keyPressed(java.awt.event.KeyEvent e) {
	        // Eğer basılan tuş Backspace (Silme) ise
	        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE) {
	            
	            // Eğer imleç en baştaysa (0. konum) ve seçili bir metin yoksa (yani silinecek hiçbir şey kalmamışsa)
	            if (textField.getCaretPosition() == 0 && textField.getSelectedText() == null) {
	                
	                e.consume(); // Olayı tamamen yut! İşletim sistemine bu tuşa basıldığını hiç haber verme.
	            }
	        }
	    }
	});
}

}
