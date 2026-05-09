package lib.panels;

import lib.UIHelper;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReadlistPanel extends JPanel {
    private JTable table_readlist;

    public ReadlistPanel() {
        setLayout(new BorderLayout(0, 0));

        // Üst Başlık
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(UIHelper.createLabel("Geçmiş İşlemleriniz", UIHelper.FONT_NORMAL, SwingConstants.LEFT));
        add(topPanel, BorderLayout.NORTH);

        // Tablo Kurulumu
        String[] readlistColumns = {"İşlem ID", "Kitap Adı", "Alış Tarihi", "İade Tarihi", "Durum"};
        table_readlist = UIHelper.createReadOnlyTable(readlistColumns);
        DefaultTableModel model = (DefaultTableModel) table_readlist.getModel();

        // Örnek Veriler
        model.addRow(new Object[]{"TRX-101", "Clean Code", "01.04.2026", "15.04.2026", "İade Edildi"});
        model.addRow(new Object[]{"TRX-102", "Suç ve Ceza", "10.04.2026", "24.04.2026", "Süresi Geçti (Cezalı)"});
        model.addRow(new Object[]{"TRX-103", "Data Structures", "05.05.2026", "-", "Okunuyor"});

        add(new JScrollPane(table_readlist), BorderLayout.CENTER);
    }
}