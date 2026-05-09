package lib;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class UIHelper {
    
    // Proje genelindeki sabit fontlar
    public static final Font FONT_NORMAL = new Font("Tahoma", Font.PLAIN, 24);
    public static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 36);

    public static void switchPanel(JPanel containerPanel, String panelName) {
        CardLayout cl = (CardLayout) (containerPanel.getLayout());
        cl.show(containerPanel, panelName);
    }

    // Standart Buton Üretici
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NORMAL);
        return btn;
    }

    // Renkli Buton Üretici (Örn: Silme, Banlama butonları)
    public static JButton createColoredButton(String text, Color bgColor, Color fgColor, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        if (bgColor != null) btn.setBackground(bgColor);
        if (fgColor != null) btn.setForeground(fgColor);
        return btn;
    }

    // Standart Label Üretici
    public static JLabel createLabel(String text, Font font, int alignment) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setHorizontalAlignment(alignment);
        return lbl;
    }

    // Dinamik, Değiştirilemez (Read-Only) Tablo Üretici
    public static JTable createReadOnlyTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücre koruması
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(40);
        table.getTableHeader().setFont(FONT_BOLD);
        table.setFillsViewportHeight(true);
        return table;
    }
}