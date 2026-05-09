import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

public class File {


    public class AdminReportManager {
        public void exportMemberPerformance(List<Member> members, String fileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("Member Name, Trust Point\n"); // CSV Başlıkları
                for (Member m : members) {
                    // Burada Member sınıfına getName() ve getTrustPoint() eklemelisin
                    // writer.write(m.getName() + "," + m.getTrustPoint() + "\n");
                }
                System.out.println("Rapor başarıyla oluşturuldu: " + fileName);
            } catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
            }
        }
    }
}
