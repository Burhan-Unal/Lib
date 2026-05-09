import java.util.ArrayList;
import java.util.List;

public interface Observer {
    // Gözlemci Arayüzü
    void update(String message);
    }

    // Kullanıcı Sınıfı (Observer'ı implement eder)
    public class Member implements Observer {
        private String name;
        private int trustPoint = 100;
        public Member(String name) { this.name = name; }

        @Override
        public void update(String message) {
            System.out.println("Bildirim [" + name + "]: " + message);
        }

    // Encapsulation: Puan yönetimi sadece yetkili metodlarla yapılır[span_12](end_span)
        public void updateTrustPoint(int change) {
            this.trustPoint += change;
        }
    }

    // Bildirim Yönetici (Subject)
    public class NotificationService {
        private List<Observer> observers = new ArrayList<>();

        public void subscribe(Observer observer) { observers.add(observer); }

        public void notifyAll(String message) {
            for (Observer obs : observers) {
                obs.update(message);
            }
        }
    }

