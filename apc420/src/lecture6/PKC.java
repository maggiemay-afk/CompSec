/**
 * Name: Maggie Herms Email: mkherms@uwm.edu
 */
package lecture6;

import java.math.BigInteger; // for generating prime numbers and for large integer arithmetics
import java.nio.charset.StandardCharsets;
import java.util.Base64;     // for encoding of byte array to string and decode string to byte array
import java.util.Random;     // need this class when generating random prime numbers

/*
 * This class implements the RSA algorithm for public-key cryptography using BigInteger class.
 */
public class PKC {
	// modulus = p * q
	// private_key = public_key^-1 mod (p-1)*(q-1) <= "private key" * "public key" mod (p-1)*(q-1) = 1
	BigInteger p, q, public_key, private_key, modulus;
	
	
	/* initialize p and q with two prime numbers of 1024 bits long using BigInteger's constructor
	// the likelihood that p or q is not a prime number is <= 2^-10
	// also initialize modulus as p * q 
	*/
	PKC() { 
		
		p = new BigInteger(1024, 40, new Random()); //chance of this not being prime is 1 - .5^40
		q = new BigInteger(1024, 40,  new Random());
		
		modulus = p.multiply(q);
	}
	
	
	/* initialize the public and private key pair
	// the public key is a 20 bit long prime number (probability of not prime is <= 2^-10
	// private key is the multiplicative inverse of the public key modulo (p-1)*(q-1)
	*/
	void genKeyPair() {
		
		public_key = new BigInteger(20, 40, new Random());
		
		BigInteger y = new BigInteger("1");
		BigInteger phiP = p.subtract(y);
		BigInteger phiQ = q.subtract(y);
		
		private_key = public_key.modInverse(phiP.multiply(phiQ)); 
	}
	
	
	/* encrypt a plaintext message as a string using the public key 
	// return the ciphertext as a string (using Base64 encoding)
	//
	// String --> bytearray --> BigInteger -- encrypt --> BigInteger --> bytearray -- encode --> String
	*/
	String encrypt(String m) {
		
		byte [] byteMessage = m.getBytes(); 
		BigInteger bigInt = new BigInteger(byteMessage); 
		
		BigInteger enBigInt = encrypt(bigInt); //encrypt
		byte [] enByteMessge = enBigInt.toByteArray();
		
		return Base64.getEncoder().encodeToString(enByteMessge);
	}
	
	
	/* decrypt a ciphertext using the private key 
	// return the plaintext as string
	//
	// String -- decode --> bytearray --> BigInteger -- decrypt --> BigInteger --> bytearray --> String
	 */
	String decrypt(String c) {
		
		byte[] cipher = Base64.getDecoder().decode(c);
		BigInteger bigInt = new BigInteger(cipher);
		
		BigInteger deBigInt = decrypt(bigInt);
		byte [] decryptedBytes = deBigInt.toByteArray();
		
		return new String(decryptedBytes, StandardCharsets.UTF_8); 
	}
	
	
	// encrypt a plaintext message as a BigInteger using the public key
	BigInteger encrypt(BigInteger m) {
		
		return m.modPow(public_key, modulus);
	}
	
	
	// decrypt a ciphertext message as a BigInteger using the private key
	BigInteger decrypt(BigInteger c) {
		
		return c.modPow(private_key, modulus);
	}
	
	
	public static void main(String[] args) {
		PKC pkc = new PKC(); 
		pkc.genKeyPair();
		System.out.printf("p: %d\n\nq: %d\n\npublic key: %d\n\nprivate key: %d\n\nmodulus: %d\n\n", 
				pkc.p, pkc.q, pkc.public_key, pkc.private_key, pkc.modulus); 
		
		BigInteger m = new BigInteger("438709437431787");
		BigInteger c = pkc.encrypt(m);
		
		System.out.printf("plaintext: %d\n\nciphertext: %d\n\ndecrypted text: %d\n\n", m, c, pkc.decrypt(c));
		
		String plaintext = "test message";
		String ciphertext = pkc.encrypt(plaintext);
		
		System.out.printf("plaintext: %s\n\nciphertext: %s\n\ndecrypted text: %s\n\n", plaintext, ciphertext, pkc.decrypt(ciphertext));
	}
}
