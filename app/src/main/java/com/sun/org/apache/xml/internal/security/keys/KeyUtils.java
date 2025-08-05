package com.sun.org.apache.xml.internal.security.keys;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import java.io.PrintStream;
import java.security.PublicKey;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/KeyUtils.class */
public final class KeyUtils {
    private KeyUtils() {
    }

    public static void prinoutKeyInfo(KeyInfo keyInfo, PrintStream printStream) throws XMLSecurityException {
        for (int i2 = 0; i2 < keyInfo.lengthKeyName(); i2++) {
            printStream.println("KeyName(" + i2 + ")=\"" + keyInfo.itemKeyName(i2).getKeyName() + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        for (int i3 = 0; i3 < keyInfo.lengthKeyValue(); i3++) {
            PublicKey publicKey = keyInfo.itemKeyValue(i3).getPublicKey();
            printStream.println("KeyValue Nr. " + i3);
            printStream.println(publicKey);
        }
        for (int i4 = 0; i4 < keyInfo.lengthMgmtData(); i4++) {
            printStream.println("MgmtData(" + i4 + ")=\"" + keyInfo.itemMgmtData(i4).getMgmtData() + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        for (int i5 = 0; i5 < keyInfo.lengthX509Data(); i5++) {
            X509Data x509DataItemX509Data = keyInfo.itemX509Data(i5);
            printStream.println("X509Data(" + i5 + ")=\"" + (x509DataItemX509Data.containsCertificate() ? "Certificate " : "") + (x509DataItemX509Data.containsIssuerSerial() ? "IssuerSerial " : "") + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }
}
