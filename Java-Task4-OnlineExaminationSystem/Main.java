import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamApp app = new ExamApp();
            app.setVisible(true);
        });
    }
}