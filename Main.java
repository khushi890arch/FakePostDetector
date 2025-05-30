import ui.FakeMessageDetectorUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new FakeMessageDetectorUI().setVisible(true);
        });
    }
}
