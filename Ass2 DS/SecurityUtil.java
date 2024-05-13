import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for handling encryption and decryption operations.
 */
class SecurityUtil {

    /**
     * Generates a AES secret key based on a password, salt, and iteration count.
     * Uses PBKDF2WithHmacSHA256 algorithm for generating a robust key.
     *
     * @return SecretKeySpec The generated AES key specification.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws InvalidKeySpecException If the specified key specification is inappropriate.
     */
    public static SecretKeySpec generateAESKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] password = "MySecretKey".toCharArray(); // Password for key generation
        byte[] salt = "salt".getBytes(); // Salt enhances the security of the key against dictionary attacks
        int iterationCount = 65536; // High iteration count makes it difficult to brute force
        int keyLength = 128; // Key length in bits (128 bits is common for AES)

        // Specification for the key generation
        KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);

        // Factory to create the secret key using the specified algorithm
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // Generating the secret key from the specified algorithm, key spec, and returning it as AES key spec
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Encrypts plaintext data using AES encryption algorithm.
     *
     * @param data The plaintext string to encrypt.
     * @param cipher The Cipher instance initialized for encryption.
     * @param secretKey The AES key used for encryption.
     * @return String The encrypted data encoded in Base64.
     * @throws InvalidKeyException If the key is invalid.
     * @throws IllegalBlockSizeException If the block size is illegal.
     * @throws BadPaddingException If the padding is bad.
     */
    public static String encrypt(String data, Cipher cipher, SecretKeySpec secretKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Initialize the cipher for encryption with the secret key
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the data and encode the result using Base64
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts encrypted data back into plaintext using AES decryption algorithm.
     *
     * @param data The encrypted string to decrypt.
     * @param cipher The Cipher instance initialized for decryption.
     * @param secretKey The AES key used for decryption.
     * @return String The decrypted data.
     * @throws InvalidKeyException If the key is invalid.
     * @throws IllegalBlockSizeException If the block size is illegal.
     * @throws BadPaddingException If the padding is bad.
     * @throws IOException If an input or output exception occurred.
     */
    public static String decrypt(String data, Cipher cipher, SecretKeySpec secretKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // Initialize the cipher for decryption with the secret key
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the data and construct a new string
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedBytes, 0, decryptedBytes.length);
    }
}
