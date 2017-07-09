import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class main
{
  public main() {}
  
  public static void main(String[] args) throws Exception
  {
	if (isFirstRun()) {
		if (setupSumFile()) {
			JOptionPane.showMessageDialog(null,"An unknown error has occured setting up the application for first time use");
			return;
		}
	}
    window w = new window();
  }
  private static boolean isFirstRun() {
	  return !(new File("sums.info").exists());
  }
  private static boolean setupSumFile() throws Exception {
	  File sumFile = new File("sums.info");
	  if(!sumFile.exists()) {
		  sumFile.createNewFile();
	  }
	  return isFirstRun();
  }
}