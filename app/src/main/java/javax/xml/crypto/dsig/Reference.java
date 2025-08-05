package javax.xml.crypto.dsig;

import java.io.InputStream;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/Reference.class */
public interface Reference extends URIReference, XMLStructure {
    List getTransforms();

    DigestMethod getDigestMethod();

    String getId();

    byte[] getDigestValue();

    byte[] getCalculatedDigestValue();

    boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException;

    Data getDereferencedData();

    InputStream getDigestInputStream();
}
