package lib.panels;

import lib.UIHelper;
import lib.DatabaseManager;
import java.awt.*;
import java.util.List;
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

        // Tablo Kurulumu - DB'den gelen verilere göre sütunları güncelledim
        String[] readlistColumns = {"Materyal İsmi", "Tip", "Durum", "İşlem Zamanı"};
        table_readlist = UIHelper.createReadOnlyTable(readlistColumns);
        
        refreshHistory();

        add(new JScrollPane(table_readlist), BorderLayout.CENTER);
    }

    public void refreshHistory() {
        DefaultTableModel model = (DefaultTableModel) table_readlist.getModel();
        model.setRowCount(0);
        
        // Senin yazdığın geçmişi getirme metodunu aktif ID ile çağırıyoruz
        List<String[]> gecmis = DatabaseManager.kullaniciGecmisiniGetir(DatabaseManager.aktifKullaniciId);
        
        for (String[] satir : gecmis) {
            model.addRow(satir);
        }
    }
}