package default_package;
import javax.swing.JButton;

public class encryptButton extends JButton {
  String label = "Encrypt";
  
  public encryptButton() { setText(label);
    setActionCommand("encrypt");
  }
}