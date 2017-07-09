package tests;

import static org.junit.Assert.*;
import default_package.VoidFile;
import org.junit.Test;

public class extension_test {

	@Test
	public void notVCEncrypted() {
		VoidFile vf = new VoidFile("test.txt");
		assertEquals(false, vf.getIsVCEncrypted());
	}
	@Test
	public void VCEncrypted() {
		VoidFile vf = new VoidFile("test.VCtxt");
		assertEquals(true, vf.getIsVCEncrypted());
	}
	@Test
	public void ExtensionIstxt() {
		VoidFile vf = new VoidFile("test.txt");
		assertEquals("txt",vf.getExtension());
	}
	
}
