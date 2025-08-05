package org.icepdf.core.pobjects.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/StandardEncryption.class */
class StandardEncryption {
    public static final String ENCRYPTION_TYPE_NONE = "None";
    public static final String ENCRYPTION_TYPE_V2 = "V2";
    public static final String ENCRYPTION_TYPE_V3 = "V3";
    public static final String ENCRYPTION_TYPE_AES_V2 = "AESV2";
    private EncryptionDictionary encryptionDictionary;
    private byte[] encryptionKey;
    private Reference objectReference;
    private byte[] rc4Key = null;
    private String userPassword = "";
    private String ownerPassword = "";
    private static final Logger logger = Logger.getLogger(StandardEncryption.class.toString());
    private static final byte[] PADDING = {40, -65, 78, 94, 78, 117, -118, 65, 100, 0, 78, 86, -1, -6, 1, 8, 46, 46, 0, -74, -48, 104, 62, Byte.MIN_VALUE, 47, 12, -87, -2, 100, 83, 105, 122};
    private static final byte[] AES_sAIT = {115, 65, 108, 84};

    public StandardEncryption(EncryptionDictionary encryptionDictionary) {
        this.encryptionDictionary = encryptionDictionary;
    }

    public byte[] generalEncryptionAlgorithm(Reference objectReference, byte[] encryptionKey, String algorithmType, byte[] inputData) {
        if (objectReference == null || encryptionKey == null || inputData == null) {
            return null;
        }
        if (this.encryptionDictionary.getVersion() >= 5) {
            if (this.encryptionDictionary.getVersion() != 5) {
                return null;
            }
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
                Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] initialisationVector = new byte[16];
                System.arraycopy(inputData, 0, initialisationVector, 0, 16);
                byte[] intermData = new byte[inputData.length - 16];
                System.arraycopy(inputData, 16, intermData, 0, intermData.length);
                IvParameterSpec iVParameterSpec = new IvParameterSpec(initialisationVector);
                aes.init(2, key, iVParameterSpec);
                return aes.doFinal(intermData);
            } catch (InvalidAlgorithmParameterException ex) {
                logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex);
                return null;
            } catch (InvalidKeyException ex2) {
                logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex2);
                return null;
            } catch (NoSuchAlgorithmException ex3) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex3);
                return null;
            } catch (BadPaddingException ex4) {
                logger.log(Level.FINE, "BadPaddingException.", (Throwable) ex4);
                return null;
            } catch (IllegalBlockSizeException ex5) {
                logger.log(Level.FINE, "IllegalBlockSizeException.", (Throwable) ex5);
                return null;
            } catch (NoSuchPaddingException ex6) {
                logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex6);
                return null;
            }
        }
        boolean isRc4 = algorithmType.equals(ENCRYPTION_TYPE_V2);
        if (this.rc4Key == null || this.encryptionKey != encryptionKey || this.objectReference != objectReference) {
            this.objectReference = objectReference;
            byte[] step3Bytes = resetObjectReference(objectReference, isRc4);
            int n2 = encryptionKey.length;
            this.rc4Key = new byte[Math.min(n2 + 5, 16)];
            System.arraycopy(step3Bytes, 0, this.rc4Key, 0, this.rc4Key.length);
        }
        byte[] finalData = null;
        try {
            if (isRc4) {
                SecretKeySpec key2 = new SecretKeySpec(this.rc4Key, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(2, key2);
                finalData = rc4.doFinal(inputData);
            } else {
                SecretKeySpec key3 = new SecretKeySpec(this.rc4Key, "AES");
                Cipher aes2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] initialisationVector2 = new byte[16];
                System.arraycopy(inputData, 0, initialisationVector2, 0, 16);
                byte[] intermData2 = new byte[inputData.length - 16];
                System.arraycopy(inputData, 16, intermData2, 0, intermData2.length);
                IvParameterSpec iVParameterSpec2 = new IvParameterSpec(initialisationVector2);
                aes2.init(2, key3, iVParameterSpec2);
                finalData = aes2.doFinal(intermData2);
            }
        } catch (InvalidAlgorithmParameterException ex7) {
            logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex7);
        } catch (InvalidKeyException ex8) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex8);
        } catch (NoSuchAlgorithmException ex9) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex9);
        } catch (BadPaddingException ex10) {
            logger.log(Level.FINE, "BadPaddingException.", (Throwable) ex10);
        } catch (IllegalBlockSizeException ex11) {
            logger.log(Level.FINE, "IllegalBlockSizeException.", (Throwable) ex11);
        } catch (NoSuchPaddingException ex12) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex12);
        }
        return finalData;
    }

    public InputStream generalEncryptionInputStream(Reference objectReference, byte[] encryptionKey, String algorithmType, InputStream input) {
        if (objectReference == null || encryptionKey == null || input == null) {
            return null;
        }
        if (this.encryptionDictionary.getVersion() >= 5) {
            if (this.encryptionDictionary.getVersion() != 5) {
                return null;
            }
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
                Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] initialisationVector = new byte[16];
                input.read(initialisationVector);
                IvParameterSpec iVParameterSpec = new IvParameterSpec(initialisationVector);
                aes.init(2, key, iVParameterSpec);
                CipherInputStream cin = new CipherInputStream(input, aes);
                return cin;
            } catch (IOException ex) {
                logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex);
                return null;
            } catch (InvalidAlgorithmParameterException ex2) {
                logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex2);
                return null;
            } catch (InvalidKeyException ex3) {
                logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex3);
                return null;
            } catch (NoSuchAlgorithmException ex4) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex4);
                return null;
            } catch (NoSuchPaddingException ex5) {
                logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex5);
                return null;
            }
        }
        boolean isRc4 = algorithmType.equals(ENCRYPTION_TYPE_V2);
        if (this.rc4Key == null || this.encryptionKey != encryptionKey || this.objectReference != objectReference) {
            this.objectReference = objectReference;
            byte[] step3Bytes = resetObjectReference(objectReference, isRc4);
            int n2 = encryptionKey.length;
            this.rc4Key = new byte[Math.min(n2 + 5, 16)];
            System.arraycopy(step3Bytes, 0, this.rc4Key, 0, this.rc4Key.length);
        }
        try {
            if (isRc4) {
                SecretKeySpec key2 = new SecretKeySpec(this.rc4Key, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(2, key2);
                CipherInputStream cin2 = new CipherInputStream(input, rc4);
                return cin2;
            }
            SecretKeySpec key3 = new SecretKeySpec(this.rc4Key, "AES");
            Cipher aes2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] initialisationVector2 = new byte[16];
            input.read(initialisationVector2);
            IvParameterSpec iVParameterSpec2 = new IvParameterSpec(initialisationVector2);
            aes2.init(2, key3, iVParameterSpec2);
            CipherInputStream cin3 = new CipherInputStream(input, aes2);
            return cin3;
        } catch (IOException ex6) {
            logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex6);
            return null;
        } catch (InvalidAlgorithmParameterException ex7) {
            logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex7);
            return null;
        } catch (InvalidKeyException ex8) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex8);
            return null;
        } catch (NoSuchAlgorithmException ex9) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex9);
            return null;
        } catch (NoSuchPaddingException ex10) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex10);
            return null;
        }
    }

    public byte[] resetObjectReference(Reference objectReference, boolean isRc4) {
        int objectNumber = objectReference.getObjectNumber();
        int generationNumber = objectReference.getGenerationNumber();
        int n2 = 5;
        if (this.encryptionDictionary.getVersion() > 1) {
            n2 = this.encryptionDictionary.getKeyLength() / 8;
        }
        int paddingLength = 5;
        if (!isRc4) {
            paddingLength = 5 + 4;
        }
        byte[] step2Bytes = new byte[n2 + paddingLength];
        System.arraycopy(this.encryptionKey, 0, step2Bytes, 0, n2);
        step2Bytes[n2] = (byte) (objectNumber & 255);
        step2Bytes[n2 + 1] = (byte) ((objectNumber >> 8) & 255);
        step2Bytes[n2 + 2] = (byte) ((objectNumber >> 16) & 255);
        step2Bytes[n2 + 3] = (byte) (generationNumber & 255);
        step2Bytes[n2 + 4] = (byte) ((generationNumber >> 8) & 255);
        if (!isRc4) {
            step2Bytes[n2 + 5] = AES_sAIT[0];
            step2Bytes[n2 + 6] = AES_sAIT[1];
            step2Bytes[n2 + 7] = AES_sAIT[2];
            step2Bytes[n2 + 8] = AES_sAIT[3];
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
        }
        md5.update(step2Bytes);
        return md5.digest();
    }

    public byte[] encryptionKeyAlgorithm(String password, int keyLength) {
        if (this.encryptionDictionary.getRevisionNumber() < 5) {
            byte[] paddedPassword = padPassword(password);
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex);
            }
            md5.update(paddedPassword);
            byte[] bigO = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigO());
            md5.update(bigO);
            int i2 = 0;
            int permissions = this.encryptionDictionary.getPermissions();
            while (true) {
                int p2 = permissions;
                if (i2 >= 4) {
                    break;
                }
                md5.update((byte) (p2 & 255));
                i2++;
                permissions = p2 >> 8;
            }
            String firstFileID = ((StringObject) this.encryptionDictionary.getFileID().get(0)).getLiteralString();
            byte[] fileID = Utils.convertByteCharSequenceToByteArray(firstFileID);
            byte[] paddedPassword2 = md5.digest(fileID);
            if (this.encryptionDictionary.getRevisionNumber() >= 3) {
                for (int i3 = 0; i3 < 50; i3++) {
                    paddedPassword2 = md5.digest(paddedPassword2);
                }
            }
            byte[] out = null;
            int n2 = 5;
            if (this.encryptionDictionary.getRevisionNumber() == 2) {
                out = new byte[5];
            } else if (this.encryptionDictionary.getRevisionNumber() >= 3) {
                n2 = keyLength / 8;
                out = new byte[n2];
            }
            if (n2 > paddedPassword2.length) {
                n2 = paddedPassword2.length;
            }
            System.arraycopy(paddedPassword2, 0, out, 0, n2);
            this.encryptionKey = out;
            return out;
        }
        if (this.encryptionDictionary.getRevisionNumber() == 5) {
            try {
                byte[] passwordBytes = Utils.convertByteCharSequenceToByteArray(password);
                if (passwordBytes == null) {
                    passwordBytes = new byte[0];
                }
                byte[] ownerPassword = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigO());
                byte[] userPassword = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigU());
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(passwordBytes, 0, Math.min(passwordBytes.length, 127));
                md.update(ownerPassword, 32, 8);
                md.update(userPassword, 0, 48);
                byte[] hash = md.digest();
                boolean isOwnerPassword = byteCompare(hash, ownerPassword, 32);
                this.encryptionDictionary.setAuthenticatedOwnerPassword(isOwnerPassword);
                if (isOwnerPassword) {
                    md.update(passwordBytes, 0, Math.min(passwordBytes.length, 127));
                    md.update(ownerPassword, 32, 8);
                    md.update(userPassword, 0, 48);
                    byte[] hash2 = md.digest();
                    byte[] oePassword = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigOE());
                    this.encryptionKey = AES256CBC(hash2, oePassword);
                } else {
                    md.update(passwordBytes, 0, Math.min(passwordBytes.length, 127));
                    md.update(userPassword, 32, 8);
                    byte[] hash3 = md.digest();
                    boolean isUserPassword = byteCompare(hash3, userPassword, 32);
                    this.encryptionDictionary.setAuthenticatedUserPassword(isUserPassword);
                    if (isUserPassword) {
                        md.update(passwordBytes, 0, Math.min(passwordBytes.length, 127));
                        md.update(userPassword, 40, 8);
                        byte[] hash4 = md.digest();
                        byte[] uePassword = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigUE());
                        this.encryptionKey = AES256CBC(hash4, uePassword);
                    } else {
                        logger.warning("User password is incorrect. ");
                    }
                }
                byte[] perms = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getPerms());
                byte[] decryptedPerms = AES256CBC(this.encryptionKey, perms);
                if (decryptedPerms[9] != 97 || decryptedPerms[10] != 100 || decryptedPerms[11] != 98) {
                    logger.warning("User password is incorrect.");
                    return null;
                }
                int permissions2 = (decryptedPerms[0] & 255) | ((decryptedPerms[1] & 255) << 8) | ((decryptedPerms[2] & 255) << 16) | ((decryptedPerms[2] & 255) << 24);
                int pPermissions = this.encryptionDictionary.getPermissions();
                if (pPermissions != permissions2) {
                    logger.warning("Perms and P do not match");
                }
                return this.encryptionKey;
            } catch (NoSuchAlgorithmException e2) {
                logger.warning("Error computing the the 3.2a Encryption key.");
                return null;
            }
        }
        logger.warning("Adobe standard Encryption R = 6 is not supported.");
        return null;
    }

    protected static byte[] padPassword(String password) {
        byte[] paddedPassword = new byte[32];
        if (password == null || "".equals(password)) {
            return PADDING;
        }
        int passwordLength = Math.min(password.length(), 32);
        byte[] bytePassword = Utils.convertByteCharSequenceToByteArray(password);
        System.arraycopy(bytePassword, 0, paddedPassword, 0, passwordLength);
        System.arraycopy(PADDING, 0, paddedPassword, passwordLength, 32 - passwordLength);
        return paddedPassword;
    }

    public byte[] calculateOwnerPassword(String ownerPassword, String userPassword, boolean isAuthentication) {
        if ("".equals(ownerPassword) && !"".equals(userPassword)) {
            ownerPassword = userPassword;
        }
        byte[] paddedOwnerPassword = padPassword(ownerPassword);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            logger.log(Level.FINE, "Could not fint MD5 Digest", (Throwable) e2);
        }
        byte[] paddedOwnerPassword2 = md5.digest(paddedOwnerPassword);
        if (this.encryptionDictionary.getRevisionNumber() >= 3) {
            for (int i2 = 0; i2 < 50; i2++) {
                paddedOwnerPassword2 = md5.digest(paddedOwnerPassword2);
            }
        }
        int dataSize = 5;
        if (this.encryptionDictionary.getRevisionNumber() >= 3) {
            dataSize = this.encryptionDictionary.getKeyLength() / 8;
        }
        if (dataSize > paddedOwnerPassword2.length) {
            dataSize = paddedOwnerPassword2.length;
        }
        byte[] encryptionKey = new byte[dataSize];
        System.arraycopy(paddedOwnerPassword2, 0, encryptionKey, 0, dataSize);
        if (isAuthentication) {
            return encryptionKey;
        }
        byte[] paddedUserPassword = padPassword(userPassword);
        byte[] finalData = null;
        try {
            SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(1, key);
            finalData = rc4.update(paddedUserPassword);
            if (this.encryptionDictionary.getRevisionNumber() >= 3) {
                byte[] indexedKey = new byte[encryptionKey.length];
                for (int i3 = 1; i3 <= 19; i3++) {
                    for (int j2 = 0; j2 < encryptionKey.length; j2++) {
                        indexedKey[j2] = (byte) (encryptionKey[j2] ^ i3);
                    }
                    SecretKeySpec key2 = new SecretKeySpec(indexedKey, "RC4");
                    rc4.init(1, key2);
                    finalData = rc4.update(finalData);
                }
            }
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex);
        } catch (NoSuchAlgorithmException ex2) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex2);
        } catch (NoSuchPaddingException ex3) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex3);
        }
        return finalData;
    }

    public byte[] calculateUserPassword(String userPassword) {
        byte[] encryptionKey = encryptionKeyAlgorithm(userPassword, this.encryptionDictionary.getKeyLength());
        if (this.encryptionDictionary.getRevisionNumber() == 2) {
            byte[] paddedUserPassword = (byte[]) PADDING.clone();
            byte[] finalData = null;
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(1, key);
                finalData = rc4.doFinal(paddedUserPassword);
            } catch (InvalidKeyException ex) {
                logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex);
            } catch (NoSuchAlgorithmException ex2) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex2);
            } catch (BadPaddingException ex3) {
                logger.log(Level.FINE, "BadPaddingException.", (Throwable) ex3);
            } catch (IllegalBlockSizeException ex4) {
                logger.log(Level.FINE, "IllegalBlockSizeException.", (Throwable) ex4);
            } catch (NoSuchPaddingException ex5) {
                logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex5);
            }
            return finalData;
        }
        if (this.encryptionDictionary.getRevisionNumber() < 3 || this.encryptionDictionary.getRevisionNumber() >= 5) {
            return null;
        }
        byte[] paddedUserPassword2 = (byte[]) PADDING.clone();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            logger.log(Level.FINE, "MD5 digester could not be found", (Throwable) e2);
        }
        md5.update(paddedUserPassword2);
        String firstFileID = ((StringObject) this.encryptionDictionary.getFileID().get(0)).getLiteralString();
        byte[] fileID = Utils.convertByteCharSequenceToByteArray(firstFileID);
        byte[] encryptData = md5.digest(fileID);
        try {
            SecretKeySpec key2 = new SecretKeySpec(encryptionKey, "RC4");
            Cipher rc42 = Cipher.getInstance("RC4");
            rc42.init(1, key2);
            encryptData = rc42.update(encryptData);
            byte[] indexedKey = new byte[encryptionKey.length];
            for (int i2 = 1; i2 <= 19; i2++) {
                for (int j2 = 0; j2 < encryptionKey.length; j2++) {
                    indexedKey[j2] = (byte) (encryptionKey[j2] ^ ((byte) i2));
                }
                SecretKeySpec key3 = new SecretKeySpec(indexedKey, "RC4");
                rc42.init(1, key3);
                encryptData = rc42.update(encryptData);
            }
        } catch (InvalidKeyException ex6) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex6);
        } catch (NoSuchAlgorithmException ex7) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex7);
        } catch (NoSuchPaddingException ex8) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex8);
        }
        byte[] finalData2 = new byte[32];
        System.arraycopy(encryptData, 0, finalData2, 0, 16);
        System.arraycopy(PADDING, 0, finalData2, 16, 16);
        return finalData2;
    }

    public boolean authenticateUserPassword(String userPassword) {
        byte[] trunkUValue;
        byte[] tmpUValue = calculateUserPassword(userPassword);
        byte[] bigU = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigU());
        if (this.encryptionDictionary.getRevisionNumber() == 2) {
            trunkUValue = new byte[32];
            System.arraycopy(tmpUValue, 0, trunkUValue, 0, trunkUValue.length);
        } else if (this.encryptionDictionary.getRevisionNumber() >= 3 && this.encryptionDictionary.getRevisionNumber() < 5) {
            trunkUValue = new byte[16];
            System.arraycopy(tmpUValue, 0, trunkUValue, 0, trunkUValue.length);
        } else {
            return false;
        }
        boolean found = true;
        int i2 = 0;
        while (true) {
            if (i2 >= trunkUValue.length) {
                break;
            }
            if (trunkUValue[i2] == bigU[i2]) {
                i2++;
            } else {
                found = false;
                break;
            }
        }
        return found;
    }

    public boolean authenticateOwnerPassword(String ownerPassword) {
        byte[] encryptionKey = calculateOwnerPassword(ownerPassword, "", true);
        byte[] decryptedO = null;
        try {
            byte[] bigO = Utils.convertByteCharSequenceToByteArray(this.encryptionDictionary.getBigO());
            if (this.encryptionDictionary.getRevisionNumber() == 2) {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(2, key);
                decryptedO = rc4.doFinal(bigO);
            } else {
                byte[] indexedKey = new byte[encryptionKey.length];
                decryptedO = bigO;
                for (int i2 = 19; i2 >= 0; i2--) {
                    for (int j2 = 0; j2 < indexedKey.length; j2++) {
                        indexedKey[j2] = (byte) (encryptionKey[j2] ^ ((byte) i2));
                    }
                    SecretKeySpec key2 = new SecretKeySpec(indexedKey, "RC4");
                    Cipher rc42 = Cipher.getInstance("RC4");
                    rc42.init(1, key2);
                    decryptedO = rc42.update(decryptedO);
                }
            }
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex);
        } catch (NoSuchAlgorithmException ex2) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex2);
        } catch (BadPaddingException ex3) {
            logger.log(Level.FINE, "BadPaddingException.", (Throwable) ex3);
        } catch (IllegalBlockSizeException ex4) {
            logger.log(Level.FINE, "IllegalBlockSizeException.", (Throwable) ex4);
        } catch (NoSuchPaddingException ex5) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex5);
        }
        String tmpUserPassword = Utils.convertByteArrayToByteString(decryptedO);
        boolean isValid = authenticateUserPassword(tmpUserPassword);
        if (isValid) {
            this.userPassword = tmpUserPassword;
            this.ownerPassword = ownerPassword;
        }
        return isValid;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public String getOwnerPassword() {
        return this.ownerPassword;
    }

    private static byte[] AES256CBC(byte[] intermediateKey, byte[] encryptedString) {
        byte[] finalData = null;
        try {
            SecretKeySpec key = new SecretKeySpec(intermediateKey, "AES");
            Cipher aes = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iVParameterSpec = new IvParameterSpec(new byte[16]);
            aes.init(2, key, iVParameterSpec);
            finalData = aes.doFinal(encryptedString);
        } catch (InvalidAlgorithmParameterException ex) {
            logger.log(Level.FINE, "InvalidAlgorithmParameterException", (Throwable) ex);
        } catch (InvalidKeyException ex2) {
            logger.log(Level.FINE, "InvalidKeyException.", (Throwable) ex2);
        } catch (NoSuchAlgorithmException ex3) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", (Throwable) ex3);
        } catch (BadPaddingException ex4) {
            logger.log(Level.FINE, "BadPaddingException.", (Throwable) ex4);
        } catch (IllegalBlockSizeException ex5) {
            logger.log(Level.FINE, "IllegalBlockSizeException.", (Throwable) ex5);
        } catch (NoSuchPaddingException ex6) {
            logger.log(Level.FINE, "NoSuchPaddingException.", (Throwable) ex6);
        }
        return finalData;
    }

    private static boolean byteCompare(byte[] byteArray1, byte[] byteArray2, int range) {
        for (int i2 = 0; i2 < range; i2++) {
            if (byteArray1[i2] != byteArray2[i2]) {
                return false;
            }
        }
        return true;
    }
}
