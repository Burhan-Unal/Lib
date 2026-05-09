package lib.panels;

import lib.UIHelper;
import java.awt.*;
import java.awt.event.*;
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
        borrowButtons.add(UIHelper.createButton("Ödünç Al"));
        borrowButtons.add(UIHelper.createButton("Kuyruğa Katıl"));
        borrowButtons.setVisible(!isAdmin);
        add(borrowButtons, BorderLayout.SOUTH);

        // Tablo Kurulumu
        String[] bookColumns = {"ID", "Kitap Adı", "Yazar", "Kategori", "Durum"};
        table_books = UIHelper.createReadOnlyTable(bookColumns);
        DefaultTableModel model = (DefaultTableModel) table_books.getModel();
        
        // Örnek Veriler
        model.addRow(new Object[]{"B001", "Clean Code", "Robert C. Martin", "Yazılım", "Müsait"});
        model.addRow(new Object[]{"B002", "Data Structures", "Alan Turing", "Mühendislik", "Ödünç Alındı"});
        model.addRow(new Object[]{"B003", "1984", "George Orwell", "Roman", "Müsait"});
        
        sorter = new TableRowSorter<>(model);
        table_books.setRowSorter(sorter);

        add(new JScrollPane(table_books), BorderLayout.CENTER);
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