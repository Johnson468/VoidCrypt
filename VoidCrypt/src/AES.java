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
  String pass;
  static Key key;
  static boolean isShread;
  
  public AES(String filePath, String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException, Exception
  {
    this.filePath = filePath.replace("\\", "/");
    this.pass = pass;
    key = keyGen(hash(pass));
  }
  /*
   * AES constructor for shred only
   */
  public AES(String filePath, boolean isShread)
    throws UnsupportedEncodingException, NoSuchAlgorithmException, Exception
  {
    this.filePath = filePath;
    isShread = isShread;
    SecureRandom rand = new SecureRandom(SecureRandom.getSeed(138002));
    key = keyGen(hash(String.valueOf(rand.nextLong())));
  }
  
  private static byte[] getFile() {
    File f = new File(filePath);
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
    for (int x = 0; x < 1000; x++) {
      byte[] content = getFile();
      try
      {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, key);
        encrypted = cipher.doFinal(content);
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
    for (int x = 0; x < 1000; x++) {
      byte[] textCryp = getFile();
      try
      {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, key);
        decrypted = cipher.doFinal(textCryp);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    saveFile(decrypted);
    JOptionPane.showMessageDialog(null, "Decryption complete");
  }
  
  private static void saveFile(byte[] bytes) throws IOException {
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
  
  private static String hash(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException { byte[] b = s.getBytes(StandardCharsets.UTF_8);
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    return new String(digest.digest(b));
  }
  
  public static boolean shread() throws Exception { encrypt();
    File f = new File(filePath.replace("\\", "/"));
    return f.delete();
  }
}