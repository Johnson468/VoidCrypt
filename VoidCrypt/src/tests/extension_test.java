package tests;

import static org.junit.Assert.*;
import default_package.VoidFile;
import org.junit.Test;

public class extension_test {

	
	@Test
	public void ExtensionIstxt() {
		VoidFile vf = new VoidFile("test.txt");
		assertEquals("txt",vf.getExtension());
	}
	
}
