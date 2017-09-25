package default_package;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class AES
{
  static String filePath;


  static String pass;
  static Key key;
  static boolean isShread;
  
  public AES(String filePath, String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException, Exception
  {
    AES.filePath = filePath.replace("\\", "/");
    AES.pass = pass;
    key = keyGen(hash(pass));
  }
  /*
   * AES constructor for shred only
   */
  public AES(String filePath, boolean isShread)
    throws UnsupportedEncodingException, NoSuchAlgorithmException, Exception
  {
    AES.filePath = filePath;
    AES.isShread = isShread;
    SecureRandom rand = new SecureRandom(SecureRandom.getSeed((int)System.currentTimeMillis()));
    key = keyGen(hash(String.valueOf(rand.nextLong())));
  }
  public AES() {
	  
  }
  private static byte[] getFile() {
    VoidFile f = new VoidFile(filePath);
    InputStream is = null;
    try {
      is = new FileInputStream(f);
    }
    catch (FileNotFoundException e2) {
      e2.printStackTrace();
    }
    byte[] content = null;
    try {
      content = new byte[is.available()];
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      is.read(content);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return content;
  }
  
  public static void encrypt() throws Exception {
    byte[] encrypted = null;
    byte[] content = getFile();
    for (int x = 0; x < 1000; x++) {
      
      try
      {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, key);
        encrypted = cipher.doFinal(content);
        content = encrypted;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    saveFile(encrypted);
    
    if (!isShread) {
      JOptionPane.showMessageDialog(null, "Encryption complete");
    } else {
      JOptionPane.showMessageDialog(null, "Shread complete");
    }
    isShread = false;
  }
  
  public static void decrypt() throws Exception
  {
    byte[] decrypted = null;
    byte[] textCryp = getFile();
    for (int x = 0; x < 1000; x++) {
      try
      {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, key);
        decrypted = cipher.doFinal(textCryp);
        textCryp = decrypted;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    saveFile(decrypted);
    JOptionPane.showMessageDialog(null, "Decryption complete");
  }
  
  private static void saveFile(byte[] bytes) throws IOException {
	VoidFile vf = new VoidFile(filePath);
    FileOutputStream fos = new FileOutputStream(filePath);
    fos.write(bytes);
    fos.close();
  }
  
  private static Key keyGen(String k) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new javax.crypto.spec.PBEKeySpec(k.toCharArray(), k.getBytes(), 12, 128);
    SecretKey tmp = factory.generateSecret(spec);
    return new SecretKeySpec(tmp.getEncoded(), "AES");
  }
  
  public static String hash(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException { 
	  MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
      byte[] result = mDigest.digest(s.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < result.length; i++) {
          sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
      } 
      return sb.toString();
  }
  
  public static boolean shread() throws Exception { encrypt();
    VoidFile f = new VoidFile(filePath.replace("\\", "/"));
    return f.delete();
  }
}