package lecture7;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSA {
	Cipher cipher;
	KeyFactory factory;
	KeyPairGenerator keyPairGenerator;
	
	/*
	 * initialize cipher object with RSA/ECB/OAEPWithSHA-256AndMGF1Padding algorithm
	 * initialize key factory for RSA algorithm
	 * initialize key pair generator for RSA algorithm with 2048 bit keys
	 */
	RSA() throws Exception {
		
		cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		factory = KeyFactory.getInstance("RSA");
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
	}
	
	// return the modulus of a key pair 
	static BigInteger getModulus(KeyPair keyPair) {
		
		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
		BigInteger modulus = pubKey.getModulus();
		return modulus;
	}
	
	// return the public exponent of a key pair 
	static BigInteger getPublicExponent(KeyPair keyPair) {
		
		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
		BigInteger pubExponent = pubKey.getPublicExponent();
		return pubExponent;
	}
	
	// return the private exponent of a key pair 
	static BigInteger getPrivateExponent(KeyPair keyPair) {
		
		RSAPrivateKey privKey = (RSAPrivateKey) keyPair.getPrivate();
		BigInteger privExponent = privKey.getPrivateExponent();
		return privExponent;
	}
	
	// return the public key of a key pair object encoded as a string
	static String getPublicKey(KeyPair keyPair) {
		
		PublicKey pubKey = keyPair.getPublic();
		byte[] pub = pubKey.getEncoded();
		return Base64.getEncoder().encodeToString(pub);
	}
	
	// return the private key of a key pair object encoded as a string
	static String getPrivateKey(KeyPair keyPair) {
		
		PrivateKey privKey = keyPair.getPrivate();
		byte[] priv = privKey.getEncoded();
		return Base64.getEncoder().encodeToString(priv);
	}
	
	// return a public key given a modulus and a public exponent
	public PublicKey loadPublicKey(BigInteger modulus, BigInteger publicExponent) throws Exception {
		
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
		return factory.generatePublic(keySpec);
	}
	
	// return a private key given a modulus and a private exponent
	public PrivateKey loadPrivateKey(BigInteger modulus, BigInteger privateExponent) throws Exception {
		
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
		return factory.generatePrivate(keySpec);
	}

	// return a public key given its string encoding
	public PublicKey loadPublicKey(String key) throws Exception {
		
		byte[] pub = Base64.getDecoder().decode(key);
		KeySpec spec = new X509EncodedKeySpec(pub);
		return factory.generatePublic(spec);
	}
	
	// return a private key given its string encoding
	public PrivateKey loadPrivateKey(String key) throws Exception {
		
		byte[] priv = Base64.getDecoder().decode(key);
		KeySpec spec = new PKCS8EncodedKeySpec(priv);
		return factory.generatePrivate(spec);
	}

	// generate a key pair 
	public KeyPair buildKeyPair() throws NoSuchAlgorithmException {    
		
		return keyPairGenerator.genKeyPair();
	}

	// encrypt a plaintext message string using a public key and return the ciphertext as a string
	public String encrypt(PublicKey publicKey, String message) throws Exception { 
		
		byte [] byteMessage = message.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, publicKey); 
		byte[] enByteMessage = cipher.doFinal(byteMessage);
		return Base64.getEncoder().encodeToString(enByteMessage);
		
	}
 
	// decrypt a ciphertext string using a private key and return the plaintext message as a string
	public String decrypt(PrivateKey privateKey, String ciphertext) throws Exception {
		
		byte[] cipherBytes = Base64.getDecoder().decode(ciphertext);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] deByteMessage = cipher.doFinal(cipherBytes);
		return new String(deByteMessage, StandardCharsets.UTF_8);
	}
	

	
	public static void main(String [] args) throws Exception {
		// generate public and private keys
		RSA rsa = new RSA();

		KeyPair keyPair = rsa.buildKeyPair(); 
		
		System.out.println("============ test 1 =============\n");
		
		System.out.printf("Modulus: %d\n\nPublic exponent: %d\n\nPrivate exponent: %d\n\n", 
				getModulus(keyPair), 
				getPublicExponent(keyPair), 
				getPrivateExponent(keyPair)); 
		 
		System.out.printf("public key: %s\n\nprivate key: %s\n\n", 
				getPublicKey(keyPair),
				getPrivateKey(keyPair));		
		
		// encrypt the message
		String ciphertext = rsa.encrypt(keyPair.getPublic(), "This is a secret message");  
		System.out.printf("ciphertext: %s\n\n", ciphertext);  // <<encrypted message>> 
		
		// decrypt the message
		String secret = rsa.decrypt(keyPair.getPrivate(), ciphertext);      
		System.out.printf("decrypted text: %s\n\n", secret);     // This is a secret message
		
		
		System.out.println("============ test 2 =============\n");
		
		// encrypt the message
		ciphertext = "GQAkFOkoMiQej1SVsXukzcQridW6gdULMBLyINLDCheueTIWROa1jxlrVOatJanfzW87qBrOogzARFUiAz/TOIzQJrsymK5PnLUEyope/fpjzk4TW0SDDxq8awR0qZjX6iNASA2OUgJYfiuuDeZW7DhOo06d80oUJuX7f6CiYD5L9L7e60a19JFGKddd/S6JGx/trkjkZyt8+yw0Ii9spCt9xIokVFCnC9s/krw8tlcK6lLcNphXMc7aleRwj5j44qpgabxQJN6YFtb6bgSWlkCD0k0K6Tlz8cAojRUJZhcOc86Z4XH2pQA+K06tN59PNjtmJxQ689ATdnWSbtuz4g==";
				  
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC8ypLGuH9PLy4hz/wA4hQj8ZIZ2Hz7JylexnrKB1Zxv9IeAG84kxOYrI9ZghphEfaLUtrPeINApk6mEnZOD3GDNkeoADZRjy+jxC+3GWdBrmpkEZK80b4HkBWOSFWyRoigZU+1LP3OOk/uVAUQHjzF4fYD9K8tZ0sd108h3ysjyc6hr2uTBQ5HeJhJdGeLdS9A+8nFLyPWGv4TPqnNscBCDlrIgWerVpr0s46TThnFauFT1RLWPqiSbmTuRLGEjkhLpzfvPX1MgGocL9wU8MOYEiOvI4vbtbIlOPVY/RtdXDVr5PwEUZcNvM3QYnpl2JI60v12nGXYm3do0ckOHYQtAgMBAAECggEBAK5aczU9fK23h+ZIO5bhM/KCDgj6i2ozn5xct1fPcQE6QyXcBtt70TRF4VmrswsE2OmQym2jAax6KtZq5IFw98dTINhB4ehu0XNwIxT5TAg3uR/Tru3c0qAPbCjsGuC7DUD8b6GF/Jwx9FHJnKdnFX83EMpUdlf7Nj9H8lf4z9GK3egz9nkYbvyd8y5g6P/5OICyo/Yb7fYziedS5FCU7ZEd8XpZVsod5ZZ5KZkOPatToGJgM8Px7mxxsPDK/BONI1t8I+wXYr08I9/7CATRG7UiTUnNaC1T0eTEpMj7rh8K1VSBmIJZlPiPTADsYY0l9aXuJP3Z5/BIfeKy1pfyaG0CgYEA8ii+w0YUpyozntU/mjwWYwpp88x7CbXm4TYDqq+xc0efNOoiHj6Plw/ToJsdxA3sIg+wwKbCpmQGthF1KBew1hKSobFZ+o10HJrrGHnrhKrGZKxIyyUgdgYKK6ISPQ/7qQn8r/vEeqhW4swPlnwYa1db6KH1YhjL3+iR/GbP8O8CgYEAx5T0IZ+wQFDC9KOiDWx2APp9syqtaRJhAYHhEkNJ7yW4+Vzqdo2tUBXkrd48fwrX7kDg2mnlspguBuewjzJLyjSmXVFrgwgBhEQMh+Iol0D73PCUVmkNqA9nZB+8HythHRLHgAu0xnlnPDzx0DwHJ/EzIduoOrz850WBMniCpKMCgYEAkkvNW5m2Fnunbfi1EQ/mEnWTJwfM+UPctQh11KOKF/QYGdatQApZo1CbcLbll7E1jCpghTL5+54Ic+w+FDejzOCxl1/W1Uip8jIhf8sAX0jwzJdM7vWCE+t4L34deKh0SF5RO1xmChgzTZcsf6R++DJxwyw9W8NRP/d4Yr0WOR8CgYEAnhIpcWAKtkXAwwW+2abnp0q9t+O7SJbX4du+KTrGGfTklguzX4ZCmEVewx5bB7vM3dTgwdxRoqvI+IsUWbXaNSKSCSLtZfA3U0yaKB1FTNaZJJoewnmd6VeZ6cEAvCcdWWuAOrgKy49n5lvWobiJamcsukpA6ZS5MWrpMtIjlsUCgYAN6z2tLHJYfBHW3ym+ZBO03i1kylIuXX/S9XO3bwhYNDF+rT5xa/cBXbsLiBl4+s2a3YOgmWnh5Nz4PTtKgyXUm9gI+6HigDDf6Pu/PM2hYYjGAxJycrKJ/Wg5zX0PKj8kebqnPFURKArCsO/An65ed0GZJ/2ZaXST/H8gzJJoCw==";
				
		// decrypt the message
		secret = rsa.decrypt(rsa.loadPrivateKey(privateKey), ciphertext);      
		
		System.out.printf("decrypted text: %s\n\n", secret);     // This is a secret message
		
		System.out.println("============ test 3 =============\n");
		
		String modulus = "18694170902812379439224396366036618975087036932342596342114016626506342732206598983678783995172555625277822049263896719947072503182171904471905033641547701355590623070520026128520048349384408203727632196904765126145922172542029099469109457176444803083919434850940490161080646701179253993966676190947444424522802676236361888906813892133443501484084982283185687704600701830163258874995337377443233355927229715267144164204821626447066761907173881018530436002646108875014680953801110896146430930420285654633930955743332382203821205484497727502636190622636126176544525315926411856699429655429265972682284364395032933547793"; 
		String privateExponent = "6598311813538244978991084986178175231269562816713443376806711210527560310685613435013162509366183327340686629592023725923000459947968330778706795217911735779140367927541224718936261324411555771070198330390482123045111489620248365486968276903207671772238954590108723597450562575212147388321667968460507567146114772784964924086719405832762974549811726151811461446737628380130889859632269140319339794753766843512034898776295782131016908779842987002431571452617189372808218434943780844213437857188360670494336950135174333082231755678039492863087141462911406980467712242906464866151883754892620600620095351615180177855041";
 
		ciphertext = "c0/7/jUt/e4TkF+gvbPcwyPR3mljiGLXFtNGaouEzF7wuzL2lizTqzkjDUHGl4P9ABkT0j5rpZ5AgRsgogTkorwTaneSp0hetlrLMlGdv/w49wi6B+2FiOs+ABtgQv2vao2x85TK/IhlD56rGeOU2xVBzAWqfMxuyxanOca49lXV1RhutCOQV94zq9eGn3bfRgyeZolLlSjnv80ECPNBhBoNt4eUPNLIpRv5ZYtPvUwafc6e8bwkQ2Y5g6q1EChXwdNJm/RVDVG9iobbOK1Mhb8krg0W9sLHBYu6RanzkNLG9ogFYtFDEn+aWLtHmheeZhretpniYRVV2yXsO32FDw==";
		
		secret = rsa.decrypt(rsa.loadPrivateKey(new BigInteger(modulus), new BigInteger(privateExponent)), ciphertext);
		
		System.out.printf("decrypted text: %s\n\n", secret);     // This is a secret message
	}

}

