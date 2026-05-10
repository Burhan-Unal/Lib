package lib.time;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    	scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); 
            return t;
        });

        Runnable checkTask = () -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                List<Object[]> cezaListesi = new ArrayList<>();

                String query = "SELECT r.kullanici_id, r.materyal_id, m.isim, r.rezervasyon_zamani " +
                               "FROM Rezervasyon r INNER JOIN Materyal m ON r.materyal_id = m.id " +
                               "WHERE m.durum = 'Rezerve Edildi'";

                try (Connection conn = DatabaseManager.connect();
                     PreparedStatement pstmt = conn.prepareStatement(query);
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        String roomName = rs.getString("isim");
                        String dbTime = rs.getString("rezervasyon_zamani");

                        LocalDateTime startTime = LocalDateTime.parse(dbTime, formatter);
                        Duration duration = Duration.between(startTime, now);

                        if (duration.toSeconds() >= 30) {
                            cezaListesi.add(new Object[]{
                                rs.getInt("kullanici_id"),
                                rs.getInt("materyal_id"),
                                roomName
                            });
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Zaman kontrolü (Okuma) hatası: " + e.getMessage());
                }

                for (Object[] ihlal : cezaListesi) {
                    int kullaniciId = (int) ihlal[0];
                    int materyalId = (int) ihlal[1];
                    String roomName = (String) ihlal[2];

                    boolean islemBasarili = DatabaseManager.rezervasyonIptalEtVeCezaVer(kullaniciId, materyalId, true);

                    if (islemBasarili) {
                        for (int i = 0; i < roomsTable.getRowCount(); i++) {
                            if (roomName.equals(roomsTable.getValueAt(i, 1))) {
                                final int rowIndex = i;
                                
                                SwingUtilities.invokeLater(() -> {
                                    observer.onReservationTimeout(roomName, rowIndex);
                                });
                                break;
                            }
                        }
                    }
                }

            } catch (Throwable t) {
                System.out.println("Zamanlayıcı motoru çöktü: " + t.getMessage());
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