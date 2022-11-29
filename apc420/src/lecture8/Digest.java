package lecture8;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Digest {
	/*
	 * print out the hash digest of the string 
	 * "test message to be hashed" using the SHA-256 algorithm
	 */
	public static void main(String[] args) {
		try {
			
			MessageDigest messDigest = MessageDigest.getInstance("SHA-256");
			String message = "test message to be hashed";
			
			messDigest.update(message.getBytes(StandardCharsets.UTF_8));
			
			byte[] encodedBytes = messDigest.digest();
			
			String encoded = Base64.getEncoder().encodeToString(encodedBytes);
			
			System.out.println("The digest of \"test message to be hashed\" is:");
			System.out.println(encoded);
			
			
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getStackTrace());
			
		}
		
	}
}
