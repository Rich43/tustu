package javax.xml.crypto.dsig;

import java.util.List;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

/* loaded from: rt.jar:javax/xml/crypto/dsig/XMLSignature.class */
public interface XMLSignature extends XMLStructure {
    public static final String XMLNS = "http://www.w3.org/2000/09/xmldsig#";

    /* loaded from: rt.jar:javax/xml/crypto/dsig/XMLSignature$SignatureValue.class */
    public interface SignatureValue extends XMLStructure {
        String getId();

        byte[] getValue();

        boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException;
    }

    boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException;

    KeyInfo getKeyInfo();

    SignedInfo getSignedInfo();

    List getObjects();

    String getId();

    SignatureValue getSignatureValue();

    void sign(XMLSignContext xMLSignContext) throws MarshalException, XMLSignatureException;

    KeySelectorResult getKeySelectorResult();
}
