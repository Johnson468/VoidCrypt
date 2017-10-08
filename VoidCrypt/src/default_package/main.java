package default_package;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class main
{
  public main() {}
  
  public static void main(String[] args) throws Exception
  {

	final Logger logger = Logger.getLogger(main.class);
	logger.info("Application starting");
	if (isFirstRun()) {
		logger.info("Preparing for first run");
		if (setupSumFile()) {
			JOptionPane.showMessageDialog(null,"An unknown error has occured setting up the application for first time use");
			logger.error("Unknown error in main");
			return;
		}
	}
    window w = new window();
  }
  private static boolean isFirstRun() {
	  return !(new File("C:/VoidCrypt/sums.info").exists());
  }
  private static boolean setupSumFile() throws Exception {
	  File sumFile = new File("C:/VoidCrypt/sums.info");
	  if(!sumFile.exists()) {
		  sumFile.createNewFile();
	  }
	  return isFirstRun();
  }
}