package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import java.io.IOException;
import javax.xml.crypto.OctetStreamData;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/ApacheOctetStreamData.class */
public class ApacheOctetStreamData extends OctetStreamData implements ApacheData {
    private XMLSignatureInput xi;

    public ApacheOctetStreamData(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, IOException {
        super(xMLSignatureInput.getOctetStream(), xMLSignatureInput.getSourceURI(), xMLSignatureInput.getMIMEType());
        this.xi = xMLSignatureInput;
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheData
    public XMLSignatureInput getXMLSignatureInput() {
        return this.xi;
    }
}
