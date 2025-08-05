package com.sun.org.apache.xml.internal.security.c14n;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/CanonicalizerSpi.class */
public abstract class CanonicalizerSpi {
    protected boolean reset = false;
    protected boolean secureValidation;

    public abstract String engineGetURI();

    public abstract boolean engineGetIncludeComments();

    public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> set) throws CanonicalizationException;

    public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> set, String str) throws CanonicalizationException;

    public abstract byte[] engineCanonicalizeSubTree(Node node) throws CanonicalizationException;

    public abstract byte[] engineCanonicalizeSubTree(Node node, String str) throws CanonicalizationException;

    public abstract byte[] engineCanonicalizeSubTree(Node node, String str, boolean z2) throws CanonicalizationException;

    public abstract void setWriter(OutputStream outputStream);

    public byte[] engineCanonicalize(byte[] bArr) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Throwable th = null;
        try {
            try {
                Document document = XMLUtils.createDocumentBuilder(false, this.secureValidation).parse(new InputSource(byteArrayInputStream));
                if (byteArrayInputStream != null) {
                    if (0 != 0) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        byteArrayInputStream.close();
                    }
                }
                return engineCanonicalizeSubTree(document);
            } finally {
            }
        } catch (Throwable th3) {
            if (byteArrayInputStream != null) {
                if (th != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    byteArrayInputStream.close();
                }
            }
            throw th3;
        }
    }

    public byte[] engineCanonicalizeXPathNodeSet(NodeList nodeList) throws CanonicalizationException {
        return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(nodeList));
    }

    public byte[] engineCanonicalizeXPathNodeSet(NodeList nodeList, String str) throws CanonicalizationException {
        return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(nodeList), str);
    }

    public boolean isSecureValidation() {
        return this.secureValidation;
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }
}
