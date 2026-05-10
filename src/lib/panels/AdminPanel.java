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

        JLabel lblAdminTitle = UIHelper.createLabel("Yönetici Kontrol Paneli", UIHelper.FONT_BOLD, SwingConstants.CENTER);
        lblAdminTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblAdminTitle, BorderLayout.NORTH);

        adminTabs = new JTabbedPane(JTabbedPane.TOP);
        adminTabs.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(adminTabs, BorderLayout.CENTER);

        JPanel adminReportPanel = new JPanel();
        adminReportPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        btnExportCSV = UIHelper.createColoredButton("Kitap Envanterini CSV Olarak İndir", null, new Color(0, 100, 0), new Font("Tahoma", Font.BOLD, 16));
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
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 20, 20));
        formPanel.setBorder(new EmptyBorder(50, 200, 300, 200));

        JTextField txtAddName = new JTextField();
        JTextField txtAddAuthor = new JTextField();
        JTextField txtAddCategory = new JTextField();
        JButton btnAddBook = UIHelper.createButton("Kataloğa Ekle");
        btnAddBook.setFont(new Font("Tahoma", Font.BOLD, 18));

        formPanel.add(new JLabel("Kitap Adı:"));      formPanel.add(txtAddName);
        formPanel.add(new JLabel("Yazar:"));          formPanel.add(txtAddAuthor);
        formPanel.add(new JLabel("Kategori:"));       formPanel.add(txtAddCategory);
        formPanel.add(new JLabel(""));                formPanel.add(btnAddBook);
        tabBookMgmt.add(formPanel, BorderLayout.CENTER);

        JButton btnDeleteBook = UIHelper.createColoredButton("Tablodan Seçili Kitabı SİL", new Color(255, 102, 102), null, new Font("Tahoma", Font.BOLD, 18));
        tabBookMgmt.add(btnDeleteBook, BorderLayout.SOUTH);
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
        JPanel roomForm = new JPanel(new GridLayout(3, 2, 10, 10));
        roomForm.setBorder(new EmptyBorder(30, 100, 30, 100));

        JTextField txtRoomName = new JTextField();
        JTextField txtCapacity = new JTextField();
        JButton btnAddRoom = UIHelper.createButton("Oda Ekle");
        JButton btnDeleteRoom = UIHelper.createButton("Seçili Odayı Sil");

        roomForm.add(new JLabel("Oda İsmi/No:"));     roomForm.add(txtRoomName);
        roomForm.add(new JLabel("Kapasite:"));       roomForm.add(txtCapacity);
        roomForm.add(btnAddRoom);                      roomForm.add(btnDeleteRoom);

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

    // 3. KULLANICI YÖNETİMİ METODU (İŞTE EKSİK OLAN BUYDU KUZEN)
    private void createUserManagementTab() {
        JPanel tabUserMgmt = new JPanel(new BorderLayout());
        JPanel userActionPanel = new JPanel(new GridLayout(6, 1, 15, 15)); 
        userActionPanel.setBorder(new EmptyBorder(30, 200, 30, 200));

        JTextField txtTargetUserId = new JTextField();
        JButton btnPunish = UIHelper.createColoredButton("Güven Puanını 10 Puan DÜŞÜR", new Color(255, 153, 51), null, new Font("Tahoma", Font.BOLD, 16));
        JButton btnBan = UIHelper.createColoredButton("Sistemden KALICI OLARAK BANLA", new Color(220, 53, 69), Color.WHITE, new Font("Tahoma", Font.BOLD, 16));

        userActionPanel.add(new JLabel("İşlem Yapılacak Kullanıcı ID:", SwingConstants.CENTER));
        userActionPanel.add(txtTargetUserId);
        userActionPanel.add(btnPunish);
        userActionPanel.add(btnBan);

        tabUserMgmt.add(userActionPanel, BorderLayout.CENTER);
        adminTabs.addTab("Kullanıcı Yönetimi", tabUserMgmt);

        btnPunish.addActionListener(e -> JOptionPane.showMessageDialog(this, txtTargetUserId.getText() + " ID'li kullanıcının puanı düşürüldü."));
    }

    // 4. TAB CHANGE LISTENER METODU (BU DA EKSİK OLABİLİR)
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
}