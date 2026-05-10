package lib.time;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import lib.DatabaseManager; 

public class TimeManager {
    private ReservationObserver observer;
    private ScheduledExecutorService scheduler;

    public TimeManager(ReservationObserver observer) {
        this.observer = observer;
    }

    public void startScheduler(JTable roomsTable) {
        scheduler = Executors.newScheduledThreadPool(1);
        
        Runnable checkTask = () -> {
            
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String query = "SELECT r.kullanici_id, r.materyal_id, m.isim, r.rezervasyon_zamani " +
                           "FROM Rezervasyon r INNER JOIN Materyal m ON r.materyal_id = m.id " +
                           "WHERE m.durum = 'Reserved'";

            try (Connection conn = DatabaseManager.connect();
                 PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int kullaniciId = rs.getInt("kullanici_id");
                    int materyalId = rs.getInt("materyal_id");
                    String roomName = rs.getString("isim");
                    String dbTime = rs.getString("rezervasyon_zamani");

                    LocalDateTime startTime = LocalDateTime.parse(dbTime, formatter);
                    Duration duration = Duration.between(startTime, now);

                   
                    if (duration.toMinutes() >= 30) {
                        
                        boolean islemBasarili = DatabaseManager.rezervasyonIptalEtVeCezaVer(kullaniciId, materyalId, true);

                        if (islemBasarili) {
                            for (int i = 0; i < roomsTable.getRowCount(); i++) {
                                if (roomName.equals(roomsTable.getValueAt(i, 0))) {
                                    final int rowIndex = i;
                                    
                                    SwingUtilities.invokeLater(() -> {
                                        observer.onReservationTimeout(roomName, rowIndex);
                                    });
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Zaman kontrolü hatası: " + e.getMessage());
            }
        };

        
        scheduler.scheduleAtFixedRate(checkTask, 0, 5, TimeUnit.SECONDS);
    }
    
    public void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}