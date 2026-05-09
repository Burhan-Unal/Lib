package lib; 
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminReportManager {


    public static void exportTableToCSV(JTable targetTable, String filePath) throws Exception {
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            
            // UTF-8 (Excel Türkçe karakter uyumu için)
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            
            DefaultTableModel model = (DefaultTableModel) targetTable.getModel();
            
            // Başlıkları Yaz
            for (int i = 0; i < model.getColumnCount(); i++) {
                osw.write("\"" + model.getColumnName(i) + "\"");
                if (i < model.getColumnCount() - 1) osw.write(";"); 
            }
            osw.write("\n");
            
            // Verileri Yaz
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    String data = (value != null) ? value.toString() : "";
                    osw.write("\"" + data.replace("\"", "\"\"") + "\"");
                    if (j < model.getColumnCount() - 1) osw.write(";");
                }
                osw.write("\n");
            }
        }
    }


}