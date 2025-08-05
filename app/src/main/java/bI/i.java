package bI;

import bH.C;
import bH.C0995c;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* loaded from: TunerStudioMS.jar:bI/i.class */
public class i {

    /* renamed from: a, reason: collision with root package name */
    static PublicKey f7102a = null;

    public static PublicKey a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            try {
                return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(C0995c.c(bufferedReader.readLine())), new BigInteger(C0995c.c(bufferedReader.readLine()))));
            } catch (Exception e2) {
                throw new IOException("Error reading key, " + e2.getMessage());
            }
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e3) {
            }
        }
    }

    private static PublicKey a() {
        if (f7102a == null) {
            f7102a = a(h.a().b());
        }
        return f7102a;
    }

    public static byte[] a(byte[] bArr) {
        return a(bArr, a());
    }

    public static byte[] a(byte[] bArr, Key key) throws IOException {
        String message;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, key);
            return cipher.doFinal(bArr);
        } catch (InvalidKeyException e2) {
            Logger.getLogger(i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            message = e2.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (NoSuchAlgorithmException e3) {
            Logger.getLogger(i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            message = e3.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (BadPaddingException e4) {
            Logger.getLogger(i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            message = e4.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (IllegalBlockSizeException e5) {
            Logger.getLogger(i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            message = e5.getMessage();
            if (bArr != null) {
                C.c(C0995c.d(bArr));
            }
            throw new IOException("Failed to encrypt data: " + message);
        } catch (NoSuchPaddingException e6) {
            Logger.getLogger(i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
            message = e6.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        }
    }

    public static byte[] b(byte[] bArr) {
        return b(bArr, a());
    }

    public static byte[] b(byte[] bArr, Key key) throws IOException {
        String message;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, key);
            return cipher.doFinal(bArr);
        } catch (InvalidKeyException e2) {
            message = e2.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (NoSuchAlgorithmException e3) {
            message = e3.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (BadPaddingException e4) {
            message = e4.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (IllegalBlockSizeException e5) {
            message = e5.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        } catch (NoSuchPaddingException e6) {
            message = e6.getMessage();
            throw new IOException("Failed to encrypt data: " + message);
        }
    }
}
