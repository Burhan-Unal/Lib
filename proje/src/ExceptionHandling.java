public class ExceptionHandling {
    // Güven puanı yetersizliği durumunda fırlatılır
    public class InsufficientPointsException extends Exception {
        public InsufficientPointsException(String message) {
            super(message);
        }
    }

    // Rezervasyon zaman aşımı durumunda fırlatılır
    public class ReservationTimeoutException extends Exception {
        public ReservationTimeoutException(String message) {
            super(message);
        }
    }
}
