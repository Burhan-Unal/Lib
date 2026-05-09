
    // Soyut Materyal Sınıfı
    public abstract class Material {
        private String id; // Sadece admin görebilecek mantık için private
        private String title;

        public Material(String id, String title) {
            this.id = id;
            this.title = title;
        }


        public String getId() { return id; }
        public String getTitle() { return title; }


        public abstract void processReturn();


    // Kitap Sınıfı
    public class Book extends Material {
        public Book(String id, String title) {
            super(id, title);
        }

        @Override
        public void processReturn() {
            System.out.println("Kitap iade işlemleri tamamlandı.");
        }
}
    }

