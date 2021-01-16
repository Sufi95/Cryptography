

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
public class DESEncryption {
	private static Cipher encryptCipher;
	private static Cipher decryptCipher;
	private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };

	public static void main(String[] args) {
	    // Change the file paths from here
        String path1 = "/Users/abdallasuleimansufi/Documents/Files/10MB.pdf";
        String path2 = "/Users/abdallasuleimansufi/Documents/Files/500 MB.mp4";
        String path3 = "/Users/abdallasuleimansufi/Documents/Files/1 GB.mp4";
        String[] mypath = new String[3];
        mypath[0]= path1;
        mypath[1]= path2;
        mypath[2]= path3;

        for (int i =0; i<3; i++)
        {

        // Change the file from here

        String clearTextFile = mypath[i];
		String cipherTextFile = "cipherfile";
		String clearTextNewFile = "original";

        System.out.println("Algorithm: DES");
        File mine = new File(clearTextFile);
        double size = mine.length() / 1000;
        System.out.println("File Size: "+ size /  1000+" MB");


        try {
			// create SecretKey using KeyGenerator
			SecretKey key = KeyGenerator.getInstance("DES").generateKey();
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

			// get Cipher instance and initiate in encrypt mode
			encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			// get Cipher instance and initiate in decrypt mode
			decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			double startTimeEnc = Instant.now().toEpochMilli();
			// method to encrypt clear text file to encrypted file
			encrypt(new FileInputStream(clearTextFile), new FileOutputStream(cipherTextFile));

            double endTimeEnc = Instant.now().toEpochMilli() - startTimeEnc;

            System.out.println("Encryption Time: "+(endTimeEnc/1000));

            double startTimeDec = Instant.now().toEpochMilli();

					// method to decrypt encrypted file to clear text file
			decrypt(new FileInputStream(cipherTextFile), new FileOutputStream(clearTextNewFile));
            double endTimeDec = Instant.now().toEpochMilli() - startTimeDec;

            System.out.println("Decryption Time: "+(endTimeDec/1000));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IOException e) {
			e.printStackTrace();
		}
            System.out.println();

        }

	}

	private static void encrypt(InputStream is, OutputStream os) throws IOException {

	    // create CipherOutputStream to encrypt the data using encryptCipher
		os = new CipherOutputStream(os, encryptCipher);
 		writeData(is, os);
	}

	private static void decrypt(InputStream is, OutputStream os) throws IOException {

		// create CipherOutputStream to decrypt the data using decryptCipher
		is = new CipherInputStream(is, decryptCipher);
		writeData(is, os);
	}

	// utility method to read data from input stream and write to output stream
	private static void writeData(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		int numRead = 0;
		// read and write operation
		while ((numRead = is.read(buf)) >= 0) {
			os.write(buf, 0, numRead);
		}
		os.close();
		is.close();
	}

}
