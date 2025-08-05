package sun.misc;

import java.io.Console;
import java.io.FileDescriptor;
import java.io.ObjectInputStream;
import java.net.HttpCookie;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.ProtectionDomain;
import java.security.Signature;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;
import javax.crypto.SealedObject;

/* loaded from: rt.jar:sun/misc/SharedSecrets.class */
public class SharedSecrets {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static JavaUtilJarAccess javaUtilJarAccess;
    private static JavaLangAccess javaLangAccess;
    private static JavaLangRefAccess javaLangRefAccess;
    private static JavaIOAccess javaIOAccess;
    private static JavaNetAccess javaNetAccess;
    private static JavaNetHttpCookieAccess javaNetHttpCookieAccess;
    private static JavaNioAccess javaNioAccess;
    private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;
    private static JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess;
    private static JavaSecurityAccess javaSecurityAccess;
    private static JavaUtilZipFileAccess javaUtilZipFileAccess;
    private static JavaAWTAccess javaAWTAccess;
    private static JavaOISAccess javaOISAccess;
    private static JavaxCryptoSealedObjectAccess javaxCryptoSealedObjectAccess;
    private static JavaObjectInputStreamReadString javaObjectInputStreamReadString;
    private static JavaObjectInputStreamAccess javaObjectInputStreamAccess;
    private static JavaSecuritySignatureAccess javaSecuritySignatureAccess;

    public static JavaUtilJarAccess javaUtilJarAccess() {
        if (javaUtilJarAccess == null) {
            unsafe.ensureClassInitialized(JarFile.class);
        }
        return javaUtilJarAccess;
    }

    public static void setJavaUtilJarAccess(JavaUtilJarAccess javaUtilJarAccess2) {
        javaUtilJarAccess = javaUtilJarAccess2;
    }

    public static void setJavaLangAccess(JavaLangAccess javaLangAccess2) {
        javaLangAccess = javaLangAccess2;
    }

    public static JavaLangAccess getJavaLangAccess() {
        return javaLangAccess;
    }

    public static void setJavaLangRefAccess(JavaLangRefAccess javaLangRefAccess2) {
        javaLangRefAccess = javaLangRefAccess2;
    }

    public static JavaLangRefAccess getJavaLangRefAccess() {
        return javaLangRefAccess;
    }

    public static void setJavaNetAccess(JavaNetAccess javaNetAccess2) {
        javaNetAccess = javaNetAccess2;
    }

    public static JavaNetAccess getJavaNetAccess() {
        return javaNetAccess;
    }

    public static void setJavaNetHttpCookieAccess(JavaNetHttpCookieAccess javaNetHttpCookieAccess2) {
        javaNetHttpCookieAccess = javaNetHttpCookieAccess2;
    }

    public static JavaNetHttpCookieAccess getJavaNetHttpCookieAccess() {
        if (javaNetHttpCookieAccess == null) {
            unsafe.ensureClassInitialized(HttpCookie.class);
        }
        return javaNetHttpCookieAccess;
    }

    public static void setJavaNioAccess(JavaNioAccess javaNioAccess2) {
        javaNioAccess = javaNioAccess2;
    }

    public static JavaNioAccess getJavaNioAccess() {
        if (javaNioAccess == null) {
            unsafe.ensureClassInitialized(ByteOrder.class);
        }
        return javaNioAccess;
    }

    public static void setJavaIOAccess(JavaIOAccess javaIOAccess2) {
        javaIOAccess = javaIOAccess2;
    }

    public static JavaIOAccess getJavaIOAccess() {
        if (javaIOAccess == null) {
            unsafe.ensureClassInitialized(Console.class);
        }
        return javaIOAccess;
    }

    public static void setJavaIOFileDescriptorAccess(JavaIOFileDescriptorAccess javaIOFileDescriptorAccess2) {
        javaIOFileDescriptorAccess = javaIOFileDescriptorAccess2;
    }

    public static JavaIOFileDescriptorAccess getJavaIOFileDescriptorAccess() {
        if (javaIOFileDescriptorAccess == null) {
            unsafe.ensureClassInitialized(FileDescriptor.class);
        }
        return javaIOFileDescriptorAccess;
    }

    public static void setJavaOISAccess(JavaOISAccess javaOISAccess2) {
        javaOISAccess = javaOISAccess2;
    }

    public static JavaOISAccess getJavaOISAccess() {
        if (javaOISAccess == null) {
            unsafe.ensureClassInitialized(ObjectInputStream.class);
        }
        return javaOISAccess;
    }

    public static void setJavaSecurityProtectionDomainAccess(JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess2) {
        javaSecurityProtectionDomainAccess = javaSecurityProtectionDomainAccess2;
    }

    public static JavaSecurityProtectionDomainAccess getJavaSecurityProtectionDomainAccess() {
        if (javaSecurityProtectionDomainAccess == null) {
            unsafe.ensureClassInitialized(ProtectionDomain.class);
        }
        return javaSecurityProtectionDomainAccess;
    }

    public static void setJavaSecurityAccess(JavaSecurityAccess javaSecurityAccess2) {
        javaSecurityAccess = javaSecurityAccess2;
    }

    public static JavaSecurityAccess getJavaSecurityAccess() {
        if (javaSecurityAccess == null) {
            unsafe.ensureClassInitialized(AccessController.class);
        }
        return javaSecurityAccess;
    }

    public static JavaUtilZipFileAccess getJavaUtilZipFileAccess() {
        if (javaUtilZipFileAccess == null) {
            unsafe.ensureClassInitialized(ZipFile.class);
        }
        return javaUtilZipFileAccess;
    }

    public static void setJavaUtilZipFileAccess(JavaUtilZipFileAccess javaUtilZipFileAccess2) {
        javaUtilZipFileAccess = javaUtilZipFileAccess2;
    }

    public static void setJavaAWTAccess(JavaAWTAccess javaAWTAccess2) {
        javaAWTAccess = javaAWTAccess2;
    }

    public static JavaAWTAccess getJavaAWTAccess() {
        if (javaAWTAccess == null) {
            return null;
        }
        return javaAWTAccess;
    }

    public static JavaObjectInputStreamReadString getJavaObjectInputStreamReadString() {
        if (javaObjectInputStreamReadString == null) {
            unsafe.ensureClassInitialized(ObjectInputStream.class);
        }
        return javaObjectInputStreamReadString;
    }

    public static void setJavaObjectInputStreamReadString(JavaObjectInputStreamReadString javaObjectInputStreamReadString2) {
        javaObjectInputStreamReadString = javaObjectInputStreamReadString2;
    }

    public static JavaObjectInputStreamAccess getJavaObjectInputStreamAccess() {
        if (javaObjectInputStreamAccess == null) {
            unsafe.ensureClassInitialized(ObjectInputStream.class);
        }
        return javaObjectInputStreamAccess;
    }

    public static void setJavaObjectInputStreamAccess(JavaObjectInputStreamAccess javaObjectInputStreamAccess2) {
        javaObjectInputStreamAccess = javaObjectInputStreamAccess2;
    }

    public static void setJavaSecuritySignatureAccess(JavaSecuritySignatureAccess javaSecuritySignatureAccess2) {
        javaSecuritySignatureAccess = javaSecuritySignatureAccess2;
    }

    public static JavaSecuritySignatureAccess getJavaSecuritySignatureAccess() {
        if (javaSecuritySignatureAccess == null) {
            unsafe.ensureClassInitialized(Signature.class);
        }
        return javaSecuritySignatureAccess;
    }

    public static void setJavaxCryptoSealedObjectAccess(JavaxCryptoSealedObjectAccess javaxCryptoSealedObjectAccess2) {
        javaxCryptoSealedObjectAccess = javaxCryptoSealedObjectAccess2;
    }

    public static JavaxCryptoSealedObjectAccess getJavaxCryptoSealedObjectAccess() {
        if (javaxCryptoSealedObjectAccess == null) {
            unsafe.ensureClassInitialized(SealedObject.class);
        }
        return javaxCryptoSealedObjectAccess;
    }
}
