package lib.panels;

import lib.UIHelper;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private JTable booksTable;
    private JTable roomsTable;
    private JTabbedPane adminTabs;
    private JButton btnExportCSV;

    // AdminPanel, dışarıdan kitaplar ve odalar tablosunu parametre olarak alır
    public AdminPanel(JTable booksTable, JTable roomsTable) {
        this.booksTable = booksTable;
        this.roomsTable = roomsTable;

        setLayout(new BorderLayout(0, 0));

        // Üst Başlık
        JLabel lblAdminTitle = UIHelper.createLabel("Yönetici Kontrol Paneli", UIHelper.FONT_BOLD, SwingConstants.CENTER);
        lblAdminTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblAdminTitle, BorderLayout.NORTH);

        // Sekmeler (Tabs)
        adminTabs = new JTabbedPane(JTabbedPane.TOP);
        adminTabs.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(adminTabs, BorderLayout.CENTER);

        // Alt Kısım (CSV İndirme Butonu)
        JPanel adminReportPanel = new JPanel();
        adminReportPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        btnExportCSV = UIHelper.createColoredButton("Kitap Envanterini CSV Olarak İndir", null, new Color(0, 100, 0), new Font("Tahoma", Font.BOLD, 16));
        adminReportPanel.add(btnExportCSV);
        add(adminReportPanel, BorderLayout.SOUTH);

        // Sekmeleri oluştur ve ekle
        createBookManagementTab();
        createRoomManagementTab();
        createUserManagementTab();

        // Sekme değiştiğinde CSV butonunu güncelle
        setupTabChangeListener();
    }

    private void createBookManagementTab() {
        JPanel tabBookMgmt = new JPanel(new BorderLayout());

        // Kitap Ekleme Formu
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 20, 20));
        formPanel.setBorder(new EmptyBorder(50, 200, 300, 200));

        JTextField txtAddId = new JTextField();
        JTextField txtAddName = new JTextField();
        JTextField txtAddAuthor = new JTextField();
        JTextField txtAddCategory = new JTextField();
        JButton btnAddBook = UIHelper.createButton("Kataloğa Ekle");
        btnAddBook.setFont(new Font("Tahoma", Font.BOLD, 18));

        formPanel.add(new JLabel("Kitap ID:"));       formPanel.add(txtAddId);
        formPanel.add(new JLabel("Kitap Adı:"));      formPanel.add(txtAddName);
        formPanel.add(new JLabel("Yazar:"));          formPanel.add(txtAddAuthor);
        formPanel.add(new JLabel("Kategori:"));       formPanel.add(txtAddCategory);
        formPanel.add(new JLabel(""));                formPanel.add(btnAddBook);

        tabBookMgmt.add(formPanel, BorderLayout.CENTER);

        // Kitap Silme Butonu
        JButton btnDeleteBook = UIHelper.createColoredButton("Tablodan Seçili Kitabı SİL", new Color(255, 102, 102), null, new Font("Tahoma", Font.BOLD, 18));
        tabBookMgmt.add(btnDeleteBook, BorderLayout.SOUTH);

        adminTabs.addTab("Kitap Yönetimi", tabBookMgmt);

        // Olaylar (Events)
        btnAddBook.addActionListener(e -> {
            if (txtAddId.getText().isEmpty() || txtAddName.getText().isEmpty() || txtAddAuthor.getText().isEmpty() || txtAddCategory.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
            } else {
                ((DefaultTableModel) booksTable.getModel()).addRow(new Object[]{
                    txtAddId.getText(), txtAddName.getText(), txtAddAuthor.getText(), txtAddCategory.getText(), "Müsait"
                });
                JOptionPane.showMessageDialog(this, "Kitap başarıyla kütüphaneye eklendi!");
                txtAddId.setText(""); txtAddName.setText(""); txtAddAuthor.setText(""); txtAddCategory.setText("");
            }
        });

        btnDeleteBook.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = booksTable.convertRowIndexToModel(selectedRow);
                ((DefaultTableModel) booksTable.getModel()).removeRow(modelRow);
                JOptionPane.showMessageDialog(this, "Kitap başarıyla silindi.");
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek kitabı tablodan seçin.");
            }
        });
    }

    private void createRoomManagementTab() {
        JPanel tabRoomMgmt = new JPanel(new BorderLayout());
        
        JPanel roomForm = new JPanel(new GridLayout(3, 2, 10, 10));
        roomForm.setBorder(new EmptyBorder(30, 100, 30, 100));

        JTextField txtRoomId = new JTextField();
        JTextField txtCapacity = new JTextField();
        JButton btnAddRoom = UIHelper.createButton("Oda Ekle");
        JButton btnDeleteRoom = UIHelper.createButton("Seçili Odayı Sil");

        roomForm.add(new JLabel("Oda Numarası:"));     roomForm.add(txtRoomId);
        roomForm.add(new JLabel("Kapasite (Kişi):"));  roomForm.add(txtCapacity);
        roomForm.add(btnAddRoom);                      roomForm.add(btnDeleteRoom);

        tabRoomMgmt.add(roomForm, BorderLayout.NORTH);
        adminTabs.addTab("Oda Yönetimi", tabRoomMgmt);

        // Olaylar
        btnAddRoom.addActionListener(e -> {
            ((DefaultTableModel) roomsTable.getModel()).addRow(new Object[]{txtRoomId.getText(), txtCapacity.getText() + " Kişi", "Müsait", "-"});
            JOptionPane.showMessageDialog(this, "Oda eklendi.");
        });

        btnDeleteRoom.addActionListener(e -> {
            int selected = roomsTable.getSelectedRow();
            if(selected != -1) {
                ((DefaultTableModel)roomsTable.getModel()).removeRow(roomsTable.convertRowIndexToModel(selected));
                JOptionPane.showMessageDialog(this, "Oda başarıyla silindi.");
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek odayı tablodan seçin.");
            }
        });
    }

    private void createUserManagementTab() {
        JPanel tabUserMgmt = new JPanel(new BorderLayout());
        
        JPanel userActionPanel = new JPanel(new GridLayout(6, 1, 15, 15)); 
        userActionPanel.setBorder(new EmptyBorder(30, 200, 30, 200));

        JTextField txtTargetUserId = new JTextField();
        txtTargetUserId.setFont(UIHelper.FONT_NORMAL);

        JButton btnPunish = UIHelper.createColoredButton("Güven Puanını 10 Puan DÜŞÜR", new Color(255, 153, 51), null, new Font("Tahoma", Font.BOLD, 16));
        JButton btnSuspend = UIHelper.createColoredButton("Hesabı ASKIYA AL", new Color(255, 204, 0), null, new Font("Tahoma", Font.BOLD, 16));
        JButton btnBan = UIHelper.createColoredButton("Sistemden KALICI OLARAK BANLA", new Color(220, 53, 69), Color.WHITE, new Font("Tahoma", Font.BOLD, 16));

        userActionPanel.add(new JLabel("İşlem Yapılacak Kullanıcı ID:", SwingConstants.CENTER));
        userActionPanel.add(txtTargetUserId);
        userActionPanel.add(btnPunish);
        userActionPanel.add(btnSuspend);
        userActionPanel.add(btnBan);

        tabUserMgmt.add(userActionPanel, BorderLayout.CENTER);
        adminTabs.addTab("Kullanıcı Yönetimi", tabUserMgmt);

        // Olaylar
        btnPunish.addActionListener(e -> executeUserAction(txtTargetUserId, "Güven puanı 10 düşürüldü."));
        btnSuspend.addActionListener(e -> executeUserAction(txtTargetUserId, "Kütüphane hizmetleri geçici olarak ASKIYA ALINDI!"));
        
        btnBan.addActionListener(e -> {
            String userId = txtTargetUserId.getText();
            if(!userId.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, userId + " ID'li kullanıcıyı BANLAMAK istediğinize emin misiniz?", "Kalıcı Ban Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if(confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, userId + " sistemden kalıcı olarak BANLANDI.", "İşlem Tamamlandı", JOptionPane.INFORMATION_MESSAGE);
                    txtTargetUserId.setText("");
                }
            }
        });
    }

    // Kullanıcı işlemleri için ortak yardımcı metot
    private void executeUserAction(JTextField textField, String message) {
        String userId = textField.getText();
        if(!userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, userId + " ID'li kullanıcının " + message);
            textField.setText("");
        }
    }

    private void setupTabChangeListener() {
        // Tab değiştiğinde buton metni ve görünürlüğü güncelleniyor
        adminTabs.addChangeListener(e -> {
            int selectedIndex = adminTabs.getSelectedIndex();
            if (selectedIndex == 0) {
                btnExportCSV.setText("Kitap Envanterini CSV Olarak İndir");
                btnExportCSV.setVisible(true);
            } else if (selectedIndex == 1) {
                btnExportCSV.setText("Oda Envanterini CSV Olarak İndir");
                btnExportCSV.setVisible(true);
            } else {
                btnExportCSV.setVisible(false); 
            }
        });

        // CSV İndirme İşlemi
        btnExportCSV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Raporu Kaydet");
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".csv")) filePath += ".csv";
                
                try {
                    JTable tableToExport = (adminTabs.getSelectedIndex() == 0) ? booksTable : roomsTable;
                    
                    // ÖNEMLİ: Projende AdminReportManager sınıfı olduğunu varsayıyoruz.
                    lib.AdminReportManager.exportTableToCSV(tableToExport, filePath);
                    
                    JOptionPane.showMessageDialog(this, "Rapor başarıyla oluşturuldu:\n" + filePath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Kayıt Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}