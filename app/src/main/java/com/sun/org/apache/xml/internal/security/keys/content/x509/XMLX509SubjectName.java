package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.RFC2253Parser;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import java.security.cert.X509Certificate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509SubjectName.class */
public class XMLX509SubjectName extends SignatureElementProxy implements XMLX509DataContent {
    public XMLX509SubjectName(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public XMLX509SubjectName(Document document, String str) {
        super(document);
        addText(str);
    }

    public XMLX509SubjectName(Document document, X509Certificate x509Certificate) {
        this(document, x509Certificate.getSubjectX500Principal().getName());
    }

    public String getSubjectName() {
        return RFC2253Parser.normalize(getTextFromTextChild());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof XMLX509SubjectName)) {
            return false;
        }
        return getSubjectName().equals(((XMLX509SubjectName) obj).getSubjectName());
    }

    public int hashCode() {
        return (31 * 17) + getSubjectName().hashCode();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509SUBJECTNAME;
    }
}
