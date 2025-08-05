package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.security.SecurityPermission;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/JavaUtils.class */
public final class JavaUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JavaUtils.class);
    private static final SecurityPermission REGISTER_PERMISSION = new SecurityPermission("com.sun.org.apache.xml.internal.security.register");

    private JavaUtils() {
    }

    public static byte[] getBytesFromFile(String str) throws IOException {
        InputStream inputStreamNewInputStream = Files.newInputStream(Paths.get(str, new String[0]), new OpenOption[0]);
        Throwable th = null;
        try {
            UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
            Throwable th2 = null;
            try {
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i2 = inputStreamNewInputStream.read(bArr);
                        if (i2 <= 0) {
                            break;
                        }
                        unsyncByteArrayOutputStream.write(bArr, 0, i2);
                    }
                    byte[] byteArray = unsyncByteArrayOutputStream.toByteArray();
                    if (unsyncByteArrayOutputStream != null) {
                        if (0 != 0) {
                            try {
                                unsyncByteArrayOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            unsyncByteArrayOutputStream.close();
                        }
                    }
                    return byteArray;
                } catch (Throwable th4) {
                    if (unsyncByteArrayOutputStream != null) {
                        if (th2 != null) {
                            try {
                                unsyncByteArrayOutputStream.close();
                            } catch (Throwable th5) {
                                th2.addSuppressed(th5);
                            }
                        } else {
                            unsyncByteArrayOutputStream.close();
                        }
                    }
                    throw th4;
                }
            } finally {
            }
        } finally {
            if (inputStreamNewInputStream != null) {
                if (0 != 0) {
                    try {
                        inputStreamNewInputStream.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    inputStreamNewInputStream.close();
                }
            }
        }
    }

    public static void writeBytesToFilename(String str, byte[] bArr) {
        if (str != null && bArr != null) {
            try {
                OutputStream outputStreamNewOutputStream = Files.newOutputStream(Paths.get(str, new String[0]), new OpenOption[0]);
                Throwable th = null;
                try {
                    try {
                        outputStreamNewOutputStream.write(bArr);
                        if (outputStreamNewOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    outputStreamNewOutputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                outputStreamNewOutputStream.close();
                            }
                        }
                        return;
                    } finally {
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th3;
                }
            } catch (IOException e2) {
                LOG.debug(e2.getMessage(), e2);
                return;
            }
        }
        LOG.debug("writeBytesToFilename got null byte[] pointed");
    }

    public static byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
        Throwable th = null;
        try {
            byte[] bArr = new byte[4096];
            while (true) {
                int i2 = inputStream.read(bArr);
                if (i2 <= 0) {
                    break;
                }
                unsyncByteArrayOutputStream.write(bArr, 0, i2);
            }
            byte[] byteArray = unsyncByteArrayOutputStream.toByteArray();
            if (unsyncByteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        unsyncByteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    unsyncByteArrayOutputStream.close();
                }
            }
            return byteArray;
        } catch (Throwable th3) {
            if (unsyncByteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        unsyncByteArrayOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    unsyncByteArrayOutputStream.close();
                }
            }
            throw th3;
        }
    }

    public static byte[] convertDsaASN1toXMLDSIG(byte[] bArr, int i2) throws IOException {
        if (bArr[0] != 48 || bArr[1] != bArr.length - 2 || bArr[2] != 2) {
            throw new IOException("Invalid ASN.1 format of DSA signature");
        }
        byte b2 = bArr[3];
        int i3 = b2;
        while (i3 > 0 && bArr[(4 + b2) - i3] == 0) {
            i3--;
        }
        byte b3 = bArr[5 + b2];
        int i4 = b3;
        while (i4 > 0 && bArr[((6 + b2) + b3) - i4] == 0) {
            i4--;
        }
        if (i3 > i2 || bArr[4 + b2] != 2 || i4 > i2) {
            throw new IOException("Invalid ASN.1 format of DSA signature");
        }
        byte[] bArr2 = new byte[i2 * 2];
        System.arraycopy(bArr, (4 + b2) - i3, bArr2, i2 - i3, i3);
        System.arraycopy(bArr, ((6 + b2) + b3) - i4, bArr2, (i2 * 2) - i4, i4);
        return bArr2;
    }

    public static byte[] convertDsaXMLDSIGtoASN1(byte[] bArr, int i2) throws IOException {
        int i3 = i2 * 2;
        if (bArr.length != i3) {
            throw new IOException("Invalid XMLDSIG format of DSA signature");
        }
        int i4 = i2;
        while (i4 > 0 && bArr[i2 - i4] == 0) {
            i4--;
        }
        int i5 = i4;
        if (bArr[i2 - i4] < 0) {
            i5++;
        }
        int i6 = i2;
        while (i6 > 0 && bArr[i3 - i6] == 0) {
            i6--;
        }
        int i7 = i6;
        if (bArr[i3 - i6] < 0) {
            i7++;
        }
        byte[] bArr2 = new byte[6 + i5 + i7];
        bArr2[0] = 48;
        bArr2[1] = (byte) (4 + i5 + i7);
        bArr2[2] = 2;
        bArr2[3] = (byte) i5;
        System.arraycopy(bArr, i2 - i4, bArr2, (4 + i5) - i4, i4);
        bArr2[4 + i5] = 2;
        bArr2[5 + i5] = (byte) i7;
        System.arraycopy(bArr, i3 - i6, bArr2, ((6 + i5) + i7) - i6, i6);
        return bArr2;
    }

    public static void checkRegisterPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(REGISTER_PERMISSION);
        }
    }
}
