package javax.xml.crypto.dsig.keyinfo;

import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/keyinfo/RetrievalMethod.class */
public interface RetrievalMethod extends URIReference, XMLStructure {
    List getTransforms();

    @Override // javax.xml.crypto.URIReference
    String getURI();

    Data dereference(XMLCryptoContext xMLCryptoContext) throws URIReferenceException;
}
