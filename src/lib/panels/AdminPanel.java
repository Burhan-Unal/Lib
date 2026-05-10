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
        setBackground(UIHelper.COLOR_BACKGROUND); 

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
        makeButtonFlat(btnExportCSV); 
        
        adminReportPanel.add(btnExportCSV);
        add(adminReportPanel, BorderLayout.SOUTH);

        //Tab çağrıları
        createBookManagementTab();
        createRoomManagementTab();
        setupTabChangeListener();
    }

    //Kitap Yönetimi
    private void createBookManagementTab() {
        JPanel tabBookMgmt = new JPanel(new BorderLayout());
        tabBookMgmt.setBackground(UIHelper.COLOR_BACKGROUND);
        
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
        
        tabBookMgmt.add(formPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); 
        
        JButton btnDeleteBook = UIHelper.createColoredButton("Tablodan Seçili Kitabı SİL", UIHelper.COLOR_DANGER, Color.WHITE, UIHelper.FONT_BOLD);
        makeButtonFlat(btnDeleteBook);
        
        bottomPanel.add(btnDeleteBook);
        tabBookMgmt.add(bottomPanel, BorderLayout.SOUTH);
        adminTabs.addTab("Kitap Yönetimi", tabBookMgmt);

        btnAddBook.addActionListener(e -> {
            if (txtAddName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen kitap adını doldurun!");
            } else {
                if (DatabaseManager.materyalEkle("Kitap", txtAddName.getText(), txtAddAuthor.getText(), txtAddCategory.getText())) {
                    JOptionPane.showMessageDialog(this, "Kitap eklendi!");
                    txtAddName.setText(""); txtAddAuthor.setText(""); txtAddCategory.setText("");
                    
                    //Tablo güncelleme
                    DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
                    model.setRowCount(0); 
                    
                    for (String[] satir : DatabaseManager.tumMateryalleriGetir("Kitap")) {
                        model.addRow(satir);
                    }
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

    // Oda Yönetimi
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
            if (txtRoomName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen oda numarasını doldurun!");
            } else {
                String kapasiteMetni = txtCapacity.getText();
                if (!kapasiteMetni.endsWith("Kişi")) {
                    kapasiteMetni += " Kişi";
                }
                
                if (DatabaseManager.materyalEkle("Oda", txtRoomName.getText(), "-", kapasiteMetni)) {
                    JOptionPane.showMessageDialog(this, "Oda eklendi.");
                    txtRoomName.setText(""); txtCapacity.setText("");
                    
                    //Tablo güncelleme
                    DefaultTableModel model = (DefaultTableModel) roomsTable.getModel();
                    model.setRowCount(0); 
                    for (String[] dbSatir : DatabaseManager.tumMateryalleriGetir("Oda")) {
                        model.addRow(new Object[]{ dbSatir[0], dbSatir[1], dbSatir[3], dbSatir[4], "-" });
                    }
                }
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

    //Tab değiştirme 
    private void setupTabChangeListener() {
        adminTabs.addChangeListener(e -> {
            int selectedIndex = adminTabs.getSelectedIndex();
            btnExportCSV.setVisible(true); 
            if (selectedIndex == 0) {
                btnExportCSV.setText("Kitap Envanterini CSV Olarak İndir");
            } else if (selectedIndex == 1) {
                btnExportCSV.setText("Oda Envanterini CSV Olarak İndir");
            }
        });

        btnExportCSV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "CSV Raporu Hazırlanıyor...");
            }
        });
    }
    
    // Metin kutularının düzgün ve modern görünmesi için
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(UIHelper.FONT_NORMAL);
        tf.setForeground(UIHelper.COLOR_TEXT);
        tf.setBackground(Color.WHITE);
        return tf;
    }

    private void makeButtonFlat(JButton btn) {
        btn.setOpaque(true); 
        btn.setBorderPainted(false); 
    }
}