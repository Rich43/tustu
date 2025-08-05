package sun.security.pkcs11.wrapper;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/PKCS11.class */
public class PKCS11 {
    private static final String PKCS11_WRAPPER = "j2pkcs11";
    private final String pkcs11ModulePath;
    private long pNativeData;
    private static final Map<String, PKCS11> moduleMap;

    public static native long freeMechanism(long j2);

    private static native void initializeLibrary();

    private static native void finalizeLibrary();

    private native void connect(String str, String str2) throws IOException;

    private native void disconnect();

    native void C_Initialize(Object obj) throws PKCS11Exception;

    public native void C_Finalize(Object obj) throws PKCS11Exception;

    public native CK_INFO C_GetInfo() throws PKCS11Exception;

    public native long[] C_GetSlotList(boolean z2) throws PKCS11Exception;

    public native CK_SLOT_INFO C_GetSlotInfo(long j2) throws PKCS11Exception;

    public native CK_TOKEN_INFO C_GetTokenInfo(long j2) throws PKCS11Exception;

    public native long[] C_GetMechanismList(long j2) throws PKCS11Exception;

    public native CK_MECHANISM_INFO C_GetMechanismInfo(long j2, long j3) throws PKCS11Exception;

    public native long C_OpenSession(long j2, long j3, Object obj, CK_NOTIFY ck_notify) throws PKCS11Exception;

    public native void C_CloseSession(long j2) throws PKCS11Exception;

    public native CK_SESSION_INFO C_GetSessionInfo(long j2) throws PKCS11Exception;

    public native byte[] C_GetOperationState(long j2) throws PKCS11Exception;

    public native void C_SetOperationState(long j2, byte[] bArr, long j3, long j4) throws PKCS11Exception;

    public native void C_Login(long j2, long j3, char[] cArr) throws PKCS11Exception;

    public native void C_Logout(long j2) throws PKCS11Exception;

    public native long C_CreateObject(long j2, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native long C_CopyObject(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native void C_DestroyObject(long j2, long j3) throws PKCS11Exception;

    public native void C_GetAttributeValue(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native void C_SetAttributeValue(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native void C_FindObjectsInit(long j2, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native long[] C_FindObjects(long j2, long j3) throws PKCS11Exception;

    public native void C_FindObjectsFinal(long j2) throws PKCS11Exception;

    public native void C_EncryptInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native int C_Encrypt(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native int C_EncryptUpdate(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native int C_EncryptFinal(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native void C_DecryptInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native int C_Decrypt(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native int C_DecryptUpdate(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native int C_DecryptFinal(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native void C_DigestInit(long j2, CK_MECHANISM ck_mechanism) throws PKCS11Exception;

    public native int C_DigestSingle(long j2, CK_MECHANISM ck_mechanism, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native void C_DigestUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native void C_DigestKey(long j2, long j3) throws PKCS11Exception;

    public native int C_DigestFinal(long j2, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native void C_SignInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native byte[] C_Sign(long j2, byte[] bArr) throws PKCS11Exception;

    public native void C_SignUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native byte[] C_SignFinal(long j2, int i2) throws PKCS11Exception;

    public native void C_SignRecoverInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native int C_SignRecover(long j2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native void C_VerifyInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native void C_Verify(long j2, byte[] bArr, byte[] bArr2) throws PKCS11Exception;

    public native void C_VerifyUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception;

    public native void C_VerifyFinal(long j2, byte[] bArr) throws PKCS11Exception;

    public native void C_VerifyRecoverInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception;

    public native int C_VerifyRecover(long j2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception;

    public native byte[] getNativeKeyInfo(long j2, long j3, long j4, CK_MECHANISM ck_mechanism) throws PKCS11Exception;

    public native long createNativeKey(long j2, byte[] bArr, long j3, CK_MECHANISM ck_mechanism) throws PKCS11Exception;

    public native long C_GenerateKey(long j2, CK_MECHANISM ck_mechanism, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native long[] C_GenerateKeyPair(long j2, CK_MECHANISM ck_mechanism, CK_ATTRIBUTE[] ck_attributeArr, CK_ATTRIBUTE[] ck_attributeArr2) throws PKCS11Exception;

    public native byte[] C_WrapKey(long j2, CK_MECHANISM ck_mechanism, long j3, long j4) throws PKCS11Exception;

    public native long C_UnwrapKey(long j2, CK_MECHANISM ck_mechanism, long j3, byte[] bArr, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native long C_DeriveKey(long j2, CK_MECHANISM ck_mechanism, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception;

    public native void C_SeedRandom(long j2, byte[] bArr) throws PKCS11Exception;

    public native void C_GenerateRandom(long j2, byte[] bArr) throws PKCS11Exception;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.security.pkcs11.wrapper.PKCS11.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                System.loadLibrary(PKCS11.PKCS11_WRAPPER);
                return null;
            }
        });
        initializeLibrary();
        moduleMap = new HashMap();
    }

    public static void loadNative() {
    }

    PKCS11(String str, String str2) throws IOException {
        connect(str, str2);
        this.pkcs11ModulePath = str;
    }

    public static synchronized PKCS11 getInstance(String str, String str2, CK_C_INITIALIZE_ARGS ck_c_initialize_args, boolean z2) throws PKCS11Exception, IOException {
        PKCS11 synchronizedPKCS11 = moduleMap.get(str);
        if (synchronizedPKCS11 == null) {
            if (ck_c_initialize_args != null && (ck_c_initialize_args.flags & 2) != 0) {
                synchronizedPKCS11 = new PKCS11(str, str2);
            } else {
                synchronizedPKCS11 = new SynchronizedPKCS11(str, str2);
            }
            if (!z2) {
                try {
                    synchronizedPKCS11.C_Initialize(ck_c_initialize_args);
                } catch (PKCS11Exception e2) {
                    if (e2.getErrorCode() != 401) {
                        throw e2;
                    }
                }
            }
            moduleMap.put(str, synchronizedPKCS11);
        }
        return synchronizedPKCS11;
    }

    public String toString() {
        return "Module name: " + this.pkcs11ModulePath;
    }

    protected void finalize() throws Throwable {
        disconnect();
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/PKCS11$SynchronizedPKCS11.class */
    static class SynchronizedPKCS11 extends PKCS11 {
        SynchronizedPKCS11(String str, String str2) throws IOException {
            super(str, str2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        synchronized void C_Initialize(Object obj) throws PKCS11Exception {
            super.C_Initialize(obj);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_Finalize(Object obj) throws PKCS11Exception {
            super.C_Finalize(obj);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized CK_INFO C_GetInfo() throws PKCS11Exception {
            return super.C_GetInfo();
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long[] C_GetSlotList(boolean z2) throws PKCS11Exception {
            return super.C_GetSlotList(z2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized CK_SLOT_INFO C_GetSlotInfo(long j2) throws PKCS11Exception {
            return super.C_GetSlotInfo(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized CK_TOKEN_INFO C_GetTokenInfo(long j2) throws PKCS11Exception {
            return super.C_GetTokenInfo(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long[] C_GetMechanismList(long j2) throws PKCS11Exception {
            return super.C_GetMechanismList(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized CK_MECHANISM_INFO C_GetMechanismInfo(long j2, long j3) throws PKCS11Exception {
            return super.C_GetMechanismInfo(j2, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_OpenSession(long j2, long j3, Object obj, CK_NOTIFY ck_notify) throws PKCS11Exception {
            return super.C_OpenSession(j2, j3, obj, ck_notify);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_CloseSession(long j2) throws PKCS11Exception {
            super.C_CloseSession(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized CK_SESSION_INFO C_GetSessionInfo(long j2) throws PKCS11Exception {
            return super.C_GetSessionInfo(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_Login(long j2, long j3, char[] cArr) throws PKCS11Exception {
            super.C_Login(j2, j3, cArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_Logout(long j2) throws PKCS11Exception {
            super.C_Logout(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_CreateObject(long j2, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            return super.C_CreateObject(j2, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_CopyObject(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            return super.C_CopyObject(j2, j3, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_DestroyObject(long j2, long j3) throws PKCS11Exception {
            super.C_DestroyObject(j2, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_GetAttributeValue(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            super.C_GetAttributeValue(j2, j3, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_SetAttributeValue(long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            super.C_SetAttributeValue(j2, j3, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_FindObjectsInit(long j2, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            super.C_FindObjectsInit(j2, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long[] C_FindObjects(long j2, long j3) throws PKCS11Exception {
            return super.C_FindObjects(j2, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_FindObjectsFinal(long j2) throws PKCS11Exception {
            super.C_FindObjectsFinal(j2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_EncryptInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_EncryptInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_Encrypt(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_Encrypt(j2, j3, bArr, i2, i3, j4, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_EncryptUpdate(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_EncryptUpdate(j2, j3, bArr, i2, i3, j4, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_EncryptFinal(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            return super.C_EncryptFinal(j2, j3, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_DecryptInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_DecryptInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_Decrypt(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_Decrypt(j2, j3, bArr, i2, i3, j4, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_DecryptUpdate(long j2, long j3, byte[] bArr, int i2, int i3, long j4, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_DecryptUpdate(j2, j3, bArr, i2, i3, j4, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_DecryptFinal(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            return super.C_DecryptFinal(j2, j3, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_DigestInit(long j2, CK_MECHANISM ck_mechanism) throws PKCS11Exception {
            super.C_DigestInit(j2, ck_mechanism);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_DigestSingle(long j2, CK_MECHANISM ck_mechanism, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_DigestSingle(j2, ck_mechanism, bArr, i2, i3, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_DigestUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            super.C_DigestUpdate(j2, j3, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_DigestKey(long j2, long j3) throws PKCS11Exception {
            super.C_DigestKey(j2, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_DigestFinal(long j2, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            return super.C_DigestFinal(j2, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_SignInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_SignInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized byte[] C_Sign(long j2, byte[] bArr) throws PKCS11Exception {
            return super.C_Sign(j2, bArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_SignUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            super.C_SignUpdate(j2, j3, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized byte[] C_SignFinal(long j2, int i2) throws PKCS11Exception {
            return super.C_SignFinal(j2, i2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_SignRecoverInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_SignRecoverInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_SignRecover(long j2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_SignRecover(j2, bArr, i2, i3, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_VerifyInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_VerifyInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_Verify(long j2, byte[] bArr, byte[] bArr2) throws PKCS11Exception {
            super.C_Verify(j2, bArr, bArr2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_VerifyUpdate(long j2, long j3, byte[] bArr, int i2, int i3) throws PKCS11Exception {
            super.C_VerifyUpdate(j2, j3, bArr, i2, i3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_VerifyFinal(long j2, byte[] bArr) throws PKCS11Exception {
            super.C_VerifyFinal(j2, bArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_VerifyRecoverInit(long j2, CK_MECHANISM ck_mechanism, long j3) throws PKCS11Exception {
            super.C_VerifyRecoverInit(j2, ck_mechanism, j3);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized int C_VerifyRecover(long j2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws PKCS11Exception {
            return super.C_VerifyRecover(j2, bArr, i2, i3, bArr2, i4, i5);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_GenerateKey(long j2, CK_MECHANISM ck_mechanism, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            return super.C_GenerateKey(j2, ck_mechanism, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long[] C_GenerateKeyPair(long j2, CK_MECHANISM ck_mechanism, CK_ATTRIBUTE[] ck_attributeArr, CK_ATTRIBUTE[] ck_attributeArr2) throws PKCS11Exception {
            return super.C_GenerateKeyPair(j2, ck_mechanism, ck_attributeArr, ck_attributeArr2);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized byte[] C_WrapKey(long j2, CK_MECHANISM ck_mechanism, long j3, long j4) throws PKCS11Exception {
            return super.C_WrapKey(j2, ck_mechanism, j3, j4);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_UnwrapKey(long j2, CK_MECHANISM ck_mechanism, long j3, byte[] bArr, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            return super.C_UnwrapKey(j2, ck_mechanism, j3, bArr, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized long C_DeriveKey(long j2, CK_MECHANISM ck_mechanism, long j3, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
            return super.C_DeriveKey(j2, ck_mechanism, j3, ck_attributeArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_SeedRandom(long j2, byte[] bArr) throws PKCS11Exception {
            super.C_SeedRandom(j2, bArr);
        }

        @Override // sun.security.pkcs11.wrapper.PKCS11
        public synchronized void C_GenerateRandom(long j2, byte[] bArr) throws PKCS11Exception {
            super.C_GenerateRandom(j2, bArr);
        }
    }
}
