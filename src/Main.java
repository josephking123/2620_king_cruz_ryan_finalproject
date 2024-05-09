import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the Ping Pong Game application.
 * It initializes the graphical user interface by creating and displaying the main menu.
 */
public class Main {

    /**
     * The main method, starting point of the application.
     * It invokes the creation and display of the main menu on the Event Dispatch Thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}