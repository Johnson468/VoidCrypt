package tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import default_package.AES;
import org.junit.Test;

public class hash_test {

	@Test
	public void hash_Equals() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		System.out.println(AES.hash("tester"));
		assertEquals("��\\S�T^�K�aS�����N�W@����",AES.hash("tester"));
	}

}
