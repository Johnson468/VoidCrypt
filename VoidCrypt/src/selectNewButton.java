import javax.swing.JButton;

public class selectNewButton extends JButton {
  String label = "Select Other File";
  
  public selectNewButton() { setText(label);
    setActionCommand("selectNew");
  }
}