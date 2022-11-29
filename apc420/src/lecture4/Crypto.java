/**********************************************
 * Name: Maggie Herms
 * Email: mkherms@uwm.edu
 * 
 * Decrypted Message: The eagle has landed!
 * Key: mpwd
 * 
 **********************************************/
package lecture4;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto { 
	Cipher cipher;
	
	/*
	 * AES is the standard symmetric key algorithm
	 */
	Crypto() throws Exception {
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}
	
	private SecretKeySpec makeKey(String myKey) throws Exception {
		SecretKeySpec secretKey; 

		byte[] key = myKey.getBytes("UTF-8"); 
		key = MessageDigest.getInstance("SHA-1").digest(key);
		key = Arrays.copyOf(key, 16);
		secretKey = new SecretKeySpec(key, "AES"); 
		return secretKey;
	}
	/*
	 * Encrypt a string using a key
	 */
	public String encrypt(String strToEncrypt, String key) throws Exception { 
		cipher.init(Cipher.ENCRYPT_MODE, makeKey(key));
		return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))); 
	}

	/*
	 * Decrypt a string using a key
	 */
	public String decrypt(String strToDecrypt, String key) throws Exception {  
		cipher.init(Cipher.DECRYPT_MODE, makeKey(key));
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))); 
	}


	/*
	 * If successfully guessed the password, print the password and the decrypted message
	 * Otherwise admit failure
	 *
	 * Use the class Guess to generate encryption keys of fixed size
	 *
	 * Use str.matches("^[a-zA-Z0-9\\-#\\.\\!\\;\\,\\(\\)\\/%&\\s]*$")) to check whether a string can possible be plaintext
	 */
	public void guess(String encryptedString, int keyLength) {
		
		Guess g = new Guess(keyLength);
		String keyGuess = g.gen();
		String decryptMessage;
		
		while (keyGuess != null) {
			try {
				decryptMessage = decrypt(encryptedString, keyGuess);
				if (decryptMessage.matches("^[a-zA-Z0-9\\-#\\.\\!\\;\\,\\(\\)\\/%&\\s]*$")) {
					System.out.println(keyGuess);
					System.out.println(decryptMessage);
					break;
				} else {
					System.out.println(keyGuess);
				}
			} catch (Exception e) {
				System.out.println(keyGuess);
			}
			
			keyGuess = g.gen();
		}
		
	}


	public static void main(String[] args) {
		int keyLength = 4;

		// I give you this ciphertext and it is your job to crack it
		String cipherText = "dYFBRNYUc7+5/rBHJ/iENbUOBkzrxzHk88lLYL90JTE=";
		 
		try {
			Crypto c = new Crypto();
			c.guess(cipherText, keyLength);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}

/*
 * This class is used to generate strings of fixed size. 
 */
class Guess {
	char[] alphabet; // stores all possible alphabets as a character array
	int keyLength;   // how long is the key
	int x = 0;       // internal count of the number of strings generated
	int max;         // maximum number of strings to be generated

	/*
	 * keyLength: length of the key strings to be generated
	 */
	Guess(int keyLength) {
		alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();  // use English's 26 alphabets 
		this.keyLength = keyLength;                             // save key length
		this.max = (int) Math.pow(alphabet.length, keyLength) - 1;  // maximum number of guesses
	}

	/*
	 * Generate a new string of keyLength long
	 * Returns null if all strings have been generated
	 */
	String gen() {
		String ret = null;

		if(x < max) {
			char[] c = new char[keyLength];
			int[] y = toDigits(x);
			for(int i = 0; i<keyLength; i++) {
				c[i] = alphabet[y[i]];
			}
			ret = new String(c);
			x = x + 1;
		}
		return ret;
	}

	/*
	 * Convert a number x into digits using the size of the alphabet as base
	 */
	int[] toDigits(int x) {
		int[] ret = new int[keyLength]; 
		for(int i = 1; i <= keyLength; i++) {
			ret[keyLength - i] = x % alphabet.length; 
			x = x / alphabet.length;
		}
		return ret;
	}
}


