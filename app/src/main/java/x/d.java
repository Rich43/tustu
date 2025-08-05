package X;

import W.C0178d;
import W.aj;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: TunerStudioMS.jar:X/d.class */
public class d implements b {

    /* renamed from: c, reason: collision with root package name */
    private static char[] f2202c = {'E', 'F', 'I', 'A', 'K', 'e', 'y', '!', '3', '8', '5', '4', '1', '5', '6', '6'};

    /* renamed from: a, reason: collision with root package name */
    String f2203a = new String(f2202c);

    /* renamed from: b, reason: collision with root package name */
    boolean f2204b = false;

    @Override // X.b
    public String a() {
        return "JavaSerialization";
    }

    @Override // X.b
    public void a(Object obj, File file) {
        if (!this.f2204b) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream2.writeObject(obj);
        objectOutputStream2.flush();
        objectOutputStream2.close();
        try {
            C0178d.a(file, a(byteArrayOutputStream.toByteArray()));
        } catch (Exception e2) {
            throw new IOException("Error encrypting Cache File: " + e2.getLocalizedMessage());
        }
    }

    @Override // X.b
    public Object a(File file) throws IOException {
        if (!this.f2204b) {
            return new ObjectInputStream(new BufferedInputStream(new FileInputStream(file))).readObject();
        }
        try {
            return new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(b(C0178d.a(file))))).readObject();
        } catch (Exception e2) {
            throw new IOException("Error decrypting Cache File: " + e2.getLocalizedMessage());
        }
    }

    private byte[] a(byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        String str = this.f2203a;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, new SecretKeySpec(str.getBytes("UTF-8"), "AES"), new IvParameterSpec("WWWWWWWWWXXXXXXX".getBytes("UTF-8")));
        return cipher.doFinal(bArr);
    }

    private byte[] b(byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, aj, InvalidKeyException, InvalidAlgorithmParameterException {
        String str = this.f2203a;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, new SecretKeySpec(str.getBytes("UTF-8"), "AES"), new IvParameterSpec("WWWWWWWWWXXXXXXX".getBytes("UTF-8")));
        try {
            return cipher.doFinal(bArr);
        } catch (BadPaddingException e2) {
            throw new aj("Invalid Password");
        } catch (IllegalBlockSizeException e3) {
            throw new aj("Invalid Password");
        }
    }

    @Override // X.b
    public void a(boolean z2) {
        this.f2204b = z2;
    }
}
