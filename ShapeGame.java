import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ShapeGame {
    public static void main(String[] args) {
        // Launch the GUI version of the game
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GameGUI();
        });
    }
}
