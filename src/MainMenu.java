import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainMenu extends JFrame{

    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;

    public MainMenu(){
        initComponents();
    }

    public void run(){
        new MainMenu().setVisible(true);
    }
}