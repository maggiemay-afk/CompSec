package lecture4;

import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class OneTimePad {

	/*
	 * Encrypt the image in file x with a key in file y and save the result into the file z
	 * Encryption is based on one-time pad (file y)
	 */
	public void xor(String x, String y, String z) throws IOException {
		// Read data x, key y, and create a blank image of the same size as x
		BufferedImage a = ImageIO.read(new File(x)), 
				b = ImageIO.read(new File(y)), 
				c = new BufferedImage(a.getWidth(), a.getHeight(), 1);


		// For each pixel of x, perform XOR with the corresponding pixel in y and save it in z
		for (int i = 0; i < a.getHeight(); i++) {
			for (int j = 0; j < a.getWidth(); j++) {
				c.setRGB(j, i, a.getRGB(j, i) ^ b.getRGB(j, i));
			}
		}
		// Save the resulting file
		ImageIO.write(c, "png", new File(z));
	}

	/*
	 * Encrypt the image in file x with PRNG (Pseudo-random number generator) and 
	 * save the result into the file z.
	 * PRNG will use the provided seed and use java.util.Random class and 
	 * its nextInt() method to generate number sequence
	 */
	public void xorWithSeed(String x, int seed, String z) throws IOException {
		
		BufferedImage a = ImageIO.read(new File(x));
		BufferedImage b = new BufferedImage(a.getWidth(), a.getHeight(), 1);
		
		Random rand = new Random(seed);
		
		for (int i = 0; i < a.getHeight(); i++) {
			for (int j = 0; j < a.getWidth(); j++) {
				b.setRGB(j, i, a.getRGB(j, i) ^ rand.nextInt());
			}
		}
		
		ImageIO.write(b, "png", new File(z));
	}


	public static void main(String[] args) throws IOException  {
		OneTimePad pad = new OneTimePad();  
		int seed = 133;

		// The following statements demonstrate how xor method works
		pad.xor("420.png", "key.png", "cipher.png");
		pad.xor("cipher.png", "key.png", "message.png");

		
		pad.xorWithSeed("420.png", seed, "cipher.png");
		// If your implementation is correct, 
		// the following call should decrypt the message and save the image to file "message.png"
		pad.xorWithSeed("cipher.png", seed, "message.png");
	}
}
