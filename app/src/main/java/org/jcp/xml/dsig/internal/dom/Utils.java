package org.jcp.xml.dsig.internal.dom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.crypto.XMLCryptoContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/Utils.class */
public final class Utils {
    private Utils() {
    }

    public static byte[] readBytesFromStream(InputStream inputStream) throws IOException {
        int i2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Throwable th = null;
        try {
            byte[] bArr = new byte[1024];
            do {
                i2 = inputStream.read(bArr);
                if (i2 == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, i2);
            } while (i2 >= 1024);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    byteArrayOutputStream.close();
                }
            }
            return byteArray;
        } catch (Throwable th3) {
            if (byteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    byteArrayOutputStream.close();
                }
            }
            throw th3;
        }
    }

    static Set<Node> toNodeSet(Iterator<Node> it) {
        HashSet hashSet = new HashSet();
        while (it.hasNext()) {
            Node next = it.next();
            hashSet.add(next);
            if (next.getNodeType() == 1) {
                NamedNodeMap attributes = next.getAttributes();
                int length = attributes.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    hashSet.add(attributes.item(i2));
                }
            }
        }
        return hashSet;
    }

    public static String parseIdFromSameDocumentURI(String str) {
        if (str.length() == 0) {
            return null;
        }
        String strSubstring = str.substring(1);
        if (strSubstring != null && strSubstring.startsWith("xpointer(id(")) {
            int iIndexOf = strSubstring.indexOf(39);
            strSubstring = strSubstring.substring(iIndexOf + 1, strSubstring.indexOf(39, iIndexOf + 1));
        }
        return strSubstring;
    }

    public static boolean sameDocumentURI(String str) {
        return str != null && (str.length() == 0 || str.charAt(0) == '#');
    }

    static boolean secureValidation(XMLCryptoContext xMLCryptoContext) {
        if (xMLCryptoContext == null) {
            return false;
        }
        return getBoolean(xMLCryptoContext, "org.jcp.xml.dsig.secureValidation");
    }

    private static boolean getBoolean(XMLCryptoContext xMLCryptoContext, String str) {
        Boolean bool = (Boolean) xMLCryptoContext.getProperty(str);
        return bool != null && bool.booleanValue();
    }
}
