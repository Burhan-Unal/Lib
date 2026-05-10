package lib.panels;

import lib.UIHelper;
import lib.DatabaseManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private JTable booksTable;
    private JTable roomsTable;
    private JTabbedPane adminTabs;
    private JButton btnExportCSV;

    public AdminPanel(JTable booksTable, JTable roomsTable) {
        this.booksTable = booksTable;
        this.roomsTable = roomsTable;

        setLayout(new BorderLayout(0, 0));
        setBackground(UIHelper.COLOR_BACKGROUND); // Ana arka planı modern renge sabitledik

        JLabel lblAdminTitle = UIHelper.createLabel("Yönetici Kontrol Paneli", UIHelper.FONT_TITLE, SwingConstants.CENTER);
        lblAdminTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblAdminTitle, BorderLayout.NORTH);

        adminTabs = new JTabbedPane(JTabbedPane.TOP);
        adminTabs.setFont(UIHelper.FONT_BOLD);
        adminTabs.setBackground(Color.WHITE);
        add(adminTabs, BorderLayout.CENTER);

        JPanel adminReportPanel = new JPanel();
        adminReportPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        adminReportPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        btnExportCSV = UIHelper.createColoredButton("Kitap Envanterini CSV Olarak İndir", UIHelper.COLOR_SUCCESS, Color.WHITE, UIHelper.FONT_BOLD);
        makeButtonFlat(btnExportCSV); // İşletim sistemi teması çakışmasını çözer
        
        adminReportPanel.add(btnExportCSV);
        add(adminReportPanel, BorderLayout.SOUTH);

        // --- ÇAĞRILAR BURADA ---
        createBookManagementTab();
        createRoomManagementTab();
        createUserManagementTab();
        setupTabChangeListener();
    }

 // 1. KİTAP YÖNETİMİ METODU
    private void createBookManagementTab() {
        JPanel tabBookMgmt = new JPanel(new BorderLayout());
        tabBookMgmt.setBackground(UIHelper.COLOR_BACKGROUND);
        
        // Boşlukları 15'e düşürüp, EmptyBorder'ı Oda Yönetimi ile aynı ferahlığa getirdik
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(new EmptyBorder(30, 100, 30, 100));
        formPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        JTextField txtAddName = createTextField();
        JTextField txtAddAuthor = createTextField();
        JTextField txtAddCategory = createTextField();
        
        JButton btnAddBook = UIHelper.createButton("Kataloğa Ekle");
        btnAddBook.setFont(UIHelper.FONT_BOLD);
        makeButtonFlat(btnAddBook);

        formPanel.add(UIHelper.createLabel("Kitap Adı:", UIHelper.FONT_NORMAL, SwingConstants.LEFT));      formPanel.add(txtAddName);
        formPanel.add(UIHelper.createLabel("Yazar:", UIHelper.FONT_NORMAL, SwingConstants.LEFT));          formPanel.add(txtAddAuthor);
        formPanel.add(UIHelper.createLabel("Kategori:", UIHelper.FONT_NORMAL, SwingConstants.LEFT));       formPanel.add(txtAddCategory);
        formPanel.add(new JLabel(""));                                                                     formPanel.add(btnAddBook);
        
        // CENTER yerine NORTH'a ekledik ki kendi doğal boyutunda kalsın, sıkışmasın
        tabBookMgmt.add(formPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Silme butonunun altına biraz boşluk ekledik
        
        JButton btnDeleteBook = UIHelper.createColoredButton("Tablodan Seçili Kitabı SİL", UIHelper.COLOR_DANGER, Color.WHITE, UIHelper.FONT_BOLD);
        makeButtonFlat(btnDeleteBook);
        
        bottomPanel.add(btnDeleteBook);
        tabBookMgmt.add(bottomPanel, BorderLayout.SOUTH);
        adminTabs.addTab("Kitap Yönetimi", tabBookMgmt);

        btnAddBook.addActionListener(e -> {
            if (txtAddName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen kitap adını doldurun!");
            } else {
                if (DatabaseManager.materyalEkle("Kitap", txtAddName.getText())) {
                    JOptionPane.showMessageDialog(this, "Kitap eklendi!");
                    txtAddName.setText(""); txtAddAuthor.setText(""); txtAddCategory.setText("");
                }
            }
        });

        btnDeleteBook.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = booksTable.convertRowIndexToModel(selectedRow);
                int bookId = Integer.parseInt(booksTable.getModel().getValueAt(modelRow, 0).toString());
                if (DatabaseManager.materyalSil(bookId)) {
                    ((DefaultTableModel) booksTable.getModel()).removeRow(modelRow);
                    JOptionPane.showMessageDialog(this, "Kitap silindi.");
                }
            }
        });
    }

    // 2. ODA YÖNETİMİ METODU
    private void createRoomManagementTab() {
        JPanel tabRoomMgmt = new JPanel(new BorderLayout());
        tabRoomMgmt.setBackground(UIHelper.COLOR_BACKGROUND);
        
        JPanel roomForm = new JPanel(new GridLayout(3, 2, 10, 10));
        roomForm.setBorder(new EmptyBorder(30, 100, 30, 100));
        roomForm.setBackground(UIHelper.COLOR_BACKGROUND);

        JTextField txtRoomName = createTextField();
        JTextField txtCapacity = createTextField();
        
        JButton btnAddRoom = UIHelper.createButton("Oda Ekle");
        makeButtonFlat(btnAddRoom);
        JButton btnDeleteRoom = UIHelper.createButton("Seçili Odayı Sil");
        makeButtonFlat(btnDeleteRoom);

        roomForm.add(UIHelper.createLabel("Oda İsmi/No:", UIHelper.FONT_NORMAL, SwingConstants.LEFT));     roomForm.add(txtRoomName);
        roomForm.add(UIHelper.createLabel("Kapasite:", UIHelper.FONT_NORMAL, SwingConstants.LEFT));        roomForm.add(txtCapacity);
        roomForm.add(btnAddRoom);                                                                          roomForm.add(btnDeleteRoom);

        tabRoomMgmt.add(roomForm, BorderLayout.NORTH);
        adminTabs.addTab("Oda Yönetimi", tabRoomMgmt);

        btnAddRoom.addActionListener(e -> {
            if (DatabaseManager.materyalEkle("Oda", txtRoomName.getText())) {
                JOptionPane.showMessageDialog(this, "Oda eklendi.");
                txtRoomName.setText(""); txtCapacity.setText("");
            }
        });
        
        btnDeleteRoom.addActionListener(e -> {
            int selected = roomsTable.getSelectedRow();
            if(selected != -1) {
                int modelRow = roomsTable.convertRowIndexToModel(selected);
                int roomId = Integer.parseInt(roomsTable.getModel().getValueAt(modelRow, 0).toString());
                if (DatabaseManager.materyalSil(roomId)) {
                    ((DefaultTableModel)roomsTable.getModel()).removeRow(modelRow);
                    JOptionPane.showMessageDialog(this, "Oda silindi.");
                }
            }
        });
    }

    // 3. KULLANICI YÖNETİMİ METODU
    private void createUserManagementTab() {
        JPanel tabUserMgmt = new JPanel(new BorderLayout());
        tabUserMgmt.setBackground(UIHelper.COLOR_BACKGROUND);
        
        JPanel userActionPanel = new JPanel(new GridLayout(6, 1, 15, 15)); 
        userActionPanel.setBorder(new EmptyBorder(30, 200, 30, 200));
        userActionPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        JTextField txtTargetUserId = createTextField();
        
        JButton btnPunish = UIHelper.createColoredButton("Güven Puanını 10 Puan DÜŞÜR", UIHelper.COLOR_WARNING, Color.WHITE, UIHelper.FONT_BOLD);
        makeButtonFlat(btnPunish);
        
        JButton btnBan = UIHelper.createColoredButton("Sistemden KALICI OLARAK BANLA", UIHelper.COLOR_DANGER, Color.WHITE, UIHelper.FONT_BOLD);
        makeButtonFlat(btnBan);

        userActionPanel.add(UIHelper.createLabel("İşlem Yapılacak Kullanıcı ID:", UIHelper.FONT_NORMAL, SwingConstants.CENTER));
        userActionPanel.add(txtTargetUserId);
        userActionPanel.add(btnPunish);
        userActionPanel.add(btnBan);

        tabUserMgmt.add(userActionPanel, BorderLayout.CENTER);
        adminTabs.addTab("Kullanıcı Yönetimi", tabUserMgmt);

        btnPunish.addActionListener(e -> JOptionPane.showMessageDialog(this, txtTargetUserId.getText() + " ID'li kullanıcının puanı düşürüldü."));
    }

    // 4. TAB CHANGE LISTENER METODU
    private void setupTabChangeListener() {
        adminTabs.addChangeListener(e -> {
            int selectedIndex = adminTabs.getSelectedIndex();
            btnExportCSV.setVisible(selectedIndex == 0 || selectedIndex == 1);
            if (selectedIndex == 0) btnExportCSV.setText("Kitap Envanterini CSV Olarak İndir");
            else if (selectedIndex == 1) btnExportCSV.setText("Oda Envanterini CSV Olarak İndir");
        });

        btnExportCSV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "CSV Raporu Hazırlanıyor...");
            }
        });
    }

    // --- YARDIMCI METOTLAR (TASARIM ÇAKIŞMASINI ÇÖZENLER) ---
    
    // Metin kutularının (JTextField) düzgün ve modern görünmesi için
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(UIHelper.FONT_NORMAL);
        tf.setForeground(UIHelper.COLOR_TEXT);
        tf.setBackground(Color.WHITE);
        return tf;
    }

    // Native buton sorununu çözen sihirli metod
    private void makeButtonFlat(JButton btn) {
        btn.setOpaque(true); // İçini bizim verdiğimiz renkle tam doldur
        btn.setBorderPainted(false); // İşletim sisteminin native kenarlığını çizme
    }
}