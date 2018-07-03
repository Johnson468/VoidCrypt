package default_package;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class window implements java.awt.event.ActionListener
{
	Logger logger;
	JFrame window;
	String filePath = null;
	JFileChooser fc = new JFileChooser();
	javax.swing.JLabel label = null;
	shredButton sb;
	encryptButton eb;
	decryptButton db;
	selectNewButton snb;
	deleteLogButton dlb;
	AES aes;
	//Constructor to set up values and initialize objects
	public window() {
		window = new JFrame("VoidCrypt");
		window.setSize(700, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(3);
		panel pan = new panel();
		label = new javax.swing.JLabel("Select a file");
		pan.add(label);
		fc.setSize(700, 500);
		pan.add(fc);
		fc.addActionListener(this);
		sb = new shredButton("Shred");
		eb = new encryptButton();
		db = new decryptButton();
		snb = new selectNewButton();
		dlb = new deleteLogButton();
		setVisibles(false);
		sb.addActionListener(this);
		eb.addActionListener(this);
		db.addActionListener(this);
		snb.addActionListener(this);
		dlb.addActionListener(this);
		pan.add(snb);
		pan.add(sb);
		pan.add(eb);
		pan.add(db);
		pan.add(dlb);
		window.add(pan);
		window.setVisible(true);
		logger = Logger.getLogger(window.getClass());
	}
	/*
	 * This action listener contains multiple possible paths a user can take
	 * Each "branch" has its own procedure behind it
	 * All functions called are below the listener, unless contained in another class
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)  {
		String reenteredPass;
		JOptionPane confirmPane = new JOptionPane();
		/*
		 * Check if the source of the click is the file selector
		 * Make sure the file selector has a selected value and it's not null
		 * If so set the filepath to the selected file
		 * Set variables
		 */
		if ((e.getSource() == fc)) {
			filePath = fc.getSelectedFile().getPath().replace("\\", "/");
			setLabel(filePath + " selected.");
			setVisibles(true);
		}else if (fc.getSelectedFile() == null && !e.getActionCommand().equals("delete_logs")) {
			JOptionPane.showMessageDialog(this.window,"Please select a file");
		}
		/*
		 * Check if the shred button is clicked
		 * If so, make sure the user wants to destroy the file
		 * Create a new AES object passing it special params 
		 * Call the shred function of the AES object and check if the deletion
		 * was successful or not. Either way the file is encrypted 10000+ times 
		 * with no recoverable key.
		 */
		if ((e.getActionCommand().equals("shred")) && (filePath != null))
		{
			confirmPane.createDialog(window, "Are you sure?");
			if (JOptionPane.showConfirmDialog(window, "Are you sure you want to destroy " + filePath + " using military grade shreding algorithms?") == 0) {
				System.out.println("Yes");
				try {
					aes = new AES(filePath, true);
					if (aes == null) return;
					if (!AES.shred()) {
						JOptionPane.showMessageDialog(window, "File deletion failed");
					} else {
						JOptionPane.showMessageDialog(window, "File deleted and overwritten");
					}

				}
				catch (UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}
				catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					System.out.println("Please allow file access");
					logger.error(e1);
					logger.info("Permission Error");
					e1.printStackTrace();
				}
			}
		}
		/*
		 * Check if the encrypt button was clicked
		 * Make sure the file isn't already encrypted with VoidCrypt
		 * Get a password to encrypt the file with
		 * Create an AES object passing the filepath and the user's password
		 * Encrypt the file
		 * Store hashed filename and salted password in the sum file
		 * 
		 */
		else if ((e.getActionCommand().equals("encrypt")) && (filePath != null)) {
			System.out.println(filePath);
			//Check if the file is already encrypted with VoidCrypt
			try {
				if(isAlreadyVCEncrypted(filePath)) {
					JOptionPane.showMessageDialog(this.window,"This file is already encryped with VoidCrypt");
					logger.info("The selected file is already encrypted with VoidCrypt");
					return;
				}
			} catch (HeadlessException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (UnsupportedEncodingException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (NoSuchAlgorithmException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//End of try/catch block
			
			String password = null;
			//Make sure people don't enter different passwords
			try {
				do{
					password = JOptionPane.showInputDialog(window, "Enter a password to encrypt the file (You MUST remember this to decrypt)");
					reenteredPass = JOptionPane.showInputDialog(window, "Enter the password again");
					if (!password.equals(reenteredPass) || password.equals("")) {
						JOptionPane.showMessageDialog(window, "Passwords must match and not be empty");
					} else if (passTooShort(password)) {
						JOptionPane.showMessageDialog(this.window, "Passwords must be 8 characters or longer");
					}
				} while(!passIsSame(password,reenteredPass) || passTooShort(password));
			}
			catch (java.awt.HeadlessException e2) {
				e2.printStackTrace();
			}
			if (password != null) {
				try {
					aes = new AES(filePath, password);
					if (aes == null) return;
					AES.encrypt();
					storeHashedPass(AES.hash(password),filePath);
				}
				catch (UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}
				catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(window, "Please allow access to this application in order to protect your sensitive data");
					return;
				}
			}
			setVisibles(false);
		}
		/*
		 * Get the users password 
		 * Create an AES object passing it the filepath and the password
		 * Decrypt the file using the password
		 * Remove file info from the sumsfile
		 */
		else if (e.getActionCommand().equals("decrypt") && filePath != null) {
			String password = null;
			try {
				password = JOptionPane.showInputDialog(window, "Enter the password for this file");
			}
			catch (java.awt.HeadlessException e2) {
				e2.printStackTrace();
			}
			if (password != null) {
				//Check if the correct password is entered for the file
				try {
					if(!correctPass(password,filePath)){
						JOptionPane.showMessageDialog(this.window, "Incorrect password entered or this file is not encrypted with VoidCrypt");
						logger.info("Incorrect password entered or file is not encrypted with VoidCrypt");
						return;
					}
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				} catch (NoSuchAlgorithmException e2) {
					e2.printStackTrace();
				}
				try {
					aes = new AES(filePath, password);
					if (aes != null) {
						AES.decrypt();
					}
					removeFileInfo(filePath);
				}
				catch (UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}
				catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(window, "Please allow access to this application in order to protect your sensitive data");
					logger.info("Access error");
					return;
				}
			}
			setVisibles(false);
			//Show the file selector
		} else if (e.getActionCommand().equals("selectNew")) {
			setVisibles(false);
			//Delete the logs
		} else if (e.getActionCommand().equals("delete_logs")) {
			File logFile = new File("C:\\VoidCrypt\\logs\\log4j-application.log");
			logFile.delete();
			JOptionPane.showMessageDialog(this.window, "Logs successfully deleted");
		}
		//End of actionlistener
	}
	/*
	 * Set the visibilites of buttons and the file chooser 
	 * They're inverses of each other
	 */
	private void setVisibles(boolean sh)
	{
		sb.setVisible(sh);
		eb.setVisible(sh);
		db.setVisible(sh);
		snb.setVisible(sh);
		fc.setVisible(!sh);
		dlb.setVisible(!sh);
		if (sh) {
			window.setSize(400, 300);
		} else {
			window.setSize(700, 500);
		}
	}
	/*
	 * Easily set the labels text
	 */
	private void setLabel(String s)
	{
		label.setText(s);
	}
	private boolean passIsSame(String p1, String p2) {
		return p1.equals(p2) && !p1.equals("");
	}
	/*
	 * Hash the users password, the file name and store them 
	 * 
	 */
	@SuppressWarnings("static-access")
	private void storeHashedPass(String s,String filePath) throws NoSuchAlgorithmException {
		VoidFile sumFile = new VoidFile("C:\\VoidCrypt\\sums.info");
		File f = new File(filePath);
		String fileName = f.getName();
		try {
			FileWriter fw = new FileWriter(sumFile,true);
			AES aes = new AES();
			String hashedFileName = aes.hash(fileName);
			String salt = "";
			for (char c:hashedFileName.toCharArray()) {
				salt+=aes.hash(c + "");
			}
			salt=aes.hash(salt);
			s=aes.hash(s+salt);
			fw.write(hashedFileName + " " + s + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * @Params: 
	 * s- The password entered by the user
	 * filePath- The filepath of the file trying to be decrypted
	 * Hash the password and file name
	 * Scan the file until it finds a hash the matches the hashed file name
	 * @Return vals:
	 * If the next value after the hashed file name is the hashed pass, return true
	 * Default to return false
	 */
	@SuppressWarnings({ "static-access", "resource" })
	private boolean correctPass(String s, String filePath) throws FileNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException {
		AES aes = new AES();
		String hashedPass = aes.hash(s);
		File decryptFile = new File(filePath);
		String hashedFileName = aes.hash(decryptFile.getName());
		String salt = "";
		for(char c: hashedFileName.toCharArray()) {
			salt+= aes.hash(c + "");
		}
		salt=aes.hash(salt);
		File sumFile = new File("C:\\VoidCrypt\\sums.info");
		Scanner myScan = new Scanner(sumFile);
		while(myScan.hasNext()) {
			if(myScan.next().equals(hashedFileName)) {
				return myScan.next().equals(aes.hash(hashedPass + salt));
			}
		}
		;
		return false;
	}
	/*
	 * Check if the file selected has already been encrypted with VoidCrypt
	 * 
	 */
	@SuppressWarnings({ "static-access", "resource" })
	private boolean isAlreadyVCEncrypted(String filePath) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException {
		AES aes = new AES();
		File f = new File(filePath);
		String hashedFileName = aes.hash(f.getName());
		File sumFile = new File("C:\\VoidCrypt\\sums.info");
		Scanner myScan = new Scanner(sumFile);
		while(myScan.hasNext()) {
			if(myScan.next().equals(hashedFileName)) {
				return true;
			}
		}
		;
		return false;
	}
	/*
	 * Iterate through the file
	 * If the line doesn't contain the hashed file name, add that line and a newline to the sb variable
	 * If it does, add a blank and continue
	 * Write the contents to the sums file
	 * 
	 */
	@SuppressWarnings({ "static-access", "resource" })
	private void removeFileInfo(String filePath) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException {
		//Contents of the file
		String sb = "";
		File f = new File(filePath);
		File sumFile = new File("C:\\VoidCrypt\\sums.info");
		String fileName = f.getName();
		AES aes = new AES();
		String hashedFileName = aes.hash(fileName);
		Scanner myScan = new Scanner(sumFile);
		String line;
		while(myScan.hasNextLine()) {
			line=myScan.nextLine();
			if(!line.contains(hashedFileName)) {
				sb += line + "\n";
			} else {
				sb+="";
		}
		try {
			FileWriter fw = new FileWriter(sumFile,false);
			fw.write(sb);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
		
	}
	public boolean passTooShort(String s) {
		return s.length()<8;
	}
	
}