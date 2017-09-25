package default_package;

import javax.swing.JButton;

public class deleteLogButton extends JButton {

	String label = "Delete Logs";
	
	public deleteLogButton() {
		setText(label);
		setActionCommand("delete_logs");
	}
	
}
