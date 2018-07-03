package default_package;
import javax.swing.JButton;

public class shredButton extends JButton {
  public shredButton(String text) {
    setText(text);
    setActionCommand("shred");
  }
}