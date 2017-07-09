package default_package;
import javax.swing.JButton;

public class decryptButton extends JButton {
  String label = "Decrypt";
  
  public decryptButton() { setText(label);
    setActionCommand("decrypt");
  }
}