package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:lib.db";
    public static int aktifKullaniciId = -1;

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void initializeDatabase() {
        String kullaniciSql = "CREATE TABLE IF NOT EXISTS Kullanici (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ad_soyad TEXT NOT NULL," +
                " ogrenci_no TEXT UNIQUE NOT NULL," +
                " guven_puani INTEGER DEFAULT 100" +
                ");";

        String materyalSql = "CREATE TABLE IF NOT EXISTS Materyal (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " tip TEXT NOT NULL,\n" 
                + " isim TEXT NOT NULL,\n"
                + " yazar TEXT,\n"       
                + " kategori TEXT,\n"    
                + " durum TEXT DEFAULT 'Available'\n" 
                + ");";

        String rezervasyonSql = "CREATE TABLE IF NOT EXISTS Rezervasyon (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kullanici_id INTEGER," +
                " materyal_id INTEGER," +
                " rezervasyon_zamani DATETIME DEFAULT CURRENT_TIMESTAMP," +
                " FOREIGN KEY (kullanici_id) REFERENCES Kullanici (id)," +
                " FOREIGN KEY (materyal_id) REFERENCES Materyal (id)" +
                ");";

        String kuyrukSql = "CREATE TABLE IF NOT EXISTS Kuyruk (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kullanici_id INTEGER," +
                " materyal_id INTEGER," +
                " eklenme_zamani DATETIME DEFAULT CURRENT_TIMESTAMP," +
                " FOREIGN KEY (kullanici_id) REFERENCES Kullanici (id)," +
                " FOREIGN KEY (materyal_id) REFERENCES Materyal (id)" +
                ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(kullaniciSql);
            stmt.execute(materyalSql);
            stmt.execute(rezervasyonSql);
            stmt.execute(kuyrukSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public static boolean kullaniciKaydet(String adSoyad, String ogrenciNo) {
        String sql = "INSERT INTO Kullanici (ad_soyad, ogrenci_no) VALUES (?, ?)";
        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adSoyad);
            pstmt.setString(2, ogrenciNo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Kayıt hatası: " + e.getMessage());
            return false;
        }
    }

    public static boolean rezervasyonYap(int kullaniciId, int materyalId) {
        String kontrolSorgu = "SELECT durum FROM Materyal WHERE id = ?";
        String materyalGuncelle = "UPDATE Materyal SET durum = 'Reserved' WHERE id = ?";
        String rezervasyonEkle = "INSERT INTO Rezervasyon (kullanici_id, materyal_id) VALUES (?, ?)";

        Connection conn = connect();
        try {
            conn.setAutoCommit(false);

            PreparedStatement kontrol = conn.prepareStatement(kontrolSorgu);
            kontrol.setInt(1, materyalId);
            ResultSet rs = kontrol.executeQuery();

            if (rs.next()) {
                String durum = rs.getString("durum");
                if (!durum.equals("Available")) {
                    conn.rollback();
                    return false;
                }
            } else {
                conn.rollback();
                return false;
            }

            PreparedStatement guncelle = conn.prepareStatement(materyalGuncelle);
            guncelle.setInt(1, materyalId);
            guncelle.executeUpdate();

            PreparedStatement ekle = conn.prepareStatement(rezervasyonEkle);
            ekle.setInt(1, kullaniciId);
            ekle.setInt(2, materyalId);
            ekle.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public static boolean akilliKuyrugaEkle(int kullaniciId, int materyalId) {
        String eklemeSorgu = "INSERT INTO Kuyruk (kullanici_id, materyal_id) VALUES (?, ?)";

        Connection conn = connect();
        try {
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(eklemeSorgu);
            pstmt.setInt(1, kullaniciId);
            pstmt.setInt(2, materyalId);
            pstmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public static boolean rezervasyonIptalEtVeCezaVer(int kullaniciId, int materyalId, boolean cezaUygula) {
        String silSorgu = "DELETE FROM Rezervasyon WHERE kullanici_id = ? AND materyal_id = ?";
        String materyalSerbest = "UPDATE Materyal SET durum = 'Available' WHERE id = ?";
        String cezaSorgu = "UPDATE Kullanici SET guven_puani = guven_puani - 10 WHERE id = ?";

        Connection conn = connect();
        try {
            conn.setAutoCommit(false);

            PreparedStatement silStmt = conn.prepareStatement(silSorgu);
            silStmt.setInt(1, kullaniciId);
            silStmt.setInt(2, materyalId);
            int silinenSatir = silStmt.executeUpdate();

            if (silinenSatir == 0) {
                conn.rollback();
                return false;
            }

            PreparedStatement serbestStmt = conn.prepareStatement(materyalSerbest);
            serbestStmt.setInt(1, materyalId);
            serbestStmt.executeUpdate();

            if (cezaUygula) {
                PreparedStatement cezaStmt = conn.prepareStatement(cezaSorgu);
                cezaStmt.setInt(1, kullaniciId);
                cezaStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public static List<String[]> kullaniciGecmisiniGetir(int kullaniciId) {
        List<String[]> liste = new ArrayList<>();

        String sorgu = "SELECT m.isim, m.tip, m.durum, r.rezervasyon_zamani " +
                       "FROM Rezervasyon r INNER JOIN Materyal m ON r.materyal_id = m.id " +
                       "WHERE r.kullanici_id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sorgu)) {
            pstmt.setInt(1, kullaniciId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] satir = {
                    rs.getString("isim"),
                    rs.getString("tip"),
                    rs.getString("durum"),
                    rs.getString("rezervasyon_zamani")
                };
                liste.add(satir);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    public static List<String> adminIcinKullaniciPerformansRaporu() {
        List<String> rapor = new ArrayList<>();

        String sorgu = "SELECT ad_soyad, ogrenci_no, guven_puani FROM Kullanici";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sorgu)) {
            while (rs.next()) {
                String satir = rs.getString("ad_soyad") + "," + rs.getString("ogrenci_no") + "," + rs.getInt("guven_puani");
                rapor.add(satir);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rapor;
    }

    public static int kullaniciGirisYap(String ogrenciNo) {
        String sql = "SELECT id FROM Kullanici WHERE ogrenci_no = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ogrenciNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

 
    public static List<String[]> tumMateryalleriGetir(String tip) {
        List<String[]> liste = new ArrayList<>();
        String sql = "SELECT id, isim, yazar, kategori, durum FROM Materyal WHERE tip = ?";
        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tip);
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] satir = new String[]{
                    String.valueOf(rs.getInt("id")), 
                    rs.getString("isim"), 
                    rs.getString("yazar") != null ? rs.getString("yazar") : "-", 
                    rs.getString("kategori") != null ? rs.getString("kategori") : "-",
                    rs.getString("durum")
                };
                liste.add(satir);
            }
        } catch (SQLException e) {
            System.out.println("Materyal çekme hatası: " + e.getMessage());
        }
        return liste;
    }
    

    public static boolean materyalEkle(String tip, String isim) {
        String sql = "INSERT INTO Materyal (tip, isim) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tip);
            pstmt.setString(2, isim);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean materyalSil(int materyalId) {
        String sql = "DELETE FROM Materyal WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, materyalId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int guvenPuaniGetir(int kullaniciId) {
        String sql = "SELECT guven_puani FROM Kullanici WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, kullaniciId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("guven_puani");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 100;
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}