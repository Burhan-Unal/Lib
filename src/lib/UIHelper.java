package lib;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class UIHelper {
    
	//Renk+Font
    public static final Color COLOR_PRIMARY = new Color(67, 97, 238);   // Modern bir mavi
    public static final Color COLOR_DANGER = new Color(239, 35, 60);    // Soft kırmızı (Ban/Silme için)
    public static final Color COLOR_WARNING = new Color(255, 159, 28);  // Soft turuncu (Suspend için)
    public static final Color COLOR_SUCCESS = new Color(45, 198, 83);   // Soft yeşil (CSV İndirme/Onay için)
    public static final Color COLOR_TEXT = new Color(43, 45, 66);       // Tam siyah yerine koyu lacivert-gri
    public static final Color COLOR_BACKGROUND = new Color(248, 249, 250); // Kırık beyaz / Açık gri
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);

    public static void switchPanel(JPanel containerPanel, String panelName) {
        CardLayout cl = (CardLayout) (containerPanel.getLayout());
        cl.show(containerPanel, panelName);
    }

    //Buton Üretici
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NORMAL);
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(Color.WHITE);
        
        btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        return btn;
    }

    // Renkli Buton Üretici (Sistem uyarıları için)
    public static JButton createColoredButton(String text, Color bgColor, Color fgColor, Font font) {
        JButton btn = createButton(text); // Standart özellikleri al
        btn.setFont(font != null ? font : FONT_BOLD);
        
        if (bgColor != null) {
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE); // Arka plan renkliyse yazı genelde beyaz şık durur
        }
        if (fgColor != null) {
            btn.setForeground(fgColor); // Özel yazı rengi istendiyse ez (Override)
        }
        return btn;
    }

    // Standart Label Üretici
    public static JLabel createLabel(String text, Font font, int alignment) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(COLOR_TEXT); // Siyah yerine modern koyu gri
        lbl.setHorizontalAlignment(alignment);
        return lbl;
    }

    // Dinamik, Değiştirilemez (Read-Only) Tablo Üretici
    public static JTable createReadOnlyTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Tablo içi font
        table.setRowHeight(35); // Satır yüksekliği biraz küçültüldü ama hala ferah
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(233, 236, 239)); // Tablo başlığına hafif gri arkaplan
        table.getTableHeader().setBorder(new EmptyBorder(0,0,0,0)); // Başlık çerçevesini sil
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false); // Dikey çizgileri kaldırmak tabloyu çok modern gösterir
        table.setGridColor(new Color(222, 226, 230)); // Yatay çizgileri silik gri yap
        
        return table;
    }
}