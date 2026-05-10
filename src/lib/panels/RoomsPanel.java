package lib.panels;

import lib.UIHelper;
import lib.DatabaseManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RoomsPanel extends JPanel {
    private JTable table_rooms;

    public RoomsPanel(boolean isAdmin) {
        setLayout(new BorderLayout(0, 0));

        // Alt Butonlar (Sadece kullanıcılara görünür)
        JPanel roomButtons = new JPanel();
        JButton btnReserve = UIHelper.createButton("Rezerve Et");
        JButton btnConfirm = UIHelper.createButton("Odayı Onayla");

        btnReserve.addActionListener(e -> {
            int row = table_rooms.getSelectedRow();
            if (row != -1) {
                int roomId = Integer.parseInt(table_rooms.getValueAt(row, 0).toString());
                boolean sonuc = DatabaseManager.rezervasyonYap(DatabaseManager.aktifKullaniciId, roomId);
                if (sonuc) {
                    JOptionPane.showMessageDialog(this, "Oda başarıyla rezerve edildi!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Hata: Oda müsait değil.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen bir oda seçin!");
            }
        });

        roomButtons.add(btnReserve);
        roomButtons.add(btnConfirm);
        roomButtons.setVisible(!isAdmin);
        add(roomButtons, BorderLayout.SOUTH);

        // Tablo Kurulumu
        String[] roomColumns = {"ID", "Oda Numarası", "Kapasite", "Durum"};
        table_rooms = UIHelper.createReadOnlyTable(roomColumns);
        
        refreshTable();

        add(new JScrollPane(table_rooms), BorderLayout.CENTER);
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table_rooms.getModel();
        model.setRowCount(0);
        List<String[]> odalar = DatabaseManager.tumMateryalleriGetir("Oda");
        for (String[] oda : odalar) {
            model.addRow(new Object[]{oda[0], oda[1], oda[3], oda[4]});
        }
    }

    // Admin panelinin bu tabloya erişmesi için gerekli Getter
    public JTable getRoomsTable() {
        return table_rooms;
    }
}