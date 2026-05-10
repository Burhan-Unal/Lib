package lib.panels;

import lib.UIHelper;
import lib.DatabaseManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class BooksPanel extends JPanel {
    private JTable table_books;
    private JTextField textField;
    private TableRowSorter<DefaultTableModel> sorter;

    public BooksPanel(boolean isAdmin) {
        setLayout(new BorderLayout(0, 0));

        // Üst Arama Çubuğu
        JPanel searchBar = new JPanel();
        searchBar.add(UIHelper.createLabel("Kitap Ara", UIHelper.FONT_NORMAL, SwingConstants.LEFT));
        
        textField = new JTextField();
        textField.setFont(UIHelper.FONT_NORMAL);
        textField.setColumns(40);
        setupSearchLogic();
        searchBar.add(textField);
        add(searchBar, BorderLayout.NORTH);

        // Alt Butonlar (Sadece kullanıcılara görünür)
        JPanel borrowButtons = new JPanel();
        JButton btnBorrow = UIHelper.createButton("Ödünç Al");
        JButton btnQueue = UIHelper.createButton("Kuyruğa Katıl");

        btnBorrow.addActionListener(e -> {
            int row = table_books.getSelectedRow();
            if (row != -1) {
                int modelRow = table_books.convertRowIndexToModel(row);
                int bookId = Integer.parseInt(table_books.getModel().getValueAt(modelRow, 0).toString());
                boolean sonuc = DatabaseManager.rezervasyonYap(DatabaseManager.aktifKullaniciId, bookId);
                if (sonuc) {
                    JOptionPane.showMessageDialog(this, "Kitap başarıyla ödünç alındı!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Hata: Kitap müsait değil.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen bir kitap seçin!");
            }
        });

        borrowButtons.add(btnBorrow);
        borrowButtons.add(btnQueue);
        borrowButtons.setVisible(!isAdmin);
        add(borrowButtons, BorderLayout.SOUTH);

        // Tablo Kurulumu
        String[] bookColumns = {"ID", "Kitap Adı", "Yazar", "Kategori", "Durum"};
        table_books = UIHelper.createReadOnlyTable(bookColumns);
        
        refreshTable();
        
        sorter = new TableRowSorter<>((DefaultTableModel) table_books.getModel());
        table_books.setRowSorter(sorter);

        add(new JScrollPane(table_books), BorderLayout.CENTER);
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table_books.getModel();
        model.setRowCount(0);
        List<String[]> kitaplar = DatabaseManager.tumMateryalleriGetir("Kitap");
        for (String[] kitap : kitaplar) {
            model.addRow(kitap);
        }
    }

    // Arama mantığı ve Placeholder buraya taşındı
    private void setupSearchLogic() {
        textField.setForeground(Color.GRAY);
        textField.setText("Kitap adı veya ID giriniz...");

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Kitap adı veya ID giriniz...")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText("Kitap adı veya ID giriniz...");
                }
            }
        });

        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
            
            private void filterTable() {
                SwingUtilities.invokeLater(() -> {
                    String text = textField.getText();
                    if (text.trim().length() == 0 || text.equals("Kitap adı veya ID giriniz...")) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
                    }
                });
            }
        });

        // Backspace engelleme
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && textField.getCaretPosition() == 0 && textField.getSelectedText() == null) {
                    e.consume();
                }
            }
        });
    }

    // Admin panelinin bu tabloya erişmesi için gerekli Getter
    public JTable getBooksTable() {
        return table_books;
    }
}