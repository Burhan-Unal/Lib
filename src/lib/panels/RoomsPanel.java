package lib.panels;

import lib.UIHelper;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RoomsPanel extends JPanel {
    private JTable table_rooms;

    public RoomsPanel(boolean isAdmin) {
        setLayout(new BorderLayout(0, 0));

        // Alt Butonlar (Sadece kullanıcılara görünür)
        JPanel roomButtons = new JPanel();
        roomButtons.add(UIHelper.createButton("Rezerve Et"));
        roomButtons.add(UIHelper.createButton("Odayı Onayla"));
        roomButtons.setVisible(!isAdmin);
        add(roomButtons, BorderLayout.SOUTH);

        // Tablo Kurulumu
        String[] roomColumns = {"Oda Numarası", "Kapasite", "Durum", "Kalan Onay Süresi"};
        table_rooms = UIHelper.createReadOnlyTable(roomColumns);
        DefaultTableModel model = (DefaultTableModel) table_rooms.getModel();

        // Örnek Veriler
        model.addRow(new Object[]{"Oda 101", "4 Kişi", "Müsait", "-"});
        model.addRow(new Object[]{"Oda 102", "2 Kişi", "Rezerve edildi", "15 Dk Kaldı"});
        model.addRow(new Object[]{"Oda 103", "6 Kişi", "Müsait", "-"});
        model.addRow(new Object[]{"Oda 104", "1 Kişi", "Rezerve edildi", "05 Dk Kaldı (Riskli)"});
        model.addRow(new Object[]{"Oda 105", "4 Kişi", "Dolu", "-"});

        add(new JScrollPane(table_rooms), BorderLayout.CENTER);
    }

    // Admin panelinin bu tabloya erişmesi için gerekli Getter
    public JTable getRoomsTable() {
        return table_rooms;
    }
}